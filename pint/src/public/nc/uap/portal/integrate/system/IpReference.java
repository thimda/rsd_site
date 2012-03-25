package nc.uap.portal.integrate.system;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <ip-reference>的XStream对应类
 * 
 * @author gd
 * 
 */
@XmlRootElement(name = "ip-reference")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpReference", propOrder = { "sourceIp", "targetIp" })
public class IpReference implements Serializable, Cloneable {
	private static final long serialVersionUID = 8270140534376829770L;
	@XmlAttribute
	protected String sourceIp;
	@XmlAttribute
	protected String targetIp;

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public Object clone() {
		try {
			IpReference ref = (IpReference) super.clone();
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
		if(obj != null && obj instanceof IpReference){
			return sourceIp != null && this.getSourceIp().equals(((IpReference) obj).getSourceIp());
		}
		return super.equals(obj);
	}
	
}
