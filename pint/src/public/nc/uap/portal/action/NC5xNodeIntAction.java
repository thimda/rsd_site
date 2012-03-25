package nc.uap.portal.action;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.portal.constant.WebKeys;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.util.PtUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 该类
 * @author licza
 *
 */
@Servlet(path = "/nc5x")
public class NC5xNodeIntAction extends BaseAction {
	
	/**
	 * 跳转到节点
	 * @param funcode
	 * @param systemcode
	 */
	@Action
	public void fwd(@Param(name="funcode") String funcode, @Param(name="systemcode") String systemcode){
		String globalPath = LfwRuntimeEnvironment.getRootPath();
		String openNodeScriptUrl = globalPath + "/html/frame/nc5xNode.js";
		print("<html><head>");
		print("<script src='" + openNodeScriptUrl + "'></script>");
		print("<script src='/lfw/frame/script/basic/BrowserSniffer.js'></script>");
		print("<script>");
		print("if(IS_IE && !IS_IE9){window.$ = document.getElementById;}else{function $(id) {	return document.getElementById(id);	}}");
		print("window.globalPath = '" + globalPath + "';");
		print("</script>");
		print("</head>");
		print("<body onload=\"openNCNode('" + funcode + "','" + systemcode + "');\"></body>");
		print("<html>");
	}
	
	@Action
	public void execNCAppletFunction(){
		String param = request.getParameter("param");
		
		String globalPath = LfwRuntimeEnvironment.getRootPath();
		String openNodeScriptUrl = globalPath + "/html/frame/nc5xNode.js";
		print("<html><head>");
		print("<script src='" + openNodeScriptUrl + "'></script>");
		print("<script src='/lfw/frame/script/basic/BrowserSniffer.js'></script>");
		print("<script>");
		print("if(IS_IE && !IS_IE9){window.$ = document.getElementById;}else{function $(id) {	return document.getElementById(id);	}}");
		print("window.globalPath = '" + globalPath + "';");
		print("</script>");
		print("</head>");
		print("<body onload=\"execNCAppletFunction('nc.client.portal.PortalInNCClient', 'openMsgPanel', 'notice;" + param + "', 'nc57');\"></body>");
		print("<html>");
	}
	
	
	/**
	 * 设置域
	 */
	@Action
	public void setdomain(){
		addExecScript("document.domain = '" + request.getLocalName() + "'");
	}
	
	/**
	 * 获取NC的登录界面的源码,返回给客户端进行执行
	 * @param screenHeight
	 * @param screenWidth
	 * @param systemcode
	 * @param gateUrl
	 */
	@Action
	public void fetch(@Param(name="screenHeight")String screenHeight, @Param(name="screenWidth")String screenWidth, @Param(name="systemcode")String systemcode, @Param(name="gateUrl")String gateUrl) {
		try { 
			// 从代办事务等打开nc默认全屏显示
			if(screenHeight != null && !screenHeight.equals(""))
				request.setAttribute("screenHeight", screenHeight);
			if(screenWidth != null && !screenWidth.equals(""))
				request.setAttribute("screenWidth", screenWidth);
			
			// 获得当前portal登录用户
			LfwSessionBean sb = LfwRuntimeEnvironment.getLfwSessionBean();
			if(sb == null)
				throw new UserAccessException("用户未登陆!");
			String userId = sb.getUser_code();
			if(systemcode == null || systemcode.trim().equals(""))
			{	
				LfwLogger.debug("===FetchNCPageSourceAction类:从req中获取systemcode为空,采用默认值NC!");
				systemcode = "NC";
			}
			
			// 请求参数中有了gateUrl则直接请求该url,不在重复调用getGateUrl进行单点登录
			if(PtUtil.isNull(gateUrl))
			{	
				LfwLogger.debug("===FetchNCPageSourceAction类:当前登录用户为=" + userId);
				PtCredentialVO credential = PintServiceFactory.getSsoQryService().getCredentials(
											userId, null, systemcode, Integer.valueOf(WebKeys.PORTLET_SHARE_APPLICATION));
				if(credential == null)
					throw new LfwRuntimeException("该用户没有进入NC系统的凭证!");
				// 单点登陆nc
				IWebAppLoginService loginService = ProviderFetcher.getInstance().getAuthService(systemcode);
				SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemcode);
				gateUrl = loginService.getGateUrl(request, null, credential, provider);
			}
			
			LfwLogger.debug("===FetchNCPageSourceAction类:gateUrl=" + gateUrl);
			
			// 抓取nc的页面源码
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(gateUrl);
			int statusCode = httpClient.executeMethod(getMethod);
			LfwLogger.debug("===FetchNCPageSourceAction类:抓取的NC页面源码返回的状态码,statusCode=" + statusCode);
			String reponseStr = getMethod.getResponseBodyAsString();
			LfwLogger.debug("===FetchNCPageSourceAction类:抓取的NC页面源码,\n" + reponseStr);
			// 返回源码给客户端展示
			print(reponseStr);
		} catch(Exception e) { 
			LfwLogger.error("===FetchNCPageSourceAction类:获取NC页面源码时出现错误:" + e.getMessage(), e);
			print(JsURLEncoder.encode("ERROR:" + e.getMessage(), "UTF-8"));
		}
	}
}
