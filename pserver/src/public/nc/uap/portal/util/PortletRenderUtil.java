package nc.uap.portal.util;

import java.io.IOException;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.portlet.PortletContainer;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.portlet.PortletWindowImpl;

/**
 * Portlet‰÷»æπ§æﬂ¿‡
 * 
 * @author licza
 * 
 */
public class PortletRenderUtil {
	public void render(PortletWindowID winId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PortletWindow window = new PortletWindowImpl(winId,
					WindowState.NORMAL, PortletMode.VIEW);
			PortletContainer container = new PortletContainer();
			container.doRender(window, request, response);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			try {
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
			}
		}
	}

	public void render(String pageModule, String pageId, String module,
			String portletId, WindowState windowState, PortletMode portletMode,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			PortletWindowID winId = new PortletWindowID(pageModule, pageId,
					module, portletId);
			PortletWindow window = new PortletWindowImpl(winId, windowState,
					portletMode);
			PortletContainer container = new PortletContainer();
			container.doRender(window, request, response);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			try {
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
			}
		}
	}
}
