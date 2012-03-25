package nc.uap.portal.container.service.impl;

import nc.uap.portal.container.portlet.FilterManagerImpl;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.service.itf.FilterManager;
import nc.uap.portal.container.service.itf.FilterManagerService;


/**
 * @version $Id: FilterManagerServiceImpl.java 771572 2009-05-05 06:02:17Z cziegeler $
 *
 */
public class FilterManagerServiceImpl implements FilterManagerService {

    /**
     * @see nc.uap.portal.container.service.itf.FilterManagerService#getFilterManager(nc.uap.portal.container.portlet.PortletWindow, java.lang.String)
     */
    public FilterManager getFilterManager(PortletWindow portletWindow, String lifeCycle) {
        return new FilterManagerImpl(portletWindow, lifeCycle);
    }
}
