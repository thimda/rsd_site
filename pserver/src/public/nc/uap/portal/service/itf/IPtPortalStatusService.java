package nc.uap.portal.service.itf;

import nc.uap.portal.deploy.PortalDeployMessage;

/**
 * Portal状态服务
 * 
 * @author licza
 * 
 */
public interface IPtPortalStatusService {
	/**
	 * 标记portal核心启动完成状态
	 */
	void signPortalCoreStartComplete(PortalDeployMessage msg);
	
	/**
	 * portal核心是否启动完毕
	 * @return
	 */
	boolean isPortalCoreStartComplete();
	/**
	 * 获得Portal核心启动时产生的环境信息
	 * @return
	 */
	PortalDeployMessage getDeployMessage();
}
