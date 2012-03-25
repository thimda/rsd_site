package nc.uap.portal.login;

import java.util.Map;

import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.itf.AbstractLfwIntegrateProvider;
import nc.uap.lfw.login.itf.ILoginHandler;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.vo.LfwFunNodeVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.login.vo.LfwTreeFunNodeVO;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.service.PortalServiceUtil;

/**
 * PortalµÇÂ½²úÉúÕß
 * @author licza
 *
 */
public class PortalLoginProvider extends AbstractLfwIntegrateProvider{

	@Override
	public ExtAuthField[] getAuthFields() {
		return null;
	}

	@Override
	public LfwTreeFunNodeVO[] getFunNodes() {
		return null;
	}

	@Override
	public LfwFunNodeVO[] getFunNodes(Map<String, String> param) {
		return null;
	}

	@Override
	public LoginHelper<? extends LfwSessionBean> getLoginHelper() {
		LoginHelper<PtSessionBean> helper = new LoginHelper<PtSessionBean>() {
			@Override
			public ILoginHandler<PtSessionBean> createLoginHandler() {
				ILoginHandler<PtSessionBean> handler = PortalServiceUtil.getServiceProvider().getLoginHandler();
				return handler;
			}
		};
		return helper;
	}

}
