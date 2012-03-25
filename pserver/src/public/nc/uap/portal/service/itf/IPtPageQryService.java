package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPageVO;

/**
 * 页面查询服务
 * 
 * @author licza
 * 
 */
public interface IPtPageQryService {
	/**
	 * 根据主键获得页面
	 * 
	 * @param pk
	 * @return
	 * @throws PortalServiceException
	 */
	PtPageVO getPageByPk(String pk) throws PortalServiceException;

	/**
	 * 根据用户主键查询页面
	 * 
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	PtPageVO[] getPageByUser(String pk_user) throws PortalServiceException;

	/**
	 * 根据模块查询系统级页面
	 * 
	 * @param module
	 * @return
	 * @throws PortalServiceException
	 */
	PtPageVO[] getSysPagesByModule(String module) throws PortalServiceException;

	/**
	 * 根据页面级别查询页面
	 * 
	 * @param level
	 * @return
	 * @throws PortalServiceException
	 */
	PtPageVO[] getPageByLevel(Integer level) throws PortalServiceException;

	/**
	 * 根据集团查询页面
	 * 
	 * @param pk_group
	 * @return
	 * @throws PortalServiceException
	 */
	PtPageVO[] getPageByGroup(String pk_group) throws PortalServiceException;
}
