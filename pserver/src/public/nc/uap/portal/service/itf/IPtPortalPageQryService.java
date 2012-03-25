package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPageVO;

/**
 * PortalPage查询类
 * 
 * @author licza
 * @since 2010-6-21
 */
public interface IPtPortalPageQryService {
	/**
	 * 检查一个页面是否存在
	 * @param module 
	 * @param pagename
	 * @return
	 * @throws PortalServiceException
	 */
	public Boolean isPortalPageExist(String module,String pagename) ;
	/**
	 * 根据条件 查询Portal页面
	 * @param clause
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPageVO[] queryPortalPageVOs(String clause) throws PortalServiceException;
	
	/**
	 * 根据主键获得页面布局VO
	 * 
	 * @param Page主键
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPageVO getPortalPageVOByPK(String pk) throws PortalServiceException;

	/**
	 * 获得数据库中的页面布局VO
	 * 
	 * @return PageVOArray
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getPageVOList() throws PortalServiceException;

	/**
	 * 获得用户的页面布局VO
	 * 
	 * @param pk_user
	 * @return 页面布局VO
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getUserPages(String pk_user) throws PortalServiceException;

	/**
	 * 获得模块的系统级页面布局VO
	 * 
	 * @param module 模块名
	 * @return 页面布局VO
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getSystemPages(String module) throws PortalServiceException;

	/**
	 * 获得模块的集团PortalPage
	 * 
	 * @param module 模块名
	 * @param parentid 页面布局VO原型
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getGroupPages(String module, String parentid) throws PortalServiceException;

	/**
	 * 获得模块的集团页面布局VO
	 * 
	 * @return 所有集团的页面布局VO
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getGroupPages() throws PortalServiceException;

	/**
	 * 获得模块的系统页面布局VO
	 * 
	 * @return 系统级页面布局VO
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getSystemPages() throws PortalServiceException;

	/**
	 * 获得用户所有的页面布局VO
	 * 
	 * @return 页面布局VO
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getUserPages() throws PortalServiceException;

	/**
	 * 获得管理类型的Portal页面
	 * 
	 * @param sbean 用户登陆信息
	 * @throws PortalServiceException
	 */
	public PtPageVO[] getAdminPages(Integer level) throws PortalServiceException;
	
	/**
	 * 从数据库中查找集团下的页面布局VO
	 * @param pk_group
	 * @return
	 */
	public PtPageVO[] getGroupsPage(String pk_group);
}
