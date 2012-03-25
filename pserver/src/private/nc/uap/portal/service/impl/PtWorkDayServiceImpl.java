package nc.uap.portal.service.impl;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtWorkDayService;
import nc.uap.portal.vo.PtWeekendVO;

/**
 * 工作日设置服务实现
 * @author licza
 *
 */
public class PtWorkDayServiceImpl implements IPtWorkDayService {

	public PtWeekendVO add(PtWeekendVO vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(vo);
			return vo;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new PortalServiceException(e.getMessage(),e);
		}
	}

	public void update(PtWeekendVO vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVO(vo);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new PortalServiceException(e.getMessage(),e);
		}
	}

}
