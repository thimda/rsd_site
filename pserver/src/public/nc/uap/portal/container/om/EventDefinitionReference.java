package nc.uap.portal.container.om;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import nc.uap.portal.container.om.EventDefinitionReference;

/**
 * The event-definition-referenceType is used to reference events declared with the event-definition element at
 * application level. Used in: portlet <p>Java class for event-definition-referenceType complex type. <p>The following
 * schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;event-definition-referenceType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;choice&gt;
 *         &lt;element name=&quot;qname&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}QName&quot;/&gt;
 *         &lt;element name=&quot;name&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}NCName&quot;/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name=&quot;id&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "event-definition-referenceType", propOrder = { "qname", "name" })
public class EventDefinitionReference implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2006314649416138318L;
	protected QName qname;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

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

    public QName getQualifiedName(String defaultNamespace)
    {
        return qname != null ? qname : name != null ? new QName(defaultNamespace, name) : null;
    }
}
