package nc.portal.portlet.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class PtPageUserVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_pageuser;
	private String pk_page;
	private String pk_user;
	private UFBoolean dr;
	private UFDateTime ts;
	
	public String getPk_pageuser() {
		return pk_pageuser;
	}
	public void setPk_pageuser(String pk_pageuser) {
		this.pk_pageuser = pk_pageuser;
	}
	public String getPk_page() {
		return pk_page;
	}
	public void setPk_page(String pk_page) {
		this.pk_page = pk_page;
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
		return "pk_pageuser";
	}
	@Override
	public String getTableName() {
		return "pt_pageuser";
	}
	public String getPk_user() {
		return pk_user;
	}
	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

}
