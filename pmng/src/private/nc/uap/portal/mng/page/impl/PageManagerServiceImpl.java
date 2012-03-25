package nc.uap.portal.mng.page.impl;

import nc.bs.dao.DAOException;
import nc.portal.portlet.vo.PtPageDeptVO;
import nc.portal.portlet.vo.PtPageRoleVO;
import nc.portal.portlet.vo.PtPageUserVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.mng.page.itf.IPageManagerService;
import nc.uap.portal.persist.dao.PtBaseDAO;

public class PageManagerServiceImpl implements IPageManagerService {

	@Override
	public void createPtPageDeptVO(PtPageDeptVO vo) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("创建页面-部门关系表出错", e);
			throw new LfwRuntimeException("创建页面-部门关系表出错", e);
		}
	}

	@Override
	public void createPtPageRoleVO(PtPageRoleVO vo) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("创建页面-部门关系表出错", e);
			throw new LfwRuntimeException("创建页面-部门关系表出错", e);
		}

	}

	@Override
	public void createPtPageUserVO(PtPageUserVO vo) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("创建页面-部门关系表出错", e);
			throw new LfwRuntimeException("创建页面-部门关系表出错", e);
		}
	}
}
