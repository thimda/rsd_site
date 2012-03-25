package nc.uap.portal.container.driver;

import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.service.itf.NamespaceMapper;


/**
 * Default implementation of <code>NamespaceMapper</code> interface.
 */
public class DefaultNamespaceMapper implements NamespaceMapper {

    public DefaultNamespaceMapper() {
    	// Do nothing.
    }

    // NamespaceMapper Impl ----------------------------------------------------

    public String encode(PortletWindowID portletWindowId, String name) {
        StringBuffer buffer = new StringBuffer(80);
        buffer.append("NCPORTAL_");
        buffer.append(portletWindowId.getStringId());
        buffer.append('_');
        buffer.append(name);
        return buffer.toString();
    }

    public String decode(PortletWindowID portletWindowId, String name) {
        if (!name.startsWith("NCPORTAL_")) {
            return null;
        }
        StringBuffer buffer = new StringBuffer(80);
        buffer.append("NCPORTAL_");
        buffer.append(portletWindowId.getStringId());
        buffer.append('_');
        if (!name.startsWith(buffer.toString())) {
            return null;
        }
        return name.substring(buffer.length());
    }
}
