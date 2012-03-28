package nc.uap.portal.task.impl;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.itf.ITaskQryTmp;
import nc.uap.wfm.dftimpl.DefaultFormOper;
import nc.uap.wfm.engine.IWfmFormOper;
import nc.uap.wfm.itf.IWfmTaskQry;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.utils.WfmClassUtil;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmTaskVO;

/**
 * 协同流程任务查询
 * 
 * @author licza
 * 
 */
public class WfmTaskQryTmpImpl implements ITaskQryTmp {
	/**
	 * 查询任务列表
	 * 
	 * @param param
	 * @param pinfo
	 * @return
	 */
	public WfmTaskVO[] qryTaskList(TaskQryParam param, PaginationInfo pinfo) {
		IWfmTaskQry q = NCLocator.getInstance().lookup(IWfmTaskQry.class);
		String userPk = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		if(userPk != null && param.getWheresql() != null)
			userPk = userPk + "' and " + param.getWheresql() + " and '1' = '1";
		String startDay = null;
		String endDay = null;
		try {
			if (param.getId() == null) {
				if (TaskQryParam.STATUS_UNREAD.equals(param.getStatus())) {
					return q.getMyUndneTasksAndUnDeliverByDate(userPk, null, startDay, endDay, pinfo);
				}

			} else if (TaskQryParam.ID_TASK.equals(param.getId())) {

				if (TaskQryParam.STATUS_UNREAD.equals(param.getStatus())) {
					return q.getMyUndneTasksByDate(userPk, null, startDay,
							endDay, pinfo);

				}
				if (TaskQryParam.STATUS_READED.equals(param.getStatus())) {
					return q.getMyCmpltTasksByDate(userPk, null, startDay,
							endDay, pinfo);
				}
				if (TaskQryParam.STATUS_END.equals(param.getStatus())) {
					return q.getMyEndedTasksByDate(userPk, null, startDay,
							endDay, pinfo);
				}
			} else if (TaskQryParam.ID_DELIVERTASK.equals(param.getId())) {

				if (TaskQryParam.STATUS_UNREAD.equals(param.getStatus())) {
					return q.getMyUnDeliverTasksByDate(userPk, null, startDay,
							endDay, pinfo);

				}
				if (TaskQryParam.STATUS_READED.equals(param.getStatus())) {
					return q.getMyDeliveredTasksByDate(userPk, null, startDay,
							endDay, pinfo);
				}
				if (TaskQryParam.STATUS_END.equals(param.getStatus())) {
					// throw new LfwRuntimeException("暂不支持.");
				}
			}
		} catch (Exception e) {
			LfwLogger.error("查询失败,参数:" + param.toString() + ";异常:"
					+ e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getTaskProcessUrl(String pk_task) {
	  Task task = WfmTaskUtil.getTaskByTaskPk(pk_task);
	  WfmFlwTypeVO vo = task.getProDef().getFlwtype();  
	  String serverClazz = vo.getServerclass();
	  if(serverClazz == null){
		  serverClazz = DefaultFormOper.class.getName();
	  }
	  IWfmFormOper formOper = (IWfmFormOper)WfmClassUtil.loadClass(serverClazz);
	  String url = formOper.getHanlderUrlByTask(task);
	  return url;
	}

	@Override
	public String doMutiTaskProcess(String[] pk_task) {
		return null;
	}
	
}
