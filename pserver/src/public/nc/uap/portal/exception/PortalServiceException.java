package nc.uap.portal.exception;

import nc.vo.pub.BusinessException;
/**
 * Portal Service异常,所有bs实现抛出此异常
 * @author dengjt
 * TODO 更改名称为PortalServiceException
 */
public class PortalServiceException extends BusinessException {

	private static final long serialVersionUID = -4851851740319982044L;

	public PortalServiceException() {
		super();
	}

	public PortalServiceException(String message) {
		super(message);
	}

	public PortalServiceException(Throwable cause) {
		super(cause);
	}

	public PortalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
