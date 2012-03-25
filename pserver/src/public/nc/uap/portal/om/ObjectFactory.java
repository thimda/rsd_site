package nc.uap.portal.om;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * JAXB½âÎö¹¤³§
 * @author licza
 *
 */
@XmlRegistry
public class ObjectFactory {
	private final static QName _PortletApp_QNAME = new QName("", "display");

	public Display createPortletApp() {
		return new Display();
	}

	@XmlElementDecl(namespace = "", name = "display")
	public JAXBElement<Display> createPortletApp(Display value) {
		return new JAXBElement<Display>(_PortletApp_QNAME, Display.class, null, value);
	}
}
