package nc.uap.portal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.servlet.LfwServletBase;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.lfw.servletplus.core.Initialization;
import nc.uap.lfw.servletplus.core.ServletForward;
import nc.uap.lfw.util.HttpUtil;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.login.thirdparty.ThirdPartyLoginHelper;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalConfigRegistryService;
import nc.uap.portal.vo.PtTrdauthVO;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ThirdPartyLoginServlet extends LfwServletBase {

	private static final long serialVersionUID = 8723923514461266975L;
	
	private static final String MAXWIN = "p_maxwin";
	
	protected Initialization inl;
	
	private String ERROR_STRING = null;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		inl=new Initialization();
		String actionFolder = getActionFolder(config);
		inl.setActionFolder(actionFolder);
		String urlPrefix = getUrlPrefix(config);
		inl.setUrlPrefix(urlPrefix);
		inl.registerUrl();
 	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String authKey = req.getPathInfo();
		if(authKey != null){
			authKey = authKey.substring(1);
			PtTrdauthVO authVO = ThirdPartyLoginHelper.getAuth(authKey);
			if(authVO == null){
				resp.sendRedirect(req.getContextPath());
				LfwLogger.error("当前令牌号" + authKey + "未注册");
				return;
			}
			try {
				ICpUserQry userQry = NCLocator.getInstance().lookup(ICpUserQry.class);
				CpUserVO cpUserVO = userQry.getUserByPk(authVO.getPk_user());
				UFDateTime now = new UFDateTime();
				if(authVO.getTtl().after(now)){
					AuthenticationUserVO userVO = new AuthenticationUserVO();
					userVO.setUserID(cpUserVO.getUser_name());
					userVO.setPassword(PortalServiceUtil.getEncodeService().decode(cpUserVO.getUser_password()));
					Map<String, String> extMap = new HashMap<String, String>();
					IPtPortalConfigRegistryService cfg = PortalServiceUtil.getConfigRegistryService();
					String openmode = cfg.get("portal.openmode");
					boolean isMaxWindow = StringUtils.equals("1", openmode);
					extMap.put(MAXWIN, isMaxWindow ? "Y" : "N");
					userVO.setExtInfo(extMap);
					getLoginHelper().processLogin(userVO);
					boolean isLogin =  LfwRuntimeEnvironment.getLfwSessionBean() != null;
					if(isLogin){
			 			Method method = inl.getUrlMap().get("/auth/login/index");
						if(method == null){
							resp.sendError(404);
						}else {
							req.setAttribute("thirdPartyUrl", authVO.getUrl());
							ServletForward.forward(method, req, resp);
						}
					}else{
						req.getSession().setAttribute("thirdPartyUserId", cpUserVO.getUser_name());
						resp.sendRedirect(req.getContextPath());
						LfwLogger.error("username：" + cpUserVO.getUser_name() + " password：" + cpUserVO.getUser_password() + "登录失败");
					}
				}else{
					req.getSession().setAttribute("thirdPartyUserId", cpUserVO.getUser_name());
					resp.sendRedirect(req.getContextPath());
					LfwLogger.error("当前访问URL已过期");
				}
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
				throw new LfwRuntimeException(e);
			} catch (Throwable e) {
				processError(req, resp, e);
			}
		}else{
			resp.sendRedirect(req.getContextPath());
			LfwLogger.error("令牌号不能为空");
		}
	}
	
	protected LoginHelper<PtSessionBean> getLoginHelper() {
		return (LoginHelper<PtSessionBean>) LfwLoginFetcher.getGeneralInstance().getLoginHelper();
	}
	
	protected String getUrlPrefix(ServletConfig config) {
		String urlPrefix=config.getInitParameter("urlprefix");
		return urlPrefix;
	}

	protected String getActionFolder(ServletConfig config) {
		String actionFolder=config.getInitParameter("actionfolder");
		return actionFolder;
	}
	
	protected void processError(HttpServletRequest request,
			HttpServletResponse response, Throwable e) throws IOException, ServletException {
		Logger.error(e.getMessage(), e);
		String title = (String) request.getAttribute("title");
		String errorContent;
		if(e instanceof LfwRuntimeException)
			errorContent = ((LfwRuntimeException)e).getHint();
		else
			errorContent = getErrorPageText(request);
		if(!response.isCommitted())
			returnXMLString(  title, errorContent, e,response);
	}
	
	private void returnXMLString(
			 String title, String content, Throwable e,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doc.createElement("AJAX_CONTENT");
		Element contentNode = doc.createElement("p_content");
		contentNode.appendChild(doc.createCDATASection(content));
		rootNode.appendChild(contentNode);
		doc.appendChild(rootNode);
		
		Element scriptNode = doc.createElement("p_script");
		scriptNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(scriptNode);
		Element titleNode = doc.createElement("p_title");
		if (title != null && !title.equals(""))
			titleNode.appendChild(doc.createTextNode(title));
		rootNode.appendChild(titleNode);
			
		Element expNode = doc.createElement("exp-sign");
		if (e != null)
			expNode.appendChild(doc.createTextNode("true"));
		else
			expNode.appendChild(doc.createTextNode("false"));
		rootNode.appendChild(expNode);
		
		Element expTextNode = doc.createElement("exp-text");
		if (e != null)
			expTextNode.appendChild(doc.createTextNode(e.getMessage() == null ? "" : e.getMessage()));
		else
			expTextNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(expTextNode);
		
		Element expStackNode = doc.createElement("exp-stack");
		if (e != null) {
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			pw.close();
			writer.close();
			expStackNode.appendChild(doc.createCDATASection(writer.toString()));
		} else
			expStackNode.appendChild(doc.createTextNode(""));
		rootNode.appendChild(expStackNode);
		
//		Element typeNode = doc.createElement("p_type");
//		typeNode.appendChild(doc.createTextNode(String.valueOf(IPortletType.JSP_PORTLET)));
//		rootNode.appendChild(typeNode);
		XMLUtil.printDOMTree(response.getWriter(), doc, 0, "UTF-8");
	}

	private String getErrorPageText(HttpServletRequest req) {
		ServletContext ctx = req.getSession().getServletContext();
		if (ERROR_STRING == null) {
			try {
				ERROR_STRING = HttpUtil.URLtoString(ctx.getResource("/html/portal/portlet_error.jsp"));
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				ERROR_STRING = "This Portlet has something error occurred!";
			}
		}
		return ERROR_STRING;
	}
	
}
