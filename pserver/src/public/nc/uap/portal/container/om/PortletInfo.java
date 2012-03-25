package nc.uap.portal.container.om;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nc.uap.portal.container.om.PortletInfo;

/**
 * <p>Java class for portlet-infoType complex type. <p>The following schema fragment specifies the expected content
 * contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;portlet-infoType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;title&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}titleType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;short-title&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}short-titleType&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;keywords&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}keywordsType&quot; minOccurs=&quot;0&quot;/&gt;
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
@XmlType(name = "portlet-infoType", propOrder = { "title", "shortTitle", "keywords" })
public class PortletInfo implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6082582523506882746L;
	@XmlElement (name = "title")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String title;
    @XmlElement(name = "short-title")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String shortTitle;
    @XmlElement(name = "keywords")
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String keywords;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String value)
    {
        title = value;
    }

    public String getShortTitle()
    {
        return shortTitle;
    }

    public void setShortTitle(String value)
    {
        shortTitle = value;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public void setKeywords(String value)
    {
        keywords = value;
    }
}
