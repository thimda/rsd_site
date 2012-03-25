package nc.uap.portal.portlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.impl.WfmTaskQryTmpImpl;
import nc.uap.portal.task.itf.ITaskQryTmp;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;
/**
 * 待办流程Portlet
 * @author licza
 *
 */
public class MyWflPortlet extends PtBasePortlet {

	private static final String TASK_LIST = "TASK_LIST";

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		String template = this.getClass().getName().replace(".", "/") + ".ftl";
		Map<String,Object> root = new HashMap<String,Object>();
		ITaskQryTmp taskQry = new WfmTaskQryTmpImpl();
		TaskQryParam param = new TaskQryParam();
		param.setStatus(TaskQryParam.STATUS_UNREAD);
		String html = null;
		
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		root.put("RES_PATH", resourcePath);
		try {
			WfmTaskVO[]  tasks = taskQry.qryTaskList(param, null);
			root.put(TASK_LIST, tasks);
			html = FreeMarkerTools.contextTemplatRender(template, root);
		} catch (Exception e) {
			html = "获取待办流程失败:" + e.getMessage();
			LfwLogger.error(html, e);
		}
		response.getWriter().print(html);
//		response.getWriter().flush();
		
	}
	
}
