package nc.uap.portal.integrate.credential;

import nc.vo.pub.SuperVO;

/**
 * ƾ֤VO
 * @author zhangxya
 *
 */
public class PtCredentialVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_credential;
	private String fk_slot;
	private String userid;
	private String password;
	private String reference;
	private ICredentialReference credentialReference = new CredentialReferenceImpl();
	
	public ICredentialReference getCredentialReference() {
		return credentialReference;
	}
	public void setCredentialReference(ICredentialReference credentialReference) {
		this.credentialReference = credentialReference;
	}
	
	
	@Override
	public String getPKFieldName() {
		return "pk_credential";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "pt_credential";
	}

	public String getPk_credential() {
		return pk_credential;
	}

	public void setPk_credential(String pk_credential) {
		this.pk_credential = pk_credential;
	}

	public String getFk_slot() {
		return fk_slot;
	}

	public void setFk_slot(String fk_slot) {
		this.fk_slot = fk_slot;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
