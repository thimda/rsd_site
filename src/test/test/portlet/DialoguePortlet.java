package test.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class DialoguePortlet extends GenericPortlet {
	private static final String VIEW_PAGE = "/pages/dailog.jsp";

	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		response.setContentType("text/html");
		PortletContext context = getPortletContext();
		PortletRequestDispatcher requestDispatcher = context
				.getRequestDispatcher(VIEW_PAGE);
		requestDispatcher.include(request, response);
	}
}
