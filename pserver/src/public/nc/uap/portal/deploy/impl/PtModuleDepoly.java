package nc.uap.portal.deploy.impl;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalModuleQryService;
import nc.uap.portal.service.itf.IPtPortalModuleService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtModuleVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.lang.StringUtils;

/**
 * Portal模块部署类
 * 
 * @author licza
 * 
 */
public class PtModuleDepoly {
	/**
	 * 部署一个模块
	 * 
	 * @param define
	 * @throws PortalServiceException
	 */
	public void deploy(PortalDeployDefinition define)  {
		String newModuleid = define.getModule();
		String newTitle = StringUtils.defaultIfEmpty(define.getTitle(), "");
		//依赖模块
		String newDependentid=StringUtils.join(define.getDependsModule(), ",");
		IPtPortalModuleQryService moduleQry = PortalServiceUtil.getModuleQryService();
		IPtPortalModuleService moduleService = PortalServiceUtil.getModuleService();
		try {
		PtModuleVO module = moduleQry.find(newModuleid);
		if (module == null) {
//			IPortalServerService groupQry = PortalServiceUtil.getPortalServerService();
//			PtGroupVO[] groups = groupQry.getAllGroups();
			module = new PtModuleVO();
			module.setActiveflag(UFBoolean.valueOf(true));
			module.setModuleid(newModuleid);
			module.setTitle(newTitle);
			module.setDependentid(newDependentid);
			//String pk =
			moduleService.add(module);
//			PtModuleVO[] churnModules = churnModuleVO(module, groups, pk);
//			moduleService.add(churnModules);
		} else {
			if (!PtUtil.isNull(module.getTitle()) && !StringUtils.equals(define.getTitle(), module.getTitle())) {
				module.setTitle(newTitle);
				moduleService.updateAll(module);
			}
		}
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

	/***
	 * 处理已删除的module
	 * 
	 * @throws PortalServiceException
	 */

	public void overdueModuleHandle() throws PortalServiceException {
		// TODO
	}

	/**
	 * 给每个集团生成一份模块
	 * 
	 * @param module
	 * @param groups
	 * @param pk
	 * @return
	 */
//	public static PtModuleVO[] churnModuleVO(PtModuleVO module,
//			PtGroupVO[] groups, String pk) {
//		List<PtModuleVO> moduleVOList = new ArrayList<PtModuleVO>();
//		for (PtGroupVO group : groups) {
//			PtModuleVO _tmp_module = (PtModuleVO) module.clone();
//			_tmp_module.setPk_group(group.getPk_group());
//			_tmp_module.setParentid(pk);
//			_tmp_module.setPk_module(null);
//			moduleVOList.add(_tmp_module);
//		}
//		return moduleVOList.toArray(new PtModuleVO[0]);
//	}
}
