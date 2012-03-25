package nc.uap.portal.deploy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.itf.IPtDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.plugins.model.ExPoint;
import nc.uap.portal.plugins.model.Extension;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;
import nc.uap.portal.plugins.model.PtPlugin;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPluginQryService;
import nc.uap.portal.service.itf.IPtPluginService;
import nc.uap.portal.util.PtUtil;

/**
 * 插件部署实现
 * 
 * @author licza
 * 
 */
public class PtPluginDeploy implements IPtDeploy {

	@Override
	public void deploy(PortalDeployDefinition define) {
		String moduleName = define.getModule();
		PtPlugin plugin = define.getPlugin();
		if (plugin == null){
			clear(moduleName);
			return;
		}
		
		//初始化
		init(plugin);
		IPtPluginQryService pluginQry = PortalServiceUtil.getPluginQryService();
		try {
			// 数据库中扩展
			PtExtension[] exArr = pluginQry.getAllExtension(define.getModule());
			PtExtensionPoint[] exPointArr=pluginQry.getAllExtensionPoint();
			deploy(exList, exArr, moduleName);
			deploy(expList, exPointArr);
		} catch (PortalServiceException e) {
			LfwLogger.error("Module :" + moduleName + " Plugin deploy fail", e);
		}
		
	}
	/**
	 * 清理模块
	 * @param moduleName
	 */
	private void clear(String moduleName){
		try {
			PortalServiceUtil.getPluginService().clearModule(moduleName);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
	/**
	 * 部署模块下的扩展
	 * 
	 * @param exList 本地的扩展列表
	 * @param exArr 数据库中的扩展列表
	 * @throws PortalServiceException 数据库操作异常
	 */
	public void deploy(List<PtExtension> exList, PtExtension[] exArr, String module) throws PortalServiceException {
		IPtPluginService pluginService = PortalServiceUtil.getPluginService();

		// 镜像
		Set<String> localMirror = new HashSet<String>();
		// 放置portlets实例
		Map<String, PtExtension> localCopy = new HashMap<String, PtExtension>();
		if (exList != null && exList.size() > 0) {
			for (PtExtension ex : exList) {
				localMirror.add(ex.getId());
				ex.setModule(module);
				localCopy.put(ex.getId(), ex);
			}
		}
		Set<PtExtension> beAdd = new HashSet<PtExtension>();
		Set<PtExtension> beUpdate = new HashSet<PtExtension>();
		Set<PtExtension> beDelete = new HashSet<PtExtension>();
		if (exArr != null && exArr.length > 0) {
			for (PtExtension exVO : exArr) {
				String localPageName = exVO.getId();
				PtExtension local = localCopy.get(localPageName);
				if (local == null) {
					// 需要删除的
					beDelete.add(exVO);
				} else {
					// 需要更新的
					if (!exVO.same(local)) {
						exVO.copy(local);
						beUpdate.add(exVO);
					}
					// 从镜中删除
					localMirror.remove(localPageName);
				}
			}
		}
		if (!localMirror.isEmpty()) {
			// 需要增加的
			for (String localmirrorEntry : localMirror) {
				beAdd.add(localCopy.get(localmirrorEntry));
			}
		}
		if (!beAdd.isEmpty()) {
			pluginService.add(beAdd.toArray(new PtExtension[0]));
		}
		if (!beUpdate.isEmpty()) {
			pluginService.update(beUpdate.toArray(new PtExtension[0]));
		}
//		if (!beDelete.isEmpty()) {
//			pluginService.delete(beDelete.toArray(new PtExtension[0]));
//		}
	}
	/**
	 * 部署模块下的扩展点
	 * 
	 * @param exList 本地的扩展点列表
	 * @param exArr 数据库中的扩展点列表
	 * @throws PortalServiceException 数据库操作异常
	 */
	public void deploy(List<PtExtensionPoint> exList, PtExtensionPoint[] exArr) throws PortalServiceException {
		IPtPluginService pluginService = PortalServiceUtil.getPluginService();

		// 镜像
		Set<String> localMirror = new HashSet<String>();
		// 放置portlets实例
		Map<String, PtExtensionPoint> localCopy = new HashMap<String, PtExtensionPoint>();
		if (exList != null && exList.size() > 0) {
			for (PtExtensionPoint ex : exList) {
				localMirror.add(ex.getPoint());
				localCopy.put(ex.getPoint(), ex);
			}
		}
		Set<PtExtensionPoint> beAdd = new HashSet<PtExtensionPoint>();
		Set<PtExtensionPoint> beUpdate = new HashSet<PtExtensionPoint>();
		Set<PtExtensionPoint> beDelete = new HashSet<PtExtensionPoint>();
		if (exArr != null && exArr.length > 0) {
			for (PtExtensionPoint exVO : exArr) {
				String localPageName = exVO.getPoint();
				PtExtensionPoint local = localCopy.get(localPageName);
				if (local == null) {
					// 需要删除的
					beDelete.add(exVO);
				} else {
					// 需要更新的
					if (!exVO.equals(local)) {
						exVO.copy(local);
						beUpdate.add(exVO);
					}
					// 从镜中删除
					localMirror.remove(localPageName);
				}
			}
		}
		if (!localMirror.isEmpty()) {
			// 需要增加的
			for (String localmirrorEntry : localMirror) {
				beAdd.add(localCopy.get(localmirrorEntry));
			}
		}
		if (!beAdd.isEmpty()) {
			pluginService.add(beAdd.toArray(new PtExtensionPoint[0]));
		}
		if (!beUpdate.isEmpty()) {
			pluginService.update(beUpdate.toArray(new PtExtensionPoint[0]));
		}
//		if (!beDelete.isEmpty()) {
//			pluginService.delete(beDelete.toArray(new PtExtensionPoint[0]));
//		}
	}
	
	private List<PtExtension> exList = new ArrayList<PtExtension>();
	
	private List<PtExtensionPoint> expList = new ArrayList<PtExtensionPoint>();
	/**
	 * 初始化
	 * @param plugin
	 */
	private void init(PtPlugin plugin){
		exList.clear();
		expList.clear();
		if(plugin != null){
			List<ExPoint> exps = plugin.getExtensionPointList();
			if(!PtUtil.isNull(exps)){
				for(ExPoint exp : exps){
					if(exp == null)
						continue;
					PtExtensionPoint expVO = new PtExtensionPoint();
					expVO.formXML(exp);
					expList.add(expVO);
					if(!PtUtil.isNull(exp.getExtensionList())){
						for(Extension ex : exp.getExtensionList()){
							PtExtension exVO = new PtExtension();
							exVO.fromXML(ex, exp.getPoint());
							exList.add(exVO);
						}
					}
				}
			}
		}
	}

}
