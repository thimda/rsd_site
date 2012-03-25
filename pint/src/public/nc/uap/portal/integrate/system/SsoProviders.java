package nc.uap.portal.integrate.system;

import java.io.Serializable;
import java.util.List;

/**
 * <sso-providers>µƒ≈‰÷√¿‡
 * @author gd 2008-12-31
 *
 */
public class SsoProviders implements Serializable {
	private static final long serialVersionUID = -7068814343512976514L;
	private List<SSOProviderVO> providers;
	
	public List<SSOProviderVO> getProviders() {
		return providers;
	}
	public void setProviders(List<SSOProviderVO> providers) {
		this.providers = providers;
	}
	
}
