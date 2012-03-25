package nc.uap.portal.util.freemarker.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortalRenderEnv;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 获得系统参数(FreeMarker模板方法)
 * 
 * @author licza
 * 
 */
public class Env implements TemplateMethodModel {
 	private static final String WEB = "web";
 	private static final String CTX = "ctx";
 	private static final String TITLE = "title";
 	private static final String COMM = "comm";
 	private static final String SHOWONLINEUSER="showOnlineUser";
	@SuppressWarnings("unchecked")
	@Override
	public Object exec(final List arg) throws TemplateModelException {
		final Map<String, String> env = new HashMap<String, String>();
		String context = LfwRuntimeEnvironment.getRootPath();
		env.put(WEB, context);
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String ctxpath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort() == 80 ? "":":"+request.getServerPort())+context+"/";
		env.put(CTX, ctxpath);
		/**将当前页面名称放入上下文中**/
		Page page = PortalRenderEnv.getCurrentPage();
		if(page != null)
			env.put(TITLE, PortalPageDataWrap.getTitle(page));

		env.put(COMM,  "/" + PortalEnv.getPortalCoreName() + "/portalspec/ftl/portaldefine/comm");
		String showOnlineUser = PortalServiceUtil.getConfigRegistryService().get("portal.onlinecountbar.display");
		/**
		 * 显示在线人数
		 */
		env.put(SHOWONLINEUSER, showOnlineUser);
	 
		return env;
	}
}
