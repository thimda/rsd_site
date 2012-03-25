package nc.uap.portal.container.portlet;

import java.io.Serializable;
import java.util.Enumeration;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.portal.util.ArgumentUtility;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * Portlet响应上下文
 * 
 * @author licza
 * 
 */
public class PortletResponseContext extends ExtendPropertySupport implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2450721290994621827L;
	protected HttpServletRequest containerRequest;
	protected HttpServletResponse containerResponse;
	protected PortletWindow window;
	protected PortletMode portletMode;
	protected WindowState windowState;

	protected boolean closed;
	protected boolean released;
	protected String title;

	public PortletResponseContext(HttpServletRequest containerRequest,
			HttpServletResponse containerResponse, PortletWindow window) {
		this.containerRequest = containerRequest;
		this.containerResponse = containerResponse;
		this.window = window;
	}

	protected boolean isClosed() {
		return closed;
	}

	protected boolean isReleased() {
		return released;
	}

	public void addProperty(Cookie cookie) {
		if (!isClosed()) {
			containerResponse.addCookie(cookie);
		}
	}

	public void addProperty(String key, Element element) {
		ArgumentUtility.validateNotEmpty("key", key);
		add2Property(key, element);
	}

	public void addProperty(String key, String value) {
		ArgumentUtility.validateNotEmpty("key", value);
		add2Property(key, value);
	}

	public Object getProperty(String key) {
		return get2Property(key);
	}

	public Element createElement(String tagName) throws DOMException {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			return doc.createElement(tagName);
		} catch (ParserConfigurationException e) {
			throw new DOMException((short) 0, "Initialization failure");
		}
	}

	public void close() {
		closed = true;
	}

	public PortletWindow getPortletWindow() {
		return window;
	}

	public HttpServletRequest getContainerRequest() {
		return containerRequest;
	}

	public HttpServletResponse getContainerResponse() {
		return containerResponse;
	}

	public void release() {
		closed = true;
		released = true;
		window = null;
	}

	public void setProperty(String key, String value) {
		ArgumentUtility.validateNotEmpty("key", key);
		set2Property(key, value);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PortletMode getPortletMode() {
		return portletMode;
	}

	public void setPortletMode(PortletMode portletMode)
			throws PortletModeException {
		ArgumentUtility.validateNotNull("portletMode", portletMode);
		this.portletMode = portletMode;
	}

	public void setWindowState(WindowState windowState)
			throws WindowStateException {
		ArgumentUtility.validateNotNull("windowState", windowState);
		if (isWindowStateAllowed(windowState)) {
			this.windowState = windowState;
		} else {
			throw new WindowStateException("Can't set this WindowState",
					windowState);
		}
	}

	public WindowState getWindowState() {
		return windowState;
	}
	protected boolean isWindowStateAllowed(WindowState state) {
		Enumeration<WindowState> supportedStates =    PortalContextImpl.getInstance().getSupportedWindowStates();
		while (supportedStates.hasMoreElements()) {
			if (supportedStates.nextElement().equals(state)) {
				return true;
			}
		}
		return false;
	}
}
