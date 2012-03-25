package nc.portal.portlet.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class PtPageDeptVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_pagedept;
	private String pk_page;
	private String pk_dept;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	public String getPk_pagedept() {
		return pk_pagedept;
	}
	public void setPk_pagedept(String pk_pagedept) {
		this.pk_pagedept = pk_pagedept;
	}
	public String getPk_page() {
		return pk_page;
	}
	public void setPk_page(String pk_page) {
		this.pk_page = pk_page;
	}
	public String getPk_dept() {
		return pk_dept;
	}
	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}
	public UFBoolean getDr() {
		return dr;
	}
	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	@Override
	public String getPKFieldName() {
		return "pk_pagedept";
	}
	@Override
	public String getTableName() {
		return "pt_pagedept";
	}
}
