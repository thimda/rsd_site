package nc.uap.portal.mng.page.impl;

import nc.bs.dao.DAOException;
import nc.portal.portlet.vo.PtPageDeptVO;
import nc.portal.portlet.vo.PtPageRoleVO;
import nc.portal.portlet.vo.PtPageUserVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.mng.page.itf.IPageManagerQryService;
import nc.uap.portal.persist.dao.PtBaseDAO;

public class PageManagerQryServiceImpl implements IPageManagerQryService {

	@Override
	public PtPageDeptVO[] getPageDeptVOByCondition(String sqlWhere) {
		PtBaseDAO dao = new PtBaseDAO();
		PtPageDeptVO[] vos = null;
		try {
			vos = (PtPageDeptVO[]) dao.queryByCondition(PtPageDeptVO.class, sqlWhere);
		} catch (DAOException e) {
			LfwLogger.error("查询布局-部门表时出错", e);
			throw new LfwRuntimeException("查询布局-部门表时出错", e);
		}
		return vos;
	}

	@Override
	public PtPageRoleVO[] getPageRoleVOByCondition(String sqlWhere) {
		PtBaseDAO dao = new PtBaseDAO();
		PtPageRoleVO[] vos = null;
		try {
			vos = (PtPageRoleVO[]) dao.queryByCondition(PtPageRoleVO.class, sqlWhere);
		} catch (DAOException e) {
			LfwLogger.error("查询布局-角色表时出错", e);
			throw new LfwRuntimeException("查询布局-角色表时出错", e);
		}
		return vos;
	}

	@Override
	public PtPageUserVO[] getPageUserVOByCondition(String sqlWhere) {
		PtBaseDAO dao = new PtBaseDAO();
		PtPageUserVO[] vos = null;
		try {
			vos = (PtPageUserVO[]) dao.queryByCondition(PtPageUserVO.class, sqlWhere);
		} catch (DAOException e) {
			LfwLogger.error("查询布局-用户表时出错", e);
			throw new LfwRuntimeException("查询布局-用户表时出错", e);
		}
		return vos;
	}

}
