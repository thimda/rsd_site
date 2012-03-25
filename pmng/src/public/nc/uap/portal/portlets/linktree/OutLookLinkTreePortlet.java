package nc.uap.portal.portlets.linktree;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.menuitem.MenuRoot;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.menu.util.LinkGroupDataWarp;
import nc.uap.portal.util.PtUtil;
import nc.uap.portlet.iframe.TriggerIframePortlet;
/**
 * OutLook式链接树Portlet
 * @author licza
 * @version 6.1
 */
public class OutLookLinkTreePortlet extends TriggerIframePortlet {

	static final String LINK_GROUP = "linkgroup";
	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		StringBuffer html = new StringBuffer();

		String linkgroup = request.getPreferences().getValue("linkgroup", "0000AA10000000002SBA");
		if(PtUtil.isNull(linkgroup)){
			return;
		}
		/**
		 * 窗口ID
		 */
		String portletWindowId = request.getWindowID();
		MenuRoot[] roots = getRoot(linkgroup);
		
		/**
		 * 渲染根目录
		 */
		if(roots != null && roots.length > 0){
			/**
			 * 渲染头部
			 */
			html.append(LinkGroupDataWarp.renderHead(portletWindowId));
			for(MenuRoot root : roots) 
				html.append(LinkGroupDataWarp.renderTree(root.doGetNodeArray(), root.getTitle()));
			/**
			 * 渲染底部
			 */
			html.append(LinkGroupDataWarp.renderFoot());
		}else{
			html.append("未查询到链接信息!");
		}
		/**
		 * 输出到响应
		 */
		print(html, response);
	}
	
	/**
	 * 获得根目录
	 * @param linkgroup
	 * @return
	 */
	private MenuRoot[] getRoot(String linkgroup){
		try {
			ICpMenuQry menuQry = NCLocator.getInstance().lookup(ICpMenuQry.class);

			 return menuQry.getMenuRoot(linkgroup);
		} catch (Exception e1) {
			LfwLogger.error("查询根目录失败",e1);
		}
		return new MenuRoot[0];
	}
}
