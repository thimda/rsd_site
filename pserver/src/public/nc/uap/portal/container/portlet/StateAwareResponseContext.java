package nc.uap.portal.container.portlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.Event;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.service.itf.EventProvider;
import nc.uap.portal.util.ArgumentUtility;

/**
 * 有状态响应上下文
 * @author licza
 *
 */
public class StateAwareResponseContext extends PortletResponseContext{

	public StateAwareResponseContext(HttpServletRequest containerRequest,
			HttpServletResponse containerResponse, PortletWindow window) {
		super(containerRequest, containerResponse, window);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -7728204360755263391L;
	protected Map publicRenderParameters = new HashMap();
	protected Map<String, String[]> renderParameters = new HashMap<String, String[]>();
	protected List<Event> events;
 	public Map<String, String[]> getRenderParameters() {
		return renderParameters;
	}

	protected EventProvider eventProvider=new EventProviderImpl(getPortletWindow());

	public EventProvider getEventProvider() {
		return eventProvider;
	}

	public void setEventProvider(EventProvider eventProvider) {
		this.eventProvider = eventProvider;
	}


	public Map getPublicRenderParameters() {
		return publicRenderParameters;
	}

	public void setPublicRenderParameters(Map publicRenderParameters) {
		this.publicRenderParameters = publicRenderParameters;
	}


	public Map<String, String[]> getRenderParameterMap() {
		Map<String, String[]> parameters = new HashMap<String, String[]>(
				getRenderParameters());
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			entry.setValue(entry.getValue().clone());
		}
		return parameters;
	}

	@SuppressWarnings("unchecked")
	public void removePublicRenderParameter(String name) {
		ArgumentUtility.validateNotEmpty("name", name);
		if (isPublicRenderParameter(name)) {
			getPublicRenderParameters().put(name, null);
			getRenderParameters().remove(name);
		}
	}

	public void setEvent(QName qname, Serializable value) {
		ArgumentUtility.validateNotNull("qname", qname);
		Event event = getEventProvider().createEvent(qname, value);
		if (event != null) {
			getEvents().add(event);
		}
	}

	public void setEvent(String name, Serializable value) {
		PortletWindow window = getPortletWindow();
		String module = window.getPortletDefinition().getModule();
		ModuleApplication app = PortalCacheManager.getModuleAppByModuleName(module);

		String defaultNamespace;
		defaultNamespace = app.getDefaultNameSpace();
		QName qname = new QName(defaultNamespace, name);
		setEvent(qname, value);
	}



	@SuppressWarnings("unchecked")
	public void setRenderParameters(java.util.Map<String, String[]> parameters) {
		ArgumentUtility.validateNotNull("parameters", parameters);

		// validate map first
		boolean emptyValuesArray;
		for (Map.Entry<? extends Object, ? extends Object> entry : parameters
				.entrySet()) {
			if (entry.getKey() == null || entry.getValue() == null) {
				throw new IllegalArgumentException(
						"parameters map contains a null key or value entry");
			}
			if (!(entry.getKey() instanceof String)) {
				throw new IllegalArgumentException(
						"parameters map contains a key which is not of type String");
			}
			if (!(entry.getValue() instanceof String[])) {
				throw new IllegalArgumentException(
						"parameters map contains a value which is not of type String[]");
			}
			emptyValuesArray = true;
			for (String s : (String[]) entry.getValue()) {
				if (s != null) {
					emptyValuesArray = false;
					break;
				}
			}
			if (emptyValuesArray) {
				throw new IllegalStateException(
						"parameters map contains a values array which is empty or contains only null values");
			}
		}

		getRenderParameters().clear();
		for (Iterator<Map.Entry<String, String[]>> iter = getPublicRenderParameters()
				.entrySet().iterator(); iter.hasNext();) {
			if (iter.next().getValue() != null) {
				iter.remove();
			}
		}
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String[] values = cloneParameterValues(entry.getValue());
			getRenderParameters().put(entry.getKey(), values);
			if (isPublicRenderParameter(entry.getKey())) {
				getPublicRenderParameters().put(entry.getKey(), values);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setRenderParameter(String key, String value) {
		ArgumentUtility.validateNotEmpty("key", key);
		ArgumentUtility.validateNotNull("value", value);
		String[] values = new String[] { value };
		getRenderParameters().put(key, values);
		if (isPublicRenderParameter(key)) {
			getPublicRenderParameters().put(key, values);
		}
	}

	@SuppressWarnings("unchecked")
	public void setRenderParameter(String key, String[] values) {
		ArgumentUtility.validateNotEmpty("key", key);
		ArgumentUtility.validateNotNull("values", values);
		values = cloneParameterValues(values);
		if (values == null) {
			throw new IllegalStateException(
					"Illegal Argument: values array is empty or contains only null values");
		}
		getRenderParameters().put(key, values);
		if (isPublicRenderParameter(key)) {
			getPublicRenderParameters().put(key, values);
		}
	}

	public List<Event> getEvents() {
		if (events == null) {
			events = new ArrayList<Event>();
		}
		return events;
	}
	private boolean isPublicRenderParameter(String name) {
		List<String> publicRenderParameterNames = getPortletWindow()
				.getPortletDefinition().getSupportedPublicRenderParameters();
		return publicRenderParameterNames.isEmpty() ? false
				: publicRenderParameterNames.contains(name);
	}

	private static String[] cloneParameterValues(String[] values) {
		int count = 0;
		for (String s : values) {
			if (s != null) {
				count++;
			}
		}
		if (count == 0) {
			return null;
		} else if (count < values.length) {
			String[] copy = new String[count];
			count = 0;
			for (String s : values) {
				if (s != null) {
					copy[count++] = s;
				}
			}
			return copy;
		} else {
			return values.clone();
		}
	}
}
