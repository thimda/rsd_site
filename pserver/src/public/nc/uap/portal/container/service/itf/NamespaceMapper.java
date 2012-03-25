package nc.uap.portal.container.service.itf;

import nc.uap.portal.container.portlet.PortletWindowID;


/**
 **/
public interface NamespaceMapper {

    String encode(PortletWindowID namespace, String name);
    String decode(PortletWindowID ns, String name);
}
