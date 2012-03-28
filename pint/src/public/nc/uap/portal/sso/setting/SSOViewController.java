package nc.uap.portal.sso.setting;

import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifSaveCmd;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.page.LfwWidget;
/** 
 * @author chouhl
 */
public class SSOViewController implements IController {
  private static final long serialVersionUID=1L;
  public void onclick_ok(  MouseEvent mouseEvent){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
		.getViewModels().getDataset("ssoSystemsDs");
	  Row row = ds.getSelectedRow();
	  String operate = (String) AppLifeCycleContext.current().getWindowContext().getAppAttribute("operate");
	  if(operate == null)
		  return;
	  CmdInvoker.invoke(new UifSaveCmd(ds.getId(), null, null));
	  
	  LfwWidget mainWidget = AppLifeCycleContext.current().getWindowContext().getViewContext("main").getView();
	  Dataset mainDs = mainWidget.getViewModels().getDataset("ssoSystemsDs");
	  AppLifeCycleContext.current().getViewContext().setView(mainWidget);
	  UifDatasetLoadCmd cmd = new UifDatasetLoadCmd(mainDs.getId());
	  CmdInvoker.invoke(cmd);
	  
	  AppLifeCycleContext.current().getWindowContext().closeView("ssoView");
  }
  public void onclick_cancel(  MouseEvent mouseEvent){
	  AppLifeCycleContext.current().getWindowContext().closeView("ssoView");
  }
  @SuppressWarnings("deprecation")
public void beforeShow(  DialogEvent dialogEvent){
	  
	  String operate = (String) AppLifeCycleContext.current().getWindowContext().getAppAttribute("operate");
	  LfwWidget widget = AppLifeCycleContext.current().getWindowContext().getViewContext("ssoView").getView();
	  Dataset ds = widget.getViewModels().getDataset("ssoSystemsDs");
	  if(operate == null)
		  return;
	  if("add".equals(operate)){
		  Row row =  ds.getEmptyRow();
		  ds.addRow(row);
		  ds.setSelectedIndex(ds.getRowCount() - 1);
		  ds.setEnabled(true);
		  ds.setEnabled(true);
	  }
	  else if("edit".equals(operate)){
		  Row selectRow = (Row) AppLifeCycleContext.current().getWindowContext().getAppAttribute("selectRow");
		  Row row = ds.getEmptyRow();
			Field[] fields = ds.getFieldSet().getFields();
			  for(Field field:fields){
				  int index = ds.nameToIndex(field.getId());
				  if(index<0)
					  continue;
				  row.setValue(index, selectRow.getValue(index));
			  }
			ds.addRow(row);
			ds.setSelectedIndex(ds.getRowIndex(row));
			ds.setEnabled(true);
	  }
  }
}
