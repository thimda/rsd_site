package nc.uap.portal.deploy.itf;

import nc.uap.portal.deploy.vo.PortalDeployDefinition;

/**
 * 部署Portal模块
 * 
 * @author licza
 * 
 */
public interface IPtDeploy {
	/**
	 * 部署一个模块
	 * 
	 * @param define 模块信息
	 */
	public void deploy(PortalDeployDefinition define);
}
