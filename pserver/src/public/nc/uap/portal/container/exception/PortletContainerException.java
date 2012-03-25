package nc.uap.portal.container.exception;

/**
 * Thrown when an internal portlet container exception occurs within nc portal.
 * This type of exception indicates an error from which the container is not
 * able to recover.
 *
 * @version 1.0
 */
public class PortletContainerException extends Exception {
    private static final long serialVersionUID = 1737400909124312241L;
    private Throwable cause;

    /**
     * Constructs a new PortletContainerException.
     * This exception will have no message and no root cause.
     */
    public PortletContainerException() {

    }

    /**
     * Constructs a new PortletContainerException with the given message.
     * @param text the message explaining the exception occurance
     */
    public PortletContainerException(String text) {
        super(text);
    }

    /**
     * Constructs a new PortletContainerException with the given message and
     * root cause.
     * @param text  the message explaining the exception occurance
     * @param cause the root cause of the is exception
     */
    public PortletContainerException(String text, Throwable cause) {
        super(text, cause);
        this.cause = cause;
    }

    /**
     * Constructs a new portlet invoker exception when the portlet needs to
     * throw an exception. The exception's message is based on the localized
     * message of the underlying exception.
     * @param cause the root cause
     */
    public PortletContainerException(Throwable cause) {
        super(cause.getLocalizedMessage(), cause);
        this.cause = cause;
    }

    /**
     * Returns the exception that cause this portlet exception.
     * @return the <CODE>Throwable</CODE> that caused this portlet exception.
     */
    public Throwable getRootCause() {
        return cause;
    }
}
