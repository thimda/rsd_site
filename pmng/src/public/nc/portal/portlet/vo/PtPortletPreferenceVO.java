package nc.portal.portlet.vo;

import nc.uap.portal.container.om.Preference;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * Portlet配置VO
 * 
 * @author licza
 * 
 */
public class PtPortletPreferenceVO extends SuperVO {
	public PtPortletPreferenceVO() {

	}

	public PtPortletPreferenceVO(Preference pf) {
		this.title = pf.getName();
		this.description=pf.getDescription();
		this.value=pf.getValues().get(0);
		this.readyonly=UFBoolean.valueOf(pf.isReadOnly());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3431996713856428265L;
	/**
	 * 主键
	 */
	private String pk_portletpf;
	/**
	 * 名称
	 */
	private String title;
	/** 介绍 **/
	private String description;
	/** 值 **/
	private String value;
	/** portlet主键 **/
	private String pk_portlet;
	/** 只读 **/
	private UFBoolean readyonly;
	private UFDate ts;
	private UFBoolean dr;

	@Override
	public String getPKFieldName() {
		return "pk_portletpf";
	}

	@Override
	public String getTableName() {
		return "pt_portletpf";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPk_portletpf() {
		return pk_portletpf;
	}

	public void setPk_portletpf(String pk_portletpf) {
		this.pk_portletpf = pk_portletpf;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPk_portlet() {
		return pk_portlet;
	}

	public void setPk_portlet(String pk_portlet) {
		this.pk_portlet = pk_portlet;
	}

	public UFBoolean getReadyonly() {
		return readyonly;
	}

	public void setReadyonly(UFBoolean readyonly) {
		this.readyonly = readyonly;
	}

	public UFDate getTs() {
		return ts;
	}

	public void setTs(UFDate ts) {
		this.ts = ts;
	}

	public UFBoolean getDr() {
		return dr;
	}

	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}

}
