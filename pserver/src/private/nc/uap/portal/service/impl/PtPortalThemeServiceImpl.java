package nc.uap.portal.service.impl;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalThemeService;
import nc.uap.portal.vo.PtThemeVO;

/**
 * 主题增加、修改
 * 
 * @author licza
 * 
 */
public class PtPortalThemeServiceImpl implements IPtPortalThemeService {

	/**
	 * 新增一个主题
	 * 
	 * @param vo
	 * @return
	 * @throws PortalServerRuntimeException
	 */
	public String add(PtThemeVO vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return dao.insertVOWithPK(vo);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 新增一组主题
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void add(PtThemeVO[] vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVOs(vo);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 更新一组主题
	 * 
	 * @param vo
	 * @throws PortalServerRuntimeException
	 */
	public void update(PtThemeVO vo) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "update pt_theme set  title=?,	 lfwthemeid=?, i18nname=?  where themeid=?";
		SQLParameter parameter = new SQLParameter();
		parameter.addParam(vo.getTitle());
		parameter.addParam(vo.getLfwthemeid());
		parameter.addParam(vo.getI18nname());
		parameter.addParam(vo.getThemeid());
		try {
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e.getCause());
			throw new PortalServiceException(e);
		}
	}

}
