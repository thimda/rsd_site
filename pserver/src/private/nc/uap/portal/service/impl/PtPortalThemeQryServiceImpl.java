package nc.uap.portal.service.impl;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalThemeQryService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtThemeVO;

/**
 * 主题查询实现
 * @author licza
 *
 */
public class PtPortalThemeQryServiceImpl implements IPtPortalThemeQryService {

	@SuppressWarnings("unchecked")
	@Override
	public PtThemeVO find(String themeId) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtThemeVO> list = (List<PtThemeVO>)dao.retrieveByClause(PtThemeVO.class, " themeid = '"+themeId+"' and ISNULL(parentid,'~') = '~' ");
			if(!PtUtil.isNull(list))
				return list.toArray(new PtThemeVO[0])[0];
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new  PortalServiceException("查询集团主题失败!");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PtThemeVO[] getThemeByGroup() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtThemeVO> list = (List<PtThemeVO>)dao.retrieveByClause(PtThemeVO.class, " isnull(pk_group,'~') = '~'");
			if(!PtUtil.isNull(list))
				return list.toArray(new PtThemeVO[0]);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new  PortalServiceException("查询集团主题失败!");
		}
		return null;
	}
}
