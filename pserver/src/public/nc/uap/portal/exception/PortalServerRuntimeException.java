package nc.uap.portal.exception;

import nc.uap.lfw.core.exception.LfwRuntimeException;

public class PortalServerRuntimeException extends LfwRuntimeException {
	private static final long serialVersionUID = 8774863645419572267L;

	public PortalServerRuntimeException(String message, String hint,
			Throwable cause) {
		super(message, hint, cause);
	}

	public PortalServerRuntimeException(String message, String hint) {
		super(message, hint);
	}

	public PortalServerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PortalServerRuntimeException(String message) {
		super(message);
	}

	public PortalServerRuntimeException(Throwable cause) {
		super(cause);
	}

}
