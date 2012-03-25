package nc.uap.portal.integrate.itf;

import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.vo.PtSsopropVO;



/**
 * sso单点配置管理服务类
 * @author gd 2008-12-11
 * @version NC5.5
 * @since NC5.5
 *
 */
public interface IPtSsoConfigService {
	 
	
	/**
	 * 增加新的SSOProviderVO
	 * @param provider
	 */
	public void add(SSOProviderVO provider);
	/**
	 * 更新SSOProviderVO
	 * @param provider
	 */
	public void update(SSOProviderVO provider);
	/**
	 * 删除provider
	 * @param systemCode
	 */
	public void delete(String systemCode);
 
	/**
	 * 增加新的PtSsopropVO
	 * @param ssoProp
	 */
	public void add(PtSsopropVO ssoProp);
	/**
	 * 更新PtSsopropVO
	 * @param ssoProp
	 */
	public void update(PtSsopropVO ssoProp);
	/**
	 * 删除
	 * @param pk_ssoprop
	 */
	public void deleteByPK(String pk_ssoprop);
	
}
