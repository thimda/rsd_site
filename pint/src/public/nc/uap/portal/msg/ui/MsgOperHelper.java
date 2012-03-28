package nc.uap.portal.msg.ui;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.portal.msg.model.MsgCategory;
import nc.uap.portal.msg.provide.IMsgProvide;
import nc.uap.portal.plugins.PluginManager;

/**
 * 消息操作类
 * 
 * @author licza
 * 
 */
public class MsgOperHelper {
	/**
	 * 树节点数据集的id
	 */
	public static final String TREE_DS = "cateds";
	/**
	 * 消息盒的TabID
	 */
	public static final String MSG_BOX_TAB = "msgBoxTab";
	/**
	 * 最后一个消息盒编号
	 */
	public static final String LAST_ITEM_INDEX = "1";
	/**
	 * 第二个消息盒数据集ID
	 */
	public static final String BOX2_DS = "sentds";
	/**
	 * 第一个消息盒数据集ID
	 */
	public static final String BOX1_DS = "msgds";

	/**
	 * 获得消息插件
	 * 
	 * @param pluginid
	 * @param pm
	 */
	public static IMsgProvide getMsgProvide(String pluginid) {
		PluginManager pm = PluginManager.newIns();
		try {
			return pm.getExtension(pluginid).newInstance(IMsgProvide.class);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获得当前的消息盒子
	 * 
	 * @return
	 */
	public static int getCurrentMsgBoxIndex() {
		/**
		 * 设置消息盒的名称到状态栏,并选中第一个
		 */
		UITabComp boxTab = getBoxTab();

		String currentItem = boxTab.getCurrentItem();

		if (LAST_ITEM_INDEX.equals(currentItem)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 从View上下文中获取消息盒容器
	 * 
	 * @return
	 */
	public static UITabComp getBoxTab() {
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext().getViewContext("main");
		// return
		// (UITabComp)UIElementFinder.findElementById(viewCtx.getUIMeta(),
		// MSG_BOX_TAB);
		return (UITabComp) viewCtx.getUIMeta().findChildById(MSG_BOX_TAB);
	}

	/**
	 * 获得当前的dataset
	 * 
	 * @return
	 */
	public static Dataset getCurrentDataset(int idx) {
		
		String dsName = BOX1_DS;
		if (idx == 1) {
			dsName = BOX2_DS;
		} else if (idx == 2)
			dsName = TREE_DS;
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext().getViewContext("main");

		return viewCtx.getView().getViewModels().getDataset(dsName);
	}
	
	/**
	 * 获得当前分类
	 * @return
	 */
	public static MsgCategory getCurrentCategory() {
		ViewContext viewCtx = AppLifeCycleContext.current().getWindowContext().getViewContext("cate");
		Dataset ds = viewCtx.getView().getViewModels().getDataset("cateds");
		Row currentRow = ds.getSelectedRow();

		int pluginidIdx = ds.nameToIndex(MsgCategory.PLUGINID);
		int idIdx = ds.nameToIndex(MsgCategory.ID);

		String id = currentRow.getString(idIdx);
		String pluginid = currentRow.getString(pluginidIdx);

		/**
		 * 根据插件ID获得当前消息插件
		 */
		IMsgProvide provide = MsgOperHelper.getMsgProvide(pluginid);

		/**
		 * 查找当前消息分类
		 */
		return MsgDataTranslator.findCategoryById(id, provide.getCategory());
	}
	public static FromWhereSQL getQryParam() {
		WebSession ws = LfwRuntimeEnvironment.getWebContext().getWebSession();
		FromWhereSQL qryParam = (FromWhereSQL) ws.getAttribute("MSG_QRY_PARAM");
		return qryParam;
	}
}
