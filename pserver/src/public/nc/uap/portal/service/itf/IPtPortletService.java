package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;

/**
 * Portlet增加&修改
 * @author licza
 *
 */
public interface IPtPortletService {
	/**
	 * 新增一组PortletVO
	 * @param pages 
	 * @return 新增Pages的主键
	 * @throws PortalServiceException
	 */
	String[] addPortlets(PtPortletVO[] portlets) throws PortalServiceException;

	/**
	 * 修改一组PortletVO
	 * @param pages
	 * @throws PortalServiceException
	 */
	void updatePortlets(PtPortletVO[] portlets) throws PortalServiceException;
	
	/**
	 * 同步用户的Portlet
	 * @param pk_group
	 * @throws PortalServiceException
	 */
	void sync(String pk_group) throws PortalServiceException;
	/**
	 * 增加一个Portlet配置
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	String addPreference(PtPreferenceVO vo)throws PortalServiceException;
	/**
	 * 修改一个Portlet配置
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	void updatePreference(PtPreferenceVO vo)throws PortalServiceException;
	/**
	 * 删除一个Portlet
	 * @param pk
	 * @throws PortalServiceException
	 */
	void delete(String pk) throws PortalServiceException;
	/**
	 * 删除一组Portlet
	 * @param pk
	 * @throws PortalServiceException
	 */
	void delete(String[] pk) throws PortalServiceException;

}
