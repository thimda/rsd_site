package nc.uap.portal.deploy.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PortletDefinePool implements Serializable {
	private static final long serialVersionUID = -1405378801595352443L;
	private Map<String, PortletDefineModule> portletModuleMap = new HashMap<String, PortletDefineModule>();
	public void addPortletDefineModule(PortletDefineModule module){
		portletModuleMap.put(module.getModule(), module);
	}
	public PortletDefineModule getPortletDefineModule(String module){
		return portletModuleMap.get(module);
	}
}
