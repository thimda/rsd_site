package nc.uap.portal.service.impl;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtConfigService;
import nc.uap.portal.vo.PtProtalConfigVO;
/**
 * Portal配置服务实现
 * @author licza
 *
 */
public class PtConfigServiceImpl implements IPtConfigService {

	@Override
	public String add(PtProtalConfigVO cfg) throws PortalServiceException {
		return add(new PtProtalConfigVO[]{cfg})[0];
	}

	@Override
	public String[] add(PtProtalConfigVO[] cfg) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return dao.insertVOs(cfg);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e.getMessage());
		}
	}

	@Override
	public void update(PtProtalConfigVO cfg) throws PortalServiceException {
		update(new PtProtalConfigVO[]{cfg});
	}

	@Override
	public void update(PtProtalConfigVO[] cfg) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			  dao.updateVOArray(cfg);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e.getMessage());
		}
	}

}
