package nc.portal.portlet.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class PtPageRoleVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_pagerole;
	private String pk_page;
	private String pk_role;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	public String getPk_pagerole() {
		return pk_pagerole;
	}
	public void setPk_pagerole(String pk_pagerole) {
		this.pk_pagerole = pk_pagerole;
	}
	public String getPk_page() {
		return pk_page;
	}
	public void setPk_page(String pk_page) {
		this.pk_page = pk_page;
	}
	public String getPk_role() {
		return pk_role;
	}
	public void setPk_role(String pk_role) {
		this.pk_role = pk_role;
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
		return "pk_pagerole";
	}
	@Override
	public String getTableName() {
		return "pt_pagerole";
	}
}
