package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import nc.uap.portal.container.om.Description;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.container.om.PortletInfo;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.om.SecurityRoleRef;
import nc.uap.portal.container.om.Supports;

/**
 * The portlet element contains the declarative data of a portlet. Used in: portlet-app <p>Java class for portletType
 * complex type. <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;portletType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;portlet-name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-nameType&quot;/&gt;
 *         &lt;element name=&quot;display-name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}display-nameType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;portlet-class&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-classType&quot;/&gt;
 *         &lt;element name=&quot;init-param&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}init-paramType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;expiration-cache&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}expiration-cacheType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;cache-scope&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}cache-scopeType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supports&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Supports&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *         &lt;element name=&quot;supported-locale&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}supported-localeType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;resource-bundle&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}resource-bundleType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;portlet-info&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-infoType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;portlet-preferences&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-preferencesType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;security-role-ref&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}security-role-refType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supported-processing-event&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}event-definition-referenceType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supported-publishing-event&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}event-definition-referenceType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supported-public-render-parameter&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}string&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;container-runtime-option&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}container-runtime-optionType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;id&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortletDefinition", propOrder = { "description", "portletName", "displayName", "portletClass", "initParam",
                                            "expirationCache", "cacheScope", "supports", "supportedLocale",
                                            "resourceBundle", "portletInfo", "portletPreferences", "securityRoleRef",
                                            "supportedProcessingEvent", "supportedPublishingEvent",
                                            "supportedPublicRenderParameter", "containerRuntimeOption" })
public class PortletDefinition implements Serializable 
{
	private static final long serialVersionUID = -7102274025251649938L;

	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(name = "portlet-name", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String portletName;
    @XmlElement(name = "display-name")
    protected List<DisplayName> displayName;
    @XmlElement(name = "portlet-class", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String portletClass;
    @XmlElement(name = "init-param")
    protected List<InitParam > initParam;
    @XmlElement(name = "expiration-cache")
    protected Integer expirationCache;
    @XmlElement(name = "cache-scope")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String cacheScope;
    @XmlElement(required = true)
    protected List<Supports> supports;
    @XmlElement(name = "supported-locale")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> supportedLocale;
    @XmlElement(name = "resource-bundle")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String resourceBundle;
    @XmlElement(name = "portlet-info")
    protected PortletInfo  portletInfo;
    @XmlElement(name = "portlet-preferences")
    protected Preferences portletPreferences;
    @XmlElement(name = "security-role-ref")
    protected List<SecurityRoleRef > securityRoleRef;
    @XmlElement(name = "supported-processing-event")
    protected List<EventDefinitionReference > supportedProcessingEvent;
    @XmlElement(name = "supported-publishing-event")
    protected List<EventDefinitionReference > supportedPublishingEvent;
    @XmlElement(name = "supported-public-render-parameter")
    protected List<String> supportedPublicRenderParameter;
    @XmlElement(name = "container-runtime-option")
    protected List<ContainerRuntimeOption > containerRuntimeOption;
    @XmlTransient
    protected String module;
    
    public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public PortletDefinition()
    {
    }
    
	public List<Description > getDescription() {
		return description;
	}

	public void setDescription(List<Description > description) {
		this.description = description;
	}

	public List<DisplayName> getDisplayName() {
		return displayName;
	}

	public void setDisplayName(List<DisplayName> displayName) {
		this.displayName = displayName;
	}

	public List<InitParam > getInitParam() {
		return initParam;
	}

	public void setInitParam(List<InitParam > initParam) {
		this.initParam = initParam;
	}

	public List<String> getSupportedLocale() {
		return supportedLocale;
	}

	public void setSupportedLocale(List<String> supportedLocale) {
		this.supportedLocale = supportedLocale;
	}

	public List<SecurityRoleRef > getSecurityRoleRef() {
		return securityRoleRef;
	}

	public void setSecurityRoleRef(List<SecurityRoleRef > securityRoleRef) {
		this.securityRoleRef = securityRoleRef;
	}

	public List<EventDefinitionReference > getSupportedProcessingEvent() {
		return supportedProcessingEvent;
	}

	public void setSupportedProcessingEvent(List<EventDefinitionReference > supportedProcessingEvent) {
		this.supportedProcessingEvent = supportedProcessingEvent;
	}

	public List<EventDefinitionReference > getSupportedPublishingEvent() {
		return supportedPublishingEvent;
	}

	public void setSupportedPublishingEvent(List<EventDefinitionReference > supportedPublishingEvent) {
		this.supportedPublishingEvent = supportedPublishingEvent;
	}

	public List<String> getSupportedPublicRenderParameter() {
		return supportedPublicRenderParameter;
	}

	public void setSupportedPublicRenderParameter(List<String> supportedPublicRenderParameter) {
		this.supportedPublicRenderParameter = supportedPublicRenderParameter;
	}

	public void setExpirationCache(Integer expirationCache) {
		this.expirationCache = expirationCache;
	}

	public void setSupports(List<Supports> supports) {
		this.supports = supports;
	}

	public void setPortletInfo(PortletInfo  portletInfo) {
		this.portletInfo = portletInfo;
	}

	public void setPortletPreferences(Preferences portletPreferences) {
		this.portletPreferences = portletPreferences;
	}
	
   

    public Description getDescription(Locale locale)
    {
        for (Description d : getDescriptions())
        {
            if (d.getLocale().equals(locale))
            {
                return d;
            }
        }
        return null;
    }
    
    public List<? extends Description> getDescriptions()
    {
        if (description == null)
        {
            description = new ArrayList<Description >();
        }
        return description;
    }
    
    public Description addDescription(String lang)
    {
        Description  d = new Description ();
        d.setLang(lang);
        if (getDescription(d.getLocale()) != null)
        {
            throw new IllegalArgumentException("Description for language: "+d.getLocale()+" already defined");
        }
        getDescriptions();
        description.add(d);
        return d;
    }

    public DisplayName getDisplayName(Locale locale)
    {
        for (DisplayName d : getDisplayNames())
        {
            if (d.getLocale().equals(locale))
            {
                return d;
            }
        }
        return null;
    }
    
    public List<? extends DisplayName> getDisplayNames()
    {
        if (displayName == null)
        {
            displayName = new ArrayList<DisplayName>();
        }
        return displayName;
    }
    
    public DisplayName addDisplayName(String lang)
    {
        DisplayName d = new DisplayName();
        d.setLang(lang);
        if (getDisplayName(d.getLocale()) != null)
        {
            throw new IllegalArgumentException("DisplayName for language: "+d.getLocale()+" already defined");
        }
        getDisplayNames();
        displayName.add(d);
        return d;
    }

    public String getPortletName()
    {
        return portletName;
    }

    public void setPortletName(String value)
    {
        portletName = value;
    }

    public String getPortletClass()
    {
        return portletClass;
    }

    public void setPortletClass(String value)
    {
        portletClass = value;
    }

    public InitParam getInitParam(String name)
    {
        for (InitParam param : getInitParams())
        {
            if (param.getParamName().equals(name))
            {
                return param;
            }
        }
        return null;
    }

    public List<? extends InitParam> getInitParams()
    {
        if (initParam == null)
        {
            initParam = new ArrayList<InitParam >();
        }
        return initParam;
    }
    
    public InitParam addInitParam(String paramName)
    {
        if (getInitParam(paramName) != null)
        {
            throw new IllegalArgumentException("Init parameter: "+paramName+" already defined");
        }
        InitParam  param = new InitParam ();
        param.setParamName(paramName);
        getInitParams();
        initParam.add(param);
        return param;
    }
    
    public int getExpirationCache()
    {
        return expirationCache != null ? expirationCache.intValue() : 0;
    }

    public void setExpirationCache(int value)
    {
        expirationCache = Integer.valueOf(value);
    }

    /**
     * Caching scope, allowed values are "private" indicating that the content should not be shared across users and
     * "public" indicating that the content may be shared across users. The default value if not present is "private".
     */
    public String getCacheScope()
    {
        return cacheScope != null ? cacheScope : "private";
    }

    public void setCacheScope(String cacheScope)
    {
        this.cacheScope = cacheScope;
    }

    public Supports getSupports(String mimeType)
    {
        for (Supports s : getSupports())
        {
            if (s.getMimeType().equals(mimeType))
            {
                return s;
            }
        }
        return null;
    }
    
    public List<? extends Supports> getSupports()
    {
        if (supports == null)
        {
            supports = new ArrayList<Supports>();
        }
        return supports;
    }
    
    public Supports addSupports(String mimeType)
    {
        if (getSupports(mimeType) != null)
        {
            throw new IllegalArgumentException("Supports for mime type: "+mimeType+" already defined");
        }
        Supports s = new Supports();
        s.setMimeType(mimeType);
        supports.add(s);
        return s;        
    }
    
    public List<String> getSupportedLocales()
    {
        if (supportedLocale == null)
        {
            supportedLocale = new ArrayList<String>();
        }
        return supportedLocale;
    }
    
    public void addSupportedLocale(String lang)
    {
        for (String l : getSupportedLocales())
        {
            if (l.equals(lang))
            {
                throw new IllegalArgumentException("Supported locale: "+lang+" already defined");
            }
        }
        supportedLocale.add(lang);    
    }

    public String getResourceBundle()
    {
        return resourceBundle;
    }

    public void setResourceBundle(String value)
    {
        resourceBundle = value;
    }

    public PortletInfo getPortletInfo()
    {
        if (portletInfo == null)
        {
            portletInfo = new PortletInfo ();
        }
        return portletInfo;
    }

    public Preferences getPortletPreferences()
    {
        if (portletPreferences == null)
        {
            portletPreferences = new Preferences();
        }
        return portletPreferences;
    }
    
    public SecurityRoleRef getSecurityRoleRef(String roleName)
    {
        for (SecurityRoleRef ref : getSecurityRoleRefs())
        {
            if (ref.getRoleName().equals(roleName))
            {
                return ref;
            }
        }
        return null;
    }

    public List<? extends SecurityRoleRef> getSecurityRoleRefs()
    {
        if (securityRoleRef == null)
        {
            securityRoleRef = new ArrayList<SecurityRoleRef >();
        }
        return securityRoleRef;
    }
    
    public SecurityRoleRef addSecurityRoleRef(String roleName)
    {
        if (getSecurityRoleRef(roleName) != null)
        {
            throw new IllegalArgumentException("Security role reference for role: "+roleName+" already defined");
        }
        SecurityRoleRef  srr = new SecurityRoleRef ();
        srr.setRoleName(roleName);
        securityRoleRef.add(srr);
        return srr;        
    }
    
    public List<? extends EventDefinitionReference> getSupportedProcessingEvents()
    {
        if (supportedProcessingEvent == null)
        {
            supportedProcessingEvent = new ArrayList<EventDefinitionReference >();            
        }
        return supportedProcessingEvent;
    }

    public EventDefinitionReference addSupportedProcessingEvent(QName qname)
    {
        // TODO: check duplicates
        getSupportedProcessingEvents();
        EventDefinitionReference  edr = new EventDefinitionReference ();
        edr.setQName(qname);
        supportedProcessingEvent.add(edr);
        return edr;
    }
    
    public EventDefinitionReference addSupportedProcessingEvent(String name)
    {
        // TODO check duplicates
        getSupportedProcessingEvents();
        EventDefinitionReference  edr = new EventDefinitionReference ();
        edr.setName(name);
        supportedProcessingEvent.add(edr);
        return edr;
    }
        
    public List<? extends EventDefinitionReference> getSupportedPublishingEvents()
    {
        if (supportedPublishingEvent == null)
        {
            supportedPublishingEvent = new ArrayList<EventDefinitionReference >();            
        }
        return supportedPublishingEvent;
    }

    public EventDefinitionReference addSupportedPublishingEvent(QName qname)
    {
        // TODO: check duplicates
        getSupportedPublishingEvents();
        EventDefinitionReference  edr = new EventDefinitionReference ();
        edr.setQName(qname);
        supportedPublishingEvent.add(edr);
        return edr;
    }
    
    public EventDefinitionReference addSupportedPublishingEvent(String name)
    {
        // TODO check duplicates
        getSupportedPublishingEvents();
        EventDefinitionReference  edr = new EventDefinitionReference ();
        edr.setName(name);
        supportedPublishingEvent.add(edr);
        return edr;
    }
        
    public List<String> getSupportedPublicRenderParameters()
    {
        if (supportedPublicRenderParameter == null)
        {
            supportedPublicRenderParameter = new ArrayList<String>();
        }
        return supportedPublicRenderParameter;
    }
    
    public void addSupportedPublicRenderParameter(String identifier)
    {
        for (String ident : getSupportedPublicRenderParameters())
        {
            if (ident.equals(identifier))
            {
                throw new IllegalArgumentException("Support for public render parameter with identifier: "+identifier+" already defined");
            }
        }
        supportedPublicRenderParameter.add(identifier);
    }

    public ContainerRuntimeOption  getContainerRuntimeOption(String name)
    {
        for (ContainerRuntimeOption  cro : getContainerRuntimeOptions())
        {
            if (cro.getName().equals(name))
            {
                return cro;
            }
        }
        return null;
    }
    
    public List<ContainerRuntimeOption > getContainerRuntimeOptions()
    {
        if (containerRuntimeOption == null)
        {
            containerRuntimeOption = new ArrayList<ContainerRuntimeOption >();
        }
        return containerRuntimeOption;
    }
    
    public ContainerRuntimeOption  addContainerRuntimeOption(String name)
    {
        if (getContainerRuntimeOption(name) != null)
        {
            throw new IllegalArgumentException("Container runtime option with name: "+name+" already defined");
        }
        ContainerRuntimeOption  cro = new ContainerRuntimeOption ();
        cro.setName(name);
        containerRuntimeOption.add(cro);
        return cro;        
    }
 
}
