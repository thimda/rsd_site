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

/**
 * The portlet-collectionType is used to identify a subset of portlets within a portlet application to which a security
 * constraint applies. Used in: security-constraint <p>Java class for portlet-collectionType complex type. <p>The
 * following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;portlet-collectionType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;portlet-name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-nameType&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "portlet-collectionType", propOrder = { "portletName" })
public class PortletCollection implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1179605543362833069L;
	@XmlElement(name = "portlet-name", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> portletName;

    public List<String> getPortletNames()
    {
        if (portletName == null)
        {
            portletName = new ArrayList<String>();
        }
        return portletName;
    }
}
