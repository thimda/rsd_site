package nc.uap.portal.service.impl;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalSkinService;
import nc.uap.portal.vo.PtSkinVo;
/**
 * 样式操作实现类
 * 
 * @author dingrf
 * 
 */

public class PtPortalSkinServiceImpl implements IPtPortalSkinService {

	@Override
	public String add(PtSkinVo vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return dao.insertVOWithPK(vo);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void add(PtSkinVo[] vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVOs(vo);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void update(PtSkinVo vo) throws PortalServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String module,String themeid,String type) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "delete from pt_skin where modulename=? and themeid=? and type=? ";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(module);
			parameter.addParam(themeid);
			parameter.addParam(type);
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

}
