
package nc.uap.portal.container.driver;

import nc.uap.portal.container.service.itf.ContainerInfo;

/**
 * @version $Id: nc portalContainerInfo.java 947094 2010-05-21 17:50:35Z edalquist $
 *
 */
public final class NCPortalContainerInfo implements ContainerInfo
{
    private static final ContainerInfo instance = new NCPortalContainerInfo();
    
    private NCPortalContainerInfo()
    {
    }
    
    public static ContainerInfo getInfo()
    {
        return instance;
    }
    /**
     * Retrieve the name of the container.
     * @return the container name.
     */
    public String getPortletContainerName() {
        return Configuration.getPortletContainerName();
    }

    /**
     * Retrieve the portlet container version.
     *
     * @return container version
     */
    public String getPortletContainerVersion() {
        return Configuration.getPortletContainerVersion();
    }

    /**
     * Retrieve the major version number of the specification which this version
     * of nc portal supports.
     * @return te major specification version.
     */
    public int getMajorSpecificationVersion() {
        return Configuration.getMajorSpecificationVersion();
    }

    /**
     * Retrieve the minor version number of the specification which this version
     * of nc portal supports.
     * @return the minor specification version.
     */
    public int getMinorSpecificationVersion() {
        return Configuration.getMinorSpecificationVersion();
    }

    /**
     * Retrieve the formatted server info String required to be returned by the
     * PortletContext.
     * @return the server info.
     */
    public String getServerInfo() {
        return Configuration.getServerInfo();
    }
}
