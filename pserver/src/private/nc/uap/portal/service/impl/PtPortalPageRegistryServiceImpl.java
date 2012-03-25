package nc.uap.portal.service.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.itf.IPtPortalPageRegistryService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtPageVO;

/**
 * 注册用户的页面布局描述到缓存
 * 
 * @author licza
 * 
 */
public class PtPortalPageRegistryServiceImpl implements IPtPortalPageRegistryService {
	@Override
	public void loadPortalPages() throws PortalServiceException {
	//		String dsName = LfwRuntimeEnvironment.getDatasource();
	//		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTTAL_PAGES_CACHE, dsName);
	//		IPtPortalPageQryService pageQry = NCLocator.getInstance().lookup(IPtPortalPageQryService.class);
	//		
	//		/**
	//		 * 初始化系统Portal页面 
	//		 */
	//		PtPortalPageVO[] systemPages = pageQry.getSystemPages();
	//		Map<String, Page> systemPortalPages = PortalPageDataWrap.getSystemPortalPages(systemPages);
	//		cache.put(CacheKeys.SYSTEM_PAGES_CACHE, systemPortalPages);
	//
	//		/**
	//		 * 初始化集团Portal页面 
	//		 */
	//		PtPortalPageVO[] groupPages = pageQry.getGroupPages();
	//		Map<String, Map<String, PtPortalPageVO>> groupPortalPages = PortalPageDataWrap.getGroupsPortalPages(groupPages);
	//		cache.put(CacheKeys.GROUP_PAGES_CACHE, groupPortalPages);
	//
	//		/**
	//		 * 初始化集团PortalPage的主键缓存
	//		 */
	//		Map<String, Map<String, String>> groupPortalPagesPk = PortalPageDataWrap.getGroupsPortalPagesPK(groupPages);
	//		cache.put(CacheKeys.GROUP_PAGES_PK_CACHE, groupPortalPagesPk);
	//		
	////		/**
	////		 * 建立缓存用户自定义Portal页面 缓存
	////		 * 仅建立 用户登陆后追加
	////		 */
	////		// 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void cachePrepareUpdatePages(Set<PtPageVO> pageVOs) {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.DEPLOY_PAGES_CACHE, dsName);
		Set<PtPageVO> pageVOsCache = (Set<PtPageVO>) cache.get(CacheKeys.DEPLOY_PAGES_UPDATE_CACHE);
		if (pageVOsCache == null)
			pageVOsCache = new HashSet<PtPageVO>();

		pageVOsCache.addAll(pageVOs);
		cache.put(CacheKeys.DEPLOY_PAGES_UPDATE_CACHE, pageVOsCache);
	}

	@Override
	public void registryUserPageCache(Page page){
		 Map<String, Page> userCacheMap = (Map<String, Page>)PortalCacheManager.getUserPageCache();
		 userCacheMap.put(PortalPageDataWrap.modModuleName(page.getModule(), page.getPagename()), page);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public PtPageVO getPreUpdatePageFromCache(String module,String pagename) {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.DEPLOY_PAGES_CACHE, dsName);
		Set<PtPageVO> pageVOsCache = (Set<PtPageVO>) cache.get(CacheKeys.DEPLOY_PAGES_UPDATE_CACHE);
		if (PtUtil.isNull(pageVOsCache))
			return null;
		 for(PtPageVO vo : pageVOsCache){
			 if(vo.getModule().equals(module) && vo.getPagename().equals(pagename))
				 return vo;
		 }
		return null;
	}
}
