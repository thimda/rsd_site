package nc.uap.portal.integrate.othersystem.nc5x;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 *   ≈‰NC5.xœµ¡–µƒUSERVO
 * 
 * @author licza
 * 
 */
public class NC5xUserVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7275800850616629366L;
	private String able_time;
	private String authen_type;
	private String cuserid;
	private String disable_time;
	private UFBoolean isca;
	private UFBoolean keyuser;
	private String langcode;
	private UFBoolean locked_tag;
	private String pk_corp;
	private String pwdlevelcode;
	private String pwdparam;
	private Integer pwdtype;
	private Integer dr;
	private UFDateTime ts;
	private String user_code;
	private String user_name;
	private String user_note;
	private String user_password;

	public String getPKFieldName() {
		return "cuserid";
	}

	public String getTableName() {
		return "sm_user";
	}

	public String getAble_time() {
		return able_time;
	}

	public void setAble_time(String able_time) {
		this.able_time = able_time;
	}

	public String getAuthen_type() {
		return authen_type;
	}

	public void setAuthen_type(String authen_type) {
		this.authen_type = authen_type;
	}

	public String getCuserid() {
		return cuserid;
	}

	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}

	public String getDisable_time() {
		return disable_time;
	}

	public void setDisable_time(String disable_time) {
		this.disable_time = disable_time;
	}

	public UFBoolean getIsca() {
		return isca;
	}

	public void setIsca(UFBoolean isca) {
		this.isca = isca;
	}

	public UFBoolean getKeyuser() {
		return keyuser;
	}

	public void setKeyuser(UFBoolean keyuser) {
		this.keyuser = keyuser;
	}

	public String getLangcode() {
		return langcode;
	}

	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}

	public UFBoolean getLocked_tag() {
		return locked_tag;
	}

	public void setLocked_tag(UFBoolean locked_tag) {
		this.locked_tag = locked_tag;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPwdlevelcode() {
		return pwdlevelcode;
	}

	public void setPwdlevelcode(String pwdlevelcode) {
		this.pwdlevelcode = pwdlevelcode;
	}

	public String getPwdparam() {
		return pwdparam;
	}

	public void setPwdparam(String pwdparam) {
		this.pwdparam = pwdparam;
	}

	public Integer getPwdtype() {
		return pwdtype;
	}

	public void setPwdtype(Integer pwdtype) {
		this.pwdtype = pwdtype;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_note() {
		return user_note;
	}

	public void setUser_note(String user_note) {
		this.user_note = user_note;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

}