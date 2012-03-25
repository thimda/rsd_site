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

import nc.uap.portal.container.om.FilterMapping;

/**
 * Declaration of the filter mappings in this portlet application is done by using filter-mappingType. The container
 * uses the filter-mapping declarations to decide which filters to apply to a request, and in what order. To determine
 * which filters to apply it matches filter-mapping declarations on the portlet-name and the lifecyle phase defined in
 * the filter element. The order in which filters are invoked is the order in which filter-mapping declarations that
 * match appear in the list of filter-mapping elements. Used in: portlet-app <p>Java class for filter-mappingType
 * complex type. <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;filter-mappingType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;filter-name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}filter-nameType&quot;/&gt;
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
@XmlType(name = "filter-mappingType", propOrder = { "filterName", "portletName" })
public class FilterMapping implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8606933812877442463L;
	@XmlElement(name = "filter-name", required = true)
    protected String filterName;
    @XmlElement(name = "portlet-name", required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> portletName;

    public String getFilterName()
    {
        return filterName;
    }

    public void setFilterName(String value)
    {
        filterName = value;
    }

    public List<String> getPortletNames()
    {
        if (portletName == null)
        {
            portletName = new ArrayList<String>();
        }
        return portletName;
    }
    
    public void addPortletName(String name)
    {
        // TODO: check for duplicates
        getPortletNames().add(name);
    }
}
