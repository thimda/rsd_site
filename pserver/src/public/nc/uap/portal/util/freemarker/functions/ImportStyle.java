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


public class ImportStyle implements TemplateDirectiveModel{
	public static final String CSS_SUFFIX=".css";
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String portalPath=	params.get("portalPath").toString();
		if(portalPath.endsWith(PortalEnv.FREE_MARKER_SUFFIX)){
			String path= portalPath.substring(0, portalPath.lastIndexOf(PortalEnv.FREE_MARKER_SUFFIX));
			if(FreeMarkerTools.isTemplateExist(path+CSS_SUFFIX)){
				FreeMarkerTools.warpTmps(env, params, body, Css.PR, Css.VARIABLE_NAME, Css.SX,path+CSS_SUFFIX);
			}
		}
	}
}
