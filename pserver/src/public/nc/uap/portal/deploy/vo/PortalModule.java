package nc.uap.portal.deploy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "portal")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalModule", propOrder = { "module" , "depends" , "description"})
public class PortalModule implements Serializable {
	private static final long serialVersionUID = 1042546781998764381L;
	
	@XmlElement(name = "module")
    protected String module;
	
	@XmlElement(name = "depends")
    protected String depends;
	
	@XmlElement(name = "description")
    protected String description;
	
	public String getDepends() {
		return depends;
	}
	public void setDepends(String depends) {
		this.depends = depends;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
