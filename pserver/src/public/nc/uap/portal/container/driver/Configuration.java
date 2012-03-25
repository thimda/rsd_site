package nc.uap.portal.container.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * Central location for Configuration info.
 *
 * @since Jul 2, 2005
 */
public class Configuration {

    public static final ResourceBundle BUNDLE =
        ResourceBundle.getBundle("nc.uap.portal.container.driver.configuration");

    private static final String DESCRIPTOR_SERVICE =
        "PortletAppDescriptorService";

    private static final String CONTAINER_RUNTIME_OPTIONS =
        "supportedContainerRuntimeOptions";

    /**
     * nc.portal.PREVENT_UNECESSARY_CROSS_CONTEXT
     */
    private static final String PREVENT_UNECESSARY_CROSS_CONTEXT =
        "nc.portal.PREVENT_UNECESSARY_CROSS_CONTEXT";

    /**
     * nc.portal.ALLOW_BUFFER
     */
    private static final String BUFFER_SUPPORT =
        "nc.portal.ALLOW_BUFFER";

    private static Boolean prevent;
    private static Boolean buffering;

    public static String getPortletAppDescriptorServiceImpl() {
        String impl = BUNDLE.getString(DESCRIPTOR_SERVICE);
        if (LfwLogger.isDebugEnabled()) {
            LfwLogger.debug("Using Descriptor Service Impl: " + impl);
        }
        return impl;
    }

    public static boolean isBufferingSupported() {
        if (buffering == null) {
            try {
                String buffer = BUNDLE.getString(BUFFER_SUPPORT);
                buffering = new Boolean(buffer);
            } catch (MissingResourceException mre) {
                buffering = Boolean.FALSE;
            }
        }
        return buffering.booleanValue();
    }

    public static List<String> getSupportedContainerRuntimeOptions() {
        String options =  BUNDLE.getString(CONTAINER_RUNTIME_OPTIONS);
        List<String> result = new ArrayList<String>();
        String[] s = options.split(",");
        for (String string : s) {
            result.add(string);
        }
        return result;
    }

    public static boolean preventUnecessaryCrossContext() {
        if (prevent == null) {
            try {
                String test = BUNDLE.getString(PREVENT_UNECESSARY_CROSS_CONTEXT);
                prevent = new Boolean(test);
            } catch (MissingResourceException mre) {
                LfwLogger.warn(mre.getMessage());
                prevent = Boolean.FALSE;
            }
        }
        return prevent.booleanValue();
    }

    /**
     * Retrieve the name of the container.
     * @return the container name.
     */
    public static final String getPortletContainerName() {
        return BUNDLE.getString("container.name");
    }

    /**
     * Retrieve the portlet container version.
     *
     * @return container version
     */
    public static final String getPortletContainerVersion() {
        return BUNDLE.getString("container.version");
    }

    /**
     * Retrieve the major version number of the specification which this version
     * of nc portal supports.
     * @return te major specification version.
     */
    public static final int getMajorSpecificationVersion() {
        return Integer.parseInt(BUNDLE.getString("javax.portlet.version.major"));
    }

    /**
     * Retrieve the minor version number of the specification which this version
     * of nc portal supports.
     * @return the minor specification version.
     */
    public static final int getMinorSpecificationVersion() {
        return Integer.parseInt(BUNDLE.getString("javax.portlet.version.minor"));
    }

    /**
     * Retrieve the formatted server info String required to be returned by the
     * PortletContext.
     * @return the server info.
     */
    public static final String getServerInfo() {
        StringBuffer sb = new StringBuffer(getPortletContainerName())
            .append("/")
            .append(getPortletContainerVersion());
        return sb.toString();
    }
}
