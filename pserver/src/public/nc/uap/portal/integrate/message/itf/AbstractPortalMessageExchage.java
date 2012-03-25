package nc.uap.portal.integrate.message.itf;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.plugins.impl.DynamicalPluginImpl;

/**
 * 消息交换基类
 * 
 * @author licza
 * 
 */
public abstract class AbstractPortalMessageExchage extends DynamicalPluginImpl
 implements IPortalMessage {
	
	/**
	 * 获得Portal当前用户主键
	 * 
	 * @return
	 */
	public String getPkUser() {
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		if (ses != null)
			return ses.getPk_user();
		throw new LfwRuntimeException("session time out!");
	}
}
