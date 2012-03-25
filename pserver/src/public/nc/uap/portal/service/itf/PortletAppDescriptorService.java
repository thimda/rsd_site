package nc.uap.portal.service.itf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nc.uap.portal.container.om.PortletApplicationDefinition;

/**
 * Read/Write services for Portlet Application configuration
 * This service reads the portlet.xml and converts it to a
 * standard bean model.
 *
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @since Mar 6, 2005
 */

/**
 * @modified by dengjt, 修改以适应Portal的部署结构
 */
public interface PortletAppDescriptorService {

    PortletApplicationDefinition createPortletApplicationDefinition();

    /**
     * Retrieve the PortletApp deployment descriptor
     * (portlet.xml).
     * @return Object representation of the descriptor.
     * @throws IOException if an IO error occurs.
     */
    PortletApplicationDefinition read(String name, InputStream in) throws IOException;

    /**
     * Merge web.xml descriptor meta data with the PortletApplicationDefinition.
     * The Portlet container needs access to (at a minimum) the servlet-mapping url-patterns (PLT.19.3.8)
     * and the optional locale-encoding-mapping-list (PLT.12.7.1)
     * @param pa the PortletApplicationDefinition
     * @param webDescriptor the web.xml InputStream
     * @throws IOException
     */
    void mergeWebDescriptor(PortletApplicationDefinition pa, InputStream webDescriptor) throws Exception;

    /**
     * Write the PortletApp deployment descriptor
     * (portlet.xml).
     * @param portletDescriptor
     * @param out
     * @throws IOException if an IO error occurs.
     */
    void write(PortletApplicationDefinition portletDescriptor, OutputStream out) throws IOException;
}
