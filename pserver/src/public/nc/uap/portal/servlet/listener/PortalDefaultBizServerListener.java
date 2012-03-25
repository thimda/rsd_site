package nc.uap.portal.servlet.listener;

import javax.servlet.ServletContext;

import nc.bs.framework.cluster.itf.ClusterMessage;
import nc.bs.framework.execute.Executor;
import nc.bs.framework.server.BusinessAppServer;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServerListener;
import nc.uap.portal.deploy.impl.PortalClusterDeployListener;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalStatusService;

/**
 * 此listener在容器启动后回调，确保相应模块正确初始化
 * 
 * @author licza
 */
public class PortalDefaultBizServerListener extends LfwServerListener {
	public PortalDefaultBizServerListener(ServletContext ctx) {
		super(ctx);
	}

	@Override
	protected void doAfterStarted() {
		new Executor(new Runnable() {
			public void run() {
				boolean coreStarting = true;
				while (coreStarting) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						LfwLogger.error(e.getMessage(), e);
					}
					IPtPortalStatusService portalStatusService = PortalServiceUtil
							.getPortalStatusService();
					boolean status = portalStatusService.isPortalCoreStartComplete();
					if (status) {
						coreStarting = false;
						ClusterMessage message = portalStatusService.getDeployMessage();
						new PortalClusterDeployListener().onMessage(message);
					}
				}
			}
		}).start();
	}

}
