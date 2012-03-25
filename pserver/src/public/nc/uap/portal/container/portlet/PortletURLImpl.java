package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.ParameterKey;
import nc.uap.portal.container.service.ContainerServices;
import nc.uap.portal.container.service.itf.PortletURLListenerService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Refactoring where functionality was pushed into JSR-286 <code>BaseURL</code>.
 * 
 * @since 2.0
 */
public class PortletURLImpl implements PortletURL, ResourceURL {

	private PortalContext portalContext;
	private String cacheLevel = ResourceURL.PAGE;
	private boolean filtering;
	private PortletWindow portletWindow;
	private PortletMode portletMode;
	private WindowState windowState;
	private Map<String, String[]> renderParameters;
	private Map<String, String[]> publicRenderParameters;
	private String resourceID;
	protected Map<String, List<String>> propertiesMap =  
		new HashMap<String, List<String>>();
	public PortletURLImpl(PortletWindow portletWindow) {
		this.portalContext = ContainerServices.getInstance().getPortalContext();
		this.portletWindow = portletWindow;
	}

	// public PortletURLImpl(String cacheLevel)
	// {
	// this(responseContext, PortletURLProvider.TYPE.RESOURCE);
	// if (cacheLevel != null)
	// {
	// this.cacheLevel = cacheLevel;
	// }
	// urlProvider.setCacheability(this.cacheLevel);
	// }

	public PortletWindow getPortletWindow() {
		return portletWindow;
	}

	public void setPortletWindow(PortletWindow portletWindow) {
		this.portletWindow = portletWindow;
	}

	public String toString() {
		filterURL();
		Writer out = new StringWriter();
		try {
			write(out);
		} catch (IOException e) {
			LfwLogger.warn("Portlet URL …˙≥…“Ï≥££°");
		}

		return out.toString();
	}

	@Override
	public void write(Writer out) throws IOException {
		// TODO Auto-generated method stub
		if(portletWindow == null || portletWindow.getId() == null )
			throw new IOException("PortletWindowID is NULL");
		PortletWindowID windId = portletWindow.getId();
		out.write(ParameterKey.ACTION_URL);
		out.write("?");
		out.write(ParameterKey.PAGE_MODULE);
		out.write("=");
		out.write(windId.getPageModule());
		out.write("&");
		out.write(ParameterKey.PAGE_NAME);
		out.write("=");
		out.write(windId.getPageName());
		out.write("&");
		out.write(ParameterKey.PORTLET_MODULE);
		out.write("=");
		out.write(windId.getModule());
		out.write("&");
		out.write(ParameterKey.PORTLET_NAME);
		out.write("=");
		out.write(windId.getPortletName());
		if(StringUtils.isNotBlank(getResourceID())){
			out.write("&");
			out.write(ParameterKey.RESOURCE_ID);
			out.write("=");
			out.write(getResourceID());
			out.write("&");
			out.write(ParameterKey.CACHEABILITY);
			out.write("=");
			out.write(getCacheability() );
		}else{
			out.write("&");
			out.write(ParameterKey.WINDOW_STATE);
			out.write("=");
			out.write(windowState.toString());
			out.write("&");
			out.write(ParameterKey.PORTLET_MODE);
			out.write("=");
			out.write(portletMode.toString());
		}
		
		if(renderParameters!=null&&(!renderParameters.isEmpty())){
		for(Map.Entry<String, String[]> entry: renderParameters.entrySet()){
			if(entry.getValue().length>0){
				for(String val:entry.getValue()){
					out.write("&"+entry.getKey()+"=");
					out.write(val);
				}
			}
		}
		}
		out.flush();
	}

	public void setPortletMode(PortletMode mode) {
		this.portletMode = mode;
	}

	public PortletMode getPortletMode() {
		return portletMode;
	}

	public void setWindowState(WindowState state) {
		this.windowState = state;
	}

	public WindowState getWindowState() {
		return windowState;
	}

	public void setSecure(boolean secure) throws PortletSecurityException {
		// ignore: not supported
	}

	public boolean isSecure() {
		return false;
	}

	public Map<String, String[]> getRenderParameters() {
		if (renderParameters == null) {
			renderParameters = new HashMap<String, String[]>();
		}
		return renderParameters;
	}

	public Map<String, String[]> getPublicRenderParameters() {
		if (publicRenderParameters == null) {
			publicRenderParameters = new HashMap<String, String[]>();
		}
		return publicRenderParameters;
	}

	public String getCacheability() {
		return cacheLevel;
	}

	public void setCacheability(String cacheLevel) {
		this.cacheLevel = cacheLevel;
	}

	public String getResourceID() {
		return resourceID;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

 

	public void write(Writer out, boolean escapeXML) throws IOException {
//		String result = apply(false);
//		if (escapeXML) {
//			result = result.replaceAll("&", "&amp;");
//			result = result.replaceAll("<", "&lt;");
//			result = result.replaceAll(">", "&gt;");
//			result = result.replaceAll("\'", "&#039;");
//			result = result.replaceAll("\"", "&#034;");
//		}
//		out.write(result);
	}

	@Override
	public void removePublicRenderParameter(String name) {
		// TODO Auto-generated method stub

	}

 

	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(String key, String value) {
		if(key == null){
    		throw new NullPointerException();
    	}
    	if(renderParameters==null){
    		renderParameters=new HashMap<String,String[]>();
    	}
    	List<String> valueList = new ArrayList<String>();
    	if(renderParameters.containsKey(key)){
    		CollectionUtils.addAll(valueList, renderParameters.get(key));
    	}
    	valueList.add(value);
    	renderParameters.put(key, valueList.toArray(new String[0]));
	}

	@Override
	public void setParameter(String key, String[] values) {
		if(key == null){
    		throw new NullPointerException();
    	}
    	if(renderParameters==null){
    		renderParameters=new HashMap<String,String[]>();
    	}
    	List<String> valueList = new ArrayList<String>();
    	if(renderParameters.containsKey(key)){
    		CollectionUtils.addAll(valueList, renderParameters.get(key));
    	}
    	CollectionUtils.addAll(valueList,values);
    	
    	renderParameters.put(key, valueList.toArray(new String[0]));

	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		// TODO Auto-generated method stub
		this.renderParameters=parameters;
	}

	@Override
	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub

	}

//	private boolean isPortletModeAllowed(PortletMode mode) {
//		if (PortletMode.VIEW.equals(mode)) {
//			return true;
//		}
//
//		String modeName = mode.toString();
//
//		PortletDefinition dd = getPortletWindow().getPortletDefinition();
//
//		for (Supports sup : dd.getSupports()) {
//			for (String m : sup.getPortletModes()) {
//				if (m.equalsIgnoreCase(modeName)) {
//					// check if a portlet managed mode which is always allowed.
//					CustomPortletMode cpm = dd.getApplication()
//							.getCustomPortletMode(modeName);
//					if (cpm != null && !cpm.isPortalManaged()) {
//						return true;
//					}
//					Enumeration<PortletMode> supportedModes = portalContext
//							.getSupportedPortletModes();
//					while (supportedModes.hasMoreElements()) {
//						if (supportedModes.nextElement().equals(mode)) {
//							return true;
//						}
//					}
//					return false;
//				}
//			}
//		}
//		return false;
//	}

	@SuppressWarnings("unused")
	private boolean isWindowStateAllowed(WindowState state) {
		Enumeration<WindowState> supportedStates = portalContext
				.getSupportedWindowStates();
		while (supportedStates.hasMoreElements()) {
			if (supportedStates.nextElement().equals(state)) {
				return true;
			}
		}
		return false;
	}

//	private boolean isPublicRenderParameter(String name) {
//		List<String> publicRenderParameterNames = getPortletWindow()
//				.getPortletDefinition().getSupportedPublicRenderParameters();
//		return publicRenderParameterNames.isEmpty() ? false
//				: publicRenderParameterNames.contains(name);
//	}
//
//	private static String[] cloneParameterValues(String[] values) {
//		int count = 0;
//		for (String s : values) {
//			if (s != null) {
//				count++;
//			}
//		}
//		if (count == 0) {
//			return null;
//		} else if (count < values.length) {
//			String[] copy = new String[count];
//			count = 0;
//			for (String s : values) {
//				if (s != null) {
//					copy[count++] = s;
//				}
//			}
//			return copy;
//		} else {
//			return values.clone();
//		}
//	}

	public void filterURL() {
		if (filtering) {
			throw new IllegalStateException(
					"Calling PortletURL toString or write methods from a PortletURLGenerationListener not allowed");
		}
		filtering = true;
		try {
			@SuppressWarnings("unused")
			PortletURLListenerService service = ContainerServices.getInstance()
					.getPortletURLListenerService();
//			PortletApplicationDefinition portletApp = getPortletWindow()
//					.getPortletDefinition().getApplication();
			// for (PortletURLGenerationListener listener :
			// service.getPortletURLGenerationListeners(portletApp))
			// {
			// if (PortletURLProvider.TYPE.ACTION == urlProvider.getType())
			// {
			// listener.filterActionURL(this);
			// }
			// else if (PortletURLProvider.TYPE.RESOURCE ==
			// urlProvider.getType())
			// {
			// listener.filterResourceURL(this);
			// }
			// else
			// {
			// listener.filterRenderURL(this);
			// }
			// }
		} finally {
			filtering = false;
		}
	}

	// BaseURL impl ------------------------------------------------------------

	 public void addProperty(String key, String value){
	 List<String> valueList = null;
 	
 	if(propertiesMap.containsKey(key)){
 		valueList = propertiesMap.get(key);//get old value list    		    	
 	}
 	else{
 		valueList = new ArrayList<String>();// create new value list    		    		
 	}

 	valueList.add(value);

 	propertiesMap.put(key, valueList);
	 }
	 }
	//
	// public Map<String, String[]> getParameterMap()
	// {
	// Map<String, String[]> parameters = urlProvider.getRenderParameters();
	// if (parameters.isEmpty())
	// {
	// parameters = Collections.emptyMap();
	// }
	// else
	// {
	// parameters = new HashMap<String,String[]>(parameters);
	// for (Map.Entry<String,String[]> entry : parameters.entrySet())
	// {
	// entry.setValue(entry.getValue().clone());
	// }
	// }
	// return parameters;
	// }
	//
	// public void setParameter(String name, String value)
	// {
	// ArgumentUtility.validateNotEmpty("name", name);
	// ArgumentUtility.validateNotNull("value", value);
	// String[] values = new String[]{value};
	// urlProvider.getRenderParameters().put(name, values);
	// if (renderURL && isPublicRenderParameter(name))
	// {
	// urlProvider.getPublicRenderParameters().put(name, values);
	// }
	// }
	//
	// public void setParameter(String name, String[] values)
	// {
	// ArgumentUtility.validateNotEmpty("name", name);
	// ArgumentUtility.validateNotNull("values", values);
	// values = cloneParameterValues(values);
	// if (values == null )
	// {
	// throw new
	// IllegalStateException("Illegal Argument: values array is empty or contains only null values");
	// }
	// urlProvider.getRenderParameters().put(name, values);
	// if (renderURL && isPublicRenderParameter(name))
	// {
	// urlProvider.getPublicRenderParameters().put(name, values);
	// }
	// }
	//
	// public void setParameters(Map<String, String[]> parameters)
	// {
	// ArgumentUtility.validateNotNull("parameters", parameters);
	//        
	// // validate map first
	// boolean emptyValuesArray;
	// for (Map.Entry<? extends Object, ? extends Object> entry :
	// parameters.entrySet())
	// {
	// if (entry.getKey() == null || entry.getValue() == null)
	// {
	// throw new
	// IllegalArgumentException("parameters map contains a null key or value entry");
	// }
	// if (!(entry.getKey() instanceof String))
	// {
	// throw new
	// IllegalArgumentException("parameters map contains a key which is not of type String");
	// }
	// if (!(entry.getValue() instanceof String[]))
	// {
	// throw new
	// IllegalArgumentException("parameters map contains a value which is not of type String[]");
	// }
	// emptyValuesArray = true;
	// for (String s : (String[])entry.getValue())
	// {
	// if (s != null)
	// {
	// emptyValuesArray = false;
	// break;
	// }
	// }
	// if (emptyValuesArray)
	// {
	// throw new
	// IllegalStateException("parameters map contains a values array which is empty or contains only null values");
	// }
	// }
	// urlProvider.getRenderParameters().clear();
	// if (renderURL)
	// {
	// for (Iterator<Map.Entry<String,String[]>> iter =
	// urlProvider.getPublicRenderParameters().entrySet().iterator();
	// iter.hasNext();)
	// {
	// if (iter.next().getValue() != null)
	// {
	// iter.remove();
	// }
	// }
	// }
	// for (Map.Entry<String,String[]> entry : parameters.entrySet())
	// {
	// String[] values = cloneParameterValues(entry.getValue());
	// urlProvider.getRenderParameters().put(entry.getKey(), values);
	// if (renderURL && isPublicRenderParameter(entry.getKey()))
	// {
	// urlProvider.getPublicRenderParameters().put(entry.getKey(), values);
	// }
	// }
	// }
	//
	// public void setProperty(String key, String value)
	// {
	// ArgumentUtility.validateNotEmpty("key", key);
	// Map<String, List<String>> properties = urlProvider.getProperties();
	// if (value == null)
	// {
	// properties.remove(key);
	// }
	// else
	// {
	// List<String> values = properties.get(key);
	// if (values == null)
	// {
	// values = new ArrayList<String>();
	// properties.put(key,values);
	// }
	// else
	// {
	// values.clear();
	// }
	// values.add(value);
	// }
	// }

	// public void setSecure(boolean secure) throws PortletSecurityException
	// {
	// urlProvider.setSecure(secure);
	// }
	//
	// public void write(Writer out, boolean escapeXML) throws IOException
	// {
	// filterURL();
	// urlProvider.write(out, escapeXML);
	// }
	//
	// public void write(Writer out) throws IOException
	// {
	// write(out, true);
	// }
	//    

	//
	// // PortletURL impl
	// ------------------------------------------------------------
	//    
	// public PortletMode getPortletMode()
	// {
	// return urlProvider.getPortletMode();
	// }
	//    
	// public WindowState getWindowState()
	// {
	// return urlProvider.getWindowState();
	// }
	//    
	// public void setPortletMode(PortletMode portletMode) throws
	// PortletModeException
	// {
	// ArgumentUtility.validateNotNull("portletMode", portletMode);
	// if (isPortletModeAllowed(portletMode))
	// {
	// urlProvider.setPortletMode(portletMode);
	// }
	// else
	// {
	// throw new PortletModeException("Can't set this PortletMode",
	// portletMode);
	// }
	// }
	//
	// public void setWindowState(WindowState windowState) throws
	// WindowStateException
	// {
	// ArgumentUtility.validateNotNull("windowState", windowState);
	// if (isWindowStateAllowed(windowState))
	// {
	// urlProvider.setWindowState(windowState);
	// }
	// else
	// {
	// throw new WindowStateException("Can't set this WindowState",
	// windowState);
	// }
	// }
	//
	// public void removePublicRenderParameter(String name)
	// {
	// ArgumentUtility.validateNotEmpty("name", name);
	// if (isPublicRenderParameter(name))
	// {
	// urlProvider.getPublicRenderParameters().put(name, null);
	// urlProvider.getRenderParameters().remove(name);
	// }
	// }
	//
	// // ResourceURL impl
	// ------------------------------------------------------------
	//    
	// public String getCacheability()
	// {
	// return urlProvider.getCacheability();
	// }
	//
	// public void setCacheability(String cacheLevel)
	// {
	// ArgumentUtility.validateNotEmpty("cachelevel", cacheLevel);
	// if (FULL.equals(cacheLevel))
	// {
	// // always OK
	// }
	// else if (PORTLET.equals(cacheLevel))
	// {
	// if (FULL.equals(this.cacheLevel))
	// {
	// throw new
	// IllegalStateException("Current request cacheablility is FULL: URLs with cacheability PORTLET not allowed");
	// }
	//            
	// }
	// else if (PAGE.equals(cacheLevel))
	// {
	// if (FULL.equals(this.cacheLevel))
	// {
	// throw new
	// IllegalStateException("Current request cacheablility is FULL: URLs with cacheability PORTLET not allowed");
	// }
	// else if (PORTLET.equals(this.cacheLevel))
	// {
	// throw new
	// IllegalStateException("Current request cacheablility is PORTLET: URLs with cacheability PAGE not allowed");
	// }
	// }
	// else
	// {
	// throw new IllegalArgumentException("Unknown cacheLevel: "+cacheLevel);
	// }
	// urlProvider.setCacheability(cacheLevel);
	// }
	//
	// public void setResourceID(String resourceID)
	// {
	// urlProvider.setResourceID(resourceID);
	// }
 
