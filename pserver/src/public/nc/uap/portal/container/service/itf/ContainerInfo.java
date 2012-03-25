package nc.uap.portal.container.service.itf;

/**
 * Information about the container.
 */
public interface ContainerInfo {

    String getServerInfo();
    String getPortletContainerName();
    String getPortletContainerVersion();
    int getMajorSpecificationVersion();
    int getMinorSpecificationVersion();
}
