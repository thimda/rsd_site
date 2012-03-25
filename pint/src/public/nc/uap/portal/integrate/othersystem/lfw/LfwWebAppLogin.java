package nc.uap.portal.integrate.othersystem.lfw;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.vo.LfwFunNodeVO;
import nc.uap.portal.integrate.IWebAppFunNodesService;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.funnode.SsoSystemNode;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;

import org.apache.commons.io.IOUtils;

/**
 * LFW项目集成类
 * 
 * @author licza
 * 
 */
public class LfwWebAppLogin implements IWebAppLoginService,
		IWebAppFunNodesService {
	static final String contentType = "content-type";
	static final String contentTypeValue = "application/x-java-serialized-object,charset=utf-8";
	static final String servletURL = "/registerServlet";
	/** 动态验证字段 **/
	public static final String VERIFY_FIELDS = "verifyFields";
	/** 用户名字段 **/
	public static final String USER_ID_FIELD = "UserIdField";
	/** 密码字段 **/
	public static final String PWD_FIELD = "PwdField";
	/** 数据源 **/
	public static final String DS_NAME = "dsname";
	
	private static final String REGISTR_URL = "registrUrl";
	static final int TYPE_USER_FUNNODE = 3;
	static final int TYPE_ALLFUNNODE = 4;
	private static final String PUT = "PUT";
 
	/**
	 * 要验证的字段
	 */
	@Override
	public PtCredentialVO credentialProcess(HttpServletRequest req,
			SSOProviderVO provider) throws CredentialValidateException {
		try {
			// 构造凭证VO
			PtCredentialVO credentialVO = new PtCredentialVO();
			String userId = (String) req.getAttribute(getUserIdFieldName(req));
			String password = (String) req
					.getAttribute(getUserPwdFieldName(req));
			if (password == null)
				password = "";
			credentialVO.setUserid(userId);
			credentialVO.setPassword(password);

			String[] verifyFields = getVerifyField(req);
			for (String field : verifyFields) {
				credentialVO.getCredentialReference().setValue(field,
						(String) req.getAttribute(field));
			}
			// 用户信息验证
			verifyUserInfo(req, credentialVO, provider);

			return credentialVO;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			String msg = e.getMessage();
			if(msg == null)
				msg = "制作进入系统的凭证失败!";
			throw new CredentialValidateException(msg, e);
		}
	}

	@Override
	public ExtAuthField[] getCredentialFields(HttpServletRequest req,
			SSOProviderVO providerVO, IUserVO userVO, PtCredentialVO credential)
			throws CredentialValidateException {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String serverUrl = getServerURL(providerVO);
		try {
			URL url = new URL(serverUrl);
			URLConnection conn = url.openConnection();
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).setChunkedStreamingMode(2048);
				((HttpURLConnection) conn).setRequestMethod("PUT");
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty(contentType, contentTypeValue);
			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeInt(0);
			oos.writeUTF(providerVO.getValue(DS_NAME));
			oos.flush();
			oos.close();
			ois = new ObjectInputStream(conn.getInputStream());
			Object obj = ois.readObject();
			if (obj instanceof Exception) {
				throw (Exception) obj;
			}
			return (ExtAuthField[]) obj;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage() + "===" + serverUrl,e);
			throw new CredentialValidateException(e);
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(ois);
		}
	}

	@Override
	public String getGateUrl(HttpServletRequest req, HttpServletResponse res,
			PtCredentialVO credential, SSOProviderVO providerVO)
			throws CredentialValidateException {
		String gateUrl = providerVO.getGateUrl();
		String key = registe(req, credential, providerVO);
			gateUrl += (gateUrl.indexOf("?") > 0 ? "&" : "?") + "ssoKey=" + key
			+ "&clienttype=portal&dsname="+providerVO.getValue("dsname");
		return gateUrl;
	}

	@Override
	public String getNodeGateUrl(HttpServletRequest req,
			HttpServletResponse res, String gateUrl, PtCredentialVO credential,
			SSOProviderVO providerVO) throws CredentialValidateException {
		String key = registe(req, credential, providerVO);
		return gateUrl  + (gateUrl.indexOf("?") > 0 ? "&" : "?")+"ssoKey=" + key + "&clienttype=portal&dsname="+providerVO.getValue("dsname") ;
		
	}

	@Override
	public String verifyUserInfo(HttpServletRequest req,
			PtCredentialVO credentialVO, SSOProviderVO providerVO)
			throws CredentialValidateException {
		try {
			String parameters = getAuthString(1, credentialVO, providerVO, req);
			URL preUrl = new URL(getServerURL(providerVO));
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

			// 获取NC对凭证的验证结果
			InputStream is = hc.getInputStream();
			String returnFlag = IOUtils.toString(is, "utf-8");		
			IOUtils.closeQuietly(is);
			if(returnFlag.startsWith("f:")){
				throw new SecurityException(returnFlag.substring(2));
			}
			return returnFlag;
		} catch (Exception e) {
			LfwLogger.error("===LfwWebAppLogin类verifyUserInfo方法:"
					+ e.getMessage(), e);
			throw new CredentialValidateException(e.getMessage());
		}
	}

	/**
	 * 注册
	 * 
	 * @param req
	 * @param credentialVO
	 * @param providerVO
	 * @return
	 * @throws CredentialValidateException
	 */
	public String registe(HttpServletRequest req, PtCredentialVO credentialVO,
			SSOProviderVO providerVO) throws CredentialValidateException {
		String url = getServerURL(providerVO);
		String parameters = getAuthString(2, credentialVO, providerVO, req);
		try {
			URL preUrl = new URL(url);
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

			// 获取NC对凭证的验证结果
			InputStream is = hc.getInputStream();
			String returnFlag = IOUtils.toString(is, "utf-8");		
			IOUtils.closeQuietly(is);
			if(returnFlag.startsWith("f:")){
				throw new SecurityException(returnFlag.substring(2));
			}
			return returnFlag.substring(2);
		} catch (Exception e) {
			LfwLogger.error("===LfwWebAppLogin类registe方法发生异常:" + e.getMessage() +"  " + url +" " + parameters , e);
			throw new CredentialValidateException(e.getMessage());
		}
	}

	public String getUserIdFieldName(HttpServletRequest req) {
		return req == null ? null : (String) req.getAttribute(USER_ID_FIELD);
	}

	public String getUserPwdFieldName(HttpServletRequest req) {
		return req == null ? null : (String) req.getAttribute(PWD_FIELD);
	}

	/**
	 * 获得其他的要验证的字段
	 * 
	 * @param providerVO
	 * @return
	 */
	private String[] getVerifyField(HttpServletRequest req) {
		String verifyFields = req == null ? null : (String) req.getAttribute(VERIFY_FIELDS);
		if (verifyFields != null)
			return verifyFields.split(",");
		return new String[0];
	}

	/**
	 * 获得验证串
	 * 
	 * @param credentialVO
	 * @param req
	 * @return
	 */
	private String getAuthString(int type, PtCredentialVO credentialVO,SSOProviderVO providerVO,
			HttpServletRequest req) {
		StringBuffer sb = new StringBuffer("type=");
		sb.append(type);
		sb.append("&dsname=");
		sb.append(providerVO.getValue(DS_NAME));
		String userIdField = getUserIdFieldName(req);
		if (userIdField != null) 
		{
			/**
			 * 用户名
			 */
			sb.append("&");
			sb.append(getUserIdFieldName(req));
			sb.append("=");
			sb.append(credentialVO.getUserid());
			String userPwdField = getUserPwdFieldName(req);
			/**
			 * 密码
			 */
			if (userPwdField != null) {
				sb.append("&");
				sb.append(getUserPwdFieldName(req));
				sb.append("=");
				sb.append(credentialVO.getPassword());
			}
			/**
			 * 其他参数
			 */
			String[] verifyFields = getVerifyField(req);
			if (verifyFields != null) {
				for (String field : verifyFields) {
					String value = credentialVO.getCredentialReference().getValue(field);
					sb.append("&");
					sb.append(field);
					sb.append("=");
					sb.append(value);
				}
			}
		} 
		else 
		{
			Map<String, String[]> map = credentialVO.getCredentialReference().getMap();
			if(map != null && !map.isEmpty())
				for (String key : map.keySet()) {
					String[] val = map.get(key);
					sb.append("&");
					sb.append(key);
					sb.append("=");
					sb.append((val != null && val.length > 0) ? val[0] : "");
				}
		}
		return sb.toString();
	}

	/**
	 * 获得服务器地址
	 * 
	 * @param providerVO
	 * @return
	 */
	private String getServerURL(SSOProviderVO providerVO) {
		String serverUrl = providerVO.getValue("registrUrl");
		return serverUrl;
	}

	@Override
	public SsoSystemNode[] getAllFunNodes(SSOProviderVO provider) {
		LfwFunNodeVO[] fns = getRegFunNodes(TYPE_ALLFUNNODE, provider, null);
		return translet(fns);
	}

	@Override
	public SsoSystemNode[] getUserNodes(SSOProviderVO provider,
			PtCredentialVO credential) throws CredentialValidateException {
		verifyUserInfo(null, credential, provider);
		LfwFunNodeVO[] fns = getRegFunNodes(TYPE_USER_FUNNODE, provider, credential);
		return translet(fns);
	}

	/**
	 * 从registerServlet中获得功能节点
	 * 
	 * @param type
	 * @param userid
	 * @return
	 */
	public LfwFunNodeVO[] getRegFunNodes(int type, SSOProviderVO providerVO,
			PtCredentialVO credential) {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		String serverURL = providerVO.getValue(REGISTR_URL);
		try {
			URL url = new URL(serverURL);
			URLConnection conn = url.openConnection();
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).setChunkedStreamingMode(2048);
				((HttpURLConnection) conn).setRequestMethod(PUT);
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty(contentType, contentTypeValue);
			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeInt(type);
			oos.writeUTF(providerVO.getValue(DS_NAME));
			if (credential != null){
				oos.writeObject(getParamMap(credential));
			}
			oos.flush();
			oos.close();
			ois = new ObjectInputStream(conn.getInputStream());
			Object obj = ois.readObject();
			if (obj instanceof Exception) {
				throw (Exception) obj;
			}
			LfwFunNodeVO[] fns = (LfwFunNodeVO[]) obj;
			return fns;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(ois);
		}
		return new LfwFunNodeVO[0];
	}
	
	/**
	 * 从Credential中获得参数
	 * @param credential
	 * @return
	 */
	protected HashMap<String, String> getParamMap(PtCredentialVO credential){
		HashMap<String, String> param = new HashMap<String, String>();
		Map<String, String[]> map = credential.getCredentialReference().getMap();
		if(map != null && !map.isEmpty()){
			for (String key : map.keySet()) {
				String[] val = map.get(key);
				if(val != null && val.length > 0){
					param.put(key, val[0]);
				}
			}
		}
		return param;
	}
	
	/**
	 * 转换lfw功能节点为portal功能节点
	 * 
	 * @param fns
	 * @return
	 */
	private SsoSystemNode[] translet(LfwFunNodeVO[] fns) {
		List<SsoSystemNode> ssns = new ArrayList<SsoSystemNode>();
		if (fns != null && fns.length > 0) {
			for (LfwFunNodeVO fn : fns) {
				SsoSystemNode ssn = new SsoSystemNode();
				ssn.setNodeId(fn.getPk_funnode());
				ssn.setNodeUrl(fn.getLinkurl());
				ssn.setNodeName(fn.getTitle());
				ssns.add(ssn);
			}
		}
		return ssns.toArray(new SsoSystemNode[0]);
	}
}
