package nc.uap.portal.integrate.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * SSO配置选项
 * 
 * @author licza
 * 
 */
@XmlRootElement(name = "sso-reference")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSOReference", propOrder = { "ipReference","reference" })
public class SSOReference implements Serializable {

	private static final long serialVersionUID = 1755619387134714214L;
	/**
	 * IP配置
	 */
	@XmlElement
	public List<IpReference> ipReference = new ArrayList<IpReference>();
	/**
	 * 普通配置文件
	 */
	@XmlElement
	private List<Reference> reference = new ArrayList<Reference>();

	public List<Reference> getReference() {
		return reference;
	}

	public void setReference(List<Reference> reference) {
		this.reference = reference;
	}

	public List<IpReference> getIpReference() {
		return ipReference;
	}

	public void setIpReference(List<IpReference> ipReference) {
		this.ipReference = ipReference;
	}

	public void addIpReference(IpReference ip) {
		ipReference.add(ip);
	}

	public void addReference(Reference ip) {
		reference.add(ip);
	}

}
