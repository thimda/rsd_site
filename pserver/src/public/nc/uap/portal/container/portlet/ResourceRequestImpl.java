
package nc.uap.portal.container.portlet;

import java.util.Enumeration;
import java.util.Map;

import javax.portlet.CacheControl;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.servlet.http.HttpServletRequest;

import nc.uap.portal.constant.ParameterKey;


public class ResourceRequestImpl extends ClientDataRequestImpl implements
		ResourceRequest {
	private CacheControl cacheControl;
	@SuppressWarnings("unused")
	private String resourceID;
	private String cacheability;

	public void setCacheability(String cacheability) {
		this.cacheability = cacheability;
	}

	public ResourceRequestImpl(HttpServletRequest request,
			PortletWindow portletWindow, CacheControl cacheControl) {
		super(request, portletWindow, PortletRequest.RESOURCE_PHASE);
		this.cacheControl = cacheControl;
	}

	@Override
	public String getProperty(String name) {
		String result = getMimeRequestProperty(name, cacheControl);
		return result != null ? result : super.getProperty(name);
	}

	public String getCacheability() {
		 return cacheability;
	}

	public String getETag() {
		return cacheControl.getETag();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String[]> getPrivateRenderParameterMap() {
		return cloneParameterMap(request.getParameterMap());
	}

	public String getResourceID() {
		return	request.getParameter(ParameterKey.RESOURCE_ID) ;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	public String getResponseContentType() {
		return getServletRequest().getHeader("accept");
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getResponseContentTypes() {
		return getServletRequest().getHeaders("accept");
	}

	@Override
	public String getContextPath() {

		return request.getContextPath();
	}
}
