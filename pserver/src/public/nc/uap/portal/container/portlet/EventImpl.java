
package nc.uap.portal.container.portlet;

import javax.portlet.Event;
import javax.xml.namespace.QName;

/**
 * Implementation of JSR-286 <code>Event</code>.
 *
 * @since 2.0
 */
public class EventImpl implements Event {

	private QName _qname;
	private java.io.Serializable _value;
	
	public EventImpl(QName qname){
		_qname = qname;
	}
	
	public EventImpl(QName qname, java.io.Serializable value){
		this(qname);
		_value = value;
	}

	public QName getQName() {
		return _qname;
	}

	public java.io.Serializable getValue() {
		return _value;
	}

	public String getName() {
		return _qname.getLocalPart();
	}
}
