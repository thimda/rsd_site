package nc.uap.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.vo.PtPageVO;

/**
 * PortalPage操作实现类
 * 
 * @author licza
 * 
 */
public class PtPortalPageServiceImpl implements IPtPortalPageService {

	@Override
	public String[] addPortalPage(PtPageVO[] pages) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String[] pks = dao.insertVOs(pages);
			return pks;
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void updatePortalPage(PtPageVO[] pages) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVOArray(pages);
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void addUserNewPages(String userId, String groupId, PtPageVO[] newPages) throws PortalServiceException {
		List<PtPageVO> pageVOs = new ArrayList<PtPageVO>();
		for (PtPageVO pageVO : newPages) {
			pageVO.setPk_group(groupId);
			pageVO.setFk_pageuser(userId);
			pageVO.setParentid(pageVO.getPk_portalpage());
			pageVO.setPk_portalpage("");
			pageVOs.add(pageVO);
		}
		PtPageVO[] pageVOArray = pageVOs.toArray(new PtPageVO[0]);
		addPortalPage(pageVOArray);
	}

	@Override
	public void add(PtPageVO portalPageVO) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.insertVOWithPK(portalPageVO);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void delete(String page_pk) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.deleteByPK(PtPageVO.class, page_pk);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}

	}
	
	@Override
	public void delete(String module,String pagename) throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "delete from pt_portalpage  where module=? and pagename=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(module);
			parameter.addParam(pagename);
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void update(PtPageVO portalPageVO) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVO(portalPageVO);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public void updateLayout(PtPageVO page) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "update pt_portalpage set settings=? where module=? and pagename=?  and fk_pageuser=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(page.getSettings());
			parameter.addParam(page.getModule());
			parameter.addParam(page.getPagename());
			parameter.addParam(page.getFk_pageuser());
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sync(String pk_group) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtPageVO> list = (List<PtPageVO>) dao.retrieveByClause(PtPageVO.class, " fk_pageuser ='*' and ISNULL(pk_group, '~') = '~' and pk_portalpage NOT IN (SELECT parentid FROM pt_portalpage WHERE pk_group ='"+pk_group+"') ") ;
 			if (CollectionUtils.isNotEmpty(list)) {
				List<PtPageVO> vos = new ArrayList<PtPageVO>();
				for(PtPageVO pg : list){
					PtPageVO vo = (PtPageVO)pg.clone();
					vo.setPk_group(pk_group);
					vo.setParentid(pg.getPk_portalpage());
					vo.setPk_portalpage(null);
					vos.add(vo);
				}
				dao.insertVOs(vos.toArray(new PtPageVO[0]));
 			}
		} catch (Exception e) {
			String msg = "集团:"+pk_group+"Portal页面资源同步错误!";
			LfwLogger.error(msg, e);
			throw new PortalServiceException(msg);
		}
	}
	
	public void delGroupPage(String pk_portalpage)throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "delete from pt_portalpage WHERE parentid  = ? or pk_portalpage = ?"; 
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(pk_portalpage);
			parameter.addParam(pk_portalpage);
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	
	public void applyPageLayout(String pk_portalpage)throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "delete from pt_portalpage WHERE parentid  = ? "; 
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(pk_portalpage);
			dao.executeUpdate(sql, parameter);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}
	
}
