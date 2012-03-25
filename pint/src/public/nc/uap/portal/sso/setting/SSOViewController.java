package nc.uap.portal.sso.setting;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * @author chouhl
 */
public class SSOViewController implements IController {
  private static final long serialVersionUID=1L;
  public void onclick_ok(  MouseEvent mouseEvent){
	  Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
		.getViewModels().getDataset("ssoSystemsDs");
	  ds.getSelectedRow();
  }
  public void onclick_cancel(  MouseEvent mouseEvent){
	  AppLifeCycleContext.current().getWindowContext().closeViewDialog("ssoView");
  }
}
