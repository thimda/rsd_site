package nc.uap.portal.constant;

import nc.uap.lfw.core.common.WebConstant;
/**
 * 页面常量类
 * @author dengjt
 *
 */
public class WebKeys extends WebConstant{

	public static final String SYSTEM_CACHE_PORTLET_STATE = "SYSTEM_CACHE_PORTLET_STATE";
	public static final String USER_CACHE_PORTLET_STATE = "USER_CACHE_PORTLET_STATE";

//	public static final String CACHE_NAME = IClusterCacheService.class.getName();
	
	public static final String CA_RADOM = "LOGIN_CA_RADOM";
	public static final String SIGNED_CA_RADOM = "SIGNED_CA_RADOM";

	public static final String CTX = "CTX";
	public static final String PORTALCTX = "PORTALCTX";
	public static final String REQUESTID = "REQUESTID";
	
	// 当前用户操作的layout
	public static final String LAYOUT = "LAYOUT";
    /*Portal主路径*/
    public static final String MAIN_PATH = "MAIN_PATH";
	//应用对应path。
	public static final String APP_PATH = "APP_PATH";
	//所见即所得编辑器目录路径
	public static final String EDITOR_PATH = "EDITOR_PATH";
	//js控件框架主目录
	public static final String FRAME_PATH = "FRAME_PATH";
	//全局图片目录
	public static final String IMAGE_PATH = "IMAGE_PATH";
	// portlet高度
	public static final String PORTLET_HEIGHT = "portletHeight";
	
	public static final String JAVAX_PORTLET_CONFIG = "javax.portlet.config";
	public static final String JAVAX_PORTLET_KEYWORDS = "javax.portlet.keywords";
	public static final String JAVAX_PORTLET_PORTLET = "javax.portlet.portlet";
	public static final String JAVAX_PORTLET_REQUEST = "javax.portlet.request";
	public static final String JAVAX_PORTLET_RESPONSE = "javax.portlet.response";
	public static final String JAVAX_PORTLET_SHORT_TITLE = "javax.portlet.short-title";
	public static final String JAVAX_PORTLET_TITLE = "javax.portlet.title";
	
	
	public static final String PORTLET_CATEGORIES = "PORTLET_CATEGORIES";
	public static final String PORTLET_CLASS_LOADER = "PORTLET_CLASS_LOADER";
	public static final String PORTLET_DECORATE = "PORTLET_DECORATE";
	public static final String PORTLET_URL = "PORTLET_URL";
	public static final String PORTLET_RENDER_PARAMETERS = "PORTLET_RENDER_PARAMETERS";
	public static final String PORTLE_SPRING_ACTION = "PORTLET_SPRING_ACTION";
	
	public static final String RENDER_PORTLET = "RENDER_PORTLET";
	public static final String RENDER_PORTLET_CUR_COLUMN_COUNT = "RENDER_PORTLET_CUR_COLUMN_COUNT";
	public static final String RENDER_PORTLET_CUR_COLUMN_ORDER = "RENDER_PORTLET_CUR_COLUMN_ORDER";
	public static final String RENDER_PORTLET_CUR_COLUMN_POS = "RENDER_PORTLET_CUR_COLUMN_POS";
	public static final String ROLE = "ROLE";
	public static final String SEL_LAYOUT = "SEL_LAYOUT";
	public static final String SEARCH_SEARCH_RESULTS = "SEARCH_SEARCH_RESULTS";
	public static final String SERVLET_CONTEXT_NAME = "SERVLET_CONTEXT_NAME";
	public static final String SESSION_LISTENER = "SESSION_LISTENER";

	public static final String TASK_LIST = "TASK_LIST";

	public static final String THEME_DISPLAY = "themeDisplay";
	public static final String TRANSLATOR_TRANSLATION = "TRANSLATOR_TRANSLATION";
	public static final String TREE_JS_CLICKS = "TREE_JS_CLICKS";
	public static final String UNIT_CONVERTER_CONVERSION = "UNIT_CONVERTER_CONVERSION";
	//当前登录用户的用户信息和可操作资源
	public static final String LOGIN_USER = "LOGIN_USER";
	// 当前用户授权的布局
	//public static final String USER_AUTH_LAYOUTS = "USER_AUTH_LAYOUTS";
	public static final String USER_PASSWORD = "USER_PASSWORD";
	
	public static final String LOCALE_KEY = "LOCALE_KEY";
	
	public static final String PORTLET_PROCESS_EXCEPTION = "PORTLET_PROCESS_EXCEPTION";
	
	public static final String PORTLET_PROCESS_FORWARD = "PORTLET_PROCESS_FORWARD";
	// PORTLET凭证槽共享级别设置
	public static final int PORTLET_SHARE_GIVEUP = -1; // 放弃
	public static final int PORTLET_SHARE_PRIVATE = 0; // 专有
	public static final int PORTLET_SHARE_APPLICATION = 1; // 应用共享
	public static final int PORTLET_SHARE_GLOBAL = 2; // 全局共享
	// 论坛权限类型
	public static final int BBSDIR_SEE_PERMISSION = 0; // 版面浏览权限
	public static final int BBSDIR_PUBLISH_PERMISSION = 1; // 版面发帖权限
	// 新闻组权限类型
	public static final int NEWSGROUP_SEE_PERMISSION = 0; // 新闻组浏览权限
	public static final int NEWSGROUP_PUBLISH_PERMISSION = 1; // 新闻组撰写新闻权限
	public static final int NEWSGROUP_MANAGE_PERMISSION = 2; // 新闻组管理权限
	//文档目录权限
	public static final int DOC_SEE_PERMISSION = 0; // 文档目录浏览权限
	public static final int DOC_PUBLISH_PERMISSION = 1; //文档目录撰写权限
	// 发文字权限类型
	public static final int COMPDOCLABEL_SEE_PERMISSION = 0; // 发文字浏览权限
	public static final int COMPDOCLABEL_PUBLISH_PERMISSION = 1; // 发文字撰写发文权限
	// 动态表单浏览权限
	public static final int FORM_USE_PERMISSION = 0;
	
	//Web application portlet的URL标志
	public static final String APPLICATION_PORTLET_URL_KEY = "APPLICATION_GATE_URL";
	public static final String SSO_PROVIDER = "SSO_PROVIDER";
	/*//所有布局组缓存常量
	public static final String LAYOUT_GROUP_CACHE = "ILayoutGroup_ALL";
	//用户显示布局组缓存常量
	public static final String SHOW_LAYOUT_GROUP_CACHE = "ILayoutGroup_SHOW";*/
	//所有布局缓存常量
	public static final String LAYOUT_CACHE = "LAYOUT_ALL";
	//用户显示布局组缓存常量
	public static final String SHOW_LAYOUT_CACHE = "LAYOUT_SHOW";
	
	public static final String EXPIRE_KEY = "$_EXPIRE_KEY";
	
	public static final String EMPTY_GROUP = "00000000000000000000";
	public static final String EMPTY_ORG = "00000000000000000000";
	
	// 文档服务器的替换标示,该串会被整体替换为配置的文档服务器地址
	public static final String DOCSERVERIP_FLAG = "http://docserverip";
}
