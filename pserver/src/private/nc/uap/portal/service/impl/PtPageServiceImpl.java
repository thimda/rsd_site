package nc.uap.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPageService;
import nc.uap.portal.vo.PtPageVO;

import org.apache.commons.collections.CollectionUtils;

/**
 * 页面服务實現
 * 
 * @author licza
 * 
 */
public class PtPageServiceImpl implements IPtPageService {

	@Override
	public String add(PtPageVO vo) throws PortalServiceException {
		try {
			return new PtBaseDAO().insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("增加一个页面出现异常" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String[] add(PtPageVO[] vo) throws PortalServiceException {
		try {
			return new PtBaseDAO().insertVOs(vo);
		} catch (DAOException e) {
			LfwLogger.error("增加一组页面出现异常" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String doCopy(String pk_org, String parentid)
			throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			PtPageVO vo = (PtPageVO) dao.retrieveByPK(PtPageVO.class, parentid);
			vo.setPk_portalpage(null);
			vo.setPk_group(pk_org);
			dao.insertVO(vo);
		} catch (Exception e) {
			LfwLogger.error("复制一个页面出现异常" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void update(PtPageVO vo) throws PortalServiceException {
		try {
			new PtBaseDAO().updateVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("增加一个页面出现异常" + e.getMessage(), e);
		}
	}

	@Override
	public void update(PtPageVO[] vo) throws PortalServiceException {
		try {
			new PtBaseDAO().updateVOArray(vo);
		} catch (DAOException e) {
			LfwLogger.error("增加一个页面出现异常" + e.getMessage(), e);
		}
	}

	@Override
	public void delete(String pk) throws PortalServiceException {
		try {
			new PtBaseDAO().deleteByPK(PtPageVO.class, pk);
		} catch (DAOException e) {
			LfwLogger.error("增加一个页面出现异常" + e.getMessage(), e);
		}
	}

	@Override
	public void delete(String[] pk) throws PortalServiceException {
		try {
			new PtBaseDAO().deleteByPKs(PtPageVO.class, pk);
		} catch (DAOException e) {
			LfwLogger.error("增加一个页面出现异常" + e.getMessage(), e);
		}
	}

	@Override
	public void updateLayout(PtPageVO page)
			throws PortalServiceException {
		String pk_user = page.getFk_pageuser();
		String module = page.getModule();
		String pagename = page.getPagename();
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String sql = "select * from pt_portalpage where module='"+module+"' and pagename='"+pagename+"'  and fk_pageuser='"+pk_user+"' ";
			PtPageVO[] userPageVO = CRUDHelper.getCRUDService().queryVOs(sql, PtPageVO.class, null, null, null);
			if(userPageVO == null || userPageVO.length == 0){
				PtPageVO oriPageVO = findOriPageVO(pk_user, module, pagename);
				oriPageVO.setParentid(oriPageVO.getPk_portalpage());
				oriPageVO.setPk_portalpage(null);
				oriPageVO.setFk_pageuser(pk_user);
				oriPageVO.doSetSettingsStr(page.doGetSettingsStr());
				dao.insertVO(oriPageVO);
			}else{
				PtPageVO pagevo = userPageVO[0];
				pagevo.doSetSettingsStr(page.doGetSettingsStr());
				dao.updateVO(pagevo);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}		
	}

	private PtPageVO findOriPageVO(String pk_user, String module,
			String pagename) throws CpbBusinessException,
			PortalServiceException {
		PtPageVO oriPageVO = null;
		ICpUserQry userQry = NCLocator.getInstance().lookup(ICpUserQry.class);
		CpUserVO user = userQry.getUserByPk(pk_user);
		if(user != null){
			PtPageVO[] pages = PortalServiceUtil.getPageQryService().getOriPagesByUser(user);
			if(pages != null && pages.length > 0 )
				for (PtPageVO p : pages) {
					if (p.getPagename().equals(pagename) && p.getModule().equals(module)) {
						oriPageVO = p;
						break;
					}
				}
		}
		return oriPageVO;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void sync(String pk_group) throws PortalServiceException {
//		PtBaseDAO dao = new PtBaseDAO();
//		try {
//			List<PtPageVO> list = (List<PtPageVO>) dao.retrieveByClause(PtPageVO.class, " fk_pageuser ='*' and ISNULL(pk_group, '~') = '~' and pk_portalpage NOT IN (SELECT parentid FROM pt_portalpage WHERE pk_group ='"+pk_group+"') ") ;
// 			if (CollectionUtils.isNotEmpty(list)) {
//				List<PtPageVO> vos = new ArrayList<PtPageVO>();
//				for(PtPageVO pg : list){
//					PtPageVO vo = (PtPageVO)pg.clone();
//					vo.setPk_group(pk_group);
//					vo.setParentid(pg.getPk_portalpage());
//					vo.setPk_portalpage(null);
//					vos.add(vo);
//				}
//				dao.insertVOs(vos.toArray(new PtPageVO[0]));
// 			}
//		} catch (Exception e) {
//			String msg = "集团:"+pk_group+"Portal页面资源同步错误!";
//			LfwLogger.error(msg, e);
//			throw new PortalServiceException(msg);
//		}
	}
	@Override
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
