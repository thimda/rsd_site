package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import nc.uap.portal.container.om.Description;
import nc.uap.portal.container.om.PublicRenderParameter;

/**
 * The public-render-parameters defines a render parameter that is allowed to be public and thus be shared with other
 * portlets. The identifier must be used for referencing this public render parameter in the portlet code. Used in:
 * portlet-app <p>Java class for public-render-parameterType complex type. <p>The following schema fragment specifies
 * the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;public-render-parameterType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}Description &quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;identifier&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}string&quot;/&gt;
 *         &lt;choice&gt;
 *           &lt;element name=&quot;qname&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}QName&quot;/&gt;
 *           &lt;element name=&quot;name&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}NCName&quot;/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name=&quot;alias&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}QName&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
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
@XmlType(name = "public-render-parameterType", propOrder = { "description", "identifier", "qname", "name", "alias" })
public class PublicRenderParameter implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1739601747228806467L;
	@XmlElement(name = "description")
    protected List<Description > description;
    @XmlElement(required = true)
    protected String identifier;
    protected QName qname;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    protected List<QName> alias;

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

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String value)
    {
        identifier = value;
    }

    public QName getQName()
    {
        return qname;
    }

    public void setQName(QName value)
    {
        qname = value;
        name = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        name = value;
        qname = null;
    }

    public List<QName> getAliases()
    {
        if (alias == null)
        {
            alias = new ArrayList<QName>();
        }
        return alias;
    }

    public void addAlias(QName alias)
    {
        // TODO: check duplicates
        getAliases().add(alias);
    }
}
