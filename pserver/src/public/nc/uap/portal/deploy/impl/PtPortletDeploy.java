package nc.uap.portal.deploy.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nc.bs.logging.Logger;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.itf.AbstractPtDeploy;
import nc.uap.portal.deploy.itf.IPtDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.util.PortletDataWrap;
import nc.uap.portal.vo.PtPortletVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * Portlet部署类
 * 
 * @author licza
 * 
 */
public class PtPortletDeploy extends AbstractPtDeploy implements IPtDeploy {
	/**
	 * 部署Portlet
	 * 
	 * @param pdd Portal模块信息
	 */
	public void deploy(PortalDeployDefinition define) {
		IPtPortletQryService pageQry = PortalServiceUtil.getPortletQryService();
		String module = define.getModule();
		PortletApplicationDefinition portletDefineModule = define.getPortletDefineModule();
		if (portletDefineModule != null) {
			PortletDefinition[] portlets = portletDefineModule.getPortlets().toArray(new PortletDefinition[0]);
			try {
				/** 得到系统的Portlet **/
				PtPortletVO[] portletvos = pageQry.getSystemPortlet(module);
				deployPortlet(portlets, portletvos, module);
//				deployResource(portlets, module, IResourceVO.RESOURCE_TYPE_PORTLET);
			} catch (Exception e) {
				Logger.error("Module:" + module + " Portlet  deploy fail", e);
			}
		}
	}

	/**
	 * 更新数据库中的Portlet
	 * 
	 * @param portlets 本地PortletDefinition数组
	 * @param portletvos 数据库中PortletVO数组
	 * @param module 模块名
	 * @throws JAXBException
	 */
	private void deployPortlet(PortletDefinition[] portlets, PtPortletVO[] portletvos, String module) throws PortalServiceException, JAXBException {

		// 准备文件
		// 放置版本
		Map<String, String> localMirror = new HashMap<String, String>();
		// 放置portlets实例
		Map<String, PortletDefinition> localCopy = new HashMap<String, PortletDefinition>();
		if (portlets != null && portlets.length > 0) {
			for (PortletDefinition portlet : portlets) {
				localMirror.put(portlet.getPortletName(), PortletDataWrap.getVersion(portlet));
				localCopy.put(portlet.getPortletName(), portlet);
			}
		}
		Set<PtPortletVO> addPortlet = new HashSet<PtPortletVO>();
		Set<PtPortletVO> updatePortletCache = new HashSet<PtPortletVO>();
		Set<String> deletePortlet = new HashSet<String>();
		if (portletvos != null && portletvos.length > 0) {
			for (PtPortletVO portletVO : portletvos) {
				String localPageName = portletVO.getPortletid();
				PortletDefinition local = localCopy.get(localPageName);
				if (local == null) {
					// 需要删除的PML
					portletVO.setNewversion("");
					deletePortlet.add(portletVO.getPk_portlet());
				} else {
					// 需要更新的portlet
					if (versionCompare(localMirror.get(localPageName), portletVO.getNewversion())) {
						portletVO.setNewversion(PortletDataWrap.getVersion(local));
						PtPortletVO portletCache = PortletDataWrap.warpVO(portletVO, local);
						updatePortletCache.add(portletCache);
					}
					// 从镜中删除
					localMirror.remove(localPageName);
				}
			}
		}
		if (!localMirror.isEmpty()) {
			// 需要增加的PML
			Set<Map.Entry<String, String>> localmirrorEntrySet = localMirror.entrySet();
			for (Map.Entry<String, String> localmirrorEntry : localmirrorEntrySet) {
				String localPageName = localmirrorEntry.getKey();
				PortletDefinition pd = localCopy.get(localPageName);
				PtPortletVO target = new PtPortletVO();
				target = PortletDataWrap.warpVO(target, pd);
				target.setActiveflag(UFBoolean.valueOf(true));
				target.setShareflag(UFBoolean.valueOf(false));
				target.setFk_portaluser("#");
				target.setModule(module);
				target.setNewversion(target.getVersion());
				addPortlet.add(target);
			}
		}
		// 需要增加
		if (!addPortlet.isEmpty()) {
			addPortletWithGroups(addPortlet);
		}
		if (!updatePortletCache.isEmpty()) {
			updatePortletWithGroups(updatePortletCache);
		}
		if (!deletePortlet.isEmpty()) {
			deletePortletWithGroups(deletePortlet);
		}
	}

	/**
	 * 批量保存Portlet
	 * 
	 * @param addPortlet 需要增加的Portlet
	 * @throws PortalServiceException 插入数据库异常
	 */
	private void addPortletWithGroups(Set<PtPortletVO> addPortlet) throws PortalServiceException {
		PtPortletVO[] portles = addPortlet.toArray(new PtPortletVO[0]);
		IPtPortletService prs = PortalServiceUtil.getPortletService();
		// 写入系统Portlet
//		String[] pks = 
		prs.addPortlets(portles);
//		PortalServiceUtil.getServiceProvider().getPortletDeployService().addPortlet(portles, prs, pks);
	}



	/**
	 * 批量更新Portlet
	 * 
	 * @param updatePortlet
	 * @throws PortalServiceException
	 */
	private void updatePortletWithGroups(Set<PtPortletVO> updatePortlet) throws PortalServiceException {
		IPtPortletService prs = PortalServiceUtil.getPortletService();
		PtPortletVO[] portlets = updatePortlet.toArray(new PtPortletVO[0]);
		prs.updatePortlets(portlets);
	}
	/**
	 * 批量删除
	 * @param delPortlet
	 * @throws PortalServiceException
	 */
	private void deletePortletWithGroups(Set<String> delPortlet)throws PortalServiceException {
		IPtPortletService prs = PortalServiceUtil.getPortletService();
		prs.delete(delPortlet.toArray(new String[]{}));
	}

}
