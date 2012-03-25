package nc.uap.portal.portlets.linktree;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
/**
 * µ¼º½ÌõPortlet
 * @author licza
 *
 */
public class NavigationPortlet extends PtBasePortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		PrintWriter out = response.getWriter();
		Page page = PortalRenderEnv.getCurrentPage();
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		String html = null;
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("RES_PATH", resourcePath);
		String title = request.getParameter("title");
		String url = request.getParameter("url");
		root.put("url", url);
		List<String> titleList = new ArrayList<String>();
		if(title == null){
			titleList.add(page.getTitle());
		}else{
			String[] tmpTitle = title.split(",,,");
			for(int i = tmpTitle.length -1; i > -1 ;i--){
				titleList.add(tmpTitle[i]);
			}
		}
		root.put("title", titleList);
		try {
			html = FreeMarkerTools.contextTemplatRender("nc/uap/portal/portlets/linktree/navbar.ftl", root);
			out.print(html);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		out.flush();

	}
	@ProcessEvent(name="showNavEvent")
	public void showNavEvent(EventRequest request,EventResponse response){
		String val = (String)request.getEvent().getValue();
		org.json.JSONObject jso = null;
		try {
			jso = new org.json.JSONObject(val);
		} catch (ParseException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		if(jso == null)
			return;
		String title = jso.getString("title");
		String url = jso.getString("url");
		response.setRenderParameter("title", title);
		response.setRenderParameter("url", url);
	}
}
