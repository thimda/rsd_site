package nc.uap.portal.integrate.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.authfield.PasswordExtAuthField;
import nc.uap.lfw.login.authfield.TextExtAuthField;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.portlet.PortletWindowImpl;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.exception.PortletLoginException;
import nc.uap.portal.integrate.system.Reference;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;

/**
 * 利用第三方系统的登录表单单点集成的单点认证类
 * 
 * @author gd 2009-10-16
 * @version NC5.6
 * @since NC5.6
 */
public class IntegrationLoginWithForm implements IWebAppLoginService {
	// 系统标识码
	public static final String SYSTEM_CODE = "system_code";
	// 密码字段前缀
	public static final String PASSWDFIELD_PREFIX = "passwdfield_";
	// 用户id字段前缀
	public static final String USERIDFIELD_PREFIX = "useridfield_";
	// 登录系统的最终url
	public static final String GATEURL = "gateUrl";
	// 字段前缀
	public static final String FIELD_PREFIX = "field_";
	public static final String SUCCESS_SIGN = "success_sign";
	public static final String FAILURE_SIGN = "failure_sign";
	public static final String REGISTRY_URL = "registryUrl";
	// 用户名统一前缀
	public static final String USERID_PREFIX = "userid_prefix";

	public PtCredentialVO credentialProcess(HttpServletRequest req,
			SSOProviderVO providerVO) throws CredentialValidateException {
		Map<String, String> refMap = getRefMap(providerVO);
		String[] fields = checkUserIdAndPwd(refMap);

		// 用户id前缀
		String userIdPre = "";
		if (refMap.containsKey(USERID_PREFIX))
			userIdPre = refMap.get(USERID_PREFIX) == null ? "" : refMap.get(
					USERID_PREFIX).trim();

		// 构造凭证VO
		PtCredentialVO credentialVO = new PtCredentialVO();
		credentialVO.setUserid(userIdPre + req.getAttribute(fields[0]));
		credentialVO.setPassword((String)req.getAttribute(fields[1]));

		verifyUserInfo(req, credentialVO, providerVO);
		return credentialVO;
	}

	public ExtAuthField[] getCredentialFields(HttpServletRequest req,
			SSOProviderVO providerVO, IUserVO userVO, PtCredentialVO credential)
			throws CredentialValidateException {
		Map<String, String> refMap = getRefMap(providerVO);
		if (refMap.isEmpty())
			throw new LfwRuntimeException("未配置任何单点登录的信息");

		String[] fields = checkUserIdAndPwd(refMap);
		ArrayList<String> credentialFields = getCredentialFields(refMap);
		ExtAuthField userName = null;
		ExtAuthField passWord = null;
		for (int i = 0; i < credentialFields.size(); i++) {
			String fieldKey = credentialFields.get(i);
			if (fieldKey.equals(fields[0]))
				userName = new TextExtAuthField("用户名", fieldKey, true);
			else if (fieldKey.equals(fields[1]))
				passWord = new PasswordExtAuthField("密码",	fieldKey, true);
		}

		return new ExtAuthField[] { userName, passWord };
	}

	public String getGateUrl(HttpServletRequest req, HttpServletResponse res,
			PtCredentialVO credential, SSOProviderVO providerVO)
			throws CredentialValidateException {

		String userId = credential.getUserid();
		String password = credential.getPassword();
		// 校验用户信息
		verifyUserInfo(req, credential, providerVO);

		// Portlet配置的gateUrl覆盖sso-prop.xml中配置的
		String formUrl = providerVO.getGateUrl();
		String portletId = (String) req.getAttribute("$portletId");
		Map<String, String> prefs = getPrefsMap(portletId);
		if (prefs.containsKey(GATEURL)) {
			String url = prefs.get(GATEURL);
			if (url != null && !url.equals(""))
				formUrl = url;
		}
		Logger.debug("===IntegrationLoginWithForm类getGateUrl方法:gateUrl=" + formUrl);

		Map<String, String> formFieldMap = new HashMap<String, String>();
		Map<String, String> refMap = getRefMap(providerVO);
		ArrayList<String> credentialFields = getCredentialFields(refMap);
		for (int i = 0; i < credentialFields.size(); i++) {
			if (credentialFields.get(i).startsWith(USERIDFIELD_PREFIX))
				formFieldMap.put(credentialFields.get(i).substring(
						USERIDFIELD_PREFIX.length()), userId);
			else if (credentialFields.get(i).startsWith(PASSWDFIELD_PREFIX))
				formFieldMap.put(credentialFields.get(i).substring(
						PASSWDFIELD_PREFIX.length()), password);
			else {
				String field = credentialFields.get(i);
				String value = providerVO.getValue(field);
				if (field.startsWith(FIELD_PREFIX))
					field = field.substring(FIELD_PREFIX.length());
				formFieldMap.put(field, value);
			}
		}
		req.getSession().setAttribute("fieldsMap" + portletId, formFieldMap);
		req.getSession().setAttribute("form_url" + portletId, formUrl);
		return "/portal/html/nodes/form_view.jsp?a=1&portletId=" + portletId;
	}

	public String getNodeGateUrl(HttpServletRequest req,
			HttpServletResponse res, String nodeId, PtCredentialVO credential,
			SSOProviderVO providerVO) throws CredentialValidateException {
		// TODO Auto-generated method stub
		return null;
	}

	public String verifyUserInfo(HttpServletRequest req,
			PtCredentialVO creadentialVO, SSOProviderVO providerVO)
			throws CredentialValidateException {
		// 所有指定的要验证的第三方系统字段
		Map<String, String> refMap = getRefMap(providerVO);
		ArrayList<String> fields = getCredentialFields(refMap);
		StringBuffer param = new StringBuffer();
		for (int i = 0; i < fields.size(); i++) {
			String fieldKey = fields.get(i);
			String realKey = getRealField(fieldKey);
			String realValue = null;
			if (fieldKey.startsWith(USERIDFIELD_PREFIX))
				realValue = creadentialVO.getUserid();
			else if (fieldKey.startsWith(PASSWDFIELD_PREFIX))
				realValue = creadentialVO.getPassword();
			else
				realValue = refMap.get(fieldKey);

			try {
				// 进行URL编码
				param.append(realKey + "=" + URLEncoder.encode(realValue, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				Logger.error(e, e);
				throw new CredentialValidateException(e);
			}
			if (i != fields.size() - 1)
				param.append("&");
		}
		Logger.debug("===IntegrationLoginWithForm类verifyUserInfo方法:param=" + param);

		// 校验
		String registryUrl = providerVO.getValue("registryUrl");
		if (registryUrl == null || registryUrl.equals(""))
			throw new CredentialValidateException("未获取registryUrl,请在单点配置中进行设置!");
		Logger
				.debug("===IntegrationLoginWithForm类verifyUserInfo方法:registryUrl="
						+ registryUrl);

		String returnFlag = null;
		try {
			returnFlag = registe(param.toString(), registryUrl);
			Logger.debug("===IntegrationLoginWithForm类verifyUserInfo方法:返回结果="
					+ returnFlag);
		} catch (Exception e) {
			Logger.error(e, e);
			throw new CredentialValidateException(e.getMessage());
		}

		String successFlag = refMap.get(SUCCESS_SIGN);
		String failureFlag = refMap.get(FAILURE_SIGN);
		if (returnFlag != null) {
			if ((failureFlag != null && returnFlag.indexOf(failureFlag) != -1)
					|| (successFlag != null && returnFlag.indexOf(successFlag) == -1))
				throw new CredentialValidateException("用户验证失败,用户名或密码有误");
		} else
			throw new CredentialValidateException("未获取任何校验返回值!");

		return returnFlag;
	}

	/**
	 * 获取portlet的个性化信息
	 * 
	 * @param portletId
	 * @return
	 */
	private Map<String, String> getPrefsMap(String portletId) {
		// 该处prefs不进行缓存,如果后台portlet的prefs修改,该处无法获得最新的prefs,另外此处缓存对效率没有大的提升
		Map<String, String> prefsMap = new HashMap<String, String>();
		PortletWindow win = new PortletWindowImpl(new PortletWindowID(portletId));
		Preferences preferences = PortalCacheManager.getPreferences(win);
		List<Preference> preferencelist = preferences.getPortletPreferences();
		if(preferencelist != null && !preferencelist.isEmpty()){
			for(Preference preference : preferencelist){
				String value = null;
				 if(preference.getValues() != null && preference.getValues().size() > 0)
				 value = preference.getValues().get(0);
				 prefsMap.put(preference.getName(), value);
			}
		}
		return prefsMap;
	}

	/**
	 * 校验是否配置了用户id和密码字段
	 * 
	 * @param portletId
	 * @return
	 * @throws PortletLoginException
	 */
	private String[] checkUserIdAndPwd(Map<String, String> prefs)
			throws CredentialValidateException {
		// 必须指定用户id字段
		Iterator<String> it = prefs.keySet().iterator();
		boolean hasUserIdField = false;
		String userIdField = null;
		while (it.hasNext()) {
			String key = it.next();
			if (key.startsWith(USERIDFIELD_PREFIX)) {
				hasUserIdField = true;
				userIdField = key;
				break;
			}
		}
		if (hasUserIdField == false)
			throw new CredentialValidateException("用户id字段必须以\"useridfield_\"开头!");

		// 必须指定用户密码字段
		boolean hasPasswordField = false;
		String passwordField = null;
		it = prefs.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key.startsWith(PASSWDFIELD_PREFIX)) {
				hasPasswordField = true;
				passwordField = key;
				break;
			}
		}
		if (hasPasswordField == false)
			throw new CredentialValidateException("用户密码字段必须以\"passwdfield_\"开头!");
		return new String[] { userIdField, passwordField };
	}

	/**
	 * 去掉前缀,返回第三方系统的真正字段名
	 * 
	 * @param fieldKey
	 * @return
	 */
	private String getRealField(String fieldKey) {
		String realKey = fieldKey;
		if (fieldKey.startsWith(FIELD_PREFIX))
			realKey = fieldKey.substring(FIELD_PREFIX.length());
		else if (fieldKey.startsWith(USERIDFIELD_PREFIX))
			realKey = fieldKey.substring(USERIDFIELD_PREFIX.length());
		else if (fieldKey.startsWith(PASSWDFIELD_PREFIX))
			realKey = fieldKey.substring(PASSWDFIELD_PREFIX.length());
		return realKey;
	}

	/**
	 * 获取sso中配置的所有单点集成信息
	 * 
	 * @param providerVO
	 * @return
	 */
	private HashMap<String, String> getRefMap(SSOProviderVO providerVO) {
		HashMap<String, String> refMap = new HashMap<String, String>();
		List<Reference> refs = providerVO.getProviderReference();
		if (refs != null) {
			for (int i = 0; i < refs.size(); i++) {
				Reference ref = refs.get(i);
				refMap.put(ref.getName(), ref.getValue());
			}
		}
		return refMap;
	}

	/**
	 * 是否为表单认证字段,其他字段为所需的内部字段
	 * 
	 * @param fieldName
	 * @return
	 */
	private boolean isCredentialFiled(String fieldName) {
		if (!fieldName.equals(SYSTEM_CODE) && !fieldName.equals(SUCCESS_SIGN)
				&& !fieldName.equals(FAILURE_SIGN)
				&& !fieldName.equals(USERID_PREFIX)
				&& !fieldName.equals(REGISTRY_URL))
			return true;
		else
			return false;
	}

	/**
	 * 获取第三方需要验证的所有字段
	 * 
	 * @param portletId
	 * @return
	 */
	private ArrayList<String> getCredentialFields(Map<String, String> refMap) {
		// 所有指定的要验证的第三方系统字段
		ArrayList<String> fields = new ArrayList<String>();
		Iterator<String> it = refMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (isCredentialFiled(key))
				fields.add(key);
		}
		return fields;
	}

	private String registe(String parameters, String registrUrl)
			throws IOException, PortletException {
		Logger.info("===registry URL:" + registrUrl);
		Logger.info("===registry parameters:" + parameters);
		// 构造BO注册URL
		URL preUrl = new URL(registrUrl);
		URLConnection uc = preUrl.openConnection();
		// 表明程序必须把名称/值对输出到服务器程序资源
		uc.setDoOutput(true);
		// 表明只能返回有用的信息
		uc.setUseCaches(false);
		// 设置Content-Type头部指示指定URL已编码数据的窗体MIME类型
		uc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 设置Content-Type头部指示指定URL已编码数据的窗体MIME类型
		uc.setRequestProperty("Content-Length", "" + parameters.length());
		// 提取连接的适当的类型
		HttpURLConnection hc = (HttpURLConnection) uc;
		// 把HTTP请求方法设置为POST（默认的是GET）
		hc.setRequestMethod("POST");
		// 输出内容
		OutputStream os = hc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeBytes(parameters);
		dos.flush();
		dos.close();
		// 获取BO对凭证的验证结果
		InputStream is = hc.getInputStream();
		String returnFlag = "";
		int ch;
		while ((ch = is.read()) != -1) {
			returnFlag += String.valueOf((char) ch);
		}
		if (is != null)
			is.close();
		return returnFlag;
	}
}
