package nc.uap.portal.sso.setting;

import java.util.ArrayList;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.portal.cache.PortalCacheProxy;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import java.util.Map;
import java.util.List;
import nc.uap.lfw.core.cmd.UifUpdateOperatorState;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.integrate.itf.IPtSsoConfigService;
import nc.uap.lfw.core.event.CellEvent;
import nc.uap.lfw.core.ctrl.IController;
import java.util.HashMap;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.portal.sso.util.SSOUtil;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import java.util.Iterator;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.lfw.core.data.Row;
import nc.uap.portal.integrate.system.IpReference;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.portal.integrate.cache.SSOProviderCache;
import nc.uap.lfw.core.event.MouseEvent;

/**
 * @author chouhl
 */
public class IpMappingViewController implements IController {
	private static final long serialVersionUID = 1L;

	public void beforeShow_ipMappingView(DialogEvent dialogEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ipMappingDs");
		String systemCode = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getAttribute("systemCode");
		String keyValue = ds.getCurrentKey();
		if (keyValue == null || (keyValue != null && keyValue.equals(""))) {
			keyValue = Dataset.MASTER_KEY;
			ds.setCurrentKey(keyValue);
		}
		RowSet rowSet = ds.getRowSet(keyValue);
		if (rowSet == null) {
			rowSet = new RowSet(keyValue);
			ds.addRowSet(keyValue, rowSet);
		}
		rowSet.setRowSetChanged(true);
		RowData rowData = rowSet.getCurrentRowData(true);
		rowData.clear();

		SSOProviderVO provider = SSOUtil.getProviderVOBySystemCode(systemCode);
		List<IpReference> list = provider.getMappingReference();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = ds.getEmptyRow();
				row.setState(Row.STATE_NORMAL);
				row.setString(ds.nameToIndex("key"), list.get(i).getSourceIp());
				row.setString(ds.nameToIndex("value"), list.get(i).getTargetIp());
				rowData.addRow(row);
			}
		}
		ds.setEnabled(false);
		ButtonStateManager.updateButtons();
	}

	public void onclick_add(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ipMappingDs");
		Row row = ds.getEmptyRow();
		ds.addRow(row);
		ds.setEnabled(true);
		WebComponent comp = AppLifeCycleContext.current().getViewContext()
				.getView().getViewModels().getWidget().getViewComponents()
				.getComponent("ipgrid");
		if (comp instanceof GridComp) {
			IGridColumn gc = ((GridComp) comp).getColumnById("key");
			if (gc instanceof GridColumn) {
				((GridColumn) gc).setEditable(true);
			}
		}
	}

	public void onclick_delete(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ipMappingDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中删除数据");
		String systemCode = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getAttribute("systemCode");
		SSOProviderVO provider = SSOUtil.getProviderVOBySystemCode(systemCode);
		Row row = ds.getSelectedRow();
		row.setState(Row.STATE_DELETED);
		String name = row.getString(ds.nameToIndex("key"));
		List<IpReference> list = provider.getMappingReference();
		if (list != null && list.size() > 0) {
			for (IpReference ref : list) {
				if (ref.getSourceIp().equals(name)) {
					list.remove(ref);
					break;
				}
			}
		}
		ds.removeRow(row);
		PortalCacheProxy.newIns().locator(new SSOProviderCache(),
				IPtSsoConfigService.class).update(provider);
		new UifUpdateOperatorState(ds, AppLifeCycleContext.current()
				.getViewContext().getView()).execute();
	}

	public void onclick_modify(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ipMappingDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中编辑数据");
		Row row = ds.getSelectedRow();
		row.setState(Row.STATE_UPDATE);
		ds.setEnabled(true);
		WebComponent comp = AppLifeCycleContext.current().getViewContext()
				.getView().getViewModels().getWidget().getViewComponents()
				.getComponent("ipgrid");
		if (comp instanceof GridComp) {
			IGridColumn gc = ((GridComp) comp).getColumnById("key");
			if (gc instanceof GridColumn) {
				((GridColumn) gc).setEditable(false);
			}
		}
		new UifUpdateOperatorState(ds, AppLifeCycleContext.current()
				.getViewContext().getView()).execute();
		
		ButtonStateManager.updateButtons();
	}

	public void onclick_save(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset("ipMappingDs");
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中修改数据");
		String systemCode = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getAttribute("systemCode");
		SSOProviderVO provider = SSOUtil.getProviderVOBySystemCode(systemCode);
		Row[] rows = ds.getSelectedRows();
		if (rows != null && rows.length > 0) {
			Map<String, Row> rowMap = new HashMap<String, Row>();
			for (Row row : rows) {
				rowMap.put(row.getString(ds.nameToIndex("key")), row);
			}
			List<IpReference> list = provider.getMappingReference();
			if (list != null && list.size() > 0) {
				Row row = null;
				for (IpReference ref : list) {
					row = rowMap.get(ref.getSourceIp());
					if (row != null) {
						if (Row.STATE_ADD == row.getState()) {
							throw new LfwRuntimeException("配置项标识已存在!");
						} else if (Row.STATE_UPDATE == row.getState()) {
							ref.setTargetIp(row.getString(ds
									.nameToIndex("value")));
						}
						rowMap.remove(ref.getSourceIp());
						if (rowMap.isEmpty()) {
							break;
						}
					}
				}
			} else {
				list = new ArrayList<IpReference>();
			}
			Iterator<String> keys = rowMap.keySet().iterator();
			if (keys != null) {
				Row row = null;
				while (keys.hasNext()) {
					row = rowMap.get(keys.next());
					IpReference ref = new IpReference();
					ref.setSourceIp(row.getString(ds.nameToIndex("key")));
					ref.setTargetIp(row.getString(ds.nameToIndex("value")));
					list.add(ref);
					row.setState(Row.STATE_NORMAL);
				}
			}
			ds.setEnabled(false);
			provider.setMappingReference(list);
			PortalCacheProxy.newIns().locator(new SSOProviderCache(),
					IPtSsoConfigService.class).update(provider);
			new UifUpdateOperatorState(ds, AppLifeCycleContext.current()
					.getViewContext().getView()).execute();
		}
	}

	public void onAfterRowSelect_ipMappingView(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		new UifUpdateOperatorState(ds, AppLifeCycleContext.current()
				.getViewContext().getView()).execute();
		ButtonStateManager.updateButtons();
	}

	public void cellValueChanged_ipgrid(CellEvent gridCellEvent) {
		if (gridCellEvent.getRowIndex() == 0) {
			gridCellEvent.setNewValue(gridCellEvent.getOldValue());
		}
	}

}
