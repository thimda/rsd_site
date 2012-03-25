package nc.uap.portal.portlets.banner;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
/**
 * µ¼º½ÌõPortlet
 * @author licza
 *
 */
public class CopyrightPortlet extends PtBasePortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Map<String, Object> root = new HashMap<String, Object>();
			String html = FreeMarkerTools.contextTemplatRender("nc/uap/portal/portlets/banner/copyright.ftl", root);
			out.print(html);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		out.flush();

	}
}
