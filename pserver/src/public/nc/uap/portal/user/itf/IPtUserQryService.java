package nc.uap.portal.user.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.user.entity.IUserVO;

/**
 * Portal用户查询服务接口
 * @author licza
 *
 */
public interface IPtUserQryService {
	/**
	 * 根据用户主键获得用户VO
	 * @param pkUser
	 * @return
	 * @throws PortalServiceException
	 */
	IUserVO getUserById(String pkUser) throws PortalServiceException;
}
