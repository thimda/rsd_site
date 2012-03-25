package nc.uap.portal.action;

import java.io.IOException;

import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.task.ui.TaskDisplayHelper;

/**
 * 处理任务的Servlet
 * @author licza
 *
 */
@Servlet(path="/task")
public class TaskProcessAction extends BaseAction {
	@Action
	public void process(@Param(name="id") String pk_task) throws IOException{
		String url = TaskDisplayHelper.getTaskQry(null).getTaskProcessUrl(pk_task);
		if(url != null)	
			gun(url);
		else
			print("获取处理任务地址失败!");
	}
}
