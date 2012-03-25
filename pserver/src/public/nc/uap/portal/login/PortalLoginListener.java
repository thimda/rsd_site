package nc.uap.portal.login;

import java.util.Map;

import javax.servlet.http.HttpSession;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.listener.AbstractLoginMouseListener;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.util.ToolKit;

/**
 * Portal登陆按钮点击事件
 * 
 * @author licza
 * 
 */
public class PortalLoginListener extends AbstractLoginMouseListener{
	public PortalLoginListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}

	 

	@Override
	public void onclick(MouseEvent event) {
		try {
			AuthenticationUserVO userVO = getLoginHelper().processLogin();
			openTargetUrl(userVO);
		} 
		catch (Exception e) {
			if(e instanceof LfwInteractionException)
				throw (LfwInteractionException)e;
			if(!(e instanceof LoginInterruptedException))
				Logger.error(e.getMessage(), e);
			String msg = e.getMessage();
			if(msg == null || msg.equals(""))
				msg = "登录过程出现错误";
			showErrorToClient(msg);
		}
	}

	protected void openTargetUrl(AuthenticationUserVO userVO) {
		// 主页地址
		String mainPageUrl = getTargetUrl();

		Map<String, String> extMap = (Map<String, String>) userVO.getExtInfo();
		String maxwin = extMap.get("p_maxwin");
		// 最大化打开窗口
		if (maxwin != null && maxwin.equals("Y")) {
			EventRequestContext.getLfwPageContext().openMaxWindow(mainPageUrl, "", true);
		} 
		// 在原来页面打开
		else {
			getGlobalContext().sendRedirect(mainPageUrl);
		}
	}

	@Override
	protected void showErrorToClient(String errorMsg) {
		LfwWidget widget = getCurrentContext().getWidget();
		LabelComp tipLabel = (LabelComp) widget.getViewComponents().getComponent("tiplabel");
		tipLabel.setVisible(true);
//		tipLabel.setText();
		if(errorMsg != null && errorMsg.length() > 16)
			tipLabel.setInnerHTML("<marquee>"+errorMsg+"</marquee>");
		else
			tipLabel.setText(errorMsg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LoginHelper<PtSessionBean> getLoginHelper() {
		return (LoginHelper<PtSessionBean>) LfwLoginFetcher.getGeneralInstance().getLoginHelper();
//		LoginHelper<PtSessionBean> helper = new LoginHelper<PtSessionBean>() {
//			@Override
//			public ILoginHandler<PtSessionBean> createLoginHandler() {
//				LfwPageContext ctx = EventRequestContext.getLfwPageContext();
////				LfwWidget widget = ctx.getWidgetContext("main").getWidget();
//				ILoginHandler<PtSessionBean> handler = new PortalLoginHandler();
////				handler.setCurrentWidget(widget);
//				return handler;
//			}
//		};
//		return helper;
	}
	
	protected String getTargetUrl() {
		String mainPageUrl = LfwRuntimeEnvironment.getRootPath() + PortalEnv.DEFAULT_PAGE;
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		String currentUrl = (String)session.getAttribute(SessionConstant.CURRENT_URL);
		if(ToolKit.notNull(currentUrl)){
			mainPageUrl = currentUrl;
			session.removeAttribute(SessionConstant.CURRENT_URL);
		}
		return mainPageUrl;
	}
}
