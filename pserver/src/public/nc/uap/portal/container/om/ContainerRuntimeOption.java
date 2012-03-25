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

import nc.uap.portal.container.om.ContainerRuntimeOption;

/**
 * The container-runtime-option element contains settings for the portlet container that the portlet expects to be
 * honored at runtime. These settings may re-define default portlet container behavior, like the javax.portlet.escapeXml
 * setting that disables XML encoding of URLs produced by the portlet tag library as default. Names with the
 * javax.portlet prefix are reserved for the Java Portlet Specification. Used in: portlet-app, portlet <p>Java class
 * for container-runtime-optionType complex type. <p>The following schema fragment specifies the expected content
 * contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;container-runtime-optionType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}nameType&quot;/&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}valueType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "container-runtime-optionType", propOrder = { "name", "value" })
public class ContainerRuntimeOption implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1905597497530681177L;
	public static final String MODULE = "module";
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected List<String> value;

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
}
