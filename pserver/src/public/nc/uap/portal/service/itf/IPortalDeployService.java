package nc.uap.portal.service.itf;

import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;


public interface IPortalDeployService {
	public void deployAll();
	public void deployModule(PortalDeployDefinition define);
	public void updateModule(PortalDeployDefinition define) throws PortletContainerException;
}
