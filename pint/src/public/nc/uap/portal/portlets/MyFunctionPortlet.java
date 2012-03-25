package nc.uap.portal.portlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.portal.integrate.funnode.IFunIntegrationProvider;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portlet.base.FreeMarkerPortlet;

/**
 * 我的功能Portlet
 * @author licza
 *
 */
public class MyFunctionPortlet extends FreeMarkerPortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("WINDOW_ID", request.getWindowID());
		List<IFunIntegrationProvider> funs = new ArrayList<IFunIntegrationProvider>();
		
		PluginManager pm = PluginManager.newIns();
		/**
		 * 插找所有消息插件
		 */
		List<PtExtension> plugins = pm.getExtensions(IFunIntegrationProvider.PID);
		if (plugins != null && !plugins.isEmpty()) {
			for (PtExtension plugin : plugins) {
				/**
				 * 获取消息插件实例
				 */
				IFunIntegrationProvider fun = plugin.newInstance(IFunIntegrationProvider.class);
				if(fun.isVisibility())
					funs.add(fun);
			}
		}
		root.put("FUNS", funs);
		print(renderHtml(root), response);
	}

}
