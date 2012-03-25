/**
 * 
 */
package nc.uap.portal.integrate.credential;

import java.io.Serializable;

import nc.uap.portal.vo.PtSlotVO;

/**
 * @author yzy
 *
 */
public class CredentialWrapper implements Serializable {
	private static final long serialVersionUID = -8996311337472278934L;
	private PtCredentialVO credentail;
	private PtSlotVO slot;
	
	/**
	 * @return Returns the credentail.
	 */
	public PtCredentialVO getCredentail() {
		return credentail;
	}
	
	/**
	 * @param credentail The credentail to set.
	 */
	public void setCredentail(PtCredentialVO credentail) {
		this.credentail = credentail;
	}
	
	/**
	 * @return Returns the slot.
	 */
	public PtSlotVO getSlot() {
		return slot;
	}
	
	/**
	 * @param slot The slot to set.
	 */
	public void setSlot(PtSlotVO slot) {
		this.slot = slot;
	}
}
