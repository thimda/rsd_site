package nc.uap.portal.integrate.system;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <reference>对应类
 * @author gd 2008-12-31
 *
 */
@XmlRootElement(name = "reference")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Reference", propOrder = { "name", "value" })
public class Reference implements Serializable,Cloneable {
	private static final long serialVersionUID = -3019978947844917452L;
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Object clone()
	{
		try {
			Reference ref = (Reference)super.clone();
			return ref;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	@Override
	public boolean equals(Object obj) {
		/**
		 * 重写比较类
		 */
		if(obj != null && obj instanceof Reference){
			return name != null && this.getName().equals(((Reference) obj).getName());
		}
		return super.equals(obj);
	}
	
}
