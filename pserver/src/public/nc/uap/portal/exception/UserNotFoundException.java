/**
 * 
 */
package nc.uap.portal.exception;

import nc.vo.pub.BusinessException;


/**
 * @author yzy
 *
 */
public class UserNotFoundException extends BusinessException {
	
	private static final long serialVersionUID = 6983983842739593159L;

	public UserNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
