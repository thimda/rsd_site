package nc.uap.portal.user.impl;

import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.portal.user.entity.IUserVO;

/**
 * Portal
 * @author licza
 *
 */
public class CpUser implements IUserVO {

	private CpUserVO vo;
	private String langcode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6013471682637546068L;

	@Override
	public String getLangcode() {
		return langcode;
	}

	@Override
	public String getPassword() {
		return getUser().getUser_password();
	}

	@Override
	public String getPk_user() {
		return getUser().getCuserid();
	}

	@Override
	public String getUserid() {
		return getUser().getUser_code();
	}

	@Override
	public String getUsername() {
		return getUser().getUser_name();
	}

	@Override
	public String getPk_group() {
		return getUser().getPk_group();
	}

	public CpUserVO getUser() {
		return vo;
	}

	public void setUser(CpUserVO vo) {
		this.vo = vo;
	}

	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}

	public CpUser(CpUserVO vo) {
		super();
		this.vo = vo;
	}
	public CpUser() {
		super();
	}

	public CpUser(CpUserVO vo, String langcode) {
		super();
		this.vo = vo;
		this.langcode = langcode;
	}

	@Override
	public Integer getUsertype() {
		return getUser().getUser_type();
	}
	
	
}
