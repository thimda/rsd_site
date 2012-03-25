/**
 * 
 */
package nc.uap.portal.service.impl;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtThirdPartyLoginService;
import nc.uap.portal.vo.PtTrdauthVO;

/**
 * @author chouhl
 *
 */
public class PtThirdPartyLoginServiceImpl implements IPtThirdPartyLoginService {

	@Override
	public void addAuth(PtTrdauthVO auth) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(auth);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtTrdauthVO getAuth(String akey) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		PtTrdauthVO[] vos = null;
		try {
			vos = (PtTrdauthVO[])dao.queryByCondition(PtTrdauthVO.class, "akey='"+akey+"'");
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
		if(vos != null && vos.length > 0){
			return vos[0];
		}else{
			return null;
		}
	}

}
