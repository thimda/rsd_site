package nc.uap.portal.container.service.itf;

import java.util.List;

import javax.portlet.PortletURLGenerationListener;

import nc.uap.portal.container.om.PortletApplicationDefinition;

public interface PortletURLListenerService
{
    List<PortletURLGenerationListener> getPortletURLGenerationListeners(PortletApplicationDefinition app);
}
