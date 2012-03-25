package nc.uap.portal.service.impl;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalModuleService;
import nc.uap.portal.vo.PtModuleVO;

public class PtPortalModuleServiceImpl implements IPtPortalModuleService {

	@Override
	public String add(PtModuleVO vo) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public void add(PtModuleVO[] vos) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVOs(vos);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

	@Override
	public void update(PtModuleVO[] vos) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVOArray(vos);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

	@Override
	public void updateAll(PtModuleVO vo) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "UPDATE pt_module SET  activeflag = ?,title = ?,dependentid = ?  where moduleid = ?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(vo.getActiveflag());
			parameter.addParam(vo.getTitle());
			parameter.addParam(vo.getDependentid());
			parameter.addParam(vo.getModuleid());
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

}
