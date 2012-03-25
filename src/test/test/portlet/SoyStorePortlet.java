package test.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * 测试Portlet事件用例 事件接收者
 * 
 * @author licza
 * 
 */
public class SoyStorePortlet extends GenericPortlet {
	private static final String VIEW_PAGE = "/pages/sales.jsp";

	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		response.setContentType("text/html");
		PortletContext context = getPortletContext();
		PortletRequestDispatcher requestDispatcher = context
				.getRequestDispatcher(VIEW_PAGE);
		requestDispatcher.include(request, response);
	}

	@ProcessEvent(name = "buysoy")
	public void soldSoy(EventRequest request, EventResponse response) {
		String soyTypo = (String) request.getEvent().getValue();
		if (soyTypo.equals("5")) {
			request.setAttribute("say", "5毛的酱油没有了");
			 response.setRenderParameter("say", "5毛的酱油没有了");
		} else if (soyTypo.equals("1")) {
			response.setRenderParameter("say", "请拿好酱油");
		} else {
			response.setRenderParameter("say", "没有您要的那种酱油");
		}
	}

	@ProcessEvent(name = "byebye")
	public void sayByeBye(ActionRequest request, ActionResponse response) {
		response.setRenderParameter("say", "5毛的酱油没有了");
	}
}
