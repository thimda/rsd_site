package nc.uap.portal.service.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.WebKeys;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtPortletPreferencesVO;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;

import org.apache.commons.lang.StringUtils;

/**
 * Portlet信息查询服务
 * 
 * @author licza
 * 
 */
public class PtPortletQryServiceImpl implements IPtPortletQryService {
	
	@SuppressWarnings("unchecked")
	public PtPortletVO[] qryPortletByClause(String clause){
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtPortletVO> list = (List<PtPortletVO>) dao.retrieveByClause(PtPortletVO.class, clause);
			if(list != null && !list.isEmpty())
				return list.toArray(new PtPortletVO[]{});
		} catch (Exception e) {
			LfwLogger.error("===PtPortletQryServiceImpl#qryPortletByClause===查询Portlet配置失败:" + clause + ";msg:" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 查询集团Portlet配置
	 * @param pk_group
	 * @param portletname.
	 * @param pagename
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPreferenceVO getGroupPortletPreference(String pk_group,String portletname , String pagename) throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtPreferenceVO> list = (List<PtPreferenceVO>) dao.retrieveByClause(PtPreferenceVO.class, " isnull(pk_user,'~') = '~' and pk_group='" + pk_group + "' and portletname='" + portletname + "' and pagename='" + pagename + "'  ");
			if(list != null && !list.isEmpty())
				return list.get(0);
		} catch (Exception e) {
			LfwLogger.error("===PtPortletQryServiceImpl#getGroupPortletPreference===查询集团Portlet配置失败" + e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 查询用户的portlet配置
	 * @param pk_user
	 * @param pk_group
	 * @param portletname
	 * @param pagename
	 * @return
	 * @throws PortalServiceException
	 */
	public PtPreferenceVO getUserPortletPreference(String pk_user,String pk_group,String portletname , String pagename) throws PortalServiceException{
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtPreferenceVO> list = (List<PtPreferenceVO>) dao.retrieveByClause(PtPreferenceVO.class, " pk_user = '"+ pk_user+"' and  pk_group='" + pk_group + "' and portletname='" + portletname + "' and pagename='" + pagename + "'  ");
			if(list != null && !list.isEmpty())
				return list.get(0);
		} catch (Exception e) {
			LfwLogger.error("===PtPortletQryServiceImpl#getUserPortletPreference===查询用户Portlet配置失败" + e.getMessage(),e);
		}
		return null;
	}
	
	
	
	@Override
	public PortletDefinition findPortlet(String pk_user, String pk_group, String portletName, String portletModule, String pageModule, String pageName) {
		PortletDefinition pt = findPortletDefinition(pk_user, pk_group, portletName, portletModule);
		return pt;
	}

	@Override
	public PtPortletVO[] getGroupPortlets(String module, String parentid) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portlet where fk_portaluser ='#' and module=?   and parentid=? ";
		try {
			parameter.addParam(module);
			parameter.addParam(parentid);
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO[] getGroupPortlets(String pk_group, String[][] portletNames) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from pt_portlet where pk_group=? and  fk_portaluser ='#' and ( ");
		if (portletNames != null) {
			for (int i = 0; i < portletNames.length; i++) {
				String[] portletName = portletNames[i];
				if (i == 0) {
					sb.append("  (module='" + portletName[0] + "' and portletid='" + portletName[1] + "')");
				} else {
					sb.append(" or (module='" + portletName[0] + "' and portletid='" + portletName[1] + "')");
				}
			}
		}
		sb.append(")");
		String sql = sb.toString();
		try {
			parameter.addParam(pk_group);
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO[] getSystemPortlet(String module) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		SQLParameter parameter = new SQLParameter();
		String sql = "select * from pt_portlet where fk_portaluser ='#' and ISNULL(pk_group,'~') = '~' and module=?  ";
		try {
			parameter.addParam(module);
			List list = (List) dao.executeQuery(sql, parameter, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO[] getSystemPortlet() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select * from pt_portlet where fk_portaluser ='#' and ISNULL(pk_group, '~') = '~'";
		try {
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO[] getGroupPortlets() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select * from pt_portlet where fk_portaluser ='#' and ISNULL(pk_group,'~') != '~'   order by pk_group ";
		try {
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO[] getUserDiyPortlets() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "select * from pt_portlet where  ISNULL(fk_portaluser,'~') != '~' and fk_portaluser !='#' order by  fk_portaluser ";
		try {
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPortletVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletVO[] portalPageList = (PtPortletVO[]) list.toArray((PtPortletVO[]) Array.newInstance(PtPortletVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	public PtPortletPreferencesVO[] getPortletPreferences() throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		String sql = "SELECT pf.dr ,pf.fk_portalpage ,pf.fk_portaluser ,pf.pk_portlet ,pf.pk_portletpreferences ,pf.preferences ,pf.ts ,portletid,module from  pt_portletpreferences  as pf, pt_portlet as pt  where pf.pk_portlet=pt.pk_portlet  order by (module+':'+portletid) ";
		try {
			List list = (List) dao.executeQuery(sql, new BeanListProcessor(PtPortletPreferencesVO.class));
			if (list == null || list.isEmpty()) {
				return null;
			}
			PtPortletPreferencesVO[] portalPageList = (PtPortletPreferencesVO[]) list.toArray((PtPortletPreferencesVO[]) Array.newInstance(
					PtPortletPreferencesVO.class, 0));
			return portalPageList;
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	@Override
	public PtPortletVO findPortletByPK(String pk_portlet) throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			return (PtPortletVO) dao.retrieveByPK(PtPortletVO.class, pk_portlet);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 从缓存中获得一个Portlet配置
	 * 
	 * @param pk_user 用户编码
	 * @param pk_group 集团编码
	 * @param portletName Portlet名称
	 * @param portletModule portlet模块
	 * @return Portlet定义
	 */
	private PortletDefinition findPortletDefinition(String pk_user, String pk_group, String portletName, String portletModule) {
		PortletDefinition pd = findPortletFromUserCache(pk_user, pk_group, portletName, portletModule);
		if (pd == null) {
			pd = findPortletFromGroupCache(pk_group, portletName, portletModule);
			if (pd == null) {
				pd = findPortletFromSystemCache(portletName, portletModule);
			}
		}
		return pd;
	}

	/**
	 * 从缓存中选择用户自定义Portlet
	 * 
	 * @param pk_user 用户编码
	 * @param pk_group 集团编码
	 * @param portletName Portlet名称
	 * @param module 模块
	 * @return 用户自定义Portlet定义
	 */
	private PortletDefinition findPortletFromUserCache(String pk_user, String pk_group, String portletName, String module) {
		if(pk_user == null)
			return null;
		Map<String, Map<String, PortletDefinition>> usersPortlets = PortalCacheManager.getUserDiyPortletsCache();
 		Map<String, PortletDefinition> userPortlets = usersPortlets.get(pk_user);
		if (userPortlets != null && (!userPortlets.isEmpty())) {
			PortletDefinition userPortlet = userPortlets.get(PortalPageDataWrap.modModuleName(module, portletName));
			return userPortlet;
		}
		return null;
	}

	@Override
	public PortletDefinition findPortletFromGroupCache(String pk_group, String portletName, String module) {
		if(pk_group == null || StringUtils.equals(pk_group, WebKeys.EMPTY_GROUP)){
			LfwLogger.warn("查询集团Portal时传入的集团编码为空!");
			return null;
		}
		Map<String, Map<String, PortletDefinition>> groupsPortlets =  PortalCacheManager.getGroupPortletsCache();
		Map<String, PortletDefinition> groupPortlets = groupsPortlets.get(pk_group);
		if(groupPortlets == null) {
			PortalServiceUtil.getPortletRegistryService().updateGroupCache(pk_group,true);
			groupsPortlets =  PortalCacheManager.getGroupPortletsCache();
		}
		if(groupPortlets == null)
			return null;
		PortletDefinition groupPortlet = groupPortlets.get(PortalPageDataWrap.modModuleName(module , portletName));
		return groupPortlet;
	}

	@Override
	public PortletDefinition findPortletFromSystemCache(String portletId, String module) {
		Map<String, PortletDefinition> systemPortlets = PortalCacheManager.getSystemPortletsCache();
		return systemPortlets.get(PortalPageDataWrap.modModuleName(module, portletId));
	}

	@Override
	public PtPortletVO[] getGroupPortlets(String pk_group)	throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			Collection  list = dao.retrieveByClause(PtPortletVO.class, " pk_group ='"+pk_group+"' ");
			if(!PtUtil.isNull(list))
				return (PtPortletVO[]) list.toArray(new  PtPortletVO[0]);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return new PtPortletVO[0];
	}
}
