package nc.uap.portal.integrate.message.listener;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.model.PageModel;

/**
 * 消息编辑器页面模型
 * 
 * @author licza
 * 
 */
public class EditorPageModel extends PageModel {
	@Override
	protected void initPageMetaStruct() {
		// 将参数中加入WebSession中
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String cmd = request.getParameter("cmd");
		String pk_message =  request.getParameter("pk_message");
		WebSession ses = getWebContext().getWebSession();
		ses.setAttribute("cmd", cmd);
		ses.setAttribute("pk_message", pk_message);
	}
}
