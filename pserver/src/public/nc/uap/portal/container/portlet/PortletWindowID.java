package nc.uap.portal.container.portlet;

/**
 * 唯一标识Portlet窗口
 */
public class PortletWindowID {

	private String portletName;
	private String module;
	private String pageModule;
	private String pageName;
	
	public PortletWindowID(String pageModule, String pageName, String module, String portletName){
		this.pageModule = pageModule;
		this.pageName = pageName;
		this.module = module;
		this.portletName = portletName;
	}
	public PortletWindowID(String portletid){
		String[] info = portletid.split("_");
		pageModule = info[0];
		pageName = info[1];
		module = info[2];
		portletName = info[3];
	}
	
	public String getStringId() {
		return pageModule + "_" + pageName + "_" + module + "_" + portletName;
	}
	public String getPortletName() {
		return portletName;
	}
	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getPageModule() {
		return pageModule;
	}
	public void setPageModule(String pageModule) {
		this.pageModule = pageModule;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
