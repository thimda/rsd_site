package nc.uap.portal.service.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.vo.PtTrdauthVO;

public interface IPtThirdPartyLoginService {
	/**
	 * 增加一个令牌
	 * @param auth
	 */
	void addAuth(PtTrdauthVO auth) throws PortalServiceException;
	/**
	 * 获得登录令牌
	 * @param id
	 * @return
	 */
	PtTrdauthVO getAuth(String akey) throws PortalServiceException;
}
