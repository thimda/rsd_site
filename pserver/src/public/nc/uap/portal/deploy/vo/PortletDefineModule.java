package nc.uap.portal.deploy.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.portal.container.om.PortletDefinition;

 
public class PortletDefineModule implements Serializable {
	private static final long serialVersionUID = 1L;
	private String module;
	private Map<String, PortletDefinition> portletMap = new HashMap<String, PortletDefinition>();
	public PortletDefinition getPortlet(String id){
		return portletMap.get(id);
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
}
