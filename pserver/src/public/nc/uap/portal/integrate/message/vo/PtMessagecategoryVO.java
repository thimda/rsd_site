package nc.uap.portal.integrate.message.vo;

import nc.vo.pub.SuperVO;

/**
 * 信息分类VO
 * 
 * @author licza
 * 
 */
public class PtMessagecategoryVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 457412838361076878L;
	private String syscode;
	private String parentid;
	private String title;
	private String i18nname;
	private String pluginid;
	/** 节点类型 **/
	private Integer type;

	public PtMessagecategoryVO() {
		super();
	}

	public PtMessagecategoryVO(String syscode, String parentid, String title, String i18nname,String pluginid) {
		super();
		this.syscode = syscode;
		this.parentid = parentid;
		this.title = title;
		this.i18nname = i18nname;
		this.pluginid = pluginid;
		this.type = 0;
	}

	public PtMessagecategoryVO(String syscode, String parentid, String title, String i18nname, int type) {
		super();
		this.syscode = syscode;
		this.parentid = parentid;
		this.title = title;
		this.i18nname = i18nname;
		this.type = type;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPluginid() {
		return pluginid;
	}

	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}

}
