package nc.uap.portal.container.service.itf;

import java.util.List;

import javax.portlet.Event;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.portal.container.portlet.PortletContainer;
import nc.uap.portal.container.portlet.PortletWindow;



/**
 * 事件处理服务接口
 * @author licza
 */
public interface EventCoordinationService
{
	/**
	 * 处理事件
	 * @param container
	 * @param portletWindow
	 * @param request
	 * @param response
	 * @param events
	 */
	void processEvents(PortletContainer container, PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response, List<Event> events);
}
