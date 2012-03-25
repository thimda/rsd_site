package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPageVO;

public interface IPtPortalPageService {
	/**
	 * 增加页面布局VO
	 * 
	 * @param portalPageVO 页面布局VO
	 */
	public void add(PtPageVO portalPageVO) throws PortalServiceException;

	/**
	 * 删除页面布局VO
	 * 
	 * @param pk_portalpage 页面布局VO编码
	 */
	public void delete(String pk_portalpage) throws PortalServiceException;

	/**
	 * 删除页面布局VO
	 * 
	 * @param module 页面module名称
	 * @param pagename 页面布局名称 
	 */
	public void delete(String module,String pagename) throws PortalServiceException;
	
	/**
	 * 更新页面布局VO
	 * 
	 * @param portalPageVO 页面布局VO
	 */
	public void update(PtPageVO portalPageVO) throws PortalServiceException;

	/**
	 * 新增一组页面布局VO
	 * 
	 * @param pagesVos 页面布局VO
	 * @return 新增页面布局VO的主键
	 * @throws PortalServiceException
	 */
	public String[] addPortalPage(PtPageVO[] pagesVos) throws PortalServiceException;

	/**
	 * 修改一组页面布局VO
	 * 
	 * @param pagesVos 页面布局VO
	 * @throws PortalServiceException
	 */
	public void updatePortalPage(PtPageVO[] pagesVos) throws PortalServiceException;

	/**
	 * 新增用户页面布局VO
	 * 
	 * @param pk_user 用户编码
	 * @param pk_group 集团编码
	 * @param newPages 页面布局VO
	 * @throws PortalServiceException
	 */
	public void addUserNewPages(String pk_user, String pk_group, PtPageVO[] newPages) throws PortalServiceException;

	/**
	 * 更新用户页面布局
	 * 
	 * @param portalPageVO 页面布局VO
	 * @throws PortalServiceException
	 */
	public void updateLayout(PtPageVO portalPageVO) throws PortalServiceException;
	/**
	 * 同步用户的Portal页面
	 * @param pk_group
	 * @throws PortalServiceException
	 */
	public void sync(String pk_group) throws PortalServiceException;
	
	/**
	 * 删除集团页面及从属于此集团的用户的页面
	 * @param pk_portalpage
	 * @throws PortalServiceException
	 */
	public void delGroupPage(String pk_portalpage)throws PortalServiceException;
	
	/**
	 * 应用集团下的页面到所有用户
	 * @param pk_portalpage
	 * @throws PortalServiceException
	 */
	public void applyPageLayout(String pk_portalpage)throws PortalServiceException;
}
