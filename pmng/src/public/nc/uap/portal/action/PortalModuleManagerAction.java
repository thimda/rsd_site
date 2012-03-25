package nc.uap.portal.action;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.PortalDeployer;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPortalDeployService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.ToolKit;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portal.vo.PtModuleVO;
import nc.vo.org.GroupVO;

/**
 * Portal Module Debug Console
 * @author licza
 *
 */
@Servlet(path = "/M0dUlE")
public class PortalModuleManagerAction extends BaseAction {
	/**
	 * 
	 */
	@Action
	public void ls(){
		try {
			PtModuleVO[] modules = CRUDHelper.getCRUDService().queryVOs(" 1=1 ", PtModuleVO.class, null, null);
			if(modules != null && modules.length > 0){
				List<Map<String,String>> nodes = new ArrayList<Map<String,String>>();
				for(PtModuleVO module : modules){
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", module.getModuleid());
					map.put("name", module.getModuleid());
					boolean health = doCrc(module.getModuleid());
					map.put("health", health ? "OK" : "<b style='color:red'>ERROR</b>");
					nodes.add(map);
				}
				Map root = new HashMap();
				root.put("nodes", nodes);
				String html = FreeMarkerTools.contextTemplatRender("nc/uap/portal/mng/module/moudleMng.ftl", root);
				//nc/uap/portal/mng/module/moudleMng.ftl
				print(html);
			}
			
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
	@Action
	public void rebuild(@Param(name="id") String id){
		if(!doCrc(id)){
			print("CRC ERROR");
			return ;
		}
		String portalModuleDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		File dir = new File(portalModuleDir + "/" + id);
		if(dir.exists()){
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
		PortalDeployer.copyPortalModule(id, portalModuleDir, Thread.currentThread().getContextClassLoader());
		print("rebuild : ok!");
	}
	@Action
	public void redeploy(@Param(name="id") String id){
		if(!doCrc(id)){
			print("CRC ERROR");
			return ;
		}
		try {
			CRUDHelper.getCRUDService().executeUpdate("delete from pt_portlet where module = '"+id+"'");
			CRUDHelper.getCRUDService().executeUpdate("delete from pt_portalpage where module = '"+id+"'");
			CRUDHelper.getCRUDService().executeUpdate("DELETE FROM pt_preference WHERE portletname LIKE '"+id+":%' OR pagename LIKE  '"+id+":%' ");
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		String portalModuleDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		File dir = new File(portalModuleDir + "/" + id);
		if(dir.exists()){
			PortalDeployDefinition module = PortalServiceUtil.getPortalSpecService().parseModule(dir.getAbsolutePath());
			IPortalDeployService pds = NCLocator.getInstance().lookup(IPortalDeployService.class);
			pds.deployModule(module);
			new PortalDeployer().syncGruopResource();
			PortalCacheManager.notify(CacheKeys.PORTLETS_CACHE, CacheKeys.GROUP_PORTLETS_CACHE);
		}
		
		print("redeploy : ok!");
	}
	/**
	 * аЃбщ
	 * @param id
	 */
	@Action
	public void crc(@Param(name="id") String id){
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		if(ToolKit.notNull(id)){
			URL path = currentClassLoader.getResource(id + "/portalspec/portal.xml" );
			if(path == null){
				print(" Module [" + id + "] description file not found." );
			}else{
				URL checksumpath = currentClassLoader.getResource(id + "/portalspec/checksum/" );
				if(checksumpath == null)
					print("Module[" + id + "] checksum  file : " + path + " not found." );
				else{
					print("<b>crc OK!</b> <a href=/portal/pt/M0dUlE/rebuild?id="+id+" target=chkcrc>ReBuild</a> <a href=/portal/pt/M0dUlE/redeploy?id="+id+" target=chkcr>ReDeploy</a>");
				}
			}
		}else{
			print("id ЮЊПе");
		}
	}
	public boolean doCrc(String id){
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		if(ToolKit.notNull(id)){
			URL path = currentClassLoader.getResource(id + "/portalspec/portal.xml" );
			if(path == null){
				return false;
			}else{
				URL checksumpath = currentClassLoader.getResource(id + "/portalspec/checksum/" );
				if(checksumpath == null)
					return false;
			}
			return true;
		}
		return false;
	}
	
}
