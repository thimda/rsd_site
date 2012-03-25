package nc.uap.portal.container.service.itf;

import nc.uap.portal.container.portlet.PortletWindow;


/**
 * Service to retrieve a FilterManager for a specific Portlet in a lifecycle
 */
public interface FilterManagerService {

    /**
     * Returns the FilterManager, this is used to process the filter.
     * @param window The portlet window.
     * @return FilterManager
     */
    FilterManager getFilterManager(PortletWindow window, String lifeCycle);
}
