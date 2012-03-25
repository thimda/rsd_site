package nc.uap.portal.testcase;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.test.AbstractTestCase;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.service.itf.IPortalDeployService;
import sun.net.www.protocol.jar.URLJarFile;

/**
 * Portal模块扫描测试用例
 * 
 * @author licza
 * 
 */
public class PortalModuleScanTestCase extends AbstractTestCase {

	/**
	 * 测试扫描模块
	 */
	public void testScanModule() {
		String dir = RuntimeEnv.getInstance().getNCHome() + "/modules";
		File f = new File(dir);
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			if (file.isDirectory()) {
				String fn = file.getName();
				URL url = Thread.currentThread().getContextClassLoader()
						.getResource(fn + "/portalspec/portal.xml");
				 if(url != null)
					 scanf(fn);
			}
		}
		IPortalDeployService pds = NCLocator.getInstance().lookup(IPortalDeployService.class);
//		pds.deployAll();
	}

	
	public void scanf(String fn) {
		String pkg = fn + ".portalspec";
		Set<String> resources = resourceScaner(pkg);
		
		//String dir = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		//copyTmpFiles(dir, new ArrayList<String>(resources));
	}
	
	
	/**
	 * 拷贝文件到零食文件夹
	 * 
	 * @param loader
	 * @param basePath
	 * @param tmpDir
	 * @param props
	 */
	protected void copyTmpFiles(String tmpDir, List<String> props) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		File f = new File(tmpDir + "/");
		if (!f.exists())
			f.mkdirs();
	
		for (int i = 1; i < props.size(); i++) {
			String classpath =  props.get(i);
			int index = classpath.lastIndexOf(".");
			String path =   classpath.substring(0,index ).replace(".", "/")+classpath.substring(index);
			if (path != null) {
				InputStream input = null;
				OutputStream fout = null;
				try {
					input = loader.getResourceAsStream( path);
					if (input != null) {
						String filePath = tmpDir + "/"  + path;
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
	public static Set<String> resourceScaner(String packageName) {
		// 第一个class类的集合
		Set<String> classes = new LinkedHashSet<String>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(
					packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				}else{
					
					Enumeration<JarEntry> entries = null;
					try {
						if("jar".equals(protocol)){
							// 如果是jar包文件
							// 定义一个JarFile
							JarFile  jar = ((JarURLConnection) url.openConnection()).getJarFile();
							// 从此jar包 得到一个枚举类
							entries = jar.entries();
						}
						
						// ---Modify by licza start--- 修正在weblogic及was下无法获取jar包的bug
						if("zip".equals(protocol) || "wsjar".equals(protocol)){
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							//去掉包信息  否则Weblogic会报FileNotFoundException
							if(idx > 0)
								filepath = filepath.substring(0,filepath.indexOf("!"));
							//通过java.io.File 获得jar包
							URLJarFile ufj = new URLJarFile(new File(filepath));
							entries = ufj.entries();
						}
						// ---Modify by licza end---
						if(entries != null){
							while (entries.hasMoreElements()) {
								// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
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
										packageName = name.substring(0, idx)
												.replace('/', '.');
									}
									// 如果可以迭代下去 并且是一个包
									if ((idx != -1) || recursive) {
										// 如果是一个.class文件 而且不是目录
										if (!entry.isDirectory()) {
											try {
												// 添加到classes
												classes.add(name.replace("/", "."));

											} catch (Exception e) {
												LfwLogger.error(e.getMessage(), e);
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						LfwLogger.error(e.getMessage(), e);
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
	public static void findAndAddClassesInPackageByFile(String packageName,
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
					classes.add(packageName + '.' + className);
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
	}

	 
}
