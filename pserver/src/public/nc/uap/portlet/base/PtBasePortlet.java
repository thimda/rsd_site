package nc.uap.portlet.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.AttributeKeys;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.vo.PtPortletVO;

/**
 * Portlet基类
 * 
 * @author licza
 * 
 */
public abstract class PtBasePortlet extends GenericPortlet {
	
	protected static final String SRC_PARAM = "if_src";
	
	/**
	 * 客户端执行代码
	 * @param script
	 */
	public void addExecScript(PortletResponse res , String script){
		setScriptModel(res);
		res.addProperty(AttributeKeys.CLIENT_EXEC_SCRIPT,script);
	}
	
	/**
	 * 设置响应体为Script
	 * @param res
	 */
	public void setScriptModel(PortletResponse res){
		res.addProperty(AttributeKeys.RESPONSE_MODE,AttributeKeys.MODE_SCRIPT);
	}
	/**
	 * 设置响应体为Script
	 * @param res
	 */
	public void setHtmlModel(PortletResponse res){
		res.addProperty(AttributeKeys.RESPONSE_MODE,AttributeKeys.MODE_HTML);
	}
	protected String calculateIncludePath() {
		return null;
	}

	protected void include(String path,RenderRequest request, RenderResponse response) throws PortletException, IOException{
		PortletContext context = getPortletContext();
		PortletRequestDispatcher requestDispatcher = context.getRequestDispatcher(path);
		requestDispatcher.include(request, response);
	}
	
	@Override
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {		
		String windowId = request.getWindowID();
		String[] parts = windowId.split("_");
		if(parts != null && parts.length == 4){
			StringBuffer url = new StringBuffer("/portal/app/mockapp/portletsetting?model=nc.uap.portal.mng.portlet.PortletSettingPageModel");
			url.append("&module=" + parts[0]);
			url.append("&pagename=" + parts[1]);
			
			IPtPortletQryService portletQry = NCLocator.getInstance().lookup(IPtPortletQryService.class);
			String pkGroup = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
			PtPortletVO[] portletVos = null;
			try {
				portletVos = portletQry.getGroupPortlets(pkGroup, new String[][]{{parts[2],parts[3]}});
			} catch (PortalServiceException e) {
				LfwLogger.error(e);
				throw new LfwRuntimeException(e);
			}
			if(portletVos != null && portletVos.length > 0){
				url.append("&pk_portlet=" + portletVos[0].getPk_portlet());
			}else{
				throw new LfwRuntimeException("Portlet个数为零!");
			}
						                            		
    		PrintWriter w = response.getWriter();
    		w.println("<h1>Edit Mode.</h1>");
    		w.println("<iframe src='" + url.toString() + "' style='width:100%;height:100%;'></iframe>");
    		w.flush();
		}else{
			new LfwRuntimeException("WindowId组成错误!");
		}
	}

	@Override
	protected void doHeaders(RenderRequest request, RenderResponse response) {
		super.doHeaders(request, response);
	}

	@Override
	protected void doHelp(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PrintWriter w = response.getWriter();
		w.println("<h1>Help Mode.</h1>");
		w.flush();
	}

	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PrintWriter w = response.getWriter();
		w.println("<h1>View Mode.</h1>");
		w.flush();
	}

	/**
	 * 获得Portal资源
	 * @see javax.portlet.GenericPortlet#serveResource(ResourceRequest, ResourceResponse)
	 */
	@Override
	public void serveResource(ResourceRequest req, ResourceResponse res)
			throws PortletException, IOException {
		String resID=req.getResourceID();
		if(resID.startsWith("/")){
			super.serveResource(req, res);
		}else{
			String windowID=	req.getWindowID();
			String module=windowID.split("_")[2];
			PortletRequestDispatcher rd = getPortletConfig().getPortletContext().getRequestDispatcher(module+"/"+req.getResourceID());
			if (rd != null)
				rd.forward(req, res);
		}
	}
	/**
	 * 打印HTML到输出
	 * @param html
	 * @param response
	 */
	protected void print(Object html,RenderResponse response){
		try {
			PrintWriter out = response.getWriter();
			out.print(html);
			out.flush();
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
		}		
	}
}
