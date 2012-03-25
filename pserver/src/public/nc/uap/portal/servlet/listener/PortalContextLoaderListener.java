package nc.uap.portal.servlet.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import nc.uap.lfw.core.servlet.LfwContextLoaderListenerBase;
import nc.uap.lfw.core.servlet.LfwServerListener;
/**
 * Portal≥ı ºªØListener.
 * @author dengjt
 * 
 */
public class PortalContextLoaderListener extends LfwContextLoaderListenerBase {

	private static final long serialVersionUID = 7894934687995498711L;

	public void contextDestroyed(ServletContextEvent ctx) {

	}
	
	public void contextInitialized(ServletContextEvent ctxEvent) {
		super.contextInitialized(ctxEvent);
	}
	@Override
	protected LfwServerListener getSinglePointServerListener(ServletContext ctx) {
		return new PortalBizServerListener(ctx);
	}

	@Override
	protected LfwServerListener getAllServerListener(ServletContext ctx) {
		return new PortalDefaultBizServerListener(ctx);
	}

	
}


