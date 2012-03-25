package nc.uap.portal.service.itf;

import java.io.File;
import java.util.List;
import java.util.Map;

import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.plugins.model.PtPlugin;

/**
 * 
 * Portal相关数据提供服务
 * 
 *@since 1.6
 */
public interface IPortalDesignDataProvider {

	/**
	 * 获取项目中所有的Portlet
	 * 
	 * @param projectPath		项目路径
	 * @param projectModuleName		项目module名称 
	 * @return	项目中的所有portlet列表
	 */
	public List<PortletDefinition> getAllPortlets(String projectPath,String projectModuleName);
	
 
	/**
	 * 删除ManagerApps
	 * 
	 * @param projectPath	项目路径
	 * @param projectModuleName	项目module名称
	 * @param id	ManagerApps的id
	 */
	public void deleteManagerApps(String projectPath,String projectModuleName,String id);
	
	public PortalDeployDefinition getPortalModule(String projectPath);
	
	public PortalModule getPortalModule(File file);
	
	/**
	 * 获取项目中的 PortletApplicationDefinition
	 * 
	 * @param projectPath 	项目路径
	 * @param projectModuleName 项目module名称
	 * @return  项目的PortletApplicationDefinition
	 */
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName);
	
	/**
	 * 获取项目中的 PortletApplicationDefinition
	 * 
	 * @param projectPath 	项目路径
	 * @param projectModuleName 项目module名称
	 * @param portletAppText	PortletApplicationDefinition字符串
	 * @return  项目的PortletApplicationDefinition
	 */
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName,String portletAppText);
	
	/**
	 * 保存PortletApplicationDefinition
	 * 
	 * @param projectPath	项目路径
	 * @param projectModuleName	项目module名称
	 * @param portletApp PortletApplicationDefinition对象
	 */
	public void savePortletAppToXml(String projectPath,String projectModuleName, PortletApplicationDefinition portletApp);

	/**
	 * 获取项目中的 PortalModule 配置
	 * 
	 * @param projectPath  	项目路径
	 * @param projectModuleName 	项目module名称
	 * @return	项目PortalModule对象
	 */
    public PortalModule getPortal(String projectPath,String projectModuleName);

    /**
     * 保存项目PortalModule
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param portalModule	项目PortalModule对象
     */
    public void savePortalToXml(String projectPath,String projectModuleName,PortalModule portalModule);
    
    /**
     * 获取项目插件
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @return 项目的PtPlugin	
     */
    public PtPlugin getPtPlugin(String projectPath,String projectModuleName);

    /**
     * 保存项目插件
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @return PtPlugin 对象	
     */
    public void savePtPluginToXml(String projectPath,String projectModuleName,PtPlugin ptPlugin);

    /**
     * 获取项目portlet分类
     * 
     * @param projectPath 项目路径
     * @param projectModuleName 项目module名称
     * @return	项目portlet分类
     */
    public Display getDisplay(String projectPath,String projectModuleName);
    
    /**
     * 获取项目portlet分类，包括依赖模块的
     * 
     * @param projectPath 项目路径
     * @param projectModuleName 项目module名称
     * @return	项目portlet分类
     */
    public List<Display> getAllDisplays(String projectPath,String projectModuleName);
    
    /**
     * 保存项目portlet分类
     * 
     * @param projectPath	项目路径
     * @param projectModuleName 项目module名称
     * @param display	Display对象
     */
    public void saveDisplayToXml(String projectPath,String projectModuleName,Display display);
    
    /**
     * 获取项目Page对象列表
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @return	项目Page对象列表
     */
    public Page[] getAllPages(String projectPath,String projectModuleName);

    /**
     * 获取项目Page对象
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param PageName	Page名称
     * @return	Page对象
     */
    public Page getPage(String projectPath,String projectModuleName,String PageName);

    /**
     * Page对象转换成XML字符串
     * 
     * @param page	Page对象
     * @return	Page对象转换成的XML字符串
     */
    public String  pageToString(Page page);

    /**
     * XML字符串转换成Page对象
     * 
     * @param xml	
     * @return	Page对象
     */
    public Page  stringToPage(String xml);

    /**
     * 保存Page对象
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param fileName	Page名称
     * @param xml	Page的XML字符串
     */
    public void  savePageToXml(String projectPath,String projectModuleName,String fileName,String xml);

    /**
     * 保存Page对象
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param page	Page对象
     */
    public void  savePageToXml(String projectPath,String projectModuleName,Page page);
    
    /**
     * 删除项目中的Page
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param pageName	Page名称
     */
    public void deletePage(String projectPath,String projectModuleName,String pageName);
    
    /**
     * 获取项目LookAndFeel对象
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @return	LookAndFeel对象
     */
    public LookAndFeel getLookAndFeel(String projectPath,String projectModuleName);

    /**
     * 保存LookAndFeel
     * 
     * @param projectPath	项目路径
     * @param projectModuleName 项目module名称
     * @param lookAndFeel	LookAndFeel对象
     */
    public void saveLookAndFeelToXml(String projectPath,String projectModuleName,LookAndFeel lookAndFeel);
    
    /**
     * 获取样式列表
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @return
     */
    public List<Skin> getSkins(String projectPath,String projectModuleName,String type);

    /**
     * 获取样式列表,包括依赖模块中的
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @return
     */
    public List<Skin> getAllSkins(String projectPath,String projectModuleName,String type);
    
    /**
     * 获取样式描述文件
     * 
     * @param projectPath 项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @param themeId	主题id
     * @return	样式描述文件
     */
    public SkinDescription getSkinDescription(String projectPath,String projectModuleName,String type,String themeId);
    
    /**
     * 保存样式描述文件
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @param themeId	主题id
     * @param skinDescription	样式描述文件
     */
    public void saveSkinDescription(String projectPath,String projectModuleName,String type,String themeId,SkinDescription skinDescription);

    /**
     * 创建样式文件
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @param themeId	主题id
     * @param fileName	样式名称
     * @param fileText	样式XML字符串
     */
    public void createSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName,String fileText);

    /**
     * 删除样式文件
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param type	样式分类
     * @param themeId	主题id
     * @param fileName	样式名称
     */
    public void deleteSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName);
    
    /**
     * 创建主题文件夹
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param themeId	主题id
     */
    public void createThemeFolder(String projectPath,String projectModuleName,String themeId);
    
    /**
     * 删除主题文件夹
     * 
     * @param projectPath	项目路径
     * @param projectModuleName	项目module名称
     * @param themeId	主题id
     */
    public void deleteThemeFolder(String projectPath,String projectModuleName,String themeId);
    
    /**
     * 获取功能页面Map 
     * 
     * @param projPaths	项目路径
     * @return 功能页面Map	
     */
    public Map<String, String>[] getPageNames(String[] projectPath);
    
    /**
     * 部署PortalModule
     * 
     * @param projectModuleName   要部署的模块名称
     */
    public void deployPortal(String projectModuleName);
    /**
     * 部署ManagerApps
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deployManagerApps(String projectModuleName);
	
    /**
     * 部署PortletApp
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deployPortletApp(String projectModuleName);
	
    /**
     * 部署PtPlugin
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deployPtPlugin(String projectModuleName);
	
    /**
     * 部署Display
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deployDisplay(String projectModuleName);
	
    /**
     * 部署Page
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deployPage(String projectModuleName, String pageName);
	
    /**
     * 部署主题
     * 
     */
	public void deployLookAndFeel();
	
    /**
     * 部署样式 
     * 
     * @param projectModuleName   要部署的模块名称
     */
	public void deploySkin(String projectModuleName);

}
