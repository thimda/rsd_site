package nc.uap.portal.integrate.othersystem.yer5x;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.authfield.ComboExtAuthField;
import nc.uap.lfw.login.authfield.DateExtAuthField;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.authfield.PasswordExtAuthField;
import nc.uap.lfw.login.authfield.RefExtAuthField;
import nc.uap.lfw.login.authfield.TextExtAuthField;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.othersystem.nc5x.NC5xLogin;
import nc.uap.portal.integrate.othersystem.nc5x.Nc5xIntConstent;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;

/**
 * 网上报销系统集成
 * @author gd 2009-01-19
 * @version NC5.6
 * @since NC5.5
 * 
 */
public class YerLogin implements IWebAppLoginService {
	
	private static final String NULL_STRING = "NULL";
	
	@SuppressWarnings("unchecked")
	public PtCredentialVO credentialProcess(HttpServletRequest req, SSOProviderVO providerVO) throws CredentialValidateException {
		try {
			
			// 构造凭证VO
			PtCredentialVO credentialVO = new PtCredentialVO();
			String userId = (String)req.getAttribute("userId");
			String password = (String)req.getAttribute("password");
			credentialVO.setUserid(userId);
			credentialVO.setPassword(password);
			// 构造其它信息（凭证Reference）
			String accountcode = (String)req.getAttribute("accountcode");
			String pkcorp = (String)req.getAttribute("pkcorp");
			String workdate = new UFDate().toLocalString();
			UFDate tick = (UFDate)req.getAttribute("workdate") ;;
			if(tick != null)
				workdate = tick.toLocalString();
			credentialVO.getCredentialReference().setValue("workdate", workdate);
			credentialVO.getCredentialReference().setValue("accountcode", accountcode);
			
			// 通过帐套查找数据源
			String dataSource = providerVO.getValue("datasource");
//			String accountName = accountService.getAccountByCode(accountcode).getAccountName();
			
			// 通过公司pk查询公司名
			
			String nc5xDs = providerVO.getValue(Nc5xIntConstent.NC5XDs);
			String oldDs = InvocationInfoProxy.getInstance().getUserDataSource();
			String corpName = null;
			try {
				InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
				Map<String,String> obj = (Map<String,String>)CRUDHelper.getCRUDService().query("SELECT unitname FROM BD_CORP WHERE PK_CORP ='"+ pkcorp +"'", new MapProcessor());
				corpName = (String)obj.get("unitname");
			} catch (LfwBusinessException e) {
				throw new CredentialValidateException(e.getMessage(),e);
			}finally{
				InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
			}
			
//			credentialVO.getCredentialReference().setValue("accountname", accountName);
			credentialVO.getCredentialReference().setValue("pkcorp", pkcorp);
			credentialVO.getCredentialReference().setValue("corpName", corpName);
			String language = "simpchn";
			credentialVO.getCredentialReference().setValue("language", language);
			credentialVO.getCredentialReference().setValue("datasource", dataSource);
			
			// 用户校验
			String ncuserpk = verifyUserInfo(req, credentialVO, providerVO);
			 
			credentialVO.getCredentialReference().setValue("ncuserpk", ncuserpk);
			return credentialVO;
		} catch (Exception e) {
			Logger.error(e, e);
			throw new CredentialValidateException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "YerLogin-000000", null, new String[]{e.getMessage()})/*制作进入WEB报销系统的凭证失败:{0}*/, e);
		}
	}

	public ExtAuthField[] getCredentialFields(HttpServletRequest req, SSOProviderVO provider, IUserVO userVO, PtCredentialVO credential)
			throws CredentialValidateException {
		Logger.debug("===YerLogin类getCredentialFields方法:进入YerLogin的getCredentialFields()方法!");
	    Map<String, String> userInputMap = getUserInputValue(req);
	    
		// 设置属性
		ExtAuthField[] fields = new ExtAuthField[5];
		fields[0] = new ComboExtAuthField("系统", "accountcode", true);
		fields[1] = new RefExtAuthField("公司", "pkcorp", false);
		fields[2] = new TextExtAuthField("用户", "userId", true);
		fields[3] = new PasswordExtAuthField("密码", "password", false);
		fields[4] = new DateExtAuthField("日期", "workdate", true);
		fields[0].setRequired(true);
		fields[2].setRequired(true);
		fields[3].setRequired(true);
		fields[4].setRequired(true);
		
		String defaultAccount = initAccountCodeField(((ComboExtAuthField)fields[0]), userInputMap, provider, userVO, credential);
		initCorpField(((RefExtAuthField)fields[1]), defaultAccount, provider, credential, userInputMap);
		initUserAndPwdField(userInputMap, (TextExtAuthField)fields[2], (PasswordExtAuthField)fields[3], credential, userVO);
		initWorkdate((DateExtAuthField)fields[4], req);
		return fields;
	}

	public String getGateUrl(HttpServletRequest req, HttpServletResponse res, PtCredentialVO credential, SSOProviderVO providerVO)
			throws CredentialValidateException {
		try {
			String portletId = (String) req.getAttribute("$portletId");
			String gateUrl = providerVO.getGateUrl() + "?fromPortal=1";
			String ncUrl = providerVO.getValue("runtimeUrl");
			
			Logger.debug("===YerLogin类getGateUrl方法:获取原始gateUrl=" + gateUrl);
			Logger.debug("===YerLogin类getGateUrl方法:获取NC的runtimeUrl=" + ncUrl);
			
			// 从凭证中获得登录信息
			String usercode = credential.getUserid();
			String password = credential.getPassword();
			String accountcode = credential.getCredentialReference().getValue("accountcode");
			String accountname = credential.getCredentialReference().getValue("accountname");
			String pkcorp = credential.getCredentialReference().getValue("pkcorp");
			String workdate = credential.getCredentialReference().getValue("workdate");
			String datasource = credential.getCredentialReference().getValue("datasource");
			String corpName = credential.getCredentialReference().getValue("corpName");
		
			// 用户校验
			verifyUserInfo(req, credential, providerVO);
			
			Map<String, String> formFieldMap = new HashMap<String, String>();
			formFieldMap.put("userId", usercode);
			formFieldMap.put("password", password);
			formFieldMap.put("workdate", workdate);
			// 帐套名称
			formFieldMap.put("accountCode", accountname);
			formFieldMap.put("accountCodeValue", accountcode);
			formFieldMap.put("datasource", datasource);
			formFieldMap.put("corpCode", pkcorp == null ? "0001" : pkcorp);
			formFieldMap.put("corpName", corpName);
			
			Logger.debug("===YerLogin类getGateUrl方法:获取最终的登录NC的gateUrl=" + gateUrl);
			
			req.getSession().setAttribute("fieldsMap" + portletId, formFieldMap);
			req.getSession().setAttribute("form_url" + portletId, gateUrl);
			return "/portal/html/nodes/form_view.jsp?a=1&portletId=" + portletId;
			
		} catch (Exception e) {
			Logger.error(e, e);
			throw new CredentialValidateException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "NcLogin-000001", null, new String[]{e.getMessage()})/*校验NC登录失败：{0}*/);
		}
	}
	
	public String verifyUserInfo(HttpServletRequest req, PtCredentialVO credentialVO, SSOProviderVO providerVO) throws CredentialValidateException
	{
		// 校验
		return new NC5xLogin().verifyUserInfo(req, credentialVO, providerVO);
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
		String username = req.getParameter("userId") == null ? NULL_STRING : req.getParameter("userId");
		String password = req.getParameter("password") == null ? NULL_STRING : req.getParameter("password");
		String workdate = req.getParameter("workdate") == null ? NULL_STRING : req.getParameter("workdate");
		
		map.put("accountcode", accountcode);
		map.put("pkcorp", pkcorp);
		map.put("userId", username);
		map.put("password", password);
		map.put("workdate", workdate);
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
     * 初始化公司参照
     * 优先级：用户输入的 > 帐套信息
     * 
     * @param corpField
     */
    private void initCorpField(RefExtAuthField corpField, String defaultAccount, SSOProviderVO provider, PtCredentialVO credential, Map<String, String> userInputMap){
    	
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
    	String userId = !userInputMap.get("userId").equals(NULL_STRING) ? userInputMap.get("userId") : null;
    	String password = !userInputMap.get("password").equals(NULL_STRING) ? userInputMap.get("password") : null;
    	
    	if(userId == null && credential != null)
    		userId = credential.getUserid();
    	if(password == null && credential != null)
    		password = credential.getPassword();

    	Logger.debug("===YerLogin类initUserAndPwdField方法:获取缺省用户名/密码=" + userId + "/" + password);
    	
    	userIdField.setDefaultValue(userId);
    	pwdField.setDefaultValue(password);
    }
    
    /**
     * 初始化登录日期，如果用户输入，以用户输入为准，如果没有，则记录当前时间
	 * @param field
	 * @param req
	 */
	 private void initWorkdate(DateExtAuthField field, HttpServletRequest req)
	 {
		 UFDate tick = new UFDate(new Date().getTime());
	 	 String workdate = req.getParameter("workdate");
	 	 if(workdate != null && !"".equals(workdate))
	 		 field.setDefaultValue(workdate);
	 	 else
	 		 field.setDefaultValue(tick.toString());
	 }

	public String getNodeGateUrl(HttpServletRequest req,
			HttpServletResponse res, String nodeId, PtCredentialVO credential,
			SSOProviderVO providerVO) throws CredentialValidateException {
//		// 单点登录
//		this.getGateUrl(req, res, credential, providerVO);
//		// 改变url为某个节点的url
//		String nodeUrl = (String) req.getSession().getAttribute("form_url");
//		String oldDs = InvocationInfoProxy.getInstance().getUserDataSource();
//		try {
//			String returnUrl = req.getParameter("returnUrl");
//			// 如果returnUrl为null,说明是进行单点登录后到达此,此时从库中查出节点的url
//			if(returnUrl == null)
//			{
//				SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(providerVO.getSystemCode());
//				String ncUrl = ProviderFetcher.getInstance().getProvider(providerVO.getSystemCode()).getValue("runtimeUrl");
//				NCLocator locator = NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
//				// 获取NC帐套
//				String accountStr = provider.getValue("AccountCode");
//				if(accountStr == null || accountStr.equals(""))
//					throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "NcNodesProvider-000000", null, new String[]{providerVO.getSystemCode()})/*{0}系统未在sso-prop.xml中配置帐套编码!*/);
//				String[] accounts = accountStr.split(",");
//				String datasource = PortalUtil.fetchDatasourceName(accounts[0], locator);
//				InvocationInfoProxy.getInstance().setUserDataSource(datasource);
//				IFuncRegisterQueryService iPower = (IFuncRegisterQueryService) NCLocator.getInstance().lookup(IFuncRegisterQueryService.class.getName());
//				FuncRegisterVO vo = iPower.findFuncRegisterVOByPrimaryKey(nodeId);
//				returnUrl = vo.getClassName();
//			}
//			req.getSession().setAttribute("form_url", nodeUrl + "&returnUrl=" + URLEncoder.encode(returnUrl, "UTF-8"));
//			return "/portal/html/portlets/application/form_view.jsp?a=1";
//		} catch (Exception e) {
//			Logger.error(e, e);
//			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("portal", "YerLogin-000002")/*获取节点单点登录url出现异常!*/, e);
//		} finally {
//			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
//		}
		return null;
	}
}
