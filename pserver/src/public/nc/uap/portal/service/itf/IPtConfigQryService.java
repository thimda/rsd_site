package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtAnnoyVO;
import nc.uap.portal.vo.PtProtalConfigVO;

/**
 * Portal配置信息查询服务
 * 
 * @author licza
 * 
 */
public interface IPtConfigQryService {
	/**
	 * 获得一项Portal配置
	 * 
	 * @param key
	 * @return
	 * @throws PortalServiceException
	 */
	public PtProtalConfigVO getPortalConfig(String key)
			throws PortalServiceException;

	/**
	 * 获得所有的Portal配置信息
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public PtProtalConfigVO[] getAllPortalConfig()
			throws PortalServiceException;

	/**
	 * 支持匿名用户
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public Boolean isSupportAnnoyMousUser() throws PortalServiceException;

	/**
	 * 获得匿名集团用户
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public PtAnnoyVO getAnnoymousUser() throws PortalServiceException;

}
