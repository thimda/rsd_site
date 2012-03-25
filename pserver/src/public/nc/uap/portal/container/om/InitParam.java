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

import nc.uap.portal.container.om.Description;
import nc.uap.portal.container.om.InitParam;

/**
 * The init-param element contains a name/value pair as an initialization param of the portlet Used in:portlet <p>Java
 * class for init-paramType complex type. <p>The following schema fragment specifies the expected content contained
 * within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;init-paramType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;name&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}nameType&quot;/&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}valueType&quot;/&gt;
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
@XmlType(name = "init-paramType", propOrder = { "description", "name", "value" })
public class InitParam implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7264033734727464175L;
	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String value;

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

    public String getParamName()
    {
        return name;
    }

    public void setParamName(String value)
    {
        name = value;
    }

    public String getParamValue()
    {
        return value;
    }

    public void setParamValue(String value)
    {
        this.value = value;
    }
}
