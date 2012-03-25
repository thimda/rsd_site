package nc.uap.portal.util;

import nc.uap.lfw.core.log.LfwLogger;


/**
 * Static class that provides utility static methods for argument validation.
 *
 */
public class ArgumentUtility {

    // Static Utility Methods --------------------------------------------------

    /**
     * Validates that the passed-in argument value is not null.
     * @param argumentName  the argument name.
     * @param argument  the argument value.
     * @throws IllegalArgumentException  if the argument value is null.
     */
    public static void validateNotNull(String argumentName, Object argument)
    throws IllegalArgumentException {
        if (argument == null) {
        	if (LfwLogger.isDebugEnabled()) {
        		LfwLogger.debug("Validation failed for argument: " + argumentName
        				+ ": argument should not be null.");
        	}
        	throw new IllegalArgumentException(
        			"Illegal Argument: " + argumentName
        			+ " (argument should not be null)");
        }
    }

    /**
     * Validates that the passed-in string argument value is not null or empty.
     * @param argumentName  the argument name.
     * @param argument  the argument value.
     * @throws IllegalArgumentException  if the argument value is null or empty.
     */
    public static void validateNotEmpty(String argumentName, String argument)
    throws IllegalArgumentException {
        if (argument == null || "".equals(argument)) {
            if (LfwLogger.isDebugEnabled()) {
                LfwLogger.debug("Validation failed for argument: " + argumentName
                		+ ": argument should not be null or empty.");
            }
            throw new IllegalArgumentException(
            		"Illegal Argument: " + argumentName
            		+ " (argument should not be null or empty)");
        }
    }

}
