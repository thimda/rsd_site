package nc.uap.portal.constant;

/**
 * Portal常量类 <BR/>
 * 定义了一些Portal运行时需要获得的常量
 * 
 * @author licza
 * 
 */
public class PortalEnv {

	/**Portal模块路径**/
	public static final String PORTAL_HOME_DIR = "/portalhome";
	
	public static final String FOLDER_TALLY = "/";
	/**
	 * Page皮肤路径
	 */
	public static final String PAGE_FOLDER = "/page/";
	/**
	 * layout皮肤路径
	 */
	public static final String LAYOUT_FOLDER = "/layout/";
	
	/**
	 * portlet皮肤路径
	 */
	public static final String PORTLET_PATH = "/portlet/";
	
	/**
	 * Page
	 */
	public static final String TYPE_PAGE = "page";
	
	/**
	 * layout 
	 */
	public static final String TYPE_LAYOUT  = "layout";
	
	/**
	 * portlet 
	 */
	public static final String TYPE_PORTLET  = "portlet";
	
	/**
	 * 皮肤路径
	 */
	public static final String SKIN_PATH = "portalspec/ftl/portaldefine/skin/";

	/**
	 * 公共皮肤路径
	 */
	public static final String COMM_FOLDER = "/comm/";

	/**
	 * FreeMarker模板后缀.
	 */
	public static final String FREE_MARKER_SUFFIX = ".ftl";
	/**
	 * 默认模板名称
	 * **/
	public static final String DEFAULT_TEMPLATE_NAME = "default";

	/**
	 * 默认皮肤名称
	 * 
	 * @deprecated 应从库中获得用户的默认皮肤
	 * **/
	public static final String DEFAULT_SKIN_NAME = "webclassic";
	
	/**默认命名空间**/
	public static final String DEFAULT_NAME_SPACE="http://www.ufida.com.cn";
	
	/**默认主页地址**/
	public static final String DEFAULT_PAGE = "/pt/home/index";
	
	/**默认系统标题**/
	public static final String DEFAULT_SYSTEM_TITLE = "portal.default.title";
	
	/**
	 * 获得Portal名称
	 * @return
	 */
	public static String getPortalName(){
		return "portal";
		//NcServiceFacility.getAppendProductService().getNCPortalProp(key)
	}
	/**
	 * 获得Portal核心模块名称
	 * @return
	 */
	public static String getPortalCoreName(){
		return "pserver";
	}
	/**
	 * 获得默认模板名称
	 * @return
	 */
	public static String getDefaultTempleteName(){
		return DEFAULT_TEMPLATE_NAME;
	}
	/**
	 * 获得默认皮肤名称
	 * @return
	 */
	public static String getDefaultSkinName(){
		return DEFAULT_SKIN_NAME;
	}
	/**Portal模块编码**/
	public static final String PORTALMODULECODE = "E001";
}
