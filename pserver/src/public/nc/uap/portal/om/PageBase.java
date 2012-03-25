package nc.uap.portal.om;

import java.io.Serializable;


/**
 * portalPage 页面.
 * 
 * @author licza
 * 
 */
public class PageBase implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 页面名称(文件名)
	 */
	private String pagename;
	/**
	 * 页面模块 
	 */
	private String module;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	/** 模板.*/
	private String template;
	/** 标题.*/
	private String title;
	/** 版本*/
	private String version;
	/**图标**/
	private String icon;
	/** 皮肤.*/
	private String skin;
	/** 只读 **/
	private Boolean readonly = true;
	/**布局.**/
	private Layout layout;
	/**级别**/
	private Integer level=new Integer(0);
	/**页签**/
	private String lable;
	/**序号**/
	private Integer ordernum=new Integer(0);
	/**是否主页**/
	private Boolean isdefault=false;
	/** 接受权限控制 **/
	private Boolean undercontrol = false;
	/** 保持页面状态 **/
	private Boolean keepstate = false;
	/**强制显示在页签里**/
	private Boolean forceshow = false;
	/** 适配设备 **/
	private String devices;
	
	/**
	 * 资源束
	 */
	private String resourcebundle;
	
	/**
	 * 绑定链接组
	 */
	private String linkgroup;
	
	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLable() {
			return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
 

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public Boolean getForceshow() {
		return forceshow;
	}

	public void setForceshow(Boolean forceshow) {
		this.forceshow = forceshow;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getUndercontrol() {
		return undercontrol;
	}

	public void setUndercontrol(Boolean undercontrol) {
		this.undercontrol = undercontrol;
	}

	public Boolean getKeepstate() {
		return keepstate;
	}

	public void setKeepstate(Boolean keepstate) {
		this.keepstate = keepstate;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

	public String getResourcebundle() {
		return resourcebundle;
	}

	public void setResourcebundle(String resourcebundle) {
		this.resourcebundle = resourcebundle;
	}

	public String getLinkgroup() {
		return linkgroup;
	}

	public void setLinkgroup(String linkgroup) {
		this.linkgroup = linkgroup;
	}
	
}
