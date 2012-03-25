package nc.uap.portal.container.driver;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletContext;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.portlet.AbstractPortletConfigImpl;

 
public class PortletConfigImpl extends AbstractPortletConfigImpl{

    protected ResourceBundleFactory bundles;
    
    public PortletConfigImpl(PortletContext portletContext,
                             PortletDefinition portletDD) {
        super(portletContext, portletDD);
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        if(LfwLogger.isDebugEnabled()) {
            LfwLogger.debug("Resource Bundle requested: "+locale);
        }
        if (bundles == null) {
            bundles = new ResourceBundleFactory(portlet, portlet.getPortletInfo());
        }
        return bundles.getResourceBundle(locale);
    }
}
