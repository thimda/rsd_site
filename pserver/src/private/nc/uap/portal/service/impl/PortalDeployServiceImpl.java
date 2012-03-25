package nc.uap.portal.service.impl;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.deploy.impl.PtDisplayDepoly;
import nc.uap.portal.deploy.impl.PtModuleDepoly;
import nc.uap.portal.deploy.impl.PtPageDeploy;
import nc.uap.portal.deploy.impl.PtPluginDeploy;
import nc.uap.portal.deploy.impl.PtPortletDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPortalDeployService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PortletDataWrap;
import nc.uap.portal.vo.PtModuleAppVO;


public class PortalDeployServiceImpl implements IPortalDeployService {

	@Override
	public void deployAll() {
		LfwLogger.info("Searching portal modules ...");
		PortalDeployDefinition[] modules = PortalServiceUtil.getPortalSpecService().getPortalModules();
		LfwLogger.info("Portal modules:[");
		for (int i = 0; i < modules.length; i++) {
			LfwLogger.info("'");
			LfwLogger.info(modules[i].getModule());
			LfwLogger.info("'");
			if (i != modules.length - 1)
				LfwLogger.info(",");
		}
		LfwLogger.info("]");

		LfwLogger.info("Calculating dependences ...");
		for (int i = 0; i < modules.length; i++) {
			PortalDeployDefinition define = modules[i];
			LfwLogger.info("Loading module '" + define.getModule() + "' ...");
			try {
				deployModule(define);
				LfwLogger.info("Module '" + define.getModule() + "' is successfully Loaded");
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
				LfwLogger.info("Module '" + define.getModule() + "'  load fail.Error message:"+e.getMessage());
			}
		}
	}

	@Override
	public void deployModule(PortalDeployDefinition define) {
			setModuleDef(define);
			new PtPageDeploy().deploy(define);
			new PtPortletDeploy().deploy(define);
			new PtPluginDeploy().deploy(define);
			new PtDisplayDepoly().deploy(define);
			new PtModuleDepoly().deploy(define);
	}

	@Override
	public void updateModule(PortalDeployDefinition define) throws PortletContainerException {

	}
	/**
	 * 存储模块模块信息失败
	 * @param define
	 */
	public void setModuleDef(PortalDeployDefinition define) { 
 		ModuleApplication ma = PortletDataWrap.getModuleApplication(define);
		String app = JaxbMarshalFactory.newIns().decodeXML(ma);
		String module = define.getModule();
		PtBaseDAO dao = new PtBaseDAO();
		boolean modify = false;
		try {
			PtModuleAppVO moduleapp = new PtModuleAppVO();
			List<PtModuleAppVO> list = (List<PtModuleAppVO>) dao.retrieveByClause(PtModuleAppVO.class, " module ='" + module + "' ");
			if(list != null && !list.isEmpty()){
				moduleapp = list.get(0);
				modify = true;
			}
			
			moduleapp.setModule(module);
			moduleapp.setDefaultns(ma.getDefaultNameSpace());
			moduleapp.setAppsdef(app);
			if(modify)
				dao.updateVO(moduleapp);
			else
				dao.insertVO(moduleapp);
		} catch (DAOException e) {
			LfwLogger.error("存储模块模块信息失败",e);
		}
	}
}
