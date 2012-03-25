package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nc.uap.portal.container.om.CustomPortletMode;
 
/**
 * A custom portlet mode that one or more portlets in this portlet application supports. If the portal does not need to
 * provide some management functionality for this portlet mode, the portal-managed element needs to be set to "false",
 * otherwise to "true". Default is "true". Used in: portlet-app <p>Java class for custom-portlet-modeType complex type.
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;custom-portlet-modeType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;portlet-mode&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-modeType&quot;/&gt;
 *         &lt;element name=&quot;portal-managed&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portal-managedType&quot; minOccurs=&quot;0&quot;/&gt;
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
@XmlType(name = "custom-portlet-modeType", propOrder = { "description", "portletMode", "portalManaged" })
public class CustomPortletMode implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4959604434462562063L;
	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(name = "portlet-mode", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String portletMode;
    @XmlElement(name = "portal-managed")
    protected Boolean portalManaged;
    
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

    public String getPortletMode()
    {
        return portletMode != null ? portletMode.toLowerCase() : null;
    }

    public void setPortletMode(String value)
    {
        portletMode = value.toLowerCase();
    }

    public boolean isPortalManaged()
    {
        return portalManaged != null ? portalManaged.booleanValue() : true;
    }

    public void setPortalManaged(boolean value)
    {
        portalManaged = value ? Boolean.TRUE : Boolean.FALSE;
    }
}
