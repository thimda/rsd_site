/**
 * 
 */
package nc.uap.portal.integrate.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * 采用xstream解析SSO文件时相应的类
 * 
 * @author gd 2009-01-15
 * @version NC5.6
 * @since NC5.5
 */
public class SSOProviderVO implements Serializable, Cloneable {
	static final long serialVersionUID = -3101778686811060362L;
	private String systemCode;
	private String systemName;
	// 是否启用南北网mapping功能,默认不启用,启用后才会进行IP映射替换
	private boolean enableMapping = false;
	// 用于存放mappingIP地址
	private List<IpReference> mappingReference;
	private List<Reference> providerReference;
	private String authClass;
	private String gateUrl;
	// 从该类获取第三方系统的所有能被集成的功能节点
	private String nodesClass;

	/**
	 * @return Returns the authClass.
	 */
	public String getAuthClass() {
		return authClass;
	}



	/**
	 * @param authClass The authClass to set.
	 */
	public void setAuthClass(String authClass) {
		this.authClass = authClass;
	}

	/**
	 * @return Returns the gateUrl.
	 */
	public String getGateUrl() {
		return gateUrl;
	}

	/**
	 * @param gateUrl The gateUrl to set.
	 */
	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}

	/**
	 * @return Returns the systemCode.
	 */
	public String getSystemCode() {
		return systemCode;
	}

	/**
	 * @param systemCode The systemCode to set.
	 */
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public boolean isEnableMapping() {
		return enableMapping;
	}

	public void setEnableMapping(boolean enableMapping) {
		this.enableMapping = enableMapping;
	}

	/**
	 * @return Returns the systemName.
	 */
	public String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName The systemName to set.
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public List<IpReference> getMappingReference() {
		return mappingReference;
	}

	public void setMappingReference(List<IpReference> mappingReference) {
		this.mappingReference = mappingReference;
	}

	public List<Reference> getProviderReference() {
		return providerReference;
	}

	public void setProviderReference(List<Reference> providerReference) {
		this.providerReference = providerReference;
	}

	public String getValue(String key) {
		if (providerReference != null && providerReference.size() > 0) {
			for (int i = 0; i < providerReference.size(); i++) {
				if (providerReference.get(i).getName().equals(key))
					return providerReference.get(i).getValue();
			}
		}
		return null;
	}

	public Object clone() {
		SSOProviderVO provider = null;
		try {
			provider = (SSOProviderVO) super.clone();

			if (this.providerReference != null && this.providerReference.size() > 0) {
				List<Reference> refs = new ArrayList<Reference>();
				for (Reference ref : this.providerReference)
					refs.add((Reference) ref.clone());
				provider.setProviderReference(refs);
			}
			if (this.mappingReference != null && this.mappingReference.size() > 0) {
				List<IpReference> refs = new ArrayList<IpReference>();
				for (IpReference ref : this.mappingReference)
					refs.add((IpReference) ref.clone());
				provider.setMappingReference(refs);
			}
		} catch (CloneNotSupportedException e) {
			LfwLogger.error(e);
		}
		return provider;
	}

	public String getNodesClass() {
		return nodesClass;
	}

	public void setNodesClass(String nodesClass) {
		this.nodesClass = nodesClass;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SSOProviderVO))
			return false;
		SSOProviderVO o = (SSOProviderVO) obj;
		return StringUtils.equals(o.getSystemCode(), getSystemCode());
	}

}
