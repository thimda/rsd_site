package nc.uap.portal.portlets.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.portal.msg.model.MsgCategory;
import nc.uap.portal.msg.provide.IMsgProvide;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portlet.base.FreeMarkerPortlet;

/**
 * 消息看板Portlet
 * @author licza
 *
 */
public class MessageCenterPortlet extends FreeMarkerPortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("WINDOW_ID", request.getWindowID());
		
		PluginManager pm = PluginManager.newIns();
		/**
		 * 插找所有消息插件
		 */
		List<PtExtension> plugins = pm.getExtensions(IMsgProvide.PID);
		List<MsgCategory> categorys = new ArrayList<MsgCategory>();
		if (plugins != null && !plugins.isEmpty()) {
			for (PtExtension plugin : plugins) {
				/**
				 * 获取消息插件实例
				 */
				IMsgProvide provide = plugin.newInstance(IMsgProvide.class);
				categorys.addAll(provide.getCategory());
			}
		}
		root.put("CATEGORY", categorys);
		print(renderHtml(root), response);
	}

}
