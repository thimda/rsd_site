package nc.uap.portal.container.driver;

import javax.portlet.PortletRequest;

import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.service.itf.PortletInvokerService;



public class PortletInvocationEvent {

    public static int LOAD = PortletInvokerService.METHOD_LOAD.intValue();

    public static int ACTION = PortletInvokerService.METHOD_ACTION.intValue();

    public static int RENDER = PortletInvokerService.METHOD_RENDER.intValue();

//    public static int ADMIN = PortletInvokerService.METHOD_ADMIN.intValue();

    private PortletRequest portletRequest;

    private PortletWindow portletWindow;

    private int invocationMethod;

    public PortletInvocationEvent(PortletRequest portletRequest, PortletWindow portletWindow, int invocationMethod) {
        this.portletRequest = portletRequest;
        this.portletWindow = portletWindow;
        this.invocationMethod = invocationMethod;
    }

    public PortletRequest getPortletRequest() {
        return portletRequest;
    }

    public int getInvocationMethod() {
        return invocationMethod;
    }

    public PortletWindow getPortletWindow() {
        return portletWindow;
    }

}