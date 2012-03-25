package nc.uap.portal.integrate.sso.impl;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.integrate.itf.IPtSsoConfigService;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.sso.util.SSOUtil;
import nc.uap.portal.vo.PtSsopropVO;

/**
 * SSO单点配置文件服务类
 * @author gd 2008-12-31
 * @version NC5.6
 * @since NC5.5
 */
public class PtSsoConfigServiceImpl implements IPtSsoConfigService {
 
	@Override
	public void add(PtSsopropVO ssoProp) {
		PtBaseDAO dao = new PtBaseDAO();
		try { 
			dao.insertVO(ssoProp);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}
	
	@Override
	public void update(PtSsopropVO ssoProp) {
		PtBaseDAO dao = new PtBaseDAO();
		try { 
			dao.updateVO(ssoProp);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}
	
	@Override
	public void deleteByPK(String pk_ssoprop) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.deleteByClause(PtSsopropVO.class, " pk_ssoprop = '"+pk_ssoprop+"'" );
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}
	
	@Override
	public void add(SSOProviderVO provider) {
		PtBaseDAO dao = new PtBaseDAO();
		PtSsopropVO vo = SSOUtil.provider2prop(provider);
		try { 
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}

	@Override
	public void delete(String systemCode) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			dao.deleteByClause(PtSsopropVO.class, " systemcode = '"+systemCode+"'" );
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(SSOProviderVO provider) {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtSsopropVO> vos = (List<PtSsopropVO>) dao.retrieveByClause(PtSsopropVO.class," systemcode = '" + provider.getSystemCode() + "'");
			if(vos == null || vos.isEmpty()){
				add(provider);
			}else{
				PtSsopropVO vo = SSOUtil.provider2prop(provider);
				vo.setPk_ssoprop(vos.get(0).getPk_ssoprop());
				dao.updateVO(vo);
			}
			
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		PortalCacheManager.notify(CacheKeys.PORTAL_SSO_PROVIDER_CACHE, CacheKeys.PORTAL_SSO_PROVIDER_MAP_CACHE);
	}
}
