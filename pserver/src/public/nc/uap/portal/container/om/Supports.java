package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nc.uap.portal.container.om.Supports;

/**
 * Supports indicates the portlet modes a portlet supports for a specific content type. All portlets must support the
 * view mode. Used in: portlet <p>Java class for Supports complex type. <p>The following schema fragment specifies
 * the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Supports&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;mime-type&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}mime-typeType&quot;/&gt;
 *         &lt;element name=&quot;portlet-mode&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-modeType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;window-state&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}window-stateType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
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
@XmlType(name = "Supports", propOrder = { "mimeType", "portletMode", "windowState" })
public class Supports implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3994566638091033171L;
	@XmlElement(name = "mime-type", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String mimeType;
    @XmlElement(name = "portlet-mode")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> portletMode;
    @XmlElement(name = "window-state")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> windowState;

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String value)
    {
        mimeType = value;
    }

    public List<String> getPortletModes()
    {
        if (portletMode == null)
        {
            portletMode = new ArrayList<String>();
        }
        return portletMode;
    }
    
    public void addPortletMode(String name)
    {
        for (String mode : getPortletModes())
        {
            if (mode.equals(name))
            {
                throw new IllegalArgumentException("Portlet mode: "+name+" already defined");
            }
        }
        portletMode.add(name);
    }

    public List<String> getWindowStates()
    {
        if (windowState == null)
        {
            windowState = new ArrayList<String>();
        }
        return windowState;
    }

    public void addWindowState(String name)
    {
        for (String state : getWindowStates())
        {
            if (state.equals(name))
            {
                throw new IllegalArgumentException("Window state: "+name+" already defined");
            }
        }
        windowState.add(name);
    }
}
