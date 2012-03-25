package nc.uap.portal.util;

import javax.portlet.PortletPreferences;

import nc.uap.portal.om.Page;
import nc.uap.portal.om.Portlet;


/**
 * Portal页面渲染时候环境
 * <pre>用来做Portal页面对象及Portlet对象穿越</pre>
 * @author licza
 *
 */
public class PortalRenderEnv {
	private static ThreadLocal<RenderContext> threadLocal = new ThreadLocal<RenderContext>(){
		protected RenderContext initialValue() {
	       return new RenderContext();
		}
	};
	/**
	 * AJAX请求 
	 */
	public static final String RENDER_REQ_TYPE_AJAX = "ajax";
	/**
	 * Get请求
	 */
	public static final String RENDER_REQ_TYPE_GET = "get";
	/**
	 * 设置当前页
	 * @param page
	 */
	public static void setCurrentPage(Page page){
		threadLocal.get().currentPage = page;
	}
	
	/**
	 * 获得当前页
	 * @return
	 */
	public static Page getCurrentPage(){
		return threadLocal.get().currentPage;
	}
	/**
	 * 设置用户所有页
	 * @param page
	 */
	public static void setPages(Page[] page){
		threadLocal.get().pages = page;
	}
	/**
	 * 获得用户所有页面
	 * @return
	 */
	public static Page[] getPages(){
		return threadLocal.get().pages;
	}
	/**
	 * 获得当前Portlet
	 * @return
	 */
	public static Portlet getCurrentPortlet(){
		return threadLocal.get().currentPortlet;
	}
	/**
	 * 设置当前Portlet
	 * @param currentPortlet
	 */
	public static void setCurrentPortlet(Portlet currentPortlet){
		threadLocal.get().currentPortlet = currentPortlet;
	}
	
	/**
	 * 获得当前Portlet配置
	 * @return
	 */
	public static PortletPreferences getCurrentPortlePreferences(){
		return threadLocal.get().currentPortlePreferences;
	}
	/**
	 * 设置当前Portlet配置
	 * @param currentPortlePreferences
	 */
	public static void  setCurrentPortlePreferences(PortletPreferences currentPortlePreferences){
		threadLocal.get().currentPortlePreferences = currentPortlePreferences;
	}
	/**
	 * 设置当前渲染类型
	 * @param renderType
	 */
	public static void setPortletRenderType(String renderType){
		threadLocal.get().renderType = renderType;
	}
	/**
	 * 获得当前渲染类型
	 * @return
	 */
	public static String getPortletRenderType(){
		return threadLocal.get().renderType;
	}
	
	public static String getCurrentTheme() {
		return threadLocal.get().currentTheme;
	}
	public static void setCurrentTheme(String currentTheme) {
		threadLocal.get().currentTheme = currentTheme;
	}
	public static String getCurrentPortletTemplet() {
		return threadLocal.get().currentPortletTemplet;
	}
	public static void setCurrentPortletTemplet(String currentPortletTemplet) {
		threadLocal.get().currentPortletTemplet = currentPortletTemplet;
	}
	public static String getCurrentPortletTempletModule() {
		return threadLocal.get().currentPortletTempletModule;
	}
	public static void setCurrentPortletTempletModule(String currentPortletTempletModule) {
		threadLocal.get().currentPortletTempletModule = currentPortletTempletModule;
	}
	
	
}
/**
 * 传递Portal页面信息载体
 * @author licza
 *
 */
class RenderContext{
	/**
	 * 当前页面
	 */
	protected Page currentPage;
	/**
	 * 当前所有页面
	 */
	protected Page[] pages;
	/**
	 * 当前Portlet
	 */
	protected Portlet currentPortlet;
	/**
	 * 当前页面样式
	 */
	protected String currentTheme;
	/**
	 * 当前Portlet皮肤样式
	 */
	protected String currentPortletTemplet;
	/**
	 * 当前Portlet皮肤所在模块
	 */
	protected String currentPortletTempletModule;
	/**
	 * 当前Portlet配置
	 */
	protected PortletPreferences currentPortlePreferences;
	/**
	 * 渲染方式
	 */
	protected String renderType;
	
}