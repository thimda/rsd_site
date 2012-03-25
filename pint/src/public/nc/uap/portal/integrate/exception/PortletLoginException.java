/**
 * 
 */
package nc.uap.portal.integrate.exception;

import nc.vo.pub.BusinessException;

/**
 * PortalµÇÂ¼Òì³£
 * @author dengjt
 *
 */
public class PortletLoginException extends BusinessException {

	private static final long serialVersionUID = -8967874979631340675L;

	/**
	 * 
	 */
	public PortletLoginException() {
		super();
	}

	/**
	 * @param message
	 */
	public PortletLoginException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PortletLoginException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PortletLoginException(String message, Throwable cause) {
		super(message, cause);
	}
}
