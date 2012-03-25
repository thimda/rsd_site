package nc.uap.portal.container.portlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletDefinition;


/**
 * Abstract PortletConfig base class Implementation.
 * <p>
 * An embedding Portal can extend this base class and is only required to provide
 * an implementation of the getResourceBundle bundle method.
 * </p>
 * 
 * @version $Id: AbstractPortletConfigImpl.java 763145 2009-04-08 08:53:16Z cziegeler $
 */
public abstract class AbstractPortletConfigImpl implements PortletConfig
{
    protected PortletContext portletContext;
    /**
     * The portlet descriptor.
     */
    protected PortletDefinition portlet;
    
    protected Map<String, String[]> containerRuntimeOptions;
    
    protected Set<String> supportedContainerRuntimeOptions; 

    public AbstractPortletConfigImpl(PortletContext portletContext, PortletDefinition portlet)
    {
        this.portletContext = portletContext;
        this.portlet = portlet;
        this.supportedContainerRuntimeOptions = new HashSet<String>();
        Enumeration<String> e = portletContext.getContainerRuntimeOptions();
        while(e.hasMoreElements()){
            supportedContainerRuntimeOptions.add(e.nextElement());
        }
    }

    public abstract ResourceBundle getResourceBundle(Locale locale);
    
    public String getPortletName() {
        return portlet.getModule() + "_" + portlet.getPortletName();
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public String getInitParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name == null");
        }

        Iterator<? extends InitParam> parms = portlet.getInitParams().iterator();
        while(parms.hasNext()) {
            InitParam param = parms.next();
            if (param.getParamName().equals(name)) {
                return param.getParamValue();
            }
        }
        return null;
    }

    public Enumeration<String> getInitParameterNames() {
        return new java.util.Enumeration<String>() {
            private Iterator<InitParam> iterator =
                new ArrayList<InitParam>(portlet.getInitParams()).iterator();

            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            public String nextElement() {
                if (iterator.hasNext()) {
                    return iterator.next().getParamName();
                } 
                return null;
            }
        };
    }

    public PortletDefinition getPortletDefinition() {
        return portlet;
    }
    // --------------------------------------------------------------------------------------------

	public Enumeration<String> getPublicRenderParameterNames() {
		if (portlet.getSupportedPublicRenderParameters() != null){
			return Collections.enumeration(portlet.getSupportedPublicRenderParameters());
		}
		return  Collections.enumeration(new ArrayList<String>());
	}

	public String getDefaultNamespace() {
		String module = portlet.getModule();
		ModuleApplication ma = PortalCacheManager.getModuleAppByModuleName(module);
		if (ma == null) return XMLConstants.NULL_NS_URI;
		if (ma.getDefaultNameSpace() == null)
			return XMLConstants.NULL_NS_URI;
		return ma.getDefaultNameSpace();
	}

	public Enumeration<QName> getProcessingEventQNames() {
	    ArrayList<QName> qnameList = new ArrayList<QName>();
        for (EventDefinitionReference ref : portlet.getSupportedProcessingEvents())
        {
            QName name = ref.getQualifiedName(getDefaultNamespace());
            if (name == null)
            {
                continue;
            }
            qnameList.add(name);
        }
        return Collections.enumeration(qnameList);
	}

	public Enumeration<QName> getPublishingEventQNames() {
        ArrayList<QName> qnameList = new ArrayList<QName>();
        for (EventDefinitionReference ref : portlet.getSupportedPublishingEvents())
        {
            QName name = ref.getQualifiedName(getDefaultNamespace());
            if (name == null)
            {
                continue;
            }
            qnameList.add(name);
        }
        return Collections.enumeration(qnameList);
	}

	public Enumeration<Locale> getSupportedLocales() {
		// for each String entry in SupportedLocales (portletDD)
		// add an entry in the resut list (new Locale(string))
		List<Locale> locals = new ArrayList<Locale>();
		List<String> localsAsStrings = portlet.getSupportedLocales();
		if (localsAsStrings!=null){
			for (String string : localsAsStrings) {
				locals.add(new Locale(string));
			}
		}
		return Collections.enumeration(locals);
	}
	
	public Map<String, String[]> getContainerRuntimeOptions()
	{
//	    synchronized(this)
//	    {
//	        if (containerRuntimeOptions == null)
//	        {
//	            containerRuntimeOptions = new HashMap<String, String[]>();
//	            String module = portlet.getModule();
//	    		ModuleApplication ma = PortalCacheManager.getModuleAppByModuleName(module);
//	            if(ma !=null && ma.getContainerRuntimeOption()!=null&&
//	            		(!portlet.getApplication().getContainerRuntimeOptions().isEmpty())){
//                 for (int ii=0;ii<portlet.getApplication().getContainerRuntimeOptions().size();ii++)
//                {
//                	ContainerRuntimeOption   option=(ContainerRuntimeOption )portlet.getApplication().getContainerRuntimeOptions().get(ii);
//                    List<String> values = option.getValues();
//                    String [] tempValues = new String[values.size()];
//                    for (int i=0;i<values.size();i++)
//                    {
//                        tempValues[i] = values.get(i);
//                    }
//                    containerRuntimeOptions.put(option.getName(),tempValues);
//                }
//                 }
//	            if(portlet.getContainerRuntimeOptions()!=null&&(!portlet.getContainerRuntimeOptions().isEmpty()))
//	            		{
//                for (int ii=0;ii<portlet.getContainerRuntimeOptions().size();ii++ )
//                {
//                	ContainerRuntimeOption   option=(ContainerRuntimeOption )portlet.getContainerRuntimeOptions().get(ii);
//                    List<String> values = option.getValues();
//                    String [] tempValues = new String[values.size()];
//                    for (int i=0;i<values.size();i++)
//                    {
//                        tempValues[i] = values.get(i);
//                    }
//                    containerRuntimeOptions.put(option.getName(),tempValues);
//                }
//	            for (Iterator<String> iter = containerRuntimeOptions.keySet().iterator(); iter.hasNext(); )
//	            {
//	                String key = iter.next();
//	                if (!supportedContainerRuntimeOptions.contains(key))
//	                {
//	                    iter.remove();
//	                }
//	            }
//	        }
//	        }
//	    }
//	    
//        if (!containerRuntimeOptions.isEmpty())
//        {
//            Map<String, String[]> result = new HashMap<String, String[]>(containerRuntimeOptions.size());
//            for (Map.Entry<String,String[]> entry : containerRuntimeOptions.entrySet())
//            {
//                if (entry.getValue() != null)
//                {
//                    result.put(entry.getKey(), entry.getValue().clone());
//                }
//            }
//            return Collections.unmodifiableMap(result);
//        }
        return Collections.emptyMap();
	}
}
