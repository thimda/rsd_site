package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;

/**
 * 插件管理
 * 
 * @author licza
 * @since 2010年9月10日10:53:18
 */
public interface IPtPluginService {
	/**
	 * 增加一个扩展点
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void add(PtExtensionPoint ex) throws PortalServiceException;

	/**
	 * 增加一组扩展点
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void add(PtExtensionPoint[] exs) throws PortalServiceException;

	/**
	 * 修改一个扩展点
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void update(PtExtensionPoint ex) throws PortalServiceException;

	/**
	 * 修改一组扩展点
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void update(PtExtensionPoint[] exs) throws PortalServiceException;

	/**
	 * 删除一个扩展点
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void delete(PtExtensionPoint ex) throws PortalServiceException;

	/**
	 * 删除一组扩展点
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void delete(PtExtensionPoint[] exs) throws PortalServiceException;

	/**
	 * 增加一个扩展
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void add(PtExtension ex) throws PortalServiceException;

	/**
	 * 增加一组扩展
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void add(PtExtension[] exs) throws PortalServiceException;

	/**
	 * 修改一个扩展
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void update(PtExtension ex) throws PortalServiceException;

	/**
	 * 修改一组扩展
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void update(PtExtension[] exs) throws PortalServiceException;

	/**
	 * 删除一个扩展
	 * 
	 * @param ex
	 * @throws PortalServiceException
	 */
	public void delete(PtExtension ex) throws PortalServiceException;

	/**
	 * 删除一组扩展
	 * 
	 * @param exs
	 * @throws PortalServiceException
	 */
	public void delete(PtExtension[] exs) throws PortalServiceException;
	/**
	 * 清理一个模块下的插件
	 * @param moduleName
	 * @throws PortalServiceException
	 */
	public void clearModule(String moduleName)  throws PortalServiceException;
}
