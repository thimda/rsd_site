package test.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * 测试Portlet事件用例
 * 事件发送者
 * @author licza
 * 
 */
public class BuySoyPortlet extends GenericPortlet {
	//private static final String VIEW_PAGE = "/pages/baby.jsp";
	private static final String VIEW_PAGE = "../pages/view.jsp";
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		response.setContentType("text/html");
		PortletContext context = getPortletContext();
		PortletRequestDispatcher requestDispatcher = context
				.getRequestDispatcher("/../../../pages/video.jsp");
		request.getPortletSession().setAttribute("src", "/portal/pages/frame.jsp");
		requestDispatcher.include(request, response);
	}

	@ProcessAction(name = "buysoy")
	public void doBuySoy(ActionRequest request, ActionResponse response) {
		String soytypo = request.getParameter("soytypo");
		request.getPortletSession().setAttribute("src", "/portal/pages/view.jsp");
		response.setEvent("buysoy", soytypo);
	}

	@ProcessAction(name = "byebye")
	public void sayByeBye(ActionRequest request, ActionResponse response) {
		response.setEvent("byebye", null);
	}
}
