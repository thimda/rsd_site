package nc.uap.portal.task.ctrl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.task.itf.ITaskQryTmp;
import nc.uap.portal.util.ToolKit;

/**
 * 系统下拉数据
 * @author licza
 *
 */
public class SystemComboData extends DynamicComboData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6932120926233918590L;

	@Override
	protected CombItem[] createCombItems() {
		List<PtExtension> extensions = PluginManager.newIns().getExtensions(ITaskQryTmp.PID);
		List<CombItem> ciList = new ArrayList<CombItem>();
 		if(ToolKit.notNull(extensions)){
 			for(PtExtension ex : extensions){
 				CombItem ci = new CombItem(ex.getTitle(),ex.getId());
 				ci.setText(ex.getTitle());
 				ci.setI18nName(ex.getI18nname());
 				ci.setValue(ex.getId());
 				ciList.add(ci);
 			}
		} 
		return ciList.toArray(new CombItem[]{});
	}

}
