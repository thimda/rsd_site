package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 获得布局
 * @author licza
 *
 */
public class LayoutLookUp implements TemplateMethodModel {

	@SuppressWarnings("unchecked")
	@Override
	public Object exec(List arg) throws TemplateModelException {
		String layoutKey = (String) arg.get(0);
		String[] keys = layoutKey.split(":");
		String layoutModule;
		String layoutName = keys[0];
		String pageModule = (String) arg.get(1);
		layoutModule = pageModule;
		if (FreeMarkerTools.isContainModuleInfo(layoutKey)) {
			layoutModule = keys[0];
			layoutName = keys[1];
		}
		String pageSkin = (String) arg.get(2);
		return FreeMarkerTools.lookUpTheme(layoutModule, layoutName,pageSkin, PortalEnv.LAYOUT_FOLDER);
	}

}
