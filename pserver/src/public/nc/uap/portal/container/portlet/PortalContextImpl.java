package nc.uap.portal.container.portlet;

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

public class PortalContextImpl implements PortalContext {

	private static PortalContext instance = new PortalContextImpl();
	public static PortalContext getInstance() {
		return instance;
	}
	
	@Override
	public String getPortalInfo() {
		return null;
	}

	@Override
	public String getProperty(String key) {
		return null;
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return null;
	}

	@Override
	public Enumeration<PortletMode> getSupportedPortletModes() {
		return null;
	}

	@Override
	public Enumeration<WindowState> getSupportedWindowStates() {
		return null;
	}
	

}
