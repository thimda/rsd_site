package nc.uap.portlet.base;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public abstract class PtJspViewPortlet extends PtBasePortlet {
	protected void include(String path, RenderRequest req, RenderResponse res)	throws PortletException, IOException {
		PortletRequestDispatcher dis = this.getPortletContext().getRequestDispatcher(path);
		dis.include(req, res);
	}
}
