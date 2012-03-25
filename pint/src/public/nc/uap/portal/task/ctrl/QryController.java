package nc.uap.portal.task.ctrl;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.data.Dataset;
/** 
 * @author chouhl
 */
public class QryController implements IController {
  private static final long serialVersionUID=1L;
  public void qryds_onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    Row row = ds.getEmptyRow();
    ds.addRow(row);
    ds.setRowSelectIndex(0);
    ds.setEnabled(true);
  }
}
