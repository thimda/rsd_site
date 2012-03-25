package nc.uap.portal.exception;

import nc.vo.pub.BusinessException;

/**
 * 用户被锁定异常
 * @author zhangxya
 *
 */
public class UserLockedException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public UserLockedException() {
		super();
	}

	/**
	 * @param message
	 */
	public UserLockedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UserLockedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserLockedException(String message, Throwable cause) {
		super(message, cause);
	}
}
