package nc.uap.portal.menu.pagemodel;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.model.PageModel;

/**
 * 树状菜单页面模型类
 * @author licza
 *
 */
public class MenuTreeViewPageModel extends PageModel {

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		WebContext ctx = LfwRuntimeEnvironment.getWebContext();
		String category = ctx.getParameter("category");
		ctx.getWebSession().setAttribute("category", category);
	}

}
