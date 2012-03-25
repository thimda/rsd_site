package nc.uap.portal.vo;

import nc.uap.portal.om.PortletDisplay;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * Portlet显示
 * 
 * @author licza
 * 
 */
public class PtDisplayVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8087816655450381574L;
	
	private String pk_display;
	/** 名称 **/
	private String title;
	/** id **/
	private String id;
	/** 模块 **/
	private String module;
	/** 国际化名称 **/
	private String i18nname;
	/** 分类id **/
	private String cateid;
	/** 可以动态添加 **/
	protected UFBoolean dynamic;
	public String getPk_display() {
		return pk_display;
	}

	public void setPk_display(String pk_display) {
		this.pk_display = pk_display;
	}

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

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	@Override
	public String getPKFieldName() {
		return "pk_display";
	}

	@Override
	public String getTableName() {
		return "pt_display";
	}

	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	public String getCateid() {
		return cateid;
	}

	public void setCateid(String cateid) {
		this.cateid = cateid;
	}
	public PtDisplayVO(){
		
	}
	public PtDisplayVO(PortletDisplay display , String cateid) {
		super();
		this.id = display.getId();
		this.module = display.getModule();
		this.title = display.getTitle();
		this.dynamic = UFBoolean.valueOf(display.getDynamic());
		this.cateid = cateid;
	}

	public UFBoolean getDynamic() {
		return dynamic;
	}

	public void setDynamic(UFBoolean dynamic) {
		this.dynamic = dynamic;
	}

}
