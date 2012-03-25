package nc.uap.portal.view.menu;


import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.menuitem.MenuRoot;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.event.AppRequestProcessor;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * 快速构建树及带有确定取消按钮的基类
 * @author dengjt
 *
 */
public class OutLookMenuPageModel extends PageModel {
	private static final String TREE = "tree";
	private static final String TREEDS = "treeds";
	private static final String WIDGET_ID = "main";
	public static final String ID_FIELD = "id";
	public static final String PID_FIELD = "pid";
	public static final String URL_FIELD = "url";
	public static final String LABEL_FIELD = "label";
	protected MenuRoot[] roots ;
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		UIMeta uimeta = new UIMeta();
		
		UIWidget widget = new UIWidget();
		widget.setId(WIDGET_ID);
		UIMeta wuimeta = new UIMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		
		constructViewUI(wuimeta);
		
		uimeta.setElement(widget);
		return uimeta;
	}

	private void constructViewUI(UIMeta wuimeta) {
		UIShutter layout =  new UIShutter();
		layout.setId("outlookbar1");
		layout.setWidgetId(WIDGET_ID);
		for (int i = 0; i < roots.length; i++) {
			MenuRoot root = roots[i];
			UIShutterItem item = new UIShutterItem();
			item.setText(root.getTitle());
			item.setId("UIShutterItem" + root.getPk_parent() );
			layout.addPanel(item);

			UIElement element = new UITreeComp();
			element.setId(TREE + root.getPk_parent());
			element.setWidgetId(WIDGET_ID);
			item.setElement(element);
		}
		wuimeta.setElement(layout);
	}
	 
 
	
	
	@Override
	protected PageMeta createPageMeta() {
		String category = LfwRuntimeEnvironment.getWebContext().getParameter("category");
		if(category == null || category.length() ==0)
			category = "0000AA10000000002SBA";
		roots = getRoot(category);
		PageMeta pm = new PageMeta();
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		LfwWidget widget = new LfwWidget();
		constructWidget(widget);
		widget.setId(WIDGET_ID);
		pm.addWidget(widget);
		if(roots != null && roots.length > 0){
			for(MenuRoot root : roots){
				Dataset ds = new Dataset();
				ds.setId(root.getPk_parent());
				ds.setLazyLoad(false);
				constructDataset(ds);
				widget.getViewModels().addDataset(ds);
				
				TreeViewComp tree = new TreeViewComp();
				tree.setWithRoot(false);
				tree.setId(TREE + root.getPk_parent());
				constructTree(tree);
				RecursiveTreeLevel level = new RecursiveTreeLevel();
				level.setId("level" + root.getPk_parent());
				constructTreeLevel(level);
				tree.setTopLevel(level);
				widget.getViewComponents().addComponent(tree);
			}
		}
		pm.setFoldPath("/html/nodes/outlook/");
		return pm;
	}

	protected void constructWidget(LfwWidget widget) {
		widget.setControllerClazz(OutLookMenuController.class.getName());
	}

	protected void constructTree(TreeViewComp tree) {
		
	}

	protected void constructTreeLevel(RecursiveTreeLevel level) {
		level.setMasterKeyField(ID_FIELD);
		level.setLabelFields(LABEL_FIELD);
		level.setRecursiveKeyField(ID_FIELD);
		level.setRecursivePKeyField(PID_FIELD);
		level.setDataset(level.getId().replace("level", ""));
	}

	protected void constructDataset(Dataset ds) {
		FieldSet fs = ds.getFieldSet();
		
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		fs.addField(idField);
		
		Field urlField = new Field();
		urlField.setId(URL_FIELD);
		urlField.setText("URL");
		fs.addField(urlField);
		
		Field pidField = new Field();
		pidField.setId(PID_FIELD);
		pidField.setText("PID");
		fs.addField(pidField);
		
		Field labelField = new Field();
		labelField.setId(LABEL_FIELD);
		labelField.setText("LABEL");
		fs.addField(labelField);
		
		EventConf dsLoadEvent = new EventConf();
		dsLoadEvent.setName(DatasetListener.ON_DATA_LOAD);
		dsLoadEvent.setMethodName("dataLoad");
		dsLoadEvent.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(dsLoadEvent);
		
		EventConf rowSelectEvent = new EventConf();
		rowSelectEvent.setName(DatasetListener.ON_AFTER_ROW_SELECT);
		rowSelectEvent.setMethodName("onAfterRowSelect");
		rowSelectEvent.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(rowSelectEvent);
	 
	}
	/**
	 * 获得根目录
	 * @param linkgroup
	 * @return
	 */
	private MenuRoot[] getRoot(String linkgroup){
		try {
			ICpMenuQry menuQry = NCLocator.getInstance().lookup(ICpMenuQry.class);

			 return menuQry.getMenuRoot(linkgroup);
		} catch (Exception e1) {
			LfwLogger.error("查询根目录失败",e1);
		}
		return new MenuRoot[0];
	}
}
