package nc.uap.portal.login;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.login.listener.LfwLoginPageListener;

/**
 * µÇÂ¼Ò³Ãæ¼ÓÔØListener
 * 
 *
 */
public class PortalLoginPageListener extends LfwLoginPageListener {

	public PortalLoginPageListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void afterPageInit(PageEvent e) {
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(WebContext.LOGIN_DID, null);
		String userId = (String)LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getAttribute("thirdPartyUserId");
		getGlobalContext().addExecScript("if(showRanImg){$ge('$d_flowv6_4').style.height='27px';$ge('randimage').style.display='';var comp = pageUI.getWidget('main').getComponent('randimg');comp.showV();}");
		if(userId != null){
			LfwRuntimeEnvironment.getWebContext().getRequest().getSession().removeAttribute("thirdPartyUserId");
			LfwWidget widget = EventRequestContext.getLfwPageContext().getWidgetContext("main").getWidget();
			TextComp userIdComp = (TextComp) widget.getViewComponents().getComponent("userid");
			userIdComp.setValue(userId);
		}
	}
	
	@Override
	public void onClosed(PageEvent e) {
		super.onClosed(e);
	}
	
}
