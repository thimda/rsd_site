package nc.uap.portal.om;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Æ¤·ô
 * 
 * @author dingrf
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Skin", propOrder = { "id","name" })
public class Skin implements Serializable{

	private static final long serialVersionUID = 9207693617100021123L;

	/**
	 * id
	 */
	@XmlAttribute(name = "id")
	protected String id;

	/**
	 * name
	 */
	@XmlAttribute(name = "name")
	protected String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
