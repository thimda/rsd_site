package nc.portal.service.itf;

import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.uap.portal.exception.PortalServiceException;
import nc.vo.pub.BusinessException;

/**
 * 在线用户查询
 * 2010-12-9 下午08:07:02  limingf
 */

public interface ILfwTokenQryService {
	
	/**
	 * 查找某集团下的在线用户
	 * @param pk_group
	 * @return
	 * @throws PortalServiceException
	 */
	public LfwTokenVO[] getOnlineUserByGroupPk(String pk_group) throws PortalServiceException;
	
	/**
	 * 查找部分集团下在线用户
	 * @param pk_groups
	 * @return
	 * @throws PortalServiceException
	 */
	public LfwTokenVO[] getOnlineUserByGroupPk(String[] pk_groups) throws PortalServiceException;
	
	/**
	 * 根据用户pk查找该用户在线信息
	 * @param pk_user
	 * @return
	 * @throws PortalServiceException
	 */
	//public LfwTokenVO[] getOnlineUserByUser(String pk_user) throws PortalServiceException;
	
	/**
	 * 获取某公司在线用户个数
	 * @return
	 * @throws BusinessException
	 */
	public int getOnlineUserCount(boolean isDistinct, String pk_corp) throws BusinessException;
	
//	/**
//	 * 对在线信息进行分页显示
//	 * @param pageSize
//	 * @param pageNumber
//	 * @return
//	 * @throws BusinessException
//	 */
//	public LfwTokenVO[] getOnlineUser(int pageSize, int pageNumber) throws BusinessException;
//	
//	/**
//	 * 根据公司获取在线用户
//	 * @param pageSize
//	 * @param pageNumber
//	 * @param pk_group
//	 * @return
//	 * @throws BusinessException
//	 */
//	public LfwTokenVO[] getOnlineUserByGroup(int pageSize, int pageNumber, String pk_group) throws BusinessException;

}
