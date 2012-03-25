package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;

/**
 * Portlet显示分组VO
 * 
 * @author licza
 * 
 */
public class PtDisplaycateVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1940538717089297L;
	private String pk_displaycate;
	/** 名称 **/
	private String title;
	/** 国际化名称 **/
	private String i18nname;
	/** id **/
	private String id;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public String getPk_displaycate() {
		return pk_displaycate;
	}

	public void setPk_displaycate(String pk_displaycate) {
		this.pk_displaycate = pk_displaycate;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public String getPKFieldName() {
		return "pk_displaycate";
	}

	@Override
	public String getTableName() {
		return "pt_displaycate";
	}
}
