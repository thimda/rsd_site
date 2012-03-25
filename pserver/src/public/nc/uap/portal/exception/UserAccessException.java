/**
 * 
 */
package nc.uap.portal.exception;

import nc.vo.pub.BusinessException;


/**
 * @author yzy
 *
 */
public class UserAccessException extends BusinessException {

	private static final long serialVersionUID = -3788664751405873640L;

	public UserAccessException() {
		super();
	}

	/**
	 * @param message
	 */
	public UserAccessException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserAccessException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
