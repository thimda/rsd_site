package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPageVO;

/**
 * 页面服务
 * 
 * @author licza
 * 
 */
public interface IPtPageService {
	/**
	 * 增加一个页面
	 * 
	 * @param vo
	 * @throws PortalServiceException
	 */
	String add(PtPageVO vo) throws PortalServiceException;

	/**
	 * 增加一组页面
	 * 
	 * @param vo
	 * @throws PortalServiceException
	 */
	String[] add(PtPageVO[] vo) throws PortalServiceException;

	/**
	 * 根据父页面创建一份拷贝
	 * 
	 * @param pk_org
	 * @param parentid
	 * @throws PortalServiceException
	 */
	String doCopy(String pk_org, String parentid) throws PortalServiceException;

	/**
	 * 更新一个页面
	 * 
	 * @param vo
	 * @throws PortalServiceException
	 */
	void update(PtPageVO vo) throws PortalServiceException;

	/**
	 * 更新一组页面
	 * 
	 * @param vo
	 * @throws PortalServiceException
	 */
	void update(PtPageVO[] vo) throws PortalServiceException;

	/**
	 * 删除一个页面
	 * @param pk
	 * @throws PortalServiceException
	 */
	void delete(String pk) throws PortalServiceException;
	/**
	 * 删除一组页面
	 * @param pk
	 * @throws PortalServiceException
	 */
	void delete(String[] pk) throws PortalServiceException;
	
	/**
	 * 更新用户页面布局
	 * 
	 * @param portalPageVO 页面布局VO
	 * @throws PortalServiceException
	 */
	void updateLayout(PtPageVO portalPageVO) throws PortalServiceException;
	
	/**
	 * 同步用户的Portal页面
	 * @param pk_group
	 * @throws PortalServiceException
	 */
	void sync(String pk_group) throws PortalServiceException;
	
	/**
	 * 应用集团下的页面到所有用户
	 * @param pk_portalpage
	 * @throws PortalServiceException
	 */
	void applyPageLayout(String pk_portalpage)throws PortalServiceException;
}
