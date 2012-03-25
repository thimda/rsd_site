package nc.uap.portal.util.freemarker.functions;

import java.io.IOException;
import java.util.Map;

import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * 引入Portal的JS 
 * 确保JS在PortletHtml加载之后加载
 * @author licza
 *
 */
public class ImportScript implements TemplateDirectiveModel{
	public static final String JS_SUFFIX=".js";
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String portalPath=	params.get("portalPath").toString();
		if(portalPath.endsWith(PortalEnv.FREE_MARKER_SUFFIX)){
			String path = portalPath.substring(0, portalPath.lastIndexOf(PortalEnv.FREE_MARKER_SUFFIX));
			if(FreeMarkerTools.isTemplateExist(path+JS_SUFFIX)){
				FreeMarkerTools.warpTmps(env, params, body, Js.PR, Js.VARIABLE_NAME, Js.SX,path+JS_SUFFIX);
			}
		}
	}

}
