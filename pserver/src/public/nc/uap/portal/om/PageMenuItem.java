package nc.uap.portal.om;

import java.io.Serializable;

/**
 * 页签项
 * 
 * @author licza
 * 
 */
public class PageMenuItem implements Serializable, Comparable<PageMenuItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5663415827540423106L;
	private String title;
	private String lable;
	private String id;
	private String module;
	private String icon;
	
	private Integer ordernum = Integer.valueOf(0);
	private Boolean isdefault = false;
	/** 接受权限控制 **/
	private Boolean undercontrol = false;
	/** 保持页面状态 **/
	private Boolean keepstate = false;
	/** 强制显示在页签里 **/
	private Boolean forceshow = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
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

	public Boolean getForceshow() {
		return forceshow;
	}

	public void setForceshow(Boolean forceshow) {
		this.forceshow = forceshow;
	}

	@Override
	public int compareTo(PageMenuItem o) {
		return this.getOrdernum() - o.getOrdernum();
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

}
