package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import nc.uap.portal.container.om.Description;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.Listener;

/**
 * The listenerType is used to declare listeners for this portlet application. Used in: portlet-app <p>Java class for
 * listenerType complex type. <p>The following schema fragment specifies the expected content contained within this
 * class.
 * 
 * <pre>
 * &lt;complexType name=&quot;listenerType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;display-name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}display-nameType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;listener-class&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}fully-qualified-classType&quot;/&gt;
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
@XmlType(name = "listenerType", propOrder = { "description", "displayName", "listenerClass" })
public class Listener implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(name = "display-name")
    protected List<DisplayName > displayName;
    @XmlElement(name = "listener-class", required = true)
    protected String listenerClass;

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
            displayName = new ArrayList<DisplayName >();
        }
        return displayName;
    }
    
    public DisplayName addDisplayName(String lang)
    {
        DisplayName  d = new DisplayName ();
        d.setLang(lang);
        if (getDisplayName(d.getLocale()) != null)
        {
            throw new IllegalArgumentException("DisplayName for language: "+d.getLocale()+" already defined");
        }
        getDisplayNames();
        displayName.add(d);
        return d;
    }

    public String getListenerClass()
    {
        return listenerClass;
    }

    public void setListenerClass(String value)
    {
        listenerClass = value;
    }
}
