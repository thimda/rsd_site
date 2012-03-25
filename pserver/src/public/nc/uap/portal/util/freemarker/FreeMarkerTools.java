package nc.uap.portal.util.freemarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.utils.ClassScan;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.NcServiceFacility;
import nc.uap.portal.util.PortalRenderEnv;
import freemarker.cache.FileTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.SimpleSequence;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;

/**
 * FreeMarker工具类.
 * 
 * @author licza
 * 
 */
public final class FreeMarkerTools {

	/** 系统级FreeMarker配置 **/
	private static Configuration systemConfig;
	
	
	/**
	 * 获得 FreeMarker配置
	 * 
	 * @return FreeMarker配置
	 */
	public synchronized static Configuration getFreeMarkerCfg() {
		if (systemConfig == null) {
			String ftlFolder = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
			systemConfig = new Configuration();
			systemConfig.setDefaultEncoding("UTF-8");
			try {
				File dir = new File(ftlFolder);
				systemConfig.setDirectoryForTemplateLoading(dir);
				loadFreeMarkerFunctions();// 加载用户自定义函数
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e.getCause());
			}
		}
		return systemConfig;
	}

	/**
	 * 生成HTML.
	 * 
	 * @param t
	 *            模板.
	 * @param root
	 * @return HTML片段
	 * @throws PortalServiceException
	 */
	public static String render(final Template template,
			final Map<String, Object> root) throws PortalServiceException {
		try {
			final Writer out = new StringWriter();
			template.process(root, out);
			return out.toString();
		} catch (Exception e) {
			LfwLogger.warn(template.getName() + e.getMessage());
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 生成HTML.
	 * 
	 * @param templateName
	 *            模板名称.
	 * @param root
	 * @param cfg
	 * @return HTML片段
	 * @throws PortalServiceException
	 */
	public static String render(String templateName, Map<String, Object> root,
			Configuration cfg) throws PortalServiceException {
		try {
			Template t = cfg.getTemplate(templateName);
			return render(t, root);
		} catch (IOException e) {
			LfwLogger.warn(e.getMessage());
			throw new PortalServiceException(e);
		}
	}

	/**
	 * 动态模板
	 * 
	 * @return HTML片段
	 * @throws PortalServiceException
	 */
	public static String dynimicRender(String templateName, String template,
			Map<String, Object> root, Configuration cfg)
			throws PortalServiceException {
		Reader re = new BufferedReader(new StringReader(template));
		Template t2;
		try {
			t2 = new Template(templateName, re, cfg);
			return render(t2, root);
		} catch (IOException e) {
			LfwLogger.warn(e.getMessage());
			throw new PortalServiceException("错误：模板文件地址错误！", e.getCause());
		}
	}

	/**
	 * 获得模板目录
	 * 
	 * @return 模板绝对路径
	 * @throws PortalServiceException
	 */
	public static String getTemplateFolder() throws PortalServiceException {
		try {
			return ((FileTemplateLoader) getFreeMarkerCfg().getTemplateLoader()).baseDir
					.getCanonicalPath();
		} catch (IOException e) {
			LfwLogger.warn(e.getMessage());
			throw new PortalServiceException("错误：模板文件地址错误！", e.getCause());
		}
	}

	/**
	 * 装载FreeMarker自定义函数
	 * 
	 */
	private static void loadFreeMarkerFunctions() {
		loadFreeMarkerFunctions(scanFreeMarkerFunctions());
	}

	/**
	 * 扫描FreeMarker自定义函数
	 * 
	 * @return
	 */
	private static Set<Class<?>> scanFreeMarkerFunctions() {
		final String pkg = "nc.uap.portal.util.freemarker.functions";
		return scanFreeMarkerFunctions(pkg);
	}

	/**
	 * 装载FreeMarker自定义函数
	 * 
	 * @param cls
	 *            Functions
	 */
	private static void loadFreeMarkerFunctions(Set<Class<?>> clazzs) {
		for (Class<?> clazz : clazzs) {
			try {
				// 检查是否实现TemplateMethodModel接口
				if (TemplateMethodModel.class.isAssignableFrom(clazz)
						|| TemplateDirectiveModel.class.isAssignableFrom(clazz)) {
					String functionName = StringUtils.uncapitalize(clazz
							.getSimpleName());
					systemConfig.setSharedVariable(functionName, clazz.newInstance());
				}
			} catch (Exception e) {
				LfwLogger.warn(e.getMessage());
			}
		}
	}

	/**
	 * 扫描FreeMarker自定义函数
	 * 
	 * @param pkg
	 *            包名
	 * @return
	 */
	private static Set<Class<?>> scanFreeMarkerFunctions(String pkg) {
		return (Set<Class<?>>) ClassScan.getClasses(pkg);
	}

	@SuppressWarnings("unchecked")
	public static void warpTmps(Environment env, Map params,
			TemplateDirectiveBody body, String PR, String VARIABLE_NAME,
			String SX, String path) throws TemplateException, IOException {
		// 路径
		SimpleSequence cssArr = (SimpleSequence) env.getCurrentNamespace().get(
				VARIABLE_NAME);
		if (cssArr == null) {
			List csr = new ArrayList();
			csr.add(path);
			env.getCurrentNamespace().put(VARIABLE_NAME, csr);
		}
		if (cssArr == null || (!cssArr.toList().contains(path))) {
			List csr = cssArr == null ? new ArrayList() : cssArr.toList();
			csr.add(path);
			includeTmp(env, params, body, PR, VARIABLE_NAME, SX, path);
			env.getCurrentNamespace().put(VARIABLE_NAME, csr);
		}
	}

	/**
	 * 引入一个模板
	 * 
	 * @param env
	 * @param params
	 * @param body
	 * @param PR
	 * @param VARIABLE_NAME
	 * @param SX
	 * @param path
	 * @throws IOException
	 * @throws TemplateException
	 */
	@SuppressWarnings("unchecked")
	private static void includeTmp(Environment env, Map params,
			TemplateDirectiveBody body, String PR, String VARIABLE_NAME,
			String SX, String path) throws IOException, TemplateException {
		Template temp = env.getTemplateForInclusion(path, "utf-8", true);
		env.getOut().write(PR);
		env.include(temp);
		env.getOut().write(SX);
	}
	
	/**
	 * 检查一个路径是否包含了模块信息
	 * @param templatePath
	 * @return
	 */
 	public static boolean isContainModuleInfo(String templatePath){
 		int i = templatePath.indexOf(":");
 		return (i > 0 && i == templatePath.lastIndexOf(":"));
	}
	
	/**
	 * 检查模板是否存在.
	 * 
	 * @param templatePath
	 * @return
	 */
	public static Boolean isTemplateExist(final String templatePath) {
		String ftlFolder = RuntimeEnv.getInstance().getNCHome() + PortalEnv.PORTAL_HOME_DIR;
		final File templateFile = new File(ftlFolder + templatePath);
		return templateFile.exists();
	}

	/**
	 * 获得模板
	 * 
	 * @param module
	 * @param theme
	 * @param skin
	 * @param type
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getTMP(String module, String theme, String skin,
			String type) {
		StringBuffer sb = new StringBuffer();// /
		sb.append(PortalEnv.FOLDER_TALLY);
		sb.append(module);
		sb.append(PortalEnv.FOLDER_TALLY);
		sb.append(PortalEnv.SKIN_PATH);
		if(skin!=null){
			sb.append(skin);
		}else{
			sb.append(PortalEnv.DEFAULT_SKIN_NAME);
		}
		sb.append(type);
		sb.append(theme);
		sb.append(PortalEnv.FREE_MARKER_SUFFIX);
		return sb.toString();
	}

	/**
	 * 查找模板
	 * @param module
	 * @param temp
	 * @param skin
	 * @param type
	 * @return
	 */
	public static String lookUpTheme(String module, String temp, String skin,String type) {
		if(temp == null){
			temp = "onerow";
		}
		String templetName = new String(temp);
		LfwLogger.debug("查找模板：模块名 :" + module + ", 模板名 : " + templetName + ", 皮肤 :" + skin + ", 类型 : "+type);
		boolean containModuleInfo = isContainModuleInfo(temp);
		String _temp_module = "";
		/**
		 * 模板中指定了模块
		 */
		if(containModuleInfo){
			String[] arr = temp.split(":");
			_temp_module = module;
			/**
			 * 将模块指向指定的模块
			 */
			module =  arr[0];
			temp = arr[1];
		}
		/**
		 * 模板查找顺序:
		 * 1.指定的模块
		 * 2.当前模块
		 * 3.核心模块
		 * 4.核心模块默认页面
		 */
		String tmpPath = FreeMarkerTools.getTMP(module, temp, skin, type);
		boolean isTemplateExist = FreeMarkerTools.isTemplateExist(tmpPath);

		if(!isTemplateExist){
			module = PortalEnv.getPortalCoreName();
			tmpPath = FreeMarkerTools.getTMP(module, temp, skin, type);
			isTemplateExist = FreeMarkerTools.isTemplateExist(tmpPath);
		}
		if(containModuleInfo){
			if(!isTemplateExist){
				module = _temp_module;
				tmpPath = FreeMarkerTools.getTMP(module, temp, skin, type);
				isTemplateExist = FreeMarkerTools.isTemplateExist(tmpPath);
			}
		}
		if(!isTemplateExist){
			module = PortalEnv.getPortalCoreName();
			temp = PortalEnv.getDefaultTempleteName();
			tmpPath = FreeMarkerTools.getTMP(module,temp , skin, type);
			isTemplateExist = FreeMarkerTools.isTemplateExist(tmpPath);
		}
		/**
		 * 如果没有查找到模板抛出异常
		 */
		if(isTemplateExist){
			PortalRenderEnv.setCurrentPortletTempletModule(module);
			PortalRenderEnv.setCurrentPortletTemplet(temp);
			return tmpPath;
		}else
			throw new NullPointerException("没有查找到模板! 模块名 :" + module + ", 模板名 : " + templetName + ", 皮肤 :" + skin + ", 类型 : "+type);
	}
	
	/**
	 * 上下文FreeMarker模板渲染
	 * <pre>使用此方法时要特别注意:慎用include指令,否则生产环境下会产生不可预知的结果</pre>
	 * @param classpath
	 * @param root
	 * @return
	 * @throws PortalServiceException
	 * 
	 */
	public static String contextTemplatRender(String classpath,Map<String,Object> root) throws PortalServiceException{
		Reader reader = null;
		InputStream  ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
		try {
			reader = new InputStreamReader(ins,"UTF-8");
			Template t2 = new Template(classpath, reader, getFreeMarkerCfg());
			return render(t2, root);
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e.getCause());
			throw new PortalServiceException("错误：模板文件地址错误！");
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(ins);
		}
	}
} 
