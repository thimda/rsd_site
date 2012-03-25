
package nc.uap.portal.container.portlet;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import nc.uap.portal.container.om.PortletDefinition;


 

/**
 * Thin representation of the portlet window for which the container
 * request should be processed.  The PortletWindow is used internally
 * to map the request to the configured Portlet Application and Portlet.
 *
 * @see nc.uap.portal.container.om.PortletDefinition
 *
 * @version 1.0
 * @since Sep 22, 2004
 */
public interface PortletWindow {

    /**
     * Retrieve this windows unique id which will be
     *  used to communicate back to the referencing portal.
     * @return unique id.
     */
    PortletWindowID getId();

    /**
     * Retrieve the current window state for this window.
     * @return the current window state.
     */
    WindowState getWindowState();

    /**
     * Retrieve the current portlet mode for this window.
     * @return the current portlet mode.
     */
    PortletMode getPortletMode();

    /**
     * Returns the portlet description. The return value cannot be NULL.
     * @return the portlet description.
     */
    PortletDefinition getPortletDefinition();
}
