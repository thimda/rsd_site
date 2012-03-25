package nc.uap.portal.integrate.othersystem.nc5x;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.authfield.ComboExtAuthField;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.authfield.PasswordExtAuthField;
import nc.uap.lfw.login.authfield.RefExtAuthField;
import nc.uap.lfw.login.authfield.TextExtAuthField;
import nc.uap.portal.integrate.IWebAppFunNodesService;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.exception.PortletLoginException;
import nc.uap.portal.integrate.funnode.SsoSystemNode;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.ToolKit;
import nc.vo.ml.IProductCode;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.cmenu.CustomMenuShortcutVO;
import nc.vo.sm.funcreg.FunRegisterConst;
import nc.vo.sm.funcreg.FuncRegisterVO;

import org.apache.commons.lang.StringUtils;

/**
 * NC5.x集成类
 * @author licza
 *
 */
public class NC5xLogin implements IWebAppLoginService,IWebAppFunNodesService{
	private static final String NULL_STRING = "NULL";
	@Override
	public PtCredentialVO credentialProcess(HttpServletRequest req,
			SSOProviderVO provider) throws CredentialValidateException {
		try {
			// 构造凭证VO
			PtCredentialVO credentialVO = new PtCredentialVO();
			String userId = (String)req.getAttribute("userid");
			String password = (String)req.getAttribute("password");
			if(password == null)
				password = "";
			credentialVO.setUserid(userId);
			credentialVO.setPassword(password);
			String pkcorp = StringUtils.defaultIfEmpty((String)req.getAttribute("pkcorp"),"0001");
			// 构造其它信息（凭证Reference）
			String accountcode = (String)req.getAttribute("accountcode");
			credentialVO.getCredentialReference().setValue("accountcode", accountcode);
			credentialVO.getCredentialReference().setValue("pkcorp", pkcorp);
			credentialVO.getCredentialReference().setValue("language", (String)req.getAttribute("language"));
			
			// 用户信息验证
			verifyUserInfo(req, credentialVO, provider);
//			// 通过帐套查找数据源
//			credentialVO.getCredentialReference().setValue("datasource", nc5xDs);
		
			
			String ncuserpk = verifyUserInfo(req, credentialVO, provider);
			// 通过用户信息查找用户的ID
			credentialVO.getCredentialReference().setValue("ncuserpk", ncuserpk);
			return credentialVO;
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new CredentialValidateException( " 制作进入NC系统的凭证失败:" + e.getMessage());
		}  
	}
	
	public ExtAuthField[] getCredentialFields(HttpServletRequest req, SSOProviderVO provider, IUserVO userVO, PtCredentialVO credential) {
	    Logger.debug("===NCLogin类getCredentialFields方法:进入NCLogin的getCredentialFields()方法!");
	    Map<String, String> userInputMap = getUserInputValue(req);
	    
		//获取已经存在的credential
		//设置属性
		ExtAuthField[] fields = new ExtAuthField[5];
		fields[0] = new ComboExtAuthField("帐套", "accountcode", true);
		fields[1] = new RefExtAuthField("公司", "pkcorp", false);
		fields[2] = new TextExtAuthField("用户", "userid", true);
		fields[3] = new PasswordExtAuthField("密码", "password", false);
		fields[4] = new ComboExtAuthField("语言", "language", true);
		fields[0].setRequired(true);
		fields[2].setRequired(true);
		fields[3].setRequired(true);
		fields[4].setRequired(true);
		//fields[5].setRequired(false);
		
		String defaultAccount = initAccountCodeField(((ComboExtAuthField)fields[0]), userInputMap, provider, userVO, credential);
		initCorpField(((RefExtAuthField)fields[1]), defaultAccount, provider, credential, userInputMap);
		initUserAndPwdField(userInputMap, (TextExtAuthField)fields[2], (PasswordExtAuthField)fields[3], credential, userVO);
		initLanageField(userInputMap, (ComboExtAuthField)fields[4], credential);
		//initWorkdate((DateExtAuthField)fields[5], req);
		return fields;
	}
	 /**
     * 初始化用户名/密码信息，优先原则：用户输入 > 已有凭证 > 当前登录用户信息（NCUserProvider的情况）
     * 
     * @param userIdField
     * @param pwdField
     * @param credential
     * @param user
     * @param isLoginProvider
     */
    private void initUserAndPwdField(Map<String, String> userInputMap, TextExtAuthField userIdField, PasswordExtAuthField pwdField, PtCredentialVO credential, IUserVO user)
    {
    	String userId = !userInputMap.get("userid").equals(NULL_STRING) ? userInputMap.get("userid") : null;
    	String password = !userInputMap.get("password").equals(NULL_STRING) ? userInputMap.get("password") : null;
    	
    	if(userId == null && credential != null)
    		userId = credential.getUserid();
    	if(password == null && credential != null)
    		password = credential.getPassword();

    	Logger.debug("===NCLogin类initUserAndPwdField方法:获取缺省用户名/密码=" + userId + "/" + password);
    	
    	userIdField.setDefaultValue(userId);
    	pwdField.setDefaultValue(password);
    }
    /**
     * 初始化公司参照
     * 优先级：用户输入的 > 帐套信息
     * 
     * @param corpField
     */
    private void initCorpField(RefExtAuthField corpField, String defaultAccount, SSOProviderVO provider, PtCredentialVO credential, Map<String, String> userInputMap){
    	
    	Logger.debug("===NCLogin类initCorpField方法:进入NcLogin的initCorpField方法,defaultAccount=" + defaultAccount);
    	corpField.setPageMeta("reference");
    	corpField.setRefmodel("nc.uap.portal.integrate.othersystem.nc5x.NC5xCropRefModel");
    	corpField.setDsloaderclass("nc.uap.portal.integrate.othersystem.nc5x.Nc5xCropRefDsListener");
    	corpField.setReadDs("masterDs");
    	corpField.setReadFields("pk_corp");
    	corpField.setPath("reference/reftree.jsp");
    	corpField.setReadDs("corpds");
    	corpField.setWriteFields("pkcorp");
			
		String pkcorp = !userInputMap.get("pkcorp").equals(NULL_STRING) ? userInputMap.get("pkcorp") : null;
		if(pkcorp == null && credential != null)
			pkcorp = credential.getCredentialReference().getValue("pkcorp");
		
		Logger.debug("===NCLogin类initCorpField方法:获取pkcorp的默认值=" + pkcorp);
		corpField.setDefaultValue(pkcorp);		
		
		String nc5xds = provider.getValue(Nc5xIntConstent.NC5XDs);
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute(Nc5xIntConstent.NC5XDs, nc5xds);

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
    private String initAccountCodeField(ComboExtAuthField accountField, Map<String, String> userInputMap, SSOProviderVO provider, IUserVO userVO, PtCredentialVO credential)
    {
    	String defaultAccount = null;
      	 
		//String accountCode = !userInputMap.get("accountcode").equals(NULL_STRING) ? userInputMap.get("accountcode") : userVO == null ? null : (String)userVO.getUserReference("accountCode");
	    String accountCode = userInputMap.get("accountcode");
		if(accountCode == null || accountCode.trim().equals(""))
		{
			if(credential != null)
			{
				accountCode = credential.getCredentialReference().getValue("accountcode");
			}
		}
		String[] xmlAccountValues = provider.getValue("AccountCode").split(",");
		String[] xmlAccountNames = provider.getValue("AccountName") == null ? new String[]{} : provider.getValue("AccountName").split(",");
		Logger.debug("===NcLogin类initAccountCodeField方法:在provider中获取配置的所有帐套编码个数=" + xmlAccountValues.length);
	
		// 如果sso-provider.xml中没有配置AccountCode属性，则使用被集成NC的所有帐套
		Map<String, String> allAccounts = new HashMap<String, String>();
 
		if(xmlAccountValues == null || xmlAccountValues.length == 0)
			xmlAccountValues = allAccounts.keySet().toArray(new String[0]);
		
		if (xmlAccountValues != null) {
			int selected = -1;
			String[][] options = new String[xmlAccountValues.length][2];
			for (int i = 0; i < xmlAccountValues.length; i++) {
				options[i][0] = xmlAccountValues[i];
				options[i][1] = xmlAccountNames.length > i ? xmlAccountNames[i] : xmlAccountValues[i];
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
	
 
	@Override
	public String getGateUrl(HttpServletRequest req, HttpServletResponse res,
			PtCredentialVO credential, SSOProviderVO provider)
			throws CredentialValidateException {
		try {
			String gateUrl = provider.getGateUrl();
			String ncUrl = provider.getValue("runtimeUrl");
			
			Logger.debug("===NCLogin类getGateUrl方法:获取原始gateUrl=" + gateUrl);
			Logger.debug("===NCLogin类getGateUrl方法:获取NC的runtimeUrl=" + ncUrl);
			
			// 从凭证中获得登录信息
			String usercode = credential.getUserid();
			String password = credential.getPassword();
			String accountcode = credential.getCredentialReference().getValue("accountcode");
			String pkcorp = credential.getCredentialReference().getValue("pkcorp");
			String language = credential.getCredentialReference().getValue("language");
			String key = req.getSession().getId();
			StringBuffer parameters = new StringBuffer("key=" + key);
			if(pkcorp != null && pkcorp.trim().length() != 0)
			    parameters.append("&pkcorp=" + pkcorp);
			else
				parameters.append("&pkcorp=0001");
			
			UFDate tick = new UFDate(new Date().getTime());
			String workdate = req.getParameter("workdate");
//			if(workdate == null || "".equals(workdate))
//				workdate = tick.toLocalString();
			workdate = "2011-10-11";
			parameters.append("&accountcode=" + accountcode);
			parameters.append("&workdate=" + workdate);
			parameters.append("&language=" + language);
			// 对用户名称和密码进行URL编码,确保特殊字符能够通过
			parameters.append("&usercode=" + URLEncoder.encode(usercode, "UTF-8"));
			parameters.append("&pwd=" + URLEncoder.encode(password, "UTF-8"));
			
			Logger.debug("===NCLogin类getGateUrl方法:获取的credential信息,usercode=" + usercode + ";accountcode=" + accountcode
					+ ";pkcorp=" + pkcorp + ";language=" + language + ";key=" + key);
			// 用户信息验证
			verifyUserInfo(req, credential, provider);
			// NC登陆信息注册
			Logger.debug("===NCLogin类getGateUrl方法:生成的注册参数信息=" + parameters.toString());
			String registeResult = ncRegiste(parameters.toString(), provider.getValue("registryUrl"));
			if(registeResult != null && registeResult.startsWith("Error"))
				throw new PortletLoginException(registeResult);
			
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
			
			gateUrl += "?key=" + key + "&clienttype=portal&width=" + clientWidth + "&height=" + clientHeight;
			Logger.debug("===NCLogin类getGateUrl方法:获取最终的登录NC的gateUrl=" + gateUrl);
			return gateUrl;
		} catch (IOException e) {
			Logger.error("===NCLogin类getGateUrl方法:登录发生错误,被集成服务器没有启动或网络连接不通!",e); 
			throw new CredentialValidateException("登录NC发生错误,被集成服务器没有启动或网络连接无法达到!");
		} catch (Exception e) {
			Logger.error(e, e);
			throw new CredentialValidateException("校验NC登录失败:" + e.getMessage());
		}
	}

	@Override
	public String getNodeGateUrl(HttpServletRequest req,
			HttpServletResponse res, String nodeId, PtCredentialVO credential,
			SSOProviderVO providerVO) throws CredentialValidateException {
		return LfwRuntimeEnvironment.getRootPath() + "/pt/nc5x/fwd?funcode=" + nodeId + "&systemcode=" + providerVO.getSystemCode();
	}

	@Override
	public String verifyUserInfo(HttpServletRequest req, PtCredentialVO credentialVO, SSOProviderVO providerVO)
			throws CredentialValidateException {
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = providerVO.getValue(Nc5xIntConstent.NC5XDs);
		NC5xUserVO ncUser = null;
		String userId = credentialVO.getUserid();
		String password = credentialVO.getPassword();
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
			
			NC5xUserVO[] vos = CRUDHelper.getCRUDService().queryVOs("user_code='"+userId+"'", NC5xUserVO.class, null, null);
			if(vos != null && vos.length > 0)
				ncUser = vos[0];
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
		if(ncUser == null){
			throw new CredentialValidateException("用户不存在！");
		}
		if(ncUser.getLocked_tag().booleanValue()){
			throw new CredentialValidateException("用户被锁定！");
		}
		try {
			if(!StringUtils.equals(PortalServiceUtil.getEncodeService().encode(password),ncUser.getUser_password()))
				throw new CredentialValidateException("密码不正确！");
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new CredentialValidateException("制作进入NC系统的凭证失败");
		}
		return ncUser.getPrimaryKey();
	}
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
	    if(!userInputMap.get("language").equals(NULL_STRING))
	    	lanaguage = userInputMap.get("language");
	    
	    if(userInputMap.get("language").equals(NULL_STRING) && credential != null)
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
	/**
	 * 向NC系统注册登录凭证
	 */
	private String ncRegiste(String parameters, String registrUrl)
			throws IOException {
		
		Logger.debug("===NCLogin类ncRegiste方法:NC registry URL:" + registrUrl);
		Logger.debug("===NCLogin类ncRegiste方法:NC registry parameters:" + parameters);
		
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
		Logger.debug("===NCLogin类ncRegiste方法:NC Registe result=" + returnFlag);
		if (is != null)
			is.close();
		return returnFlag;
	}

	@Override
	public SsoSystemNode[] getAllFunNodes(SSOProviderVO provider) {
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = provider.getValue(Nc5xIntConstent.NC5XDs);
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
			List<SsoSystemNode> nodes = new ArrayList<SsoSystemNode>();

			FuncRegisterVO[] vos = CRUDHelper.getCRUDService().queryVOs(" isnull(isenable, 'Y')='Y'", FuncRegisterVO.class, null, null);
			if(vos != null && vos.length > 0){
				for(FuncRegisterVO regVO:vos)
					nodes.add(funcRegister2SystemNode(regVO));
				return nodes.toArray(new SsoSystemNode[0]);
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
		return null;
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
	
	@Override
	public SsoSystemNode[] getUserNodes(SSOProviderVO provider,
			PtCredentialVO credential) throws CredentialValidateException {
		String cuserid =credential.getCredentialReference().getValue("ncuserpk");
		//
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = provider.getValue(Nc5xIntConstent.NC5XDs);
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
			List<SsoSystemNode> nodes=new ArrayList<SsoSystemNode>();

			//CustomMenuShortcutVO[] vos = 
			List<CustomMenuShortcutVO> list = (List<CustomMenuShortcutVO>)CRUDHelper.getCRUDService().query("select cCustomMenuShortcutID, userId, fun_code,  iconText, iconId from sm_custom_menu_shortcut ,sm_funcregister where  funcid = cfunid and  userId='"+ cuserid +"'",  new ArrayListProcessor() {
					private static final long serialVersionUID = -7977064364672208452L;

					public Object processResultSet(ResultSet rs) throws SQLException {
						List<CustomMenuShortcutVO> list = new ArrayList<CustomMenuShortcutVO>();
						while (rs.next()) {
							CustomMenuShortcutVO customMenuShortcut = new CustomMenuShortcutVO();
							//
							String cCustomMenuShortcutID = rs.getString(1);
							customMenuShortcut.setCCustomMenuShortcutID(cCustomMenuShortcutID == null ? null : cCustomMenuShortcutID.trim());
							//
							String userId = rs.getString(2);
							customMenuShortcut.setUserId(userId == null ? null : userId.trim());
							//
							String funcId = rs.getString(3);
							customMenuShortcut.setFuncId(funcId == null ? null : funcId.trim());
							//
							String iconText = rs.getString(4);
							customMenuShortcut.setIconText(iconText == null ? null : iconText.trim());
							//
							String iconId = rs.getString(5);
							customMenuShortcut.setIconPath(iconId == null ? null : iconId.trim());
							//
							if(iconText == null || iconText.trim().length() <=0){
								String funcode = funcId;
								String funName = NCLangResOnserver.getInstance().getStrByID(IProductCode.PRODUCTCODE_FUNCODE, "D"+funcode.trim());
								customMenuShortcut.setIconText(funName);
							}
							list.add(customMenuShortcut);
						}
						return list;
					}
				});

			if(ToolKit.notNull(list)){
				for(CustomMenuShortcutVO regVO : list){
					nodes.add(shortcut2SystemNode(regVO));
				}
				return nodes.toArray(new SsoSystemNode[0]);
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
		
		return null;
	}
}
