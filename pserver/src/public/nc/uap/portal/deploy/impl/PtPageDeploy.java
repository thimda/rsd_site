package nc.uap.portal.deploy.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.itf.AbstractPtDeploy;
import nc.uap.portal.deploy.itf.IPtDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPageService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.vo.PtPageVO;

/**
 * Portal页面部署
 * @author licza
 *
 */
public class PtPageDeploy extends AbstractPtDeploy implements IPtDeploy {
	/**
	 * 部署模块中的页面布局描述文件
	 * 
	 * @param pd 模块信息
	 * @throws PortalServiceException
	 * @see {@link IPtDeploy#deploy(PortalDeployDefinition)}
	 */
	public void deploy(PortalDeployDefinition define) {
		 
		String module = define.getModule();
		Page[] pages = define.getPages();
		try {
			PtPageVO[] pagevos = PortalServiceUtil.getPageQryService().getSysPagesByModule(module);
			deployPml(pages, pagevos, module);
		} catch (PortalServiceException e) {
			Logger.error("Module:" + module + " deploy fail", e);
		}
	}

	/**
	 * 更新数据库中的PML文件
	 * 
	 * @param pages 本地PortalPage数组
	 * @param 数据库中PortalPage数组
	 */
	public void deployPml(Page[] pages, PtPageVO[] pagevos, String module) {

		/**
		 *  准备文件
		 *  放置版本
		 */
		Map<String, String> localMirror = new HashMap<String, String>();
		/**
		 * 放置Portal页面实例 
		 */
		Map<String, Page> localCopy = new HashMap<String, Page>();
		if (pages != null && pages.length > 0) {
			for (Page page : pages) {
				localMirror.put(page.getPagename(), page.getVersion());
				localCopy.put(page.getPagename(), page);
			}
		}
		Set<PtPageVO> addPage = new HashSet<PtPageVO>();
		
		Set<PtPageVO> updatePage = new HashSet<PtPageVO>();
		
		Set<PtPageVO> updatePageCache = new HashSet<PtPageVO>();
		
		Set<String> deletePage = new HashSet<String>();
		
		if (pagevos != null && pagevos.length > 0) {
			for (PtPageVO pageVO : pagevos) {
				String localPageName = pageVO.getPagename();
				Page local = localCopy.get(localPageName);
				if (local == null) {
					/**
					 * 需要删除的Portal页面
					 * 将新版本设置为空
					 */
					deletePage.add(pageVO.getPk_portalpage());
				} else {
					if (versionCompare(localMirror.get(localPageName), pageVO.getNewversion())) {
						/**
						 * 需要更新的Portal页面
						 * 设置新版本为本地版本
						 */
						pageVO=PortalPageDataWrap.copyPage2PageVO(localCopy.get(localPageName),pageVO);
						pageVO.setNewversion(local.getVersion());
						updatePage.add(pageVO);
					}
					/**
					 * 克隆一份携有新信息的VO到缓存中 
					 */
					PtPageVO pageVOCache = (PtPageVO) pageVO.clone();
					pageVOCache = PortalPageDataWrap.copyPage2PageVO(local, pageVOCache);
					updatePageCache.add(pageVOCache);
					/**
					 * 从镜像中删除 
					 * 循环完毕镜像中剩下的内容即为新增的内容
					 */
					localMirror.remove(localPageName);
				}
			}
		}
		
		if (!localMirror.isEmpty()) {
			/**
			 * 需要增加的 
			 */
			Set<Map.Entry<String, String>> localmirrorEntrySet = localMirror.entrySet();
			for (Map.Entry<String, String> localmirrorEntry : localmirrorEntrySet) {
				String localPageName = localmirrorEntry.getKey();
				PtPageVO ppv = PortalPageDataWrap.copyPage2PageVO(localCopy.get(localPageName), new PtPageVO());
				ppv.setModule(module);
				ppv.setNewversion(ppv.getVersion());
				addPage.add(ppv);
			}
		}
		
		/**
		 * 持久化到数据库中
		 */
		IPtPageService ps = PortalServiceUtil.getPageService();
		try {
		if (!addPage.isEmpty()) 
			ps.add(addPage.toArray(new PtPageVO[0]));
		
		if (!updatePage.isEmpty())  
			ps.update(updatePage.toArray(new PtPageVO[0]));
		
		if (!deletePage.isEmpty()) 
			ps.delete(deletePage.toArray(new String[0]));
		}catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
}
