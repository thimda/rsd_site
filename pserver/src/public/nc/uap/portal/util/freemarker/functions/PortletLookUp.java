package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 获得PortLet模板地址(FreeMarker模板方法)
 * 
 * @author licza.
 * 
 */
public class PortletLookUp implements TemplateMethodModel {

	@SuppressWarnings("unchecked")
	@Override
	public Object exec(final List arg) throws TemplateModelException {
		String portletKey = (String) arg.get(0);
		String[] keys = portletKey.split(":");
		String portletModule;
		String portletTheme = (String) arg.get(1);
		String pageSkin = (String) arg.get(2);
		String pageModule = (String) arg.get(3);
		portletModule = pageModule;
		if (FreeMarkerTools.isContainModuleInfo(portletKey)) {
			portletModule = keys[0];
		}
		if (FreeMarkerTools.isContainModuleInfo(portletTheme)) {
			keys = portletTheme.split(":");
			portletTheme  = keys[1];
			portletModule = keys[0];
		}
		return FreeMarkerTools.lookUpTheme(portletModule, portletTheme,pageSkin, PortalEnv.PORTLET_PATH);
	}
}
