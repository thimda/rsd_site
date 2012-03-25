package nc.uap.portal.integrate.exception;

import nc.uap.lfw.core.exception.LfwBusinessException;

/**
 * PortalµÇÂ¼Òì³£
 * @author dengjt
 *
 */
public class CredentialValidateException extends LfwBusinessException {

	private static final long serialVersionUID = -8967874979631340675L;

	/**
	 * 
	 */
	public CredentialValidateException() {
		super();
	}

	/**
	 * @param message
	 */
	public CredentialValidateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CredentialValidateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CredentialValidateException(String message, Throwable cause) {
		super(message, cause);
	}
}
