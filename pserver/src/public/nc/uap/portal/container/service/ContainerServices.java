package nc.uap.portal.container.service;

import javax.portlet.PortalContext;
import nc.bs.framework.common.NCLocator;
import nc.uap.portal.container.portlet.PortalContextImpl;
import nc.uap.portal.container.service.impl.DefaultPortletInvokerService;
import nc.uap.portal.container.service.impl.EventCoordinationServiceImpl;
import nc.uap.portal.container.service.itf.EventCoordinationService;
import nc.uap.portal.container.service.itf.FilterManagerService;
import nc.uap.portal.container.service.itf.NamespaceMapper;
import nc.uap.portal.container.service.itf.PortletEnvironmentService;
import nc.uap.portal.container.service.itf.PortletInvokerService;
import nc.uap.portal.container.service.itf.PortletURLListenerService;
import nc.uap.portal.container.service.itf.RequestDispatcherService;
import nc.uap.portal.container.service.itf.UserInfoService;


public class ContainerServices {

	private static ContainerServices instance = new ContainerServices();

	private ContainerServices() {
	}

	public EventCoordinationService getEventCoordinationService() {
		return new EventCoordinationServiceImpl();
	}

	public FilterManagerService getFilterManagerService() {
		return NCLocator.getInstance().lookup(FilterManagerService.class);
	}

	public NamespaceMapper getNamespaceMapper() {
		return NCLocator.getInstance().lookup(NamespaceMapper.class);
	}

	public PortalContext getPortalContext() {
		return PortalContextImpl.getInstance();
	}

	public PortletEnvironmentService getPortletEnvironmentService() {
		return new PortletEnvironmentService();
	}

	public PortletInvokerService getPortletInvokerService() {
		return new DefaultPortletInvokerService();
	}
//
//	public IPortletPreferencesService getPortletPreferencesService() {
//		return new PortletPreferencesServiceImpl();
//	}

	public PortletURLListenerService getPortletURLListenerService() {
		return NCLocator.getInstance().lookup(PortletURLListenerService.class);
	}

	public RequestDispatcherService getRequestDispatcherService() {
		// TODO Auto-generated method stub
		return null;
	}

	public UserInfoService getUserInfoService() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ContainerServices getInstance() {
		return instance;
	}

}
