package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtProtalConfigVO;

/**
 * Portal配置服务
 * @author licza
 *
 */
public interface IPtConfigService {
	/**
	 * 插入一条Portal配置信息
	 * @param cfg
	 * @return
	 * @throws PortalServiceException
	 */
	public String add(PtProtalConfigVO cfg) throws PortalServiceException;
	/**
	 * 插入一组Portal配置信息
	 * @param cfg
	 * @return
	 * @throws PortalServiceException
	 */
	public String[] add(PtProtalConfigVO[] cfg) throws PortalServiceException;
	/**
	 * 更新一条Portal配置信息
	 * @param cfg
	 * @throws PortalServiceException
	 */
	public void update(PtProtalConfigVO cfg) throws PortalServiceException;
	/**
	 * 更新一组Portal配置信息
	 * @param cfg
	 * @throws PortalServiceException
	 */
	public void update(PtProtalConfigVO[] cfg) throws PortalServiceException;
}
