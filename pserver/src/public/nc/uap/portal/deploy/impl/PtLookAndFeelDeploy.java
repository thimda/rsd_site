package nc.uap.portal.deploy.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalModuleQryService;
import nc.uap.portal.service.itf.IPtPortalSkinQryService;
import nc.uap.portal.service.itf.IPtPortalSkinService;
import nc.uap.portal.service.itf.IPtPortalThemeQryService;
import nc.uap.portal.service.itf.IPtPortalThemeService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtModuleVO;
import nc.uap.portal.vo.PtSkinVo;
import nc.uap.portal.vo.PtThemeVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * 部署portal主题
 * 
 * @author licza
 * 
 */
public class PtLookAndFeelDeploy {
	/**FTL文件过滤器**/
	private static final FileFilter ftlFilter = FileFilterUtils.suffixFileFilter(PortalEnv.FREE_MARKER_SUFFIX);

	/**
	 * 工作
	 */
	public void doIt(){
		/**
		 * 部署portal主题文件
		 */
		deployLookAndFeel();
		
		/**
		 * 部署皮肤到缓存中
		 */
		deploySkins();
	}
	
	public void deployLookAndFeel(){
		InputStream ins = null;
		try {
			ins = ContextResourceUtil.getResourceAsStream("/WEB-INF/conf/look-and-feel.xml");
			LookAndFeel lookAndFeel = (LookAndFeel) JaxbMarshalFactory.newIns().lookupUnMarshaller(LookAndFeel.class).unmarshal(ins);
			deploy(lookAndFeel);
		} catch (JAXBException e) {
			LfwLogger.error("Portal主题文件解析异常",e);
		} catch (PortalServiceException e) {
			LfwLogger.error("Portal主题部署错误",e);
		}finally{
			IOUtils.closeQuietly(ins);
		}
	}
	
	/**
	 * 部署主题
	 * 
	 * @param lookAndFeel
	 * @throws PortalServiceException
	 */
	private void deploy(LookAndFeel lookAndFeel) throws PortalServiceException {
		List<Theme> themes = lookAndFeel.getTheme();
		if (themes != null && (!themes.isEmpty())) {
			for (Theme theme : themes) {
				deploy(theme);
			}
		}
	}

	/**
	 * 部署一个主题
	 * 
	 * @param theme
	 * @throws PortalServiceException
	 */
	private void deploy(Theme theme) throws PortalServiceException {
		IPtPortalThemeQryService themeQry = PortalServiceUtil.getThemeQryService();
		IPtPortalThemeService themeService = PortalServiceUtil.getThemeService();
		String themeId = theme.getId();
		PtThemeVO themeVO = themeQry.find(themeId);
		if (themeVO == null) {
			themeVO = new PtThemeVO();
			themeVO.setActiveflag(UFBoolean.valueOf(true));
			themeVO.setI18nname(theme.getI18nName());
			themeVO.setThemeid(themeId);
			themeVO.setTitle(theme.getTitle());
			themeVO.setLfwthemeid(theme.getLfwThemeId());
			themeService.add(themeVO);
		} else {
//			if (themeVO.getI18nname().equals(theme.getI18nName())
//					|| themeVO.getTitle().equals(theme.getTitle())
//					|| themeVO.getLfwthemeid().equals(theme.getLfwThemeId())) {
				themeVO.setI18nname(theme.getI18nName());
				themeVO.setTitle(theme.getTitle());
				themeVO.setLfwthemeid(theme.getLfwThemeId());
				themeService.update(themeVO);
//			}
		}

	}
	/**
	 * 部署皮肤
	 */
	private void deploySkins(){
		IPtPortalModuleQryService pmq = PortalServiceUtil.getModuleQryService();
		try {
			PtModuleVO[] modules = pmq.findALl();
			if(!PtUtil.isNull(modules)){
				for(PtModuleVO m : modules){
					try {
						scanSkins(m.getModuleid());
					} catch (Exception e) {
						LfwLogger.error(e.getMessage(),e);
					}
				}
			}
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 扫描皮肤
	 * @param module
	 * @throws PortalServiceException 
	 */
	public void scanSkins(String module) throws PortalServiceException{
		IPtPortalSkinService skinService = PortalServiceUtil.getSkinService();
		String skinDirs = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR+"/"+module+"/portalspec/ftl/portaldefine/skin/";
		List<Skin> pages = new ArrayList<Skin>();
		List<Skin> layouts = new ArrayList<Skin>();
		List<Skin> portlets = new ArrayList<Skin>();
		File f = new File(skinDirs);
		if(f.exists() && f.isDirectory()){
			File[] skins = f.listFiles();
			for(File skin : skins){
				if(skin.isDirectory()){
					//皮肤文件夹
					skinService.delete(module, skin.getName(), PortalEnv.TYPE_PAGE);
					scanf(pages,module,skin,PortalEnv.TYPE_PAGE);
					//布局
					skinService.delete(module, skin.getName(), PortalEnv.TYPE_LAYOUT);
					scanf(layouts,module,skin,PortalEnv.TYPE_LAYOUT);
					//Portlet
					skinService.delete(module, skin.getName(), PortalEnv.TYPE_PORTLET);
					scanf(portlets,module,skin,PortalEnv.TYPE_PORTLET);
				}
			}
		}
		/**
		 * 清除缓存
		 */
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_SKINS_CACHE, dsName);
		cache.remove(PortalEnv.TYPE_PAGE);
		cache.remove(PortalEnv.TYPE_LAYOUT);
		cache.remove(PortalEnv.TYPE_PORTLET);
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
    	if (f.exists()){
    		try {
    			String xml = FileUtils.readFileToString(f, "UTF-8");
    			SkinDescription skinDescription= JaxbMarshalFactory.newIns().encodeXML(SkinDescription.class, xml);
    			for (Skin skin: skinDescription.getSkin()){
    				deploySkinDesc(module,dir.getName(),tp,skin);
    				skins.add(skin);
    			}
    		} catch (Exception e) {
    			LfwLogger.error(e.getMessage(),e);
    		}
    	}
    	else{
    		List<String> elementlist = new ArrayList<String>();
    		File elementHome = new File(dir.getPath()+"/"+tp);
    		File[] elements = elementHome.listFiles(ftlFilter);
    		if (elements == null)
    			return;
			for(File element : elements){
				elementlist.add(PortalPageDataWrap.modModuleName(module,element.getName().replace(PortalEnv.FREE_MARKER_SUFFIX, "")));
			}
			List<Skin> skin = new ArrayList<Skin>();
			for (String  element: elementlist){
				Skin s = new Skin();
				s.setId(element);
				s.setName(element);
				try {
					deploySkinDesc(module,dir.getName(),tp,s);
				} catch (PortalServiceException e) {
					LfwLogger.error(e.getMessage(),e);
				}
				skin.add(s);
			}
			skins.addAll(skin);
    	}
	}
	
	/**
	 * //描述文件写到数据库中
	 * 
	 * @param module
	 * @param themeId
	 * @param type
	 * @param skin
	 */
	public void deploySkinDesc(String module,String themeId,String type,Skin skin) throws PortalServiceException{
		IPtPortalSkinQryService skinQry = PortalServiceUtil.getSkinQryService();
		IPtPortalSkinService skinService = PortalServiceUtil.getSkinService();
		
		PtSkinVo skinVo = skinQry.find(module, themeId, type,skin.getId());
		if (skinVo == null) {
			skinVo = new PtSkinVo();
			skinVo.setModulename(module);
			skinVo.setThemeid(themeId);
			skinVo.setType(type);
			skinVo.setSkinid(skin.getId());
			skinVo.setSkinname(skin.getName());
			skinService.add(skinVo);
		} else {
			skinVo.setSkinname(skin.getName());
			skinService.update(skinVo);
		}
	}
}
