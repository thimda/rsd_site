package nc.uap.portal.integrate.message.listener;


import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.PageServerListener;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.MessageQueryParam;
/**
 * 消息中心后处理Listener
 * <pre>主要作用:根据传入的系统编码,打开对应的消息列表</pre>
 * @author licza
 *
 */
public class MessagePageListener extends PageServerListener{

	public MessagePageListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void afterPageInit(PageEvent e) {
		WebSession  ses = getGlobalContext().getWebSession();
		String parentid = (String)ses.getAttribute("parentid");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget("main");
		Dataset msgds =	widget.getViewModels().getDataset("msgds");
		String syscode = (String)ses.getAttribute("syscode");
		String pluginid = (String)ses.getAttribute("pluginid");
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		MessageQueryParam param = new MessageQueryParam(syscode,parentid,0,new String[]{"0"},pk_user,pluginid);
		if(!param.isValdate())
			return;
		MessageCenter.processMessageQry(param, msgds, true);
		String[] tile = MessageCenter.getTile(param);
		if(tile != null && tile.length == 2){
			getGlobalContext().addExecScript("showTab('"+tile[0]+"');");
			getGlobalContext().addExecScript("setSidleTitle('"+tile[1]+"');");
		}
		
	}

	@Override
	public void beforeActive(PageEvent e) {
		
	}

}
