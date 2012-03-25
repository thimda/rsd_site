package nc.uap.portal.deploy.vo;

import java.io.Serializable;

import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.Page;
import nc.uap.portal.plugins.model.PtPlugin;

/**
 * Portal模块信息
 * <p>
 * 部署时使用
 * </p>
 * 
 * @author licza
 * 
 */
public class PortalDeployDefinition implements Serializable {
	private static final long serialVersionUID = -1274059181881560697L;
	/**
	 * 模块名
	 */
	private String module;
	/** 插件 **/
	private PtPlugin plugin;
	/** 依赖模块 **/
	private String[] dependsModule;

	/** 模块显示名称 **/
	private String title;
	/** Portlet定义 **/
	private PortletApplicationDefinition portletDefineModule;
	/** PML对象 **/
	private Page[] pages;
	/** 管理员节点 **/
	/** Portlet分类 **/
	private Display display;
	
	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

 

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private PortalModule portalModule;

	public Page[] getPages() {
		return pages;
	}

	public void setPages(Page[] pages) {
		this.pages = pages;
	}

	public PortletApplicationDefinition getPortletDefineModule() {
		return portletDefineModule;
	}

	public void setPortletDefineModule(PortletApplicationDefinition portletDefineModule) {
		this.portletDefineModule = portletDefineModule;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public PortalModule getPortalModule() {
		return portalModule;
	}

	public void setPortalModule(PortalModule portalModule) {
		this.portalModule = portalModule;
	}

	public PtPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(PtPlugin plugin) {
		this.plugin = plugin;
	}

	public String[] getDependsModule() {
		return dependsModule;
	}

	public void setDependsModule(String[] dependsModule) {
		this.dependsModule = dependsModule;
	}

	@Override
	public String toString() {
		return "Module:" + getModule();
	}

}
