/**
 * 
 */
package nc.uap.portal.integrate.sso.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.vo.PtSlotVO;
import nc.vo.pub.BusinessException;


/**
 * SSO 凭证管理接口： 获得凭证槽、凭证操作的Dao实例; 获得某个用户登录portal后对某个portlet应用的访问凭证（按优先级别排列）; 存储凭证
 * @author yzy Created on 2006-02-22
 * @author lkp 
 */
public interface ISSOService {


	/**
	 * 创建、更新凭证（创建、更新凭证槽；创建、更新凭证库）
	 * @param credentialVO 凭证VO
	 * @param slotVO 凭证槽VO
	 * @throws PortalServiceException
	 */
	public void createCredentials(PtCredentialVO credentialVO, PtSlotVO slotVO) throws BusinessException;
	
	/**
	 * 该方法是一个比较专有的方法，只用于用户管理取消凭证所用。
	 * 该方法删除的是应用共享级别的凭证。
	 * 
	 * @param userId
	 * @param className
	 * @throws BusinessException
	 */
	public void removeApplicationSharedCredentials(String userId, String portletId, String className, Integer shareLevel) throws BusinessException;
	/**
	 * 增加一个凭证槽
	 * @param vo
	 * @throws PortalServiceException
	 */
	public void addSlot(PtSlotVO vo) throws PortalServiceException;
	/**
	 * 删除一个凭证槽信息
	 * @param pk_slot
	 * @throws PortalServiceException
	 */
	public void removeSlot(String pk_slot) throws PortalServiceException;
}
