package nc.uap.portal.integrate;

import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SSOConfigVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1758817895767489228L;
	private String systemCode;
	private String systemName;
	// 是否启用南北网mapping功能,默认不启用,启用后才会进行IP映射替换
	private UFBoolean enableMapping;
	// 用于存放mappingIP地址
	private String authClass;
	private String gateUrl;
	// 从该类获取第三方系统的所有能被集成的功能节点
	private String nodesClass;

	public SSOConfigVO(SSOProviderVO provider) {
		this.authClass = provider.getAuthClass();
		this.enableMapping = UFBoolean.valueOf(provider.isEnableMapping());
		this.systemCode = provider.getSystemCode();
		this.systemName = provider.getSystemName();
		this.gateUrl = provider.getGateUrl();
		this.nodesClass = provider.getNodesClass();
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public UFBoolean getEnableMapping() {
		return enableMapping;
	}

	public void setEnableMapping(UFBoolean enableMapping) {
		this.enableMapping = enableMapping;
	}

	public String getAuthClass() {
		return authClass;
	}

	public void setAuthClass(String authClass) {
		this.authClass = authClass;
	}

	public String getGateUrl() {
		return gateUrl;
	}

	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}

	public String getNodesClass() {
		return nodesClass;
	}

	public void setNodesClass(String nodesClass) {
		this.nodesClass = nodesClass;
	}

}
