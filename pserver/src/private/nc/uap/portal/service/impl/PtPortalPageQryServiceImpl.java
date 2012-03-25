package nc.uap.portal.service.impl;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalPageQryService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtPageVO;

import org.apache.commons.collections.CollectionUtils;
/**
 * PortalPage查询实现类
 * 
 * @author licza
 * 
 */
public class PtPortalPageQryServiceImpl implements IPtPortalPageQryService {
	@Override
	public PtPageVO getPortalPageVOByPK(String pk_portalpage) throws PortalServiceException {
		try {
			BaseDAO dao = new BaseDAO();
			return (PtPageVO) dao.retrieveByPK(PtPageVO.class, pk_portalpage);
		} catch (Exception e) {
			throw new PortalServiceException(e.getMessage(), e.getCause());
		}
	}
	@Override
	public PtPageVO[] getPageVOList() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtPageVO[]) dao.queryByCondition(PtPageVO.class, "fk_pageuser ='*'");
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getUserPages(String pk_user) throws PortalServiceException {
		String sql = "select * from pt_portalpage where pt_portalpage.fk_pageuser= ?  ";
		try {
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(pk_user);
			List list = (List) dao.executeQuery(sql, param, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getSystemPages(String module) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portalpage where fk_pageuser ='*' and ISNULL(pk_group, '~') = '~' and module=?  ";
		try {
			parameter.addParam(module);
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getGroupPages(String module, String parentid) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portalpage where fk_pageuser ='*' and module=?   and parentid=? ";
		try {
			parameter.addParam(module);
			parameter.addParam(parentid);
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getUserPages() throws PortalServiceException {
		String sql = "select * from pt_portalpage where ISNULL(pt_portalpage.fk_pageuser,'~') != '~' and fk_pageuser !='*' ";
		try {
			BaseDAO dao = new BaseDAO();
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getSystemPages() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select * from pt_portalpage where fk_pageuser ='*' and ISNULL(pk_group, '~') = '~'";
		try {
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e);
			throw new PortalServiceException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getGroupPages() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portalpage where fk_pageuser ='*'    and ISNULL(parentid,'~') != '~' order by pk_group ";
		try {
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	/**
	 * 获得非用户级页面描述VO
	 * 
	 * @param level 用户级别
	 * @return 页面描述VO
	 * @throws PortalServiceException
	 */
	@SuppressWarnings("unchecked")
	public PtPageVO[] getAdminPages(Integer level) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portalpage where fk_pageuser ='*' and ISNULL(parentid, '~') = '~'  and levela=" + level + " order by pk_group ";
		try {
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPageVO.class));
			if (CollectionUtils.isNotEmpty(list)) {
				PtPageVO[] portalPageList = (PtPageVO[]) list.toArray((PtPageVO[]) Array.newInstance(PtPageVO.class, 0));
				return portalPageList;
			} else {
				return new PtPageVO[0];
			}
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getGroupsPage(String pk_group) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Collection list = dao.retrieveByClause(PtPageVO.class, " fk_pageuser ='*' and pk_group ='" + pk_group + "' ");
			if (!PtUtil.isNull(list))
				return (PtPageVO[]) list.toArray(new PtPageVO[0]);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return new PtPageVO[0];
	}
	@Override
	public PtPageVO[] queryPortalPageVOs(String clause) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Collection list = dao.retrieveByClause(PtPageVO.class, clause);
			if (!PtUtil.isNull(list))
				return (PtPageVO[]) list.toArray(new PtPageVO[0]);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return new PtPageVO[0];
	}
	@Override
	public Boolean isPortalPageExist(String module, String pagename){
		try {
			 PtPageVO[] vos = queryPortalPageVOs(" module='"+module+"' and pagename ='"+pagename+"'");
			 return vos != null && vos.length >0;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return true;
	}
}
