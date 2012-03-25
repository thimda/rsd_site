package nc.uap.portal.integrate.itf;

import java.util.List;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.system.SSOProviderVO;

/**
 * Portal单点登陆配置查询服务接口
 * @author licza
 *
 */
public interface IPtSsoConfigQryService {
	/**
	 * 从数据库中查询所有的配置项目
	 * @return
	 * @throws PortalServiceException
	 */
	public List<SSOProviderVO> getAllConfig() throws PortalServiceException;
}
