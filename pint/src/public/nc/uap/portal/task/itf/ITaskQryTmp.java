package nc.uap.portal.task.itf;

import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.wfm.vo.WfmTaskVO;

/**
 * 任务查询
 * @author licza
 *
 */
public interface ITaskQryTmp {
	public static final String PID = "TASK_QRY_PLUGIN";
	/**
	 * 查询任务列表.
	 * @param param
	 * @param pinfo
	 * @return
	 */
	WfmTaskVO[] qryTaskList(TaskQryParam param, PaginationInfo pinfo);
	
	/**
	 * 获得任务处理URL
	 * @param pk_task
	 * @return
	 */
	String getTaskProcessUrl(String pk_task);
	String doMutiTaskProcess(String[] pk_task);
}
