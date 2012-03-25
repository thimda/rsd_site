package nc.uap.portal.service.impl;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtDeviceQryService;
import nc.uap.portal.vo.PtDeviceVO;
public class PtDeviceQryService implements IPtDeviceQryService {
	@Override
	public PtDeviceVO[] getAllDevice() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		PtDeviceVO[] vos = null;
		try {
			vos = (PtDeviceVO[]) dao.queryByCondition(PtDeviceVO.class, "");
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e.getMessage());
		}
		return vos;
	}
	@Override
	public PtDeviceVO getDeviceVoByPk(String devicePk) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		PtDeviceVO[] vos = null;
		try {
			vos = (PtDeviceVO[]) dao.queryByCondition(PtDeviceVO.class, "pk_device='" + devicePk + "'");
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PortalServiceException(e.getMessage());
		}
		if (vos != null && vos.length == 1) {
			return vos[0];
		} else {
			return null;
		}
	}
}
