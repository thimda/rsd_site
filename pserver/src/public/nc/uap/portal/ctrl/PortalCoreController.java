package nc.uap.portal.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwCoreController;
import nc.uap.lfw.core.LfwRuntimeEnvironment;

/**
 * Portal专用Lfw处理器
 * @author dengjt
 *
 */
public class PortalCoreController extends LfwCoreController{

	protected void beforeInitPageModel(HttpServletRequest req, HttpServletResponse res) {
		String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
//		if(pageId.equals("login")){
//			req.setAttribute(MODEL, PageModel.class.getName());
//		}
//		if(pageId.equals("reference")){
//			req.setAttribute(MODEL, DefaultReferencePageModel.class.getName());
//		}
//		else if(pageId.endsWith(".qry")){
//			req.setAttribute(MODEL, NCQueryTemplatePageModel.class.getName());
//		}
	}

}
