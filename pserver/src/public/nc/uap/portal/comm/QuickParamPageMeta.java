package nc.uap.portal.comm;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.model.PageModel;
/**
 * 将传入的参数放到websession中
 * @author licza
 *
 */
public class QuickParamPageMeta extends PageModel{

	@Override
	protected void initPageMetaStruct() {
		// 将参数中加入WebSession中
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		Enumeration enu = request.getParameterNames();
		WebSession ses = getWebContext().getWebSession();
		if(enu != null){
			while(enu.hasMoreElements()){
				String en = (String) enu.nextElement();
				String val = request.getParameter(en);
				ses.setAttribute(en, val);
			}
		}
	}

}
