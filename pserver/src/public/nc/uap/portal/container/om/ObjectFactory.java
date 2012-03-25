package nc.uap.portal.container.om;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated. 
 * <p>An ObjectFactory allows you to programatically construct new
 * instances of the Java representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * 
 * @version $Id$
 */
@XmlRegistry
public class ObjectFactory
{
    private final static QName _PortletApp_QNAME = new QName("http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd",
                                                             "portlet-app");

    public ObjectFactory()
    {
    }

    public PortletApplicationDefinition createPortletApp()
    {
        return new PortletApplicationDefinition();
    }

    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd", name = "portlet-app")
    public JAXBElement<PortletApplicationDefinition> createPortletApp(PortletApplicationDefinition value)
    {
        return new JAXBElement<PortletApplicationDefinition>(_PortletApp_QNAME, PortletApplicationDefinition.class, null, value);
    }
}
