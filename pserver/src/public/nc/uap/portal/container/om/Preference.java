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

import nc.uap.portal.container.om.Preference;

/**
 * Persistent preference values that may be used for customization and personalization by the portlet. Used in:
 * portlet-preferences <p>Java class for preferenceType complex type. <p>The following schema fragment specifies the
 * expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;preferenceType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}nameType&quot;/&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}valueType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;read-only&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}read-onlyType&quot; minOccurs=&quot;0&quot;/&gt;
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
@XmlType(name = "preferenceType", propOrder = { "name", "value", "readOnly","description","resourceid"})
public class Preference implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8927422166301417132L;
	@XmlElement(required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> value;
    @XmlElement(name = "read-only")
    protected Boolean readOnly;

    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String description;
    
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String resourceid;
    
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResourceid() {
		return resourceid;
	}

	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}

	public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        name = value;
    }

    public List<String> getValues()
    {
        if (value == null)
        {
            value = new ArrayList<String>();
        }
        return value;
    }
    
    public void addValue(String value)
    {
        getValues().add(value);
    }

    public boolean isReadOnly()
    {
        return readOnly != null ? readOnly.booleanValue() : false;
    }

    public void setReadOnly(boolean value)
    {
        readOnly = value ? Boolean.TRUE : Boolean.FALSE;
    }
}
