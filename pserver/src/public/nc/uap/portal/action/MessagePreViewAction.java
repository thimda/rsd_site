package nc.uap.portal.action;

import java.util.Map;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.After;
import nc.uap.lfw.servletplus.annotation.Before;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.itf.IPortalMessage;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.util.PtUtil;

/**
 * 消息预览页面
 * 
 * @author licza
 * 
 */
@Servlet(path = "/message")
public class MessagePreViewAction extends BaseAction {
	
	
	@Before
	public void before(){
		boolean ios = LfwRuntimeEnvironment.getBrowserInfo().isIos();
		if(!ios)
			return;
		print("<div style='height:290px;overflow:auto;'>");
	}
	/**
	 * 用来包装ios下宽度
	 */
	@After
	public void after(){
		boolean ios = LfwRuntimeEnvironment.getBrowserInfo().isIos();
		if(!ios)
			return;
		print("</div>");
	}
	/**
	 * 预览信息内容
	 */
	@Action
	public void preview() {
		String pk = request.getParameter("id");
		if (PtUtil.isNull(pk))
			return;
		String systemcode = request.getParameter("systemcode");
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		if (ses == null)
			return;
		String usercode = ses.getPk_user();
		try {
			Map<String,String> dic = new MessageCenter().getSys2PluginDic();
			if(dic == null){
				print("error: no plugin message!");
			}else{
				String pluginid = dic.get(systemcode);
				IPortalMessage me = MessageCenter.lookup(pluginid);
				PtMessageVO message = me.getMessage(pk);
				if(message == null)
					return;
				/**
				 * 非用户接收或者发送邮件不显示
				 */
				if (!usercode.equals(message.getPk_user()) && !usercode.equals(message.getPk_sender()))
					return;
				print(message.doGetContent());
			}
		} catch (CpbBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
}
