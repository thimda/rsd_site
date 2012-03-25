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

import nc.uap.portal.container.om.CustomWindowState;
 
/**
 * A custom window state that one or more portlets in this portlet application supports. Used in: portlet-app <p>Java
 * class for custom-window-stateType complex type. <p>The following schema fragment specifies the expected content
 * contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;custom-window-stateType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;window-state&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}window-stateType&quot;/&gt;
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
@XmlType(name = "custom-window-stateType", propOrder = { "description", "windowState" })
public class CustomWindowState implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4931808847573278646L;
	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(name = "window-state", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String windowState;

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

    public String getWindowState()
    {
        return windowState != null ? windowState.toLowerCase() : null;
    }

    public void setWindowState(String value)
    {
        windowState = value.toLowerCase();
    }
}
