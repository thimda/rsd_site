package nc.uap.portal.container.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURLGenerationListener;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.Listener;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.service.itf.PortletURLListenerService;


public class PortletURLListenerServiceImpl implements PortletURLListenerService
{
    public List<PortletURLGenerationListener> getPortletURLGenerationListeners(PortletApplicationDefinition app)
    {
        List<PortletURLGenerationListener> listeners = new ArrayList<PortletURLGenerationListener>();
        //this list is needed for the classnames
        List<? extends Listener> portletURLFilterList = app.getListeners();
        //Iterate over the classnames and for each entry in the list the filter..URL is called.
        if (portletURLFilterList != null){
            for (Listener listener : portletURLFilterList) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class<? extends Object> clazz;
                try {
                    clazz = loader.loadClass(listener.getListenerClass() );
                    if (clazz != null){
                        listeners.add((PortletURLGenerationListener)clazz.newInstance());
                    }
                } catch (ClassNotFoundException e) {
                    LfwLogger.error(e.getMessage(), e);
                } catch (InstantiationException e) {
                	LfwLogger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                	LfwLogger.error(e.getMessage(), e);
                }
            }
        }
        return listeners;
    }
}
