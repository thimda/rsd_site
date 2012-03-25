package nc.uap.portal.servlet.listener;

import javax.servlet.http.HttpSessionEvent;

import nc.uap.lfw.core.servlet.dft.LfwDefaultSessionListener;

/**
 * 进行session事件监听
 * @author dengjt
 */
public class CountSessionListener extends LfwDefaultSessionListener{

	public void sessionCreated(HttpSessionEvent ses) {
		super.sessionCreated(ses);
	}

	public void sessionDestroyed(HttpSessionEvent ses) {
		super.sessionDestroyed(ses);
	}
}
