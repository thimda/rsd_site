package nc.uap.portal.integrate.othersystem.nc;
 
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.itf.uap.sf.ICustomMenuQueryService;
import nc.login.bs.INCLoginService;
import nc.login.bs.INCUserQueryService;
import nc.login.bs.LoginVerifyBean;
import nc.login.vo.ILoginConstants;
import nc.login.vo.ISystemIDConstants;
import nc.login.vo.LoginRequest;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.authfield.ComboExtAuthField;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.authfield.PasswordExtAuthField;
import nc.uap.lfw.login.authfield.TextExtAuthField;
import nc.uap.portal.integrate.IWebAppFunNodesService;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.exception.PortletLoginException;
import nc.uap.portal.integrate.funnode.SsoSystemNode;
import nc.uap.portal.integrate.system.PortletRuntimeEnv;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;
import nc.vo.sm.cmenu.CustomMenuShortcutVO;
import nc.vo.sm.funcreg.FunRegisterConst;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.login.LoginFailureInfo;

import org.apache.commons.lang.StringUtils;


/**
 * @author yzy
 * @update lkp 采用lfw的登录方式
 * @author gd 2008-07-02 细化代码结构,加入详细的日志记录
 * @version NC5.6
 * @since NC5.0 
 */
public class NcLogin implements IWebAppLoginService ,IWebAppFunNodesService{
	  
	private static final String NULL_STRING = "NULL";
	public static Map<Integer,String> LOGIN_RSL_MAP  ;
	static{
		LOGIN_RSL_MAP = new HashMap<Integer,String>();
		LOGIN_RSL_MAP.put(0, "登录成功");
		LOGIN_RSL_MAP.put(1, "身份不合法");
		LOGIN_RSL_MAP.put(2, "名称错误");
		LOGIN_RSL_MAP.put(3, "密码错误");
		LOGIN_RSL_MAP.put(4, "用户被锁定");
		LOGIN_RSL_MAP.put(5, "用户已在线");
		LOGIN_RSL_MAP.put(6, "用户未到生效日期");
		LOGIN_RSL_MAP.put(7, "用户已到失效日期");
		LOGIN_RSL_MAP.put(8, "达到用户数上限");
		LOGIN_RSL_MAP.put(9, "用户未启用");
		LOGIN_RSL_MAP.put(10, "用户已停用");
		
		LOGIN_RSL_MAP.put(21, "业务中心有效");
		LOGIN_RSL_MAP.put(22, "业务中心被锁定");
		LOGIN_RSL_MAP.put(23, "业务中心还未到生效日期");
		LOGIN_RSL_MAP.put(24, "业务中心已到失效日期");
		LOGIN_RSL_MAP.put(25, "业务中心不存在");
	}
	
	public String getGateUrl(HttpServletRequest req, HttpServletResponse res, PtCredentialVO credential, SSOProviderVO provider)
			throws CredentialValidateException {
		try {
			String gateUrl = provider.getGateUrl();
			String ncUrl = provider.getValue("runtimeUrl");
			
			Logger.debug("===NCLogin类getGateUrl方法:获取原始gateUrl=" + gateUrl);
			Logger.debug("===NCLogin类getGateUrl方法:获取NC的runtimeUrl=" + ncUrl);
			
			// 从凭证中获得登录信息
			String usercode = credential.getUserid();
			String bc = credential.getCredentialReference().getValue("accountcode");
			String pkcorp = credential.getCredentialReference().getValue("pkcorp");
			String language = credential.getCredentialReference().getValue("language");
			String key = req.getSession().getId();
			StringBuffer parameters = new StringBuffer("ssoKey=" + key);
			parameters.append("&busiCenter=" + bc);
 			// 对用户名称和密码进行URL编码,确保特殊字符能够通过
			parameters.append("&userCode=" + URLEncoder.encode(usercode, "UTF-8"));
			parameters.append("&langCode=" + language);
			
			Logger.debug("===NCLogin类getGateUrl方法:获取的credential信息,usercode=" + usercode + ";accountcode=" + bc
					+ ";pkcorp=" + pkcorp + ";language=" + language + ";key=" + key);
			// 用户信息验证
			verifyUserInfo(req, credential, provider);
			// NC登陆信息注册
			Logger.debug("===NCLogin类getGateUrl方法:生成的注册参数信息=" + parameters.toString());
			String registeResult = ncRegiste(parameters.toString(), provider.getValue("registryUrl"));
			if(registeResult != null && registeResult.startsWith("Error"))
				throw new PortletLoginException(registeResult);
			
			// 进入NC系统
			String screenWidth,screenHeight;
			boolean hasSize = false;
			screenWidth = (String)req.getAttribute("screenWidth");
			screenHeight = (String)req.getAttribute("screenHeight");
			gateUrl += "?ssoKey=" + key + "&clienttype=portal";
			if(screenWidth != null && !screenWidth.trim().equals("")){
				gateUrl = gateUrl + "&width=" + screenWidth;
				hasSize = true;
			}
			if(screenHeight != null && !screenHeight.trim().equals("")){
				gateUrl = gateUrl + "&height=" + screenHeight;
				hasSize = true;
			}
			
			Logger.debug("===NCLogin类getGateUrl方法:获取最终的登录NC的gateUrl=" + gateUrl);
			if(hasSize)
				return gateUrl;
			
			return  LfwRuntimeEnvironment.getRootPath() + "/html/frame/nc.jsp?fwd=" + URLEncoder.encode(gateUrl, "UTF-8");
			
		} catch (IOException e) {
			Logger.error("===NCLogin类getGateUrl方法:登录发生错误,被集成服务器没有启动或网络连接不通!",e); 
			throw new CredentialValidateException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "NcLogin-000000")/*登录NC发生错误,被集成服务器没有启动或网络连接无法达到!*/);
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new CredentialValidateException("校验NC登录失败:" + e.getMessage());
		}
	}

	public PtCredentialVO credentialProcess(HttpServletRequest req, SSOProviderVO provider)
			throws CredentialValidateException {
		try {
			// 构造凭证VO
			PtCredentialVO credentialVO = new PtCredentialVO();
			String userId = (String)req.getAttribute("userid");
			String password =  (String)req.getAttribute("password");
			if(password == null)
				password = "";
			credentialVO.setUserid(userId);
			credentialVO.setPassword(password);
			// 构造其它信息（凭证Reference）
			String accountcode =  (String)req.getAttribute("accountcode");
			credentialVO.getCredentialReference().setValue("accountcode", accountcode);
			credentialVO.getCredentialReference().setValue("pkcorp", (String)req.getAttribute("pkcorp"));
			credentialVO.getCredentialReference().setValue("language", (String)req.getAttribute("language"));
			
			String ncUrl = provider.getValue("runtimeUrl");
			NCLocator locator = NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
			
			// 用户信息验证
			verifyUserInfo(req, credentialVO, provider);
			// 通过帐套查找数据源
			IBusiCenterManageService service = (IBusiCenterManageService)locator.lookup(IBusiCenterManageService.class.getName());
			BusiCenterVO bcVO = service.getBusiCenterByCode(accountcode);
			String dataSource = bcVO.getDataSourceName();
			
			credentialVO.getCredentialReference().setValue("datasource", dataSource);
			INCUserQueryService ncuqs = (INCUserQueryService)locator.lookup(INCUserQueryService.class.getName());
			
			// 通过用户信息查找用户的ID
			UserVO ncUser = ncuqs.findUserVO(dataSource, userId);
			credentialVO.getCredentialReference().setValue("ncgrouppk", ncUser.getPk_group());
			credentialVO.getCredentialReference().setValue("ncuserpk", ncUser.getPrimaryKey());
			return credentialVO;
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new CredentialValidateException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "NcLogin-000002", null, new String[]{e.getMessage()})/*制作进入NC系统的凭证失败:{0}*/, e);
		}
	}
	
	public String verifyUserInfo(HttpServletRequest req, PtCredentialVO credentialVO, SSOProviderVO providerVO) throws CredentialValidateException
	{
		
		 
		// 校验
		String ncUrl = providerVO.getValue("runtimeUrl");
		NCLocator locator = NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
		String language = credentialVO.getCredentialReference().getValue("language");
		String accountcode = credentialVO.getCredentialReference().getValue("accountcode");
		String pkcorp = credentialVO.getCredentialReference().getValue("pkcorp");
		String userId = credentialVO.getUserid();
		String password = credentialVO.getPassword();
		String workdate = req.getParameter("workdate");
		UFDate tick = new UFDate(new Date().getTime());
		INCLoginService loginService = (INCLoginService)locator.lookup(INCLoginService.class.getName());
		LoginRequest loginReq = new LoginRequest();
		loginReq.setBusiCenterCode(accountcode);
		loginReq.setLangCode(language);
		loginReq.setUserCode(userId);
		loginReq.setUserPWD(password);
		
		LoginVerifyBean verifyBean = new LoginVerifyBean(ISystemIDConstants.NCSYSTEM);
		verifyBean.setStaticPWDVerify(true);
		
		if(workdate == null || "".equals(workdate))
			workdate = tick.toString();
		try {
			int verifyResult = loginService.verify(loginReq, verifyBean);
			if (verifyResult != ILoginConstants.USER_IDENTITY_LEGAL) {
				if(LOGIN_RSL_MAP.containsKey(verifyResult))
					throw new CredentialValidateException(LOGIN_RSL_MAP.get(verifyResult));
				else
					throw new CredentialValidateException("未知错误");
			}
		} catch (BusinessException e) {
			Logger.error(e, e);
			throw new CredentialValidateException(e.getMessage());
		}
		return null;
	}

	/**
	 * 向NC系统注册登录凭证
	 */
	private String ncRegiste(String parameters, String registrUrl)
			throws IOException {
		
		
		// 构造NC注册URL
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
		
		// 获取NC对凭证的验证结果
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

	/**
	 * 用户可以不在sso-provider.xml中配置AccountCode信息,如果不配置,则采用当前被集成
	 * NC的所有帐套信息
	 */
	public   ExtAuthField[] getCredentialFields(HttpServletRequest req, SSOProviderVO provider, nc.uap.portal.user.entity.IUserVO userVO, PtCredentialVO credential) throws CredentialValidateException {
	    Map<String, String> userInputMap = getUserInputValue(req);
	    
		//获取已经存在的credential
		//设置属性
		ExtAuthField[] fields = new ExtAuthField[4];
		fields[0] = new ComboExtAuthField("系统", "accountcode", true);
		fields[1] = new TextExtAuthField("用户", "userid", true);
		fields[2] = new PasswordExtAuthField("密码", "password", false);
		fields[3] = new ComboExtAuthField("语言", "language", true);
		//fields[5] = new DateExtAuthField("日期", "workdate", true); 
		fields[0].setRequired(true);
		fields[1].setRequired(true);
		fields[2].setRequired(true);
		fields[3].setRequired(true);
		
		initAccountCodeField(((ComboExtAuthField)fields[0]), userInputMap, provider, userVO, credential);
		initLanageField(userInputMap, (ComboExtAuthField)fields[3], credential);
		return fields;
	}
	
	/**
	 * 当在用户输入信息错误时再进入获取认证域方法时通过
	 * 此方法获取用户之前输入的信息。
	 * 
	 * @param req
	 * @return
	 */
	private Map<String, String> getUserInputValue(HttpServletRequest req)
	{
		Map<String, String> map = new HashMap<String, String>();
		String accountcode = req.getParameter("accountcode") == null ? NULL_STRING : req.getParameter("accountcode");
		String pkcorp = req.getParameter("pkcorp") == null ? NULL_STRING : req.getParameter("pkcorp");
		String username = req.getParameter("userid") == null ? NULL_STRING : req.getParameter("userid");
		String password = req.getParameter("password") == null ? NULL_STRING : req.getParameter("password");
		String language = req.getParameter("language") == null ? NULL_STRING : req.getParameter("language");
		
		map.put("accountcode", accountcode);
		map.put("pkcorp", pkcorp);
		map.put("userid", username);
		map.put("password", password);
		map.put("language", language);
		return map;
	}

	/**
	 * 页面显示的帐套原则优先顺序 ： 用户输入的>用户登录时输入的帐套（NCUserProvider登录系统时） > 已有的credential中的帐套信息。
	 * 如果用户在sso-provider.xml没有配置当前使用的帐套，则使用当前被集成NC系统的所有有效帐套。
	 * 
	 * @param accountField
	 * @param req
	 * @param provider
	 * @param userVO
	 * @param credential
	 * @return 返回当前设置的默认帐套，用于设置公司参照
	 */
    private String initAccountCodeField(ComboExtAuthField accountField, Map<String, String> userInputMap, SSOProviderVO provider, nc.uap.portal.user.entity.IUserVO userVO, PtCredentialVO credential)
    {
    	String defaultAccount = null;
	    	 
	    String runtimeUrl = provider.getValue("runtimeUrl");
		//String accountCode = !userInputMap.get("accountcode").equals(NULL_STRING) ? userInputMap.get("accountcode") : userVO == null ? null : (String)userVO.getUserReference("accountCode");
	    String accountCode = userInputMap.get("accountcode");
		if(accountCode == null || accountCode.trim().equals(""))
		{
			if(credential != null)
			{
				accountCode = credential.getCredentialReference().getValue("accountcode");
			}
		}
		
		String accountStr = provider.getValue("AccountCode");
		if(accountStr == null || accountStr.equals(""))
			return null;
		
		// 获取sso-provider.xml中配置的
		String[] xmlAccountValues = accountStr.split(",");
		Logger.debug("===NcLogin类initAccountCodeField方法:在provider中获取配置的所有帐套编码个数=" + xmlAccountValues.length);
	
		// 如果sso-provider.xml中没有配置AccountCode属性，则使用被集成NC的所有帐套
		Map<String, String> allAccounts = new HashMap<String, String>();
		BusiCenterVO[] ncAccounts =  null;
		try {
			IBusiCenterManageService accountService = (IBusiCenterManageService)NCLocator.getInstance(
	                   								PortletRuntimeEnv.getInstance().getNcProperties(runtimeUrl)).lookup(IBusiCenterManageService.class.getName());
		    ncAccounts = accountService.getBusiCenterVOs();
		}
		catch(Exception e) {
		    Logger.error("===NcLogin类initAccountCodeField方法:获取所有帐套信息时出错," + e.getMessage());	
		}
		
		if(ncAccounts != null)
		{
			for(int i = 0;i < ncAccounts.length; i++)
			   allAccounts.put(ncAccounts[i].getCode(), ncAccounts[i].getName());
		}
		
		if(xmlAccountValues == null || xmlAccountValues.length == 0)
			xmlAccountValues = allAccounts.keySet().toArray(new String[0]);
		
		if (xmlAccountValues != null) {
			int selected = -1;
			String[][] options = new String[xmlAccountValues.length][2];
			for (int i = 0; i < xmlAccountValues.length; i++) {
				options[i][0] = xmlAccountValues[i];
				options[i][1] = allAccounts.containsKey(xmlAccountValues[i]) ? allAccounts.get(xmlAccountValues[i]) : xmlAccountValues[i];
				if (xmlAccountValues[i] != null && xmlAccountValues[i].equals(accountCode)) {
					selected = i;
				}
			}
			if(selected == -1)
				selected = 0;
			accountField.setOptions(options);
			accountField.setSelectedIndex(selected);
			
			defaultAccount = xmlAccountValues[selected];
	    }
		
		Logger.debug("===NcLogin类initAccountCodeField方法:获取默认帐套=" + defaultAccount);
		return defaultAccount;
    }
    
//    /**
//     * 初始化公司参照
//     * 优先级：用户输入的 > 帐套信息
//     * 
//     * @param corpField
//     */
//    private void initCorpField(RefExtAuthField corpField, String defaultAccount, SSOProviderVO provider, PtCredentialVO credential, Map<String, String> userInputMap){
//    	
//    	Logger.debug("===NCLogin类initCorpField方法:进入NcLogin的initCorpField方法,defaultAccount=" + defaultAccount);
//    	corpField.setPageMeta("reference.portalnccorp.pagemeta");
//    	corpField.setPath("reference/ncCorpRefTree.jsp");
//    	corpField.setReadDs("corpds");
//    	corpField.setReadFields("pk_corp");
//    	corpField.setWriteFields("pkcorp");
//		 
//    	try	{
//	    		String runtimeUrl = provider.getValue("runtimeUrl");
//				Logger.debug("===NCLogin类initCorpField方法:获得runtimeUrl=" + runtimeUrl);
//				   
//				IConfigFileService accountService = (IConfigFileService)NCLocator.getInstance(
//			                   PortletRuntimeEnv.getInstance().getNcProperties(runtimeUrl)).lookup(IConfigFileService.class.getName());
//				Logger.debug("===NCLogin类initCorpField方法:成功获得IConfigFileService的服务!" );
//				   
//				String defaultDs = accountService.getAccountByCode(defaultAccount).getDataSourceName();   
//				Logger.debug("===NCLogin类initCorpField方法:根据帐套" + defaultAccount + "获得默认数据源=" + defaultDs);
//				   
//				// 设置公司参照的缺省数据源
//				corpField.setDatasource(defaultDs);
//			}
//    		catch(Exception e) {
//				Logger.error("===NCLogin类initCorpField方法:获取默认数据源时出现错误,不影响使用," + e.getMessage(), e);
//			}
//			
//			String pkcorp = !userInputMap.get("pkcorp").equals(NULL_STRING) ? userInputMap.get("pkcorp") : null;
//			if(pkcorp == null && credential != null)
//				pkcorp = credential.getCredentialReference().getValue("pkcorp");
//			
//			Logger.debug("===NCLogin类initCorpField方法:获取pkcorp的默认值=" + pkcorp);
//			corpField.setDefaultValue(pkcorp);		
//    }

//    /**
//     * 初始化用户名/密码信息，优先原则：用户输入 > 已有凭证 > 当前登录用户信息（NCUserProvider的情况）
//     * 
//     * @param userIdField
//     * @param pwdField
//     * @param credential
//     * @param user
//     * @param isLoginProvider
//     */
//    private void initUserAndPwdField(Map<String, String> userInputMap, TextExtAuthField userIdField, PasswordExtAuthField pwdField, PtCredentialVO credential, PtUserVO user)
//    {
//    	String userId = !userInputMap.get("userid").equals(NULL_STRING) ? userInputMap.get("userid") : null;
//    	String password = !userInputMap.get("password").equals(NULL_STRING) ? userInputMap.get("password") : null;
//    	
//    	if(userId == null && credential != null)
//    		userId = credential.getUserid();
//    	if(password == null && credential != null)
//    		password = credential.getPassword();
//
//    	Logger.debug("===NCLogin类initUserAndPwdField方法:获取缺省用户名/密码=" + userId + "/" + password);
//    	
//    	userIdField.setDefaultValue(userId);
//    	pwdField.setDefaultValue(password);
//    }

    /**
     * 初始化语言设置选项
     * 
     * @param userInputMap
     * @param languageField
     * @param credential
     */
    private void initLanageField(Map<String, String> userInputMap, ComboExtAuthField languageField, PtCredentialVO credential)
    {
	    String lanaguage = "simpchn";
//	    if(!userInputMap.get("language").equals(NULL_STRING))
//	    	lanaguage = userInputMap.get("language");
	    
//	    if(userInputMap.get("language").equals(NULL_STRING) && credential != null)
	    if(credential != null)
	    	lanaguage = credential.getCredentialReference().getValue("language");
	    
		String[][] options = new String[3][2];
		options[0][0] = "simpchn";
		options[0][1] = "简体中文";
		options[1][0] = "tradchn";
		options[1][1] = "繁體中文";
		options[2][0] = "english";
		options[2][1] = "English";
		int selected = 0;
		for (int i = 0; i < options.length; i++) {
			if (options[i][0].equals(lanaguage)) {
				selected = i;
			}
		}
		languageField.setOptions(options);
		languageField.setSelectedIndex(selected);
    }	

	public String getNodeGateUrl(HttpServletRequest req,
			HttpServletResponse res, String nodeId, PtCredentialVO credential,
			SSOProviderVO provider) throws CredentialValidateException {
		
		String gateUrl = provider.getGateUrl();
		String ncUrl = provider.getValue("runtimeUrl");
		
		Logger.debug("===NCLogin类getGateUrl方法:获取原始gateUrl=" + gateUrl);
		Logger.debug("===NCLogin类getGateUrl方法:获取NC的runtimeUrl=" + ncUrl);
		
		// 从凭证中获得登录信息
		String usercode = credential.getUserid();
		String accountcode = credential.getCredentialReference().getValue("accountcode");
		String pkcorp = credential.getCredentialReference().getValue("pkcorp");
		String language = credential.getCredentialReference().getValue("language");
		String key = req.getSession().getId();
		StringBuffer parameters = new StringBuffer("ssoKey=" + key);
		//?userCode=wb&busiCenter=ptagg
		parameters.append("&busiCenter=" + accountcode);
			// 对用户名称和密码进行URL编码,确保特殊字符能够通过
		try {
			parameters.append("&userCode=" + URLEncoder.encode(usercode, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		Logger.debug("===NCLogin类getGateUrl方法:获取的credential信息,usercode=" + usercode + ";accountcode=" + accountcode
				+ ";pkcorp=" + pkcorp + ";language=" + language + ";key=" + key);
		// 用户信息验证
		verifyUserInfo(req, credential, provider);
		// NC登陆信息注册
		Logger.debug("===NCLogin类getGateUrl方法:生成的注册参数信息=" + parameters.toString());
		String registeResult = null;
		try {
			registeResult = ncRegiste(parameters.toString(), provider.getValue("registryUrl"));
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		if(registeResult != null && registeResult.startsWith("Error"))
			throw new CredentialValidateException(registeResult);
		
		// 进入NC系统
		int clientWidth = 2048;
		int clientHeight = 1436;
		String screenWidth,screenHeight;
		screenWidth = (String)req.getAttribute("screenWidth");
		screenHeight = (String)req.getAttribute("screenHeight");
		if(screenWidth != null && !screenWidth.trim().equals(""))
			clientWidth = Integer.parseInt(screenWidth);
		if(screenHeight != null && !screenHeight.trim().equals(""))
			clientHeight = Integer.parseInt(screenHeight);
		
		gateUrl += "?ssoKey=" + key + "&clienttype=portal&width=" + clientWidth + "&height=" + clientHeight;
		gateUrl += "&uiloader=nc.uap.lfw.applet.NodeUILoader&nodeid=" + nodeId;
		Logger.debug("===NCLogin类getNodeGateUrl方法:获取最终的登录NC的gateUrl=" + gateUrl);
		return gateUrl;
	}

	@Override
	public SsoSystemNode[] getAllFunNodes(SSOProviderVO provider){
		NCLocator locator = getLocator(provider);
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		String[][] bcs = getAliveDataSourceName(provider);
		IFuncRegisterQueryService frq = (IFuncRegisterQueryService)locator.lookup(IFuncRegisterQueryService.class.getName());
		List<SsoSystemNode> nodes = new ArrayList<SsoSystemNode>();
		
		if(bcs != null){
			try {
				for(String[] rmtDs : bcs){
					InvocationInfoProxy.getInstance().setUserDataSource(rmtDs[0]);
					try {
						FuncRegisterVO[] vos = frq.queryAllNCFunction(false);
						if(vos != null && vos.length > 0)
							for(FuncRegisterVO regVO:vos){
								SsoSystemNode node = funcRegister2SystemNode(regVO);
								node.setNodeName("[帐套：" + rmtDs[1] + "]" + node.getNodeName());
								nodes.add(node);
							}
					} catch (Exception e) {
						LfwLogger.error(e.getMessage(),e);
					}
				}
			} catch (Exception e) {
				LfwLogger.error(this.getClass().getName()+"#getAllFunNodes===获得NC全部功能节点异常:"+e.getMessage() , e);
			}finally{
				InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
			}
			
		}
		return nodes.toArray(new SsoSystemNode[0]);
	}

	@Override
	public SsoSystemNode[] getUserNodes(SSOProviderVO provider,PtCredentialVO credential) {
		String cuserid =credential.getCredentialReference().getValue("ncuserpk");
	    String currentDs = LfwRuntimeEnvironment.getDatasource();
	    NCLocator locator = getLocator(provider);
	    /**
	     * 业务中心
	     */
	    String bc = credential.getCredentialReference().getValue("accountcode");
		try {
			/**
			 * 获得NC端数据源
			 */
			String rmtDs = locator.lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			ICustomMenuQueryService cmqs = locator.lookup(ICustomMenuQueryService.class);
			/**
			 * 查询条件
			 */
			CustomMenuShortcutVO conditionVO = new CustomMenuShortcutVO();
			conditionVO.setUserId(cuserid);
			CustomMenuShortcutVO[]  shortcuts = cmqs.queryShortcutsByVO(conditionVO, new Boolean(true));
			if(shortcuts != null && shortcuts.length > 0){
				List<SsoSystemNode> nodes=new ArrayList<SsoSystemNode>();
				for(CustomMenuShortcutVO regVO:shortcuts){
					nodes.add(shortcut2SystemNode(regVO));
				}
				return nodes.toArray(new SsoSystemNode[0]);
			}
		} catch (Exception e) {
			LfwLogger.error(this.getClass().getName() + "查询功能节点错误!", e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		return null;
	}
	/**
	 * 获得活动的数据源名称
	 * @return
	 */
	protected String[][] getAliveDataSourceName(SSOProviderVO provider){
		Set<String[]> avliableDs = new HashSet<String[]>();
		try {
			BusiCenterVO[] bcs = getLocator(provider).lookup(IBusiCenterManageService.class).getBusiCenterVOs();
			if(bcs != null){
				for(BusiCenterVO bc : bcs){
					if(isBcAlive(bc))
						avliableDs.add(new String[]{bc.getDataSourceName(), bc.getName()});
				}
			}
		} catch (Exception e) {
			LfwLogger.error(this.getClass().getName()+"#getAliveDataSourceName===NC获得活动数据源名称出现异常:" + e.getMessage() , e);
		}
		if(avliableDs.isEmpty())
			avliableDs.add(new String[]{"design", "design"});
		return avliableDs.toArray(new String[0][0]);
	}
	/**
	 * 获得被集成NC的Locator
	 * @return
	 */
	protected NCLocator getLocator(SSOProviderVO provider){
		String ncUrl = provider.getValue("runtimeUrl");
		NCLocator locator = NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
		return locator;
	}
	/**
	 * 检查系统是否活动
	 * @param bc
	 * @return
	 */
	private boolean isBcAlive(BusiCenterVO bc){
		UFDate now = new UFDate();
		return !bc.isLocked() && StringUtils.isNotBlank(bc.getDataSourceName()) && bc.getEffectDate().before(now) && bc.getExpireDate().after(now);
	}
	/**
	 * NC功能节点转换为Portal集成节点
	 * @param regVO
	 * @return
	 */
	private SsoSystemNode funcRegister2SystemNode(FuncRegisterVO regVO){
		SsoSystemNode node = new SsoSystemNode();
		node.setNodeCode(regVO.getFuncode());
		node.setNodeName(regVO.getFun_name());
		node.setNodeId(regVO.getPrimaryKey());
		if(regVO.getFun_property() != null && regVO.getFun_property().equals(FunRegisterConst.LFW_FUNC_NODE)){
			node.setNodeType(0);
		}else{
			node.setNodeType(1);
		}
		return node;
	}
	/**
	 * 将NC快捷方式转换为Portal集成节点
	 * @param regVO
	 * @return
	 */
	private SsoSystemNode shortcut2SystemNode(CustomMenuShortcutVO regVO){
		SsoSystemNode node = new SsoSystemNode();
		node.setNodeCode(regVO.getFuncId());
		node.setNodeName(regVO.getIconText());
		node.setNodeId(regVO.getPrimaryKey());
		return node;
	}
	
	 
//    /**
//     * 初始化登录日期，如果用户输入，以用户输入为准，如果没有，则记录当前时间
//     * @param field
//     * @param req
//     */
//    private void initWorkdate(DateExtAuthField field, HttpServletRequest req)
//    {
//    	UFDate tick = new UFDate(new Date().getTime());
//    	String workdate = req.getParameter("workdate");
//    	if(workdate != null && !"".equals(workdate))
//    		field.setDefaultValue(workdate);
//    	else
//    		field.setDefaultValue(tick.toString());
//    }
}
