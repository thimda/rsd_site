package nc.uap.portal.vo;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * 根据单据类型指定代理人配置信息
 * @author licza
 *
 */
public class PtFrmAgentVO extends SuperVO {
	private static final long serialVersionUID = -9098101226610245790L;
	private String pk_agentsetting;
	private String pk_user;
	private String pk_frmdef;
	private String frmdefname;
	private String userpks;
	private String usernames;
	private UFBoolean useflag;
	private UFDateTime startdate;
	private UFDateTime stopdate;
	private UFDateTime ts;
	private UFBoolean dr;
	public String getPk_agentsetting() {
		return pk_agentsetting;
	}
	public void setPk_agentsetting(String pk_agentsetting) {
		this.pk_agentsetting = pk_agentsetting;
	}
	public String getPk_frmdef() {
		return pk_frmdef;
	}
	public void setPk_frmdef(String pk_frmdef) {
		this.pk_frmdef = pk_frmdef;
	}
	public String getFrmdefname() {
		return frmdefname;
	}
	public void setFrmdefname(String frmdefname) {
		this.frmdefname = frmdefname;
	}
	public String getUserpks() {
		return userpks;
	}
	public void setUserpks(String userpks) {
		this.userpks = userpks;
	}
	public String getUsernames() {
		return usernames;
	}
	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}
	public UFBoolean getUseflag() {
		return useflag;
	}
	public void setUseflag(UFBoolean useflag) {
		this.useflag = useflag;
	}
	public UFDateTime getStartdate() {
		return startdate;
	}
	public void setStartdate(UFDateTime startdate) {
		this.startdate = startdate;
	}
	public UFDateTime getStopdate() {
		return stopdate;
	}
	public void setStopdate(UFDateTime stopdate) {
		this.stopdate = stopdate;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public UFBoolean getDr() {
		return dr;
	}
	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}
	public String getPk_user() {
		return pk_user;
	}
	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}
	public String getPKFieldName() {
		return "pk_agentsetting";
	}
	public String getTableName() {
		return "wfm_agentsetting";
	}
	
}