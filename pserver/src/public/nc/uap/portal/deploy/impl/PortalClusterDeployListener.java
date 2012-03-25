package nc.uap.portal.deploy.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import nc.bs.framework.cluster.itf.ClusterMessage;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ServerConfiguration;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.PortalDeployer;

/**
 * Portal从机部署监听类
 * <pre>此消息只会接收一次</pre>
 * @author licza
 *
 */
public class PortalClusterDeployListener   {

	String nchome = null;
	String appsDir = null;
	
	public void onMessage(ClusterMessage message) {
			String dsName = message.getHeaderValue("dsname");
			if(dsName != null)
				LfwRuntimeEnvironment.setDatasource(dsName);
			
			nchome  = RuntimeEnv.getInstance().getNCHome();
			String rootPath = message.getHeaderValue("rootpath");
			appsDir = nchome + "/hotwebs/" + rootPath;
			final ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			LfwLogger.error("##################从机：" + sc.getServerName() + " 初始化ing... ROOT_PATH : " + rootPath);
			String portalModuleDir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
			//PortalDeployer.cleanPortalHome(portalModuleDir);
			 
			/**
			 * 拷贝web文件到Portal服务器相应目录下
			 */
			String folders = message.getHeaderValue("folders");
			String modules = message.getHeaderValue("modules");
		
			String[] folderArr = folders.split(",");
			String[] moduleArr = modules.split(",");
				
			if (folderArr != null && folderArr.length > 0 && moduleArr != null && moduleArr.length == folderArr.length) {
				for (int i = 0; i< folderArr.length; i++) {
					String folder = folderArr[i];
					String module = moduleArr[i];
					//PortalDeployer.copyPortalModule(module, portalModuleDir, cl);
					copyWebFiles(module, folder);
				}
			}else{
				LfwLogger.error("模块和文件夹无法对应!");
			}
	}
	/**
	 * 拷贝文件
	 * 
	 * @param module
	 * @param moduleName
	 */
	public void copyWebFiles(String moduleName, String modulePath) {

		File webSrcDir = new File(nchome + PortalEnv.PORTAL_HOME_DIR + PortalEnv.FOLDER_TALLY + modulePath+ "/portalspec/web");
		String webAppsDir = appsDir + "/apps/" + moduleName;
		Logger.debug("copy files to:" + webAppsDir);
		if (webSrcDir.exists() && webSrcDir.isDirectory()) {
			try {
				File webApps = new File(webAppsDir);
				
				if (!webApps.exists()) {
					webApps.mkdir();
				}else{
					FileUtils.cleanDirectory(webApps);
				}
				FileUtils.copyDirectory(webSrcDir, webApps);
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}
}
