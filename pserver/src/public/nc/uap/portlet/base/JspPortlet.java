package nc.uap.portlet.base;

import java.io.IOException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * 基于jsp架构的portlet通用实现。只需要实现portlet.xml配置文件中相应的view-jsp等 jsp页面，就可以成为一个标准portlet
 * 
 * @author dengjt
 */
public class JspPortlet extends PtBasePortlet {
	protected String editJSP;
	protected String helpJSP;
	protected String viewJSP;

	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		editJSP = getInitParameter("edit-jsp");
		helpJSP = getInitParameter("help-jsp");
		viewJSP = getInitParameter("view-jsp");
	}

	public void doEdit(RenderRequest req, RenderResponse res)
			throws IOException, PortletException {
		if (editJSP != null) {
			include(editJSP, req, res);
		} else {
			super.doEdit(req, res);
		}
	}

	public void doHelp(RenderRequest req, RenderResponse res)
			throws IOException, PortletException {
		include(helpJSP, req, res);
	}

	public void doView(RenderRequest req, RenderResponse res)
			throws IOException, PortletException {
		include(viewJSP, req, res);
	}
}
