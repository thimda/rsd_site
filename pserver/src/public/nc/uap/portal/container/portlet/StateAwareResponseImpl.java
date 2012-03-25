package nc.uap.portal.container.portlet;

import java.io.Serializable;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.StateAwareResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.xml.namespace.QName;

/**
 * Implementation of JSR-286 <code>StateAwareResponse</code>.
 * 
 * @author licza
 */
public abstract class StateAwareResponseImpl extends PortletResponseImpl implements StateAwareResponse {
	protected StateAwareResponseContext context;

	public StateAwareResponseImpl(StateAwareResponseContext context) {
		super(context);
		this.context = context;
	}

	protected abstract void checkSetStateChanged();

	@Override
	public PortletMode getPortletMode() {
		return this.context.getPortletMode();
	}

	@Override
	public Map<String, String[]> getRenderParameterMap() {

		return this.context.getRenderParameterMap();
	}

	@Override
	public WindowState getWindowState() {
		return this.context.getWindowState();
	}

	@Override
	public void removePublicRenderParameter(String name) {
		this.context.removePublicRenderParameter(name);
	}

	@Override
	public void setEvent(QName qname, Serializable value) {
		this.context.setEvent(qname, value);
	}

	@Override
	public void setEvent(String name, Serializable value) {
		this.context.setEvent(name, value);
	}

	@Override
	public void setPortletMode(PortletMode portletMode)
			throws PortletModeException {
		this.context.setPortletMode(portletMode);
	}

	@Override
	public void setWindowState(WindowState windowState)
			throws WindowStateException {
		this.context.setWindowState(windowState);
	}

	@Override
	public void setRenderParameters(java.util.Map<String, String[]> parameters) {
		this.context.setRenderParameters(parameters);
	}

	@Override
	public void setRenderParameter(String key, String value) {
		this.context.setRenderParameter(key, value);
	}

	@Override
	public void setRenderParameter(String key, String[] values) {
		this.context.setRenderParameter(key, values);
	}

}
