package nc.uap.portal.deploy;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ServerConfiguration;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.utils.ClassScan;
import nc.uap.portal.constant.PortalConst;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.impl.PtLookAndFeelDeploy;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPageService;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.ToolKit;
import nc.uap.portal.vo.PtModuleVO;
import nc.vo.org.GroupVO;
import nc.vo.pub.lang.UFBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Portal部署
 * 
 * @author licza
 * 
 */
public class PortalDeployer {

	/** HOTWEBS路径 **/
	//private static final String HOTWEBS = "/hotwebs";

	/** Portal模块定义文件 **/
	//private static final String PORTAL_DEF = "/WEB-INF/portal.xml";

	/** Portal文件所在包 **/
	private static final String PORTAL_SPEC = ".portalspec";

	/**
	 * 扫描模块并部署
	 */
	public void doIt() {
		String portalModuleDir = RuntimeEnv.getInstance().getNCHome()
				+ PortalEnv.PORTAL_HOME_DIR;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		/**
		 * 清理Portal模块文件夹
		 */
		LfwLogger.error("PortalDeployer#doIt : 清理Portal模块文件夹 --- start ---");
		cleanPortalHome(portalModuleDir);
		LfwLogger.error("PortalDeployer#doIt : 清理Portal模块文件夹 --- end ---");
		/**
		 * 获得Portal模块并拷贝到Portal模块文件夹
		 */
		LfwLogger.error("PortalDeployer#doIt : 获得Portal模块并拷贝到Portal模块文件夹 --- start ---");

		List<String> modules = getPortalModules();
		checksum(modules);
		if (!PtUtil.isNull(modules)) {
			LfwLogger.error("PortalDeployer#doIt : 获得Portal模块 :" + modules);
			for (String name : modules)
				copyPortalModule(name, portalModuleDir, cl);
		} 
		else {
			LfwLogger.error("PortalDeployer#doIt : 没有获得Portal模块 ");
		}
		LfwLogger.error("PortalDeployer#doIt : 获得Portal模块并拷贝到Portal模块文件夹 --- end ---");

		final ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
		/**
		 * 单机或者Master上，进行单点的必要初始化
		 */
		if (sc.isSingle() || sc.isMaster()) {
			LfwLogger.error("PortalDeployer#doIt : 在单机或者Master上，进行单点的必要初始化 --- start ---");

			LfwLogger.error("PortalDeployer#doIt : 部署Portal配置文件 --- start ---");

			/**
			 * 部署Portal配置文件
			 */
			PortalServiceUtil.getPortalDeployService().deployAll();

			LfwLogger.error("PortalDeployer#doIt : 部署Portal配置文件 --- end ---");

			/**
			 * 部署Portal主题
			 */
			LfwLogger.error("PortalDeployer#doIt : 部署Portal主题 --- start ---");

			new PtLookAndFeelDeploy().doIt();
			LfwLogger.error("PortalDeployer#doIt : 部署Portal主题 --- end ---");

			/**
			 * 为新增的集团同步资源文件 
			 */
			 //syncGruopResource();
			/**
			 * 初始化Portal页面及Portlet缓存
			 */
			// LfwLogger.debug("PortalDeployer#doIt : 初始化Portal页面及Portlet缓存 --- start ---");
			// initCache();
			// LfwLogger.debug("PortalDeployer#doIt : 初始化Portal页面及Portlet缓存 --- end ---");
			/**
			 * 通知从机Portal初始化已经完成
			 */
			nodifyCluster();
			
			LfwLogger
					.error("PortalDeployer#doIt : 在单机或者Master上，进行单点的必要初始化 --- end ---");
		}
	}
	
	/**
	 * 通知从机Portal初始化已经完成
	 */
	private void nodifyCluster(){
		List<String> modules = new ArrayList<String>();
		List<String> folders = new ArrayList<String>();
//		Map<String, String> folderMap = PortalServiceUtil.getPortletRegistryService().findModuleFolders();
//		Set<String> st = folderMap.keySet();
//		for(String key : st){
//			modules.add(key);
//			folders.add(folderMap.get(key));
//		}
//		
		PtModuleVO moduleQryVO = new PtModuleVO();
		moduleQryVO.setActiveflag(UFBoolean.TRUE);
		try {
			PtModuleVO[] vos = CRUDHelper.getCRUDService().queryVOs(moduleQryVO, null, null, null, null);
			if(vos != null && vos.length > 0){
				for(PtModuleVO vo : vos){
					modules.add(vo.getModuleid());
					folders.add(vo.getModuleid());
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		
		PortalDeployMessage pdm = new PortalDeployMessage();
		pdm.addHeader("dsname", LfwRuntimeEnvironment.getDatasource());
		pdm.addHeader("folders", StringUtils.join(folders.iterator(), ","));
		pdm.addHeader("modules", StringUtils.join(modules.iterator(), ","));
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		pdm.addHeader("rootpath", rootPath);
		PortalServiceUtil.getPortalStatusService().signPortalCoreStartComplete(pdm);
	}
	
	/**
	 * 为新增的集团同步资源
	 */
	 public void syncGruopResource(){
		 try {
			 GroupVO[] groups = CpbServiceFacility.getCpGroupQry().queryAllGroupVOs();
			 if(PtUtil.isNull(groups))
				 return;
			 for(GroupVO gg : groups){
				String pk_group = gg.getPk_group();
			 	PortalServiceUtil.getPageService().sync(pk_group);
			 	PortalServiceUtil.getPortletService().sync(pk_group);
//				CpbServiceFacility.getCpResourceBill().resourceSynchronize(pk_group);
			 }
		 } catch (Exception e) {
			 LfwLogger.error("集团同步失败",e);
		 }
	 }
	/**
	 * 初始化Portal页面及Portlet缓存
	 */
	// private void initCache(){
	// PortalServiceUtil.getRegistryService().loadPortalPages();
	// PortalServiceUtil.getPortletRegistryService().loadPortlets();
	// PortalServiceUtil.getConfigRegistryService().init();
	// }
	/**
	 * 清理Portal模块文件夹
	 */
	public static void cleanPortalHome(String portalModuleDir) {
		File pmd = new File(portalModuleDir);
		if (pmd.exists()) {
			try {
				FileUtils.cleanDirectory(pmd);
			} catch (IOException e) {
				LfwLogger.error("Portal模块清理失败!", e);
			}
		}
	}

	/**
	 * 获得Portal模块
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	private List<String> getPortalModules() { 
		String moduleStr = LfwRuntimeEnvironment.getServletContext().getInitParameter(PortalConst.MODULES);
		String[] modules = null;
		if(moduleStr != null && !moduleStr.equals(""))
			modules = moduleStr.split(",");
		else 
			modules = new String[0];
		List<String> modulelist = new ArrayList<String>();
		modulelist.addAll(Arrays.asList(modules));
		Set<String> moduleSet = resourceScaner("portalspec", Thread.currentThread().getContextClassLoader());
		if(moduleSet != null && !moduleSet.isEmpty()){
			Iterator<String> it = moduleSet.iterator();
			while(it.hasNext()){
				modulelist.add(it.next().replace("portalspec/", "").replace("/portal.xml", ""));
			}
		}else{
			LfwLogger.error("###Portal部署时异常###：从没有扫描到模块定义文件，这将只加载web.xml配置的默认模块[" + moduleStr + "]。");
		}
		return modulelist;
	}
	/**
	 * 对模块进行校验
	 * @param modules
	 */
	public static void checksum( List<String> modules){
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		if(ToolKit.notNull(modules)){
			for(String module : modules){
				URL path = currentClassLoader.getResource(module + "/portalspec/portal.xml" );
				if(path == null){
					LfwLogger.error("###Portal部署时异常###： 模块[" + module + "]描述文件未找到！" );
				}else{
					URL checksumpath = currentClassLoader.getResource(module + "/portalspec/checksum/" );
					if(checksumpath == null)
						LfwLogger.error("###Portal部署时异常###： 模块[" + module + "]文件" + path + "校验失败！" );
				}
			}
			
		}
	}
	
	
	/**
	 * 扫描Portal模块文件并拷贝到Portal模块文件夹
	 * 
	 * @param name
	 * @param dir
	 * @param cl
	 */
	public static void copyPortalModule(String name, String dir, ClassLoader cl) {
		String pkg = name + PORTAL_SPEC;
		LfwLogger.info("portal module pkg : "  + pkg);
		Set<String> resources = resourceScaner(pkg, cl);
		LfwLogger.info("portal module pkg : "  + resources.toString());
		copyModuleResource(dir, new ArrayList<String>(resources), cl);
	}

	/**
	 * 拷贝文件到Portal模块文件夹
	 * 
	 * @param moduleHome
	 * @param resources
	 * @param cl
	 */
	private static void copyModuleResource(String moduleHome, List<String> resources,
			ClassLoader loader) {
		File f = new File(moduleHome + "/");
		if (!f.exists())
			f.mkdirs();
		for (int i = 0; i < resources.size(); i++) {
			String path = resources.get(i);
			if (path != null) {
				InputStream input = null;
				OutputStream fout = null;
				try {
					input = loader.getResourceAsStream(path);
					if (input != null) {
						String filePath = moduleHome + "/" + path;
						String dirPath = filePath.substring(0, filePath
								.lastIndexOf("/"));
						File dir = new File(dirPath);
						if (!dir.exists())
							dir.mkdirs();
						fout = new FileOutputStream(filePath);
						byte[] bytes = new byte[1024 * 4];
						int count = input.read(bytes);
						while (count != -1) {
							fout.write(bytes, 0, count);
							count = input.read(bytes);
						}
					}
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
				} finally {
					if (input != null)
						try {
							input.close();
						} catch (IOException e1) {
							LfwLogger.error(e1.getMessage(), e1);
						}
					if (fout != null)
						try {
							fout.close();
						} catch (IOException e) {
							LfwLogger.error(e.getMessage(), e);
						}
				}
			} else
				break;
		}
	}

	/**
	 * 根据报名获取包中所有的资源
	 * 
	 * @param packageName
	 * @return
	 */
	private static Set<String> resourceScaner(String packageName, ClassLoader cl) {
		// 资源集合
		Set<String> classes = new LinkedHashSet<String>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		if(RuntimeEnv.isRunningInWebSphere()){
			if(!packageDirName.endsWith("/")){
				packageDirName = packageDirName + "/";
			}
		}
		Enumeration<URL> dirs;
		try {
			dirs = cl.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if (ClassScan.URL_PROTOCOL_FILE.equals(protocol)) {
					// 获取物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else {
					Enumeration<JarEntry> entries = null;
					JarFile jar = null;
					boolean isNewJar = false;
					// 如果是jar包文件
					try {
						if (ClassScan.URL_PROTOCOL_JAR.equals(protocol)
								|| ClassScan.URL_PROTOCOL_ZIP.equals(protocol)
								|| ClassScan.URL_PROTOCOL_WSJAR
										.equals(protocol)) {
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							// 去掉包信息 否则Weblogic会报FileNotFoundException
							if (idx > 0)
								filepath = filepath.substring(0, filepath
										.indexOf("!"));
							jar = ClassScan.getJarFile(filepath);
						}
						if (jar != null) {
							entries = jar.entries();
							isNewJar = true;

							if (entries != null) {
								while (entries.hasMoreElements()) {
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// 如果是以/开头的
									if (name.charAt(0) == '/') {
										// 获取后面的字符串
										name = name.substring(1);
									}
									// 如果前半部分和定义的包名相同
									if (name.startsWith(packageDirName)) {
										int idx = name.lastIndexOf('/');
										// 如果以"/"结尾 是一个包
										if (idx != -1) {
											// 获取包名 把"/"替换成"."
											packageName = name
													.substring(0, idx).replace(
															'/', '.');
										}
										// 如果可以迭代下去 并且是一个包
										if ((idx != -1) || recursive) {
											// 如果是一个.class文件 而且不是目录
											if (!entry.isDirectory()) {
												try {
													// 添加到classes
													classes.add(name);

												} catch (Exception e) {
													LfwLogger.error(e
															.getMessage(), e);
												}
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						LfwLogger.error(e.getMessage(), e);
					} finally {
						if (isNewJar) {
							if (jar != null) {
								jar.close();
								jar = null;
							}
							isNewJar = false;
						}
					}
				}
			}
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return classes;
	}

	/**
	 * 
	 * 
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<String> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.isFile());
			}
		});

		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "."
						+ file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				String className = file.getName();
				try {
					classes
							.add(packageName.replace(".", "/") + '/'
									+ className);
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
	}
}
