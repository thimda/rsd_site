package nc.uap.portal.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.design.noexport.IFuncRegisterService;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.plugins.model.PtPlugin;
import nc.uap.portal.service.itf.IPortalDesignDataProvider;
import nc.uap.portal.service.itf.IPortalSpecService;
import nc.uap.portal.util.PortalModuleUtil;


public class PortalDesignDataProviderImpl implements IPortalDesignDataProvider{

	private IPortalSpecService getIPortalService(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		//props.setProperty(NCLocator.TARGET_MODULE, "portal");
		IPortalSpecService portalService = NCLocator.getInstance(props).lookup(IPortalSpecService.class);
		return portalService;
	}

	private IFuncRegisterService getFuncRegisterService(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		//props.setProperty(NCLocator.TARGET_MODULE, "uapweb");
		IFuncRegisterService service = NCLocator.getInstance(props).lookup(IFuncRegisterService.class);
		return service;
	}
	
	@Override
	public List<PortletDefinition> getAllPortlets(String projectPath,String projectModuleName) {
		IPortalSpecService portalService = getIPortalService();
		return portalService.getAllPortlet(projectPath,projectModuleName);
	}


	@Override
	public void deleteManagerApps(String projectPath,String projectModuleName,String id){
		IPortalSpecService portalService = getIPortalService();
		portalService.deleteManagerApps(projectPath, projectModuleName, id);
	}

	@Override
	public PortalDeployDefinition getPortalModule(String projectPath){
		IPortalSpecService portalService = getIPortalService();
		return portalService.parseModule(projectPath);
	}

	@Override
	public PortalModule getPortalModule(File file) {
		try {
			return PortalModuleUtil.parsePortalModule(file);
		} catch (PortalServiceException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("解析PortalModule出现异常," + file.getName());
		}
	}
	
	@Override
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName){
		IPortalSpecService portalService = getIPortalService();
		return portalService.getPortletApp(projectPath,projectModuleName);
	}
	
	@Override
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName,String portletAppText){
		IPortalSpecService portalService = getIPortalService();
		return portalService.getPortletApp(projectPath,projectModuleName,portletAppText);
	}
	
	@Override
	public void savePortletAppToXml(String projectPath,String projectModuleName, PortletApplicationDefinition portletApp){
		IPortalSpecService portalService = getIPortalService();
		portalService.savePortletAppToXml(projectPath,projectModuleName, portletApp);
		//IPortalPortletService ppProvider = NCLocator.getInstance(props).lookup(IPortalPortletService.class);
		//ppProvider.savePortletAppToXml(rootPath, filePath, fileName, portletApp);
	}	

	/**
	 *读取portal.xml文件
	 */
	@Override
    public PortalModule getPortal(String projectPath,String projectModuleName){
		IPortalSpecService portalService = getIPortalService();
    	return portalService.getPortal(projectPath,projectModuleName);
    }

    /**
     *写入portal.xml文件
     */
	@Override
    public void savePortalToXml(String projectPath,String projectModuleName,PortalModule portalModule){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.savePortalToXml(projectPath, projectModuleName, portalModule);
    }
    
	/**
	 *读取plugin.xml文件
	 */
	@Override
    public PtPlugin getPtPlugin(String projectPath,String projectModuleName){
		IPortalSpecService portalService = getIPortalService();
    	return portalService.getPtPlugin(projectPath,projectModuleName);
    }

	/**
	 *写入plugin.xml文件
	 */
	@Override
    public void savePtPluginToXml(String projectPath,String projectModuleName,PtPlugin ptPlugin){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.savePtPluginToXml(projectPath,projectModuleName,ptPlugin);
    }

    /**
     *读取display.xml文件
     */
	@Override
    public Display getDisplay(String projectPath,String projectModuleName){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getDisplay(projectPath,projectModuleName);
    }

	@Override
    public List<Display> getAllDisplays(String projectPath,String projectModuleName){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getAllDisplays(projectPath,projectModuleName);
	}
	
    /**
     *写入display.xml文件
     */
	@Override
    public void saveDisplayToXml(String projectPath,String projectModuleName,Display display){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.saveDisplayToXml(projectPath,projectModuleName,display);
    }

    /**
     * 读取page文件表列
     */
	@Override
	public Page[] getAllPages(String projectPath,String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getAllPages(projectPath,projectModuleName);
	}
	
    /**
     * 读取page文件
     */
	@Override
    public Page getPage(String projectPath,String projectModuleName,String PageName){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getPage(projectPath,projectModuleName,PageName);
    }
	
    /**
     * page to string
     */
	@Override
    public String  pageToString(Page page){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.pageToString(page);
    }
	
    /**
     * string to page
     */
	@Override
    public Page stringToPage(String xml){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.stringToPage(xml);
    }

    /**
     * save page
     */
	@Override
    public void  savePageToXml(String projectPath,String projectModuleName,String fileName,String xml){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.savePageToXml(projectPath, projectModuleName,fileName,xml);
    }

	@Override
    public void  savePageToXml(String projectPath,String projectModuleName,Page page){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.savePageToXml(projectPath, projectModuleName,page);
    }

    /**
     * delete page
     */
	@Override
    public void deletePage(String projectPath,String projectModuleName,String pageName){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deletePage(projectPath, projectModuleName,pageName);
    }
    
    /**
     * 读取LookAndFeel
     */
	@Override
    public LookAndFeel getLookAndFeel(String projectPath,String projectModuleName){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getLookAndFeel(projectPath,projectModuleName);
    }

	@Override
    public void saveLookAndFeelToXml(String projectPath,String projectModuleName,LookAndFeel lookAndFeel){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.saveLookAndFeelToXml(projectPath, projectModuleName,lookAndFeel);
    }
    
	@Override
    public List<Skin> getSkins(String projectPath,String projectModuleName,String type){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getSkins(projectPath,projectModuleName,type);
    }
    
	@Override
	public List<Skin> getAllSkins(String projectPath,String projectModuleName,String type){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getAllSkins(projectPath,projectModuleName,type);
	}
	
	@Override
    public SkinDescription getSkinDescription(String projectPath,String projectModuleName,String type,String themeId){
    	IPortalSpecService portalService = getIPortalService();
    	return portalService.getSkinDescription(projectPath,projectModuleName,type,themeId);
    }
    
	@Override
    public void saveSkinDescription(String projectPath,String projectModuleName,String type,String themeId,SkinDescription skinDescription){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.saveSkinDescription(projectPath,projectModuleName,type,themeId,skinDescription);
    }    
    
	@Override
    public void createSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName,String fileText){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.createSkinFile(projectPath,projectModuleName,type,themeId,fileName,fileText);
    }
    
	@Override
    public void deleteSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deleteSkinFile(projectPath, projectModuleName, type, themeId, fileName);
    }
    
	@Override
    public void createThemeFolder(String projectPath,String projectModuleName,String themeId){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.createThemeFolder(projectPath,projectModuleName,themeId);
    }    
    
	@Override
    public void deleteThemeFolder(String projectPath,String projectModuleName,String themeId){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deleteThemeFolder(projectPath,projectModuleName,themeId);
    }    
    
	@Override
	public Map<String, String>[] getPageNames(String[] projPaths){
		IFuncRegisterService service = getFuncRegisterService();
		return service.getPageNames(projPaths);
	}
	
	@Override
    public void deployPortal(String projectModuleName){
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployPortal(projectModuleName);
    }

	@Override
	public void deployDisplay(String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployDisplay(projectModuleName);
	}

	@Override
	public void deployLookAndFeel() {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployLookAndFeel();
	}

	@Override
	public void deployManagerApps(String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployManagerApps(projectModuleName);
	}

	@Override
	public void deployPage(String projectModuleName, String pageName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployPage(projectModuleName,pageName);
	}

	@Override
	public void deployPortletApp(String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployPortletApp(projectModuleName);
	}

	@Override
	public void deployPtPlugin(String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deployPtPlugin(projectModuleName);
	}

	@Override
	public void deploySkin(String projectModuleName) {
    	IPortalSpecService portalService = getIPortalService();
    	portalService.deploySkin(projectModuleName);
	}
}
