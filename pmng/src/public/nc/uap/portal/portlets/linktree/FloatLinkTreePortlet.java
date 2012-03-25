package nc.uap.portal.portlets.linktree;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.menuitem.MenuRoot;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortalUtil;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.iframe.TriggerIframePortlet;
/**
 * 浮动的链接数Portlet
 * @author licza
 *
 */
public class FloatLinkTreePortlet extends TriggerIframePortlet {

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		Page[] pages = null;
		try {
			pages = PortalPageDataWrap.getUserPages();
		} catch (UserAccessException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		if(pages != null && pages.length > 0){
			for(int i=0; i<pages.length; i++){
				String linkgroup = pages[i].getLinkgroup();
				if(PtUtil.isNull(linkgroup)){
					continue;
				}
				PrintWriter out = response.getWriter();
				Map<String,Object> root = new HashMap<String,Object>();
				String title = pages[i].getTitle();
				try {
					PtSessionBean sesBean = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
					String pk_user = sesBean.getPk_user();
					ICpMenuQry menuQry = NCLocator.getInstance().lookup(ICpMenuQry.class);
					//MenuItemAdapterVO[] miaos = menuQry.getMenuItemsWithPermission(linkgroup, pk_user, false, false);
					MenuRoot[] linkRoots = null;
					//管理员不走权限
					//if(sesBean.getUserType().equals(CpUserVO.USER_TYPE_GROUP_ADM))
						linkRoots = menuQry.getMenuRoot(linkgroup);
					//else
					//	menuQry.getMenuRootWithPermission(linkgroup, pk_user, true, false);
					//MenuRoot[] linkRoots = menuQry.getMenuRoot(linkgroup);
					root.put("LINK_ROOT", linkRoots);
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(),e);
				}
				root.put("PAGE_CARD_ID", pages[i].getModule() + "_" + pages[i].getPagename());
				root.put("windowID", request.getWindowID());
				root.put("windowTitle", title);
				root.put("pageIndex", "pageIndex"+i);
				response.setTitle("");
				String html = null;
				try {
					html = FreeMarkerTools.contextTemplatRender("nc/uap/portal/portlets/linktree/floatTree.html", root);
					out.print(html);
				} catch (PortalServiceException e) {
					LfwLogger.error(e.getMessage(),e);
				}
				out.flush();
			}
		}
	}
	public void processAction(ActionRequest request, ActionResponse response) {
		String url = request.getParameter("frameUrl");
		response.setEvent("switchIframeContentEvent", url);
		String title = PortalUtil.convertToCorrectEncoding(request.getParameter("title"));
		org.json.JSONObject node=new org.json.JSONObject();
		node.put("title", title);
		node.put("url", url);
		response.setEvent("showNavEvent", node.toString());
		
	}
	
}
