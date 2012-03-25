package nc.uap.portal.integrate.sso.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.CredentialWrapper;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.vo.PtSlotVO;

 
/**
 * SSO凭证的查询操作接口
 * 
 * @author lkp
 *
 */
public interface ISSOQueryService {
	
	/**
	 * 获得凭证对象
	 * 
	 * 查找规则：
	 * 如果sharelevel==0，则通过portletId + userId + sharelevel 进行查找
	 * 如果sharelevel==1,则通过classname + userId + sharelevel 进行查找
	 * 如果sharelevel==2,则通过classname + sharelevel 进行查找
	 * 
	 * @param userId
	 * @param portletId
	 * @param className
	 * @param sharelevel
	 * @return
	 */
	public PtCredentialVO getCredentials(String userId, String portletId,
			String className, Integer sharelevel) throws PortalServiceException ;
	
	/**
	 * 根据portletid和className获取所有的CredentialWrapper对象
	 * 即当前系统拥有的所有slotvo/credentialVO的信息。
	 * 
	 * @param portletId
	 * @param className
	 * @param sharelevel
	 * @return
	 * @throws PortalServiceException
	 */
	public CredentialWrapper[] getPortletCredentials(String portletId,
			String className,Integer sharelevel) throws PortalServiceException;
	
	/**
	 * 获得凭证槽对象
	 * 
	 * 查找规则：
	 * 如果sharelevel==0，则通过portletId + userId + sharelevel 进行查找
	 * 如果sharelevel==1,则通过classname + userId + sharelevel 进行查找
	 * 如果sharelevel==2,则通过classname + sharelevel 进行查找 
	 * 
	 * @param userId
	 * @param portletId
	 * @param className
	 * @param sharelevel
	 * @return
	 */
	public PtSlotVO[] getSlots(String userId, String portletId,
			               String className,Integer sharelevel) throws PortalServiceException;
	/**
	 * 根据主键获得凭证槽对象
	 * @param pk_slot
	 * @return
	 * @throws PortalServiceException
	 */
	public PtSlotVO getSlotByPK(String pk_slot)throws PortalServiceException;
}
