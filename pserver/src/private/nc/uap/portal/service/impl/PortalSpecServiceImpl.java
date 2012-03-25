package nc.uap.portal.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.container.om.ContainerRuntimeOption;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.impl.PtDisplayDepoly;
import nc.uap.portal.deploy.impl.PtLookAndFeelDeploy;
import nc.uap.portal.deploy.impl.PtModuleDepoly;
import nc.uap.portal.deploy.impl.PtPageDeploy;
import nc.uap.portal.deploy.impl.PtPluginDeploy;
import nc.uap.portal.deploy.impl.PtPortletDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.plugins.PluginDefParser;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtPlugin;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPortalSpecService;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.service.itf.IPtPortletRegistryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PmlUtil;
import nc.uap.portal.util.PortalModuleUtil;
import nc.uap.portal.util.XmlUtil;
import nc.uap.portal.vo.PtModuleVO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


public class PortalSpecServiceImpl implements IPortalSpecService {
 
	@Override
	public PortalDeployDefinition[] getPortalModules() {
		List<PortalDeployDefinition> moduleList = new ArrayList<PortalDeployDefinition>();
		String dir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		File f = new File(dir);
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; i++) { 
			File mDir = fs[i];
			if (mDir.isDirectory()) {
				File specDir = new File(mDir.getAbsolutePath() + "/portalspec");
				if (specDir.exists() && specDir.isDirectory()) {
					File specFile = new File(specDir.getAbsolutePath() + "/portal.xml");
					if (specFile.exists() && specFile.isFile()) {
						PortalDeployDefinition definition = parseModule(mDir.getAbsolutePath());
						if (definition != null)
							moduleList.add(definition);
					}
				}
			}
		}
		
		try {
			return PortalModuleUtil.sortDefinition(moduleList);
		} catch (PortalServiceException e) {
			LfwLogger.info("Error:"+e.getMessage());
			return new PortalDeployDefinition[0] ;
		}
	}

	@Override
	public PortalDeployDefinition parseModule(String modulePath) {
		LfwLogger.info("Parsing modules in directory '" + modulePath + "'");
		try {
			PortalDeployDefinition define = new PortalDeployDefinition();
			PortalModule pm = parsePortalModule(modulePath);
			String moduleName = pm.getModule();
			define.setModule(moduleName);
			define.setPortalModule(pm);
			
			/*Portal*/
			PortletApplicationDefinition pad = parsePortlets(modulePath, define, moduleName);
			define.setPortletDefineModule(pad);

			/* 加入Portal页面 */
			Page[] pages = PmlUtil.getPages(modulePath + "/portalspec/pml");
			if (pages != null) {
				define.setPages(pages);
			}
			
		 
			
			/*Portlet分组信息*/
			Reader displayins = null;
			try {
				File f = new File(modulePath+ "/portalspec/display.xml");
				if(f.exists()){
					String xmlText = FileUtils.readFileToString(f, "UTF-8");
					displayins = new StringReader(xmlText);
	 				Display display = XmlUtil.readDisplay(displayins) ;
	 				if(display != null)
	 					define.setDisplay(display);
				}
			} catch (JAXBException e) {
				LfwLogger.error(e.getMessage(),e);
			}catch (FileNotFoundException e) {
				LfwLogger.error(e.getMessage(),e);
			}finally{
				IOUtils.closeQuietly(displayins);
			}
			
			/*Portal插件*/
			PtPlugin  plugin= PluginDefParser.reader(modulePath+ "/portalspec/plugin.xml");
			define.setPlugin(plugin);
			
//			/*skin信息*/
//			PtSkinVo[] ptSkinVo =  parseSkinVo(modulePath,moduleName);
//			define.setPtSkinVo(ptSkinVo);
			
			/*将模块的实际路径放入缓存中*/ 
			IPtPortletRegistryService portletReg = PortalServiceUtil.getPortletRegistryService();
			File moduleDir = new File(modulePath);
			String moduleFolder = moduleDir.getName();
			portletReg.addModuleFolder(moduleName, moduleFolder);
			return define;
		} catch (Throwable e) {
			LfwLogger.error("error occurred while parsing module:" + modulePath, e);
			return null;
		}
	}

	private PortletApplicationDefinition parsePortlets(String modulePath, PortalDeployDefinition define,
			String moduleName) throws FileNotFoundException, IOException {
		FileInputStream input = null;
		try {
			File f = new File(modulePath + "/portalspec/portlet.xml");
			if(!f.exists()) return null;
			input = new FileInputStream(f);
			PortletApplicationDefinition  pad = (PortletApplicationDefinition  ) PortalServiceUtil.getPortletAppDescriptorService().read(moduleName,
					input);
			List<PortletDefinition> pList = pad.getPortlets();
			Iterator<PortletDefinition> pit = pList.iterator();
			while (pit.hasNext()) {
				PortletDefinition p = pit.next();
				ContainerRuntimeOption  cr = new ContainerRuntimeOption ();
				cr.setName(ContainerRuntimeOption.MODULE);
				cr.addValue(moduleName);
				p.getContainerRuntimeOptions().add(cr);
			}
			return pad;
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
		}
	}

	private PortalModule parsePortalModule(String modulePath) throws PortalServiceException {
		PortalModule pm = PortalModuleUtil.parsePortalModule(new File(modulePath + "/portalspec/portal.xml"));
		return pm;
	}
	

	@Override
	public List<PortletDefinition> getAllPortlet(String projectPath,String projectModuleName) {
		PortletApplicationDefinition portletApp = getPortletApp(projectPath,projectModuleName);
		if(portletApp == null)
			return null;
		List<PortletDefinition> list =  (List<PortletDefinition>) portletApp.getPortlets();
		return list;
	}

	 
	
	@Override
	public void deployManagerApps(String projectModuleName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;		
		PortalDeployDefinition define = parseModule(runtimeDir);
		new PtModuleDepoly().deploy(define);
	}
	
	@Override
	public void deleteManagerApps(String projectPath,String projectModuleName,String id){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/manager"; 
    	File file = new File(filePath + "/portal" + id + ".xml");
    	file.delete();
	}

	@Override
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName){
		File file = new  File(getPortalSpecPath(projectPath,projectModuleName) + "/portlet.xml");
		try {
			String xml = FileUtils.readFileToString(file, "UTF-8");
			PortletApplicationDefinition portletApp = JaxbMarshalFactory.newIns().encodeXML(PortletApplicationDefinition.class, xml);
			return portletApp;
    	}catch(IOException e){
    		LfwLogger.error(e.getMessage(),e);
    		return null; 
    	}
	}
	
	@Override
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName,String portletAppText){
		PortletApplicationDefinition portletApp = JaxbMarshalFactory.newIns().encodeXML(PortletApplicationDefinition.class, portletAppText);
		return portletApp;
	}

	@Override
	public void savePortletAppToXml(String projectPath,String projectModuleName, PortletApplicationDefinition portletApp){
//		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;
		String filePath = getPortalSpecPath(projectPath,projectModuleName);
		File f = new File(filePath);
		if(!f.exists())
			f.mkdirs();
		File file = new File(filePath + "/portlet.xml");
    	String d = JaxbMarshalFactory.newIns().decodeXML(portletApp);
    	try {
    		FileUtils.writeStringToFile(file, d, "UTF-8");

    	} catch (Exception e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
    	}
	}
	
	@Override
	public void deployPortletApp(String projectModuleName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;		
		PortalDeployDefinition define = parseModule(runtimeDir);
		new PtPortletDeploy().deploy(define);
		PortalCacheManager.notify(CacheKeys.PORTLETS_CACHE, CacheKeys.USER_DIY_PORTLETS_CACHE);
		PortalCacheManager.notify(CacheKeys.PORTLETS_CACHE, CacheKeys.GROUP_PORTLETS_CACHE);
		PortalCacheManager.notify(CacheKeys.PORTLETS_CACHE, CacheKeys.SYSTEM_PORTLETS_CACHE);
	}
	
	@Override
    public PortalModule getPortal(String projectPath,String projectModuleName){
		try {
			PortalModule pm = PortalModuleUtil.parsePortalModule(new File(getPortalSpecPath(projectPath,projectModuleName) + "/portal.xml"));
			return pm;
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return null;
    }

	@Override
    public void savePortalToXml(String projectPath,String projectModuleName,PortalModule portalModule){
		 
    	String filePath = getPortalSpecPath(projectPath,projectModuleName);
		File f = new File(filePath);
		if(!f.exists())
			f.mkdirs();
		File file = new File(filePath + File.separatorChar + "portal.xml");
		String pt = JaxbMarshalFactory.newIns().decodeXML(portalModule);
		try {
			FileUtils.writeStringToFile(file, pt, "UTF-8");
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e);
		}
    }
    
	@Override
	public void deployPortal(String projectModuleName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;		
		PortalDeployDefinition define = parseModule(runtimeDir);
		new PtModuleDepoly().deploy(define);
	}
	
	@Override
    public PtPlugin getPtPlugin(String projectPath,String projectModuleName){
    	String file = getPortalSpecPath(projectPath,projectModuleName) + "/plugin.xml";
    	File f = new File(file);
    	try {
    		String xml = FileUtils.readFileToString(f, "UTF-8");
			PtPlugin ptPlugin = JaxbMarshalFactory.newIns().encodeXML(PtPlugin.class, xml);
			return ptPlugin;
    	}catch(IOException e){
    		LfwLogger.error(e.getMessage(),e);
    		return null; 
    	}
    }

	@Override
    public void savePtPluginToXml(String projectPath,String projectModuleName,PtPlugin ptPlugin){
		String filePath = getPortalSpecPath(projectPath,projectModuleName);
		File f = new File(filePath);
		if(!f.exists())
			f.mkdirs();
		File file = new File(filePath + "/plugin.xml");
		String pt = JaxbMarshalFactory.newIns().decodeXML(ptPlugin);
		try {
			FileUtils.writeStringToFile(file, pt, "UTF-8");

		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e);
		}
    }
	
	@Override
	public void deployPtPlugin(String projectModuleName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;		
		PortalDeployDefinition define = parseModule(runtimeDir);
		new PtPluginDeploy().deploy(define);
		PluginManager.newIns().refresh();
	}

	@Override
    public Display getDisplay(String projectPath,String projectModuleName){
    	InputStream is = null;
    	String file = getPortalSpecPath(projectPath,projectModuleName) + "/display.xml";
    	try {
    		is = new FileInputStream(file);
    		BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
    		StringBuffer buffer = new StringBuffer();
    		String line = "";
    		while ((line = in.readLine()) != null){
    			buffer.append(line);
    		}
    		String xml = buffer.toString();
    		Display display= JaxbMarshalFactory.newIns().encodeXML(Display.class, xml);
    		return display;
    	}catch(IOException e){
    		LfwLogger.error(e.getMessage(),e);
    		return null;
    	}
    }
    
	@Override
	public List<Display> getAllDisplays(String projectPath,String projectModuleName){
		
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		
		List<Display> displays = new ArrayList<Display>();
    	InputStream is = null;
    	//取项目自身displays
    	String displayFile = getPortalSpecPath(projectPath,projectModuleName) + "/display.xml";
    	try {
    		is = new FileInputStream(displayFile);
    		BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
    		StringBuffer buffer = new StringBuffer();
    		String line = "";
    		while ((line = in.readLine()) != null){
    			buffer.append(line);
    		}
    		String xml = buffer.toString();
    		Display display= JaxbMarshalFactory.newIns().encodeXML(Display.class, xml);
    		displays.add(display);
    	}catch(IOException e){
    		LfwLogger.error(e.getMessage(),e);
    	}
		
		//获得所有依赖的module
		List<String> modules = new ArrayList<String>();
		PortalModule portalModule = getPortal(projectPath, projectModuleName);
		getDependModules(modules, portalModule, projectModuleName,projectPath,runtimeDir);
		for(String module : modules){
			String file =  runtimeDir + "/"+module+"/portalspec/display.xml";
			try {
				is = new FileInputStream(file);
				BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				StringBuffer buffer = new StringBuffer();
				String line = "";
				while ((line = in.readLine()) != null){
					buffer.append(line);
				}
				String xml = buffer.toString();
				Display display= JaxbMarshalFactory.newIns().encodeXML(Display.class, xml);
				displays.add(display);
			}catch(IOException e){
				LfwLogger.error(e.getMessage(),e);
			}
		}
		return displays;
	}
	
	@Override
    public void saveDisplayToXml(String projectPath,String projectModuleName,Display display){
		String filePath = getPortalSpecPath(projectPath,projectModuleName);
		File f = new File(filePath);
    	if(!f.exists())
    		f.mkdirs();
    	File file = new File(filePath + "/display.xml");
    	String d = JaxbMarshalFactory.newIns().decodeXML(display);
    	try {
    		FileUtils.writeStringToFile(file, d, "UTF-8");

    	} catch (Exception e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
    	}
    }

	@Override
	public void deployDisplay(String projectModuleName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;		
		PortalDeployDefinition define = parseModule(runtimeDir);
		new PtDisplayDepoly().deploy(define);
		
		PortalCacheManager.clearCache(CacheKeys.PORTLETS_CACHE, CacheKeys.USER_DIY_PORTLETS_CACHE);
		PortalCacheManager.clearCache(CacheKeys.PORTLETS_CACHE, CacheKeys.GROUP_PORTLETS_CACHE);
		PortalCacheManager.clearCache(CacheKeys.PORTLETS_CACHE, CacheKeys.SYSTEM_PORTLETS_CACHE);
	}

	@Override
	public Page[] getAllPages(String projectPath,String projectModuleName){
		String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml";
		return PmlUtil.getPages(filePath);
	}

	@Override
    public Page getPage(String projectPath,String projectModuleName,String PageName){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml"; 
    	String fileName =  PageName;
    	File f = new File(filePath + "/" + fileName + ".pml");
		try {
			return PmlUtil.parser(f);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
			return null;
		}
    }
	
	@Override
    public String  pageToString(Page page){
    	return page.toXml();
    }

	@Override
    public Page stringToPage(String xml){
    	Page page= JaxbMarshalFactory.newIns().encodeXML(Page.class, xml);
    	return (Page)page;
    }
    
	@Override
    public void  savePageToXml(String projectPath,String projectModuleName,String fileName,String xml){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml"; 
    	File file = new File(filePath + "/" + fileName + ".pml");
    	try {
    		FileUtils.writeStringToFile(file, xml, "UTF-8");
    		
    	} catch (Exception e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
    	}
    }

	@Override
    public void  savePageToXml(String projectPath,String projectModuleName,Page page){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml";
    	File f = new File(filePath);
    	if (!f.exists()){
    		f.mkdirs();
    	}
    	String fileName = page.getPagename() + ".pml";
    	File file = new File(filePath + "/" + fileName);
    	String d = page.toXml();
    	try {
    		FileUtils.writeStringToFile(file, d, "UTF-8");

    	} catch (Exception e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
    	}
    }
	
	@Override
	public void deployPage(String projectModuleName,String pageName){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;
		try{
			/*从数据库中删除page,重新部署*/
	    	IPtPortalPageService prs = NCLocator.getInstance().lookup(IPtPortalPageService.class);
//	    	PortalServiceUtil.getPageService();
			prs.delete(projectModuleName, pageName);
			
			PortalDeployDefinition define = parseModule(runtimeDir);
			new PtPageDeploy().deploy(define);
			
			PortalCacheManager.clearCache(CacheKeys.PORTTAL_PAGES_CACHE, CacheKeys.GROUP_PAGES_CACHE);
		}	
		catch(Exception e){
			LfwLogger.error(e.getMessage(),e);
		}
	}

	@Override
    public void deletePage(String projectPath,String projectModuleName,String pageName){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/pml"; 
    	File file = new File(filePath + "/" + pageName + ".pml");
    	file.delete();
    }
    
	@Override
    public LookAndFeel getLookAndFeel(String projectPath,String projectModuleName){
    	String file = projectPath + "/web/WEB-INF/conf/look-and-feel.xml";
    	File f = new File(file);
		try {
			String xml = FileUtils.readFileToString(f, "UTF-8");
			LookAndFeel lookAndFeel= JaxbMarshalFactory.newIns().encodeXML(LookAndFeel.class, xml);
			return lookAndFeel;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
			return null;
		}
    }

	@Override
    public void saveLookAndFeelToXml(String projectPath,String projectModuleName,LookAndFeel lookAndFeel){
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;
    	String filePath = projectPath + "/web/WEB-INF/conf";
    	File f = new File(filePath);
    	if(!f.exists())
    		f.mkdirs();
    	File file = new File(filePath + "/look-and-feel.xml");
    	String d = JaxbMarshalFactory.newIns().decodeXML(lookAndFeel);
    	try {
    		if(!file.exists())
    			file.createNewFile();
    		FileUtils.writeStringToFile(file, d, "UTF-8");
    		
    		/* 重新部署*/
    		File runtimePath = new File(runtimeDir+"/portalspec");
    		if (runtimePath.exists()){
    			deployLookAndFeel();
    		}
    	} catch (Exception e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
    	}
    }

	@Override
	public void deployLookAndFeel(){
		new PtLookAndFeelDeploy().deployLookAndFeel();
	}
    
	@Override
    public List<Skin> getSkins(String projectPath,String projectModuleName,String type){
    	List<Skin> skins = new ArrayList<Skin>();
    	String skinDirs = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/"; 
    	
		File f = new File(skinDirs);
		if(f.exists() && f.isDirectory()){
			addSkins(skins,f,projectModuleName,type);
		}
		return skins;
    }

	@Override
	public List<Skin> getAllSkins(String projectPath,String projectModuleName,String type){
		List<Skin> skins = new ArrayList<Skin>();
		String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		
		//获得所有依赖的module
		List<String> modules = new ArrayList<String>();
		modules.add(projectModuleName);
		PortalModule portalModule = getPortal(projectPath, projectModuleName);
		getDependModules(modules, portalModule, projectModuleName,projectPath,runtimeDir);
		
		for(String module : modules){
			String skinDirs = runtimeDir + "/"+module+"/portalspec/ftl/portaldefine/skin/";
			File f = new File(skinDirs);
			if(f.exists() && f.isDirectory()){
				addSkins(skins,f,module,type);
			}
		}
		return skins;
	}
	
	@Override
    public SkinDescription getSkinDescription(String projectPath,String projectModuleName,String type,String themeId){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/" + themeId + "/" + type;
    	File f = new File(filePath + "/description.xml");
    	try {
    		String xml = FileUtils.readFileToString(f, "UTF-8");
    		SkinDescription skinDescription = JaxbMarshalFactory.newIns().encodeXML(SkinDescription.class, xml);
    		return skinDescription;
    	}catch(IOException e){
    		LfwLogger.error(e.getMessage(),e);
    		return null;
    	}
    }
    
	@Override
    public void saveSkinDescription(String projectPath,String projectModuleName,String type,String themeId,SkinDescription skinDescription){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/" + themeId + "/" + type;
    	File f = new File(filePath);
    	if(!f.exists())
    		f.mkdirs();
    	File file = new File(filePath + "/description.xml");
    	String xml = JaxbMarshalFactory.newIns().decodeXML(skinDescription);
    	try {
    		FileUtils.writeStringToFile(file, xml, "UTF-8");
    	}catch(Exception e){
    		LfwLogger.error(e.getMessage(),e);
    	}
    }    
    
	@Override
    public void createSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName,String fileText){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/" + themeId + "/" + type;
    	File f = new File(filePath);
    	if(!f.exists())
    		f.mkdirs();
    	File file = new File(filePath + "/" + fileName);
    	try {
    		FileUtils.writeStringToFile(file, fileText, "UTF-8");
    	}catch(Exception e){
    		LfwLogger.error(e.getMessage(),e);
    	}
    }
    
	@Override
    public void deleteSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/" + themeId + "/" + type; 
    	File file = new File(filePath + "/" + fileName);
    	try {
	    	file.delete();
    	}catch(Exception e){
    		LfwLogger.error(e.getMessage(),e);
    	}
    }
	
	@Override
	public void deploySkin(String projectModuleName){
		try {
			new PtLookAndFeelDeploy().scanSkins(projectModuleName);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
    
	@Override
    public void createThemeFolder(String projectPath,String projectModuleName,String themeId){
    	String runtimeDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR + "/" + projectModuleName;
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) +  "/ftl/portaldefine/skin/"+themeId;
    	File file = new File(filePath);
		file.mkdir(); 
		
    	File layoutFile = new File(filePath + "/layout");
    	layoutFile.mkdir();
    	
    	File pageFile = new File(filePath + "/page");
    	pageFile.mkdir();
    	
    	File portletFile = new File(filePath + "/portlet");
    	portletFile.mkdir();
    	
    	/* 同步到portalhome下 */
		File runtimePath = new File(runtimeDir+"/portalspec");
		if (runtimePath.exists()){
	    	String themePath = runtimeDir + "/portalspec/ftl/portaldefine/skin/"+themeId;
	    	file = new File(themePath);
			file.mkdir(); 
			
	    	layoutFile = new File(themePath + "/layout");
	    	layoutFile.mkdir();
	    	
	    	pageFile = new File(themePath + "/page");
	    	pageFile.mkdir();
	    	
	    	portletFile = new File(themePath + "/portlet");
	    	portletFile.mkdir();
		}	
    }    
    
	@Override
    public void deleteThemeFolder(String projectPath,String projectModuleName,String themeId){
    	String filePath = getPortalSpecPath(projectPath,projectModuleName) + "/ftl/portaldefine/skin/"+themeId; 
    	File file = new File(filePath);
    	try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
    		LfwLogger.error(e.getMessage(),e);
    		throw new LfwRuntimeException(e);
		}
    }    
    
	/**
	 * 获得所有依赖的module
	 */
    private void getDependModules(List<String> modules, PortalModule portalModule, String projectModuleName,String projectPath,String runtimeDir){
//		PortalModule portalModule = getPortal(projectPath,projectModuleName); 
//		if (portalModule == null){
//			try {
//				portalModule = PortalModuleUtil.parsePortalModule(new File(runtimeDir+ "/" + projectModuleName + "/portalspec/portal.xml"));
//			} catch (PortalServiceException e) {
//				LfwLogger.error(e.getMessage(),e);
//			}
//		}
		if (portalModule != null){
			String dependStr = portalModule.getDepends();
			if(dependStr != null){
				String[] depends = dependStr.split(",");
				for (int i = 0; i < depends.length; i ++){
					modules.add(depends[i]);
					PortalModule pm = getPortalModuleById(depends[i]);
					if(pm == null)
						throw new LfwRuntimeException("没有找到依赖组件:" + depends[i]);
					getDependModules(modules, pm, depends[i],projectPath,runtimeDir);
				}
			}
		}
    }
    
    /**
     * 加载指定目录下的skin
     * @param skins
     * @param dir
     * @param module
     * @param type
     */
	private void addSkins(List<Skin> skins,File dir,String module,String type){
		File[] skinFiles = dir.listFiles();
		for(File skinFile : skinFiles){
			if(skinFile.isDirectory()){
				if ("page".equals(type))
					scanf(skins,module,skinFile,PortalEnv.TYPE_PAGE);
				else if ("layout".equals(type))
					scanf(skins,module,skinFile,PortalEnv.TYPE_LAYOUT);
				else if ("portlet".equals(type))
					scanf(skins,module,skinFile,PortalEnv.TYPE_PORTLET);
			}
		}
	}
	
    /**
	 * 扫描文件
	 * @param dir
	 * @param tp
	 * @return
	 */
	private void scanf(List<Skin> skins, String module,File dir,String tp){
    	String file = dir + "/" + tp + "/description.xml";
    	File f = new File(file);
		try {
			String xml = FileUtils.readFileToString(f, "UTF-8");
			SkinDescription skinDescription= JaxbMarshalFactory.newIns().encodeXML(SkinDescription.class, xml);
			for (Skin skin: skinDescription.getSkin()){
				skins.add(skin);
			}
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
	
	private String getPortalSpecPath(String projectPath,String projectModuleName){
		return projectPath +"/src/portalspec/" + projectModuleName +  "/portalspec";
	}

	@Override
	public PortalModule getPortalModuleById(String moduleid) {
		try {
			PtModuleVO[] vos = CRUDHelper.getCRUDService().queryVOs(" moduleid = '" + moduleid + "'", PtModuleVO.class, null, null, null);
			if(vos != null && vos.length > 0){
				PtModuleVO vo = vos[0];
				PortalModule module = new PortalModule();
				module.setModule(vo.getModuleid());
				module.setDepends(vo.getDependentid());
				return module;
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage());
		}
		return null;
	}
    
}
