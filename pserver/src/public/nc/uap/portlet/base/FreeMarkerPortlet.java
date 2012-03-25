package nc.uap.portlet.base;

import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

/**
 * Ì×ÓÃÄ£°åµÄFreeMarker Portlet
 * @author licza
 *
 */
public class FreeMarkerPortlet extends PtBasePortlet {
	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}
	public String renderHtml(Map<String, Object> root){
		String template = this.getClass().getName().replace(".", "/") + ".ftl";
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		root.put("RES_PATH", resourcePath);
		try {
			return FreeMarkerTools.contextTemplatRender(template, root);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		}
	}
	
}
