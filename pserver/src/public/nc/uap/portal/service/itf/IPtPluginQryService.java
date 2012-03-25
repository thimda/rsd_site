package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;

/**
 * 插件查询
 * 
 * @author licza
 * 
 */
public interface IPtPluginQryService {
	/**
	 * 获得所有扩展
	 * 
	 * @param module 模块名
	 * @throws PortalServiceException
	 * @return 扩展列表
	 */
	public PtExtension[] getAllExtension(String module) throws PortalServiceException;

	/**
	 * 获得所有扩展
	 * 
	 * @throws PortalServiceException
	 * @return 扩展列表
	 */
	public PtExtension[] getAllExtension() throws PortalServiceException;

	/**
	 * 获得扩展点下的扩展
	 * 
	 * @param point 扩展点ID
	 * @return 扩展列表
	 * @throws PortalServiceException
	 */
	public PtExtension[] getExtensionByPoint(String point) throws PortalServiceException;

	/**
	 * 获得所有扩展点
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public PtExtensionPoint[] getAllExtensionPoint() throws PortalServiceException;
	/**
	 * 根据扩展主键获得扩展
	 * @param pk_extension
	 * @return
	 * @throws PortalServiceException
	 */
	public PtExtension getExtension(String pk_extension) throws PortalServiceException;
	
}
