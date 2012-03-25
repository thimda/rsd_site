package nc.uap.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;

/**
 * Portlet添加&修改
 * @author licza
 *
 */
public class PtPortletServiceImpl implements IPtPortletService {
	/**
	 * 增加一个Portlet配置
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	public String addPreference(PtPreferenceVO vo)throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String  pks = dao.insertVO(vo);
			return pks;
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}
	/**
	 * 修改一个Portlet配置
	 * @param vo
	 * @return
	 * @throws PortalServiceException
	 */
	public void updatePreference(PtPreferenceVO vo)throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVO(vo);
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}
	
	/**
	 * 新增一组PortletVO
	 * @param pages 
	 * @return 新增Pages的主键
	 * @throws PortalServiceException
	 */
	@Override
	public String[] addPortlets(PtPortletVO[] portlets) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			String[] pks = dao.insertVOs(portlets);
			return pks;
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 修改一组PortletVO
	 * @param pages
	 * @throws PortalServiceException
	 */
	@Override
	public void updatePortlets(PtPortletVO[] portlets) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.updateVOArray(portlets);
		} catch (DAOException e) {
			throw new PortalServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sync(String pk_group) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtPortletVO> list = (List<PtPortletVO>) dao.retrieveByClause(PtPortletVO.class, "fk_portaluser ='#' and ISNULL(pk_group, '~') = '~' and pk_portlet NOT IN (SELECT parentid FROM pt_portlet  WHERE pk_group ='"+pk_group+"') ") ;
 			if (CollectionUtils.isNotEmpty(list)) {
				List<PtPortletVO> vos = new ArrayList<PtPortletVO>();
				for(PtPortletVO pg : list){
					PtPortletVO vo = (PtPortletVO)pg.clone();
					vo.setPk_group(pk_group);
					vo.setParentid(pg.getPk_portlet());
					vo.setPk_portlet(null);
					vos.add(vo);
				}
				dao.insertVOs(vos.toArray(new PtPortletVO[0]));
				/**
				 * Portal缓存是否已经初始化
				 */
				boolean isInitialize = PortalServiceUtil.getPortletRegistryService().getModuleInitializeStatus();
				if(isInitialize)
					PortalServiceUtil.getPortletRegistryService().updateGroupCache(pk_group,true);
 			}
		} catch (Exception e) {
			String msg = "集团:"+pk_group+"Portlet资源同步错误!";
			LfwLogger.error(msg, e);
			throw new PortalServiceException(msg);
		}
	}
	@Override
	public void delete(String pk) throws PortalServiceException {
		try {
			new PtBaseDAO().deleteByPK(PtPortletVO.class, pk);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
	@Override
	public void delete(String[] pk) throws PortalServiceException {
		try {
			new PtBaseDAO().deleteByPKs(PtPortletVO.class, pk);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
}
