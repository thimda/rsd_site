package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/**
 * Ä£¿éÓ¦ÓÃ
 * @author licza
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "module-application", propOrder = { "module","defaultNameSpace","customPortletMode", "customWindowState", "eventDefinition", "publicRenderParameter"})
public class ModuleApplication implements Serializable{
	
	@XmlAttribute
	protected String module;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3496821178302242697L;
	
    @XmlElement(name = "custom-portlet-mode")
    protected List<CustomPortletMode> customPortletMode;
    @XmlElement(name = "custom-window-state")
    protected List<CustomWindowState> customWindowState;
    @XmlElement(name = "event-definition")
    protected List<EventDefinition> eventDefinition;
    @XmlElement(name = "public-render-parameter")
    protected List<PublicRenderParameter> publicRenderParameter;
    @XmlAttribute
    protected String defaultNameSpace;
    
    public CustomPortletMode getCustomPortletMode(String name)
    {
        for (CustomPortletMode cpm : getCustomPortletModes())
        {
            if (cpm.getPortletMode().equalsIgnoreCase(name))
            {
                return cpm;
            }
        }
        return null;
    }
    
    public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<? extends CustomPortletMode> getCustomPortletModes()
    {
        if (customPortletMode == null)
        {
            customPortletMode = new ArrayList<CustomPortletMode>();
        }
        return customPortletMode;
    }
    
    public CustomPortletMode addCustomPortletMode(String name)
    {
        if (getCustomPortletMode(name) != null)
        {
            throw new IllegalArgumentException("Custom PortletMode with mode name: "+name+" already defined");
        }
        CustomPortletMode cpm = new CustomPortletMode();
        cpm.setPortletMode(name);
        customPortletMode.add(cpm);
        return cpm;        
    }
    
    public CustomWindowState getCustomWindowState(String name)
    {
        for (CustomWindowState cws : getCustomWindowStates())
        {
            if (cws.getWindowState().equalsIgnoreCase(name))
            {
                return cws;
            }
        }
        return null;
    }
    
    public List<? extends CustomWindowState> getCustomWindowStates()
    {
        if (customWindowState == null)
        {
            customWindowState = new ArrayList<CustomWindowState>();
        }
        return customWindowState;
    }
    
    public CustomWindowState addCustomWindowState(String name)
    {
        if (getCustomWindowState(name) != null)
        {
            throw new IllegalArgumentException("Custom WindowState with state name: "+name+" already defined");
        }
        CustomWindowState cws = new CustomWindowState();
        cws.setWindowState(name);
        customWindowState.add(cws);
        return cws;        
    }
    
    public List<? extends EventDefinition> getEventDefinitions()
    {
        if (eventDefinition == null)
        {
            eventDefinition = new ArrayList<EventDefinition>();
        }
        return eventDefinition;
    }
    
    public EventDefinition addEventDefinition(String name)
    {
        // TODO: check duplicates (complication: set of qname and name)
        EventDefinition ed = new EventDefinition();
        ed.setName(name);
        eventDefinition.add(ed);
        return ed;        
    }
    
    public EventDefinition addEventDefinition(QName qname)
    {
        // TODO: check duplicates (complication: set of qname and name)
        EventDefinition ed = new EventDefinition();
        ed.setQName(qname);
        eventDefinition.add(ed);
        return ed;        
    }
    
    public PublicRenderParameter getPublicRenderParameter(String identifier)
    {
        for (PublicRenderParameter prp : getPublicRenderParameters())
        {
            if (prp.getIdentifier().equals(identifier))
            {
                return prp;
            }
        }
        return null;
    }
    
    public List<? extends PublicRenderParameter> getPublicRenderParameters()
    {
        if (publicRenderParameter == null)
        {
            publicRenderParameter = new ArrayList<PublicRenderParameter >();
        }
        return publicRenderParameter;
    }
    
    public PublicRenderParameter addPublicRenderParameter(String name, String identifier)
    {
        if (getPublicRenderParameter(identifier) != null)
        {
            throw new IllegalArgumentException("PublicRenderParameter with identifier: "+identifier+" already defined");
        }
        // TODO: check duplicates on name|qname?
        PublicRenderParameter  p = new PublicRenderParameter ();
        p.setName(name);
        p.setIdentifier(identifier);
        publicRenderParameter.add(p);
        return p;        
    }
    
    public PublicRenderParameter addPublicRenderParameter(QName qname, String identifier)
    {
        if (getPublicRenderParameter(identifier) != null)
        {
            throw new IllegalArgumentException("PublicRenderParameter with identifier: "+identifier+" already defined");
        }
        // TODO: check duplicates on name|qname?
        PublicRenderParameter  p = new PublicRenderParameter ();
        p.setQName(qname);
        p.setIdentifier(identifier);
        publicRenderParameter.add(p);
        return p;        
    }

	public List<CustomPortletMode> getCustomPortletMode() {
		return customPortletMode;
	}

	public void setCustomPortletMode(List<CustomPortletMode> customPortletMode) {
		this.customPortletMode = customPortletMode;
	}

	public List<CustomWindowState> getCustomWindowState() {
		return customWindowState;
	}

	public void setCustomWindowState(List<CustomWindowState> customWindowState) {
		this.customWindowState = customWindowState;
	}

	public List<EventDefinition> getEventDefinition() {
		return eventDefinition;
	}

	public void setEventDefinition(List<EventDefinition> eventDefinition) {
		this.eventDefinition = eventDefinition;
	}

	public List<PublicRenderParameter> getPublicRenderParameter() {
		return publicRenderParameter;
	}

	public void setPublicRenderParameter(
			List<PublicRenderParameter> publicRenderParameter) {
		this.publicRenderParameter = publicRenderParameter;
	}

	public String getDefaultNameSpace() {
		return defaultNameSpace;
	}

	public void setDefaultNameSpace(String defaultNameSpace) {
		this.defaultNameSpace = defaultNameSpace;
	}}
