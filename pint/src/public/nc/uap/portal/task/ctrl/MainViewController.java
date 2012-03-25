package nc.uap.portal.task.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.portal.task.itf.ITaskQryTmp;
import nc.uap.portal.task.ui.TaskDisplayHelper;
import nc.uap.portal.util.ToolKit;
import nc.uap.wfm.vo.WfmTaskVO;

/**
 * @author chouhl
 */
public class MainViewController implements IController {
	private static final String STATUS_CARD = "status_card";
	private static final String TASK_QRY_PARAM = "TASK_QRY_PARAM";
	private static final String ID = "id";
	private static final String TASKSTATUS = "taskstatus";
	private static final String DS_TASK = "ds_task";
	private static final String WFMTASKQRY = "wfmtaskqry";
	private static final long serialVersionUID = 1L;
	public void pluginsimplequery_plugin(Map<String, Object> keys) {
		FromWhereSQL whereSql = (FromWhereSQL) keys.get("whereSql");
		String wheresql = whereSql.getWhere();
		TaskQryParam qryParam = getQryParam();
		//processQryParam(keys, qryParam);
		qryParam.setWheresql(wheresql);
		fillDs(qryParam);
	}
	
	/**
	 * Plugin 执行操作
	 * 
	 * @param keys
	 */
	public void plugindoFilterTaskStatus(Map<String, Object> keys) {
		TaskQryParam qryParam = getQryParam();
		ViewContext viewContex = AppLifeCycleContext.current().getViewContext();
		processQryParam(keys, qryParam);

		initQryState(qryParam, viewContex);

		fillDs(qryParam);
	}

	/**
	 * 填充Dataset
	 * 
	 * @param qryParam
	 */
	private void fillDs(TaskQryParam qryParam) {
		Dataset ds = getCurrentView().getViewModels().getDataset(DS_TASK);
		String key = ds.getCurrentKey();
		if (key == null)
			ds.setCurrentKey(Dataset.MASTER_KEY);
		PaginationInfo pi = ds.getCurrentRowSet().getPaginationInfo();
		ITaskQryTmp qry = TaskDisplayHelper.getTaskQry(getSystemCode());
		WfmTaskVO[] vos = qry.qryTaskList(qryParam, pi);
		if (vos != null){
			new SuperVO2DatasetSerializer().serialize(vos, ds, 0);
			ds.setRowSelectIndex(0);
		}
	}

	/**
	 * 设置默认显示状态
	 * 
	 * @param qryParam
	 * @param viewContex
	 */
	private void initQryState(TaskQryParam qryParam, ViewContext viewContex) {
		UICardLayout card = (UICardLayout) UIElementFinder.findElementById(
				viewContex.getUIMeta(), STATUS_CARD);
		if (TaskQryParam.ID_TASK.equals(qryParam.getId())) {
			card.setCurrentItem("0");
			ComboBoxComp c001 = (ComboBoxComp) getCurrentView()
					.getViewComponents().getComponent("c001");
			c001.setValue(TaskQryParam.STATUS_UNREAD);
		} else {
			card.setCurrentItem("1");
			ComboBoxComp c002 = (ComboBoxComp) getCurrentView()
					.getViewComponents().getComponent("c002");
			c002.setValue(TaskQryParam.STATUS_UNREAD);
		}
	}

	/**
	 * 处理查询参数
	 * 
	 * @param keys
	 * @param qryParam
	 */
	void processQryParam(Map<String, Object> keys, TaskQryParam qryParam) {
		TranslatedRow row = (TranslatedRow) keys.get(TASKSTATUS);
		Object id = row.getValue(ID);
		if (id != null) {
			qryParam.setId((String) id);
			qryParam.setStatus(TaskQryParam.STATUS_UNREAD);
		}
	}

	/**
	 * 获取查询参数
	 * 
	 * @return
	 */
	TaskQryParam getQryParam() {
		WebSession ws = LfwRuntimeEnvironment.getWebContext().getWebSession();
		TaskQryParam qryParam = (TaskQryParam) ws.getAttribute(TASK_QRY_PARAM);
		if (qryParam == null) {
			qryParam = new TaskQryParam();
			ws.setAttribute(TASK_QRY_PARAM, qryParam);
		}
		return qryParam;
	}

	/**
	 * 点击执行
	 * 
	 * @param mouseEvent
	 */
	public void onclick_exec(MouseEvent<ButtonComp> mouseEvent) {
		Dataset ds = getCurrentView().getViewModels().getDataset(DS_TASK);
		Row selectedRow = ds.getSelectedRow();
		String id = selectedRow.getString(ds.nameToIndex("pk_task"));
		String title = selectedRow.getString(ds.nameToIndex("prodefname"));

		ITaskQryTmp taskQry = TaskDisplayHelper.getTaskQry(getSystemCode());
		String url = taskQry.getTaskProcessUrl(id);

		if (url != null) {
			ApplicationContext ctx = AppLifeCycleContext.current()
					.getApplicationContext();
			ctx.showModalDialog(url, title, "800", "480", id, true, true, "");
		}
	}
	/**
	 * 点击批审
	 */
	public void onclick_mutiexec(nc.uap.lfw.core.event.MouseEvent<ButtonComp> mouseEvent){
		Dataset ds = getCurrentView().getViewModels().getDataset(DS_TASK);
		Row[] selectedRows = ds.getSelectedRows();
		List<String> pks = new ArrayList<String>();
		if(selectedRows != null && selectedRows.length > 0){
			for(Row selectedRow : selectedRows){
				String id = selectedRow.getString(ds.nameToIndex("pk_task"));
				pks.add(id);
			}
		}
		ITaskQryTmp taskQry = TaskDisplayHelper.getTaskQry(getSystemCode());
		taskQry.doMutiTaskProcess(pks.toArray(new String[]{}));
	}
	/**
	 * 获得当前系统编码
	 * 
	 * @return
	 */
	public String getSystemCode() {
		ComboBoxComp sysField = getComboBox("sys");
		String sys = sysField.getValue();
		return ToolKit.notNull(sys) ? sys : WFMTASKQRY;
	}

	/**
	 * 获得当前View中的ComboBox
	 * 
	 * @param id
	 * @return
	 */
	private ComboBoxComp getComboBox(String id) {
		return (ComboBoxComp) getCurrentView().getViewComponents()
				.getComponent(id);
	}

	private LfwWidget getCurrentView() {
		return AppLifeCycleContext.current().getViewContext().getView();
	}

	/**
	 * 状态修改
	 * 
	 * @param textEvent
	 */
	public void status_valueChanged(TextEvent textEvent) {
		String val = textEvent.getSource().getValue();
		TaskQryParam param = getQryParam();
		param.setStatus(val);
		fillDs(param);
	}

	public void status_valueChanged2(TextEvent textEvent) {
		status_valueChanged(textEvent);
	}
	
	/**
	 * 修改系统
	 * @param textEvent
	 */
	public void sys_valueChanged(TextEvent textEvent) {
		fillDs(getQryParam());
	}
}
