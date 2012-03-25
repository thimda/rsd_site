package nc.uap.portal.task.ui;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.task.itf.ITaskQryTmp;

/**
 * 任務顯示輔助類
 * @author licza
 *
 */
public class TaskDisplayHelper {
	   /**
	   * 获得查询插件
	   * @return
	   */
	public static ITaskQryTmp getTaskQry(String extensionId){
		PtExtension extension = null;
		try {
			extension = PluginManager.newIns().getExtension(extensionId);
			return extension.newInstance(nc.uap.portal.task.itf.ITaskQryTmp.class);
		} catch (Exception e) {
			throw new LfwRuntimeException("获取任务插件失败:" + e.getMessage(),e);
		}
	} 
}
