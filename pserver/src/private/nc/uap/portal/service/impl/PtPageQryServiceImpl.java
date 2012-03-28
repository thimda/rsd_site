package nc.uap.portal.service.impl;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.uapbd.IPsndocPubService;
import nc.uap.cpb.org.util.SecurityUtil;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPageQryService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 页面查询服务实现
 * @author licza
 *
 */
public class PtPageQryServiceImpl implements IPtPageQryService {
	
	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getPageByGroup(String pk_group)
			throws PortalServiceException {
		String clause = "  pk_group = '"+ pk_group +"' ";
		try {
			List<PtPageVO> list = (List<PtPageVO>)new PtBaseDAO().retrieveByClause(PtPageVO.class, clause);
			if(list != null && list.size() > 0)
				return list.toArray(new PtPageVO[0]);
		} catch (Exception e) {
			LfwLogger.error("查询页面 SQL : ("+ clause +")出现异常:" + e, e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getPageByLevel(Integer level)
			throws PortalServiceException {
		String clause = " fk_pageuser='*' and levela = "+ level +" ";
		try {
			List<PtPageVO> list = (List<PtPageVO>)new PtBaseDAO().retrieveByClause(PtPageVO.class, clause);
			if(list != null && list.size() > 0)
				return list.toArray(new PtPageVO[0]);
		} catch (Exception e) {
			LfwLogger.error("查询页面 SQL : ("+ clause +")出现异常:" + e, e);
		}
		return null;
	}

	@Override
	public PtPageVO getPageByPk(String pk) throws PortalServiceException {
		try {
			return (PtPageVO) new PtBaseDAO().retrieveByPK(PtPageVO.class, pk);
		} catch (Exception e) {
			LfwLogger.error("查询页面出现异常:" + e, e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getPageByUser(String pk_user)
			throws PortalServiceException {
		String clause = " fk_pageuser = '"+ pk_user +"' ";
		try {
			List<PtPageVO> list = (List<PtPageVO>)new PtBaseDAO().retrieveByClause(PtPageVO.class, clause);
			if(list != null && list.size() > 0)
				return list.toArray(new PtPageVO[0]);
		} catch (Exception e) {
			LfwLogger.error("查询页面 SQL : ("+ clause +")出现异常:" + e, e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO[] getSysPagesByModule(String module)
			throws PortalServiceException {
		String clause = " isnull(parentid, '~') = '~' and module = '"+ module +"' ";
		try {
			List<PtPageVO> list = (List<PtPageVO>)new PtBaseDAO().retrieveByClause(PtPageVO.class, clause);
			if(list != null && list.size() > 0)
				return list.toArray(new PtPageVO[0]);
		} catch (Exception e) {
			LfwLogger.error("查询页面 SQL : ("+ clause +")出现异常:" + e, e);
		}
		return null;
	}

	@Override
	public PtPageVO[] getOriPagesByUser(CpUserVO user)
			throws PortalServiceException {
		String pk_user = user.getCuserid();
		String pk_psn = user.getPk_base_doc();
		IPsndocPubService psnQry = NCLocator.getInstance().lookup(IPsndocPubService.class);
		String depts = "";
		int userType = user.getUser_type();
 		try {
 			Map<String, List<String>> map = psnQry.queryDeptIDByPsndocIDs(new String[]{pk_psn});
 			if(map != null && !map.isEmpty() && map.containsKey(pk_psn)){
 				depts = StringUtils.join(map.get(pk_psn).iterator(),"','");
 			}
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
		String roles = StringUtils.join(SecurityUtil.getRolePks(pk_user), "','");
		
		
		//用户有权限的
		String sql = "SELECT * FROM pt_portalpage WHERE levela = " + userType + " and fk_pageuser = '*' AND  (" +
		"pk_portalpage IN (SELECT pk_page FROM pt_pagedept WHERE pk_dept IN ('" + depts + "')) " +
		"OR pk_portalpage IN (SELECT pk_page FROM pt_pageuser WHERE pk_user ='"+user.getCuserid()+"' )"+
		"OR pk_portalpage IN (SELECT pk_page FROM pt_pagerole WHERE pk_role IN ('"+ roles +"')))";
		
		
		//公有
		String sql2 = "SELECT * FROM pt_portalpage WHERE levela = " + userType + " and fk_pageuser = '*' AND isnull(pk_group,'~')='~' AND pk_portalpage NOT IN (SELECT DISTINCT(pk_page) FROM pt_pageuser) AND pk_portalpage NOT IN (SELECT DISTINCT(pk_page) FROM pt_pagerole) AND pk_portalpage NOT IN (SELECT DISTINCT(pk_page) FROM pt_pagedept) ";
		
		try {
			//用户私有
			PtPageVO[] userPages = getPageByUser(user.getCuserid());
			
			PtPageVO[] powerPages = CRUDHelper.getCRUDService().queryVOs(sql, PtPageVO.class, null, null);
			
			PtPageVO[] publicPage = CRUDHelper.getCRUDService().queryVOs(sql2, PtPageVO.class, null, null);
			

			/**
			 * 合并1 powerPages  publicPage 取并集 
			 */
			PtPageVO[] pnp = PortalPageDataWrap.checkNews(powerPages, publicPage);
			
			PtPageVO[]  pup = (PtPageVO[]) ArrayUtils.addAll(powerPages, pnp);
			
			
			/**
			 * 合并2 userPages 和 pup
			 */
			PtPageVO[] unp = PortalPageDataWrap.checkNews(userPages, pup);
			
			 //TODO 这个地方没有漏掉了用户自定义后,源页面权限修改.
			PtPageVO[] uup = (PtPageVO[]) ArrayUtils.addAll(userPages, unp);
			return uup;
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
		return null;
	}

}
