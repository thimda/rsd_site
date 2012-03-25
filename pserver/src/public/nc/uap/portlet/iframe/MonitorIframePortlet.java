package nc.uap.portlet.iframe;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.ProcessEvent;
/**
 * 监听switchIframeContentEvent事件的IframePortlet
 * <br/>常与TriggerIframePortlet共同使用
 * <br/>自定义触发的Portlet时应将switchIframeContentEvent加入support-publish-event
 * <br/><b>请勿在一个Page中引用两个MonitorIframePortlet</b>
 * @author licza
 *
 */
public class MonitorIframePortlet extends BaseIframePortlet{
	/**
	 * 处理左侧Iframe中点击的事件
	 * @param request
	 * @param response
	 */
	@ProcessEvent(name="switchIframeContentEvent")
	public void ProcessClickUrl(EventRequest request,EventResponse response){
		String url=	(String)request.getEvent().getValue();
		response.setRenderParameter(SRC_PARAM, url);
		response.setRenderParameter("if_src_type", "src");
	}
	
	
	@ProcessEvent(name="doMaxEvent")
	public void ProcessDoMax(EventRequest request,EventResponse response){
		addExecScript(response, "getContainer('#" + request.getWindowID() + "').doMax();addResizeHanlder2FrmPortlet('"+request.getWindowID()+"');");
	}

	/**
	 * 处理脚本事件
	 */
	public void processAction(ActionRequest request, ActionResponse response) {
		String action = request.getParameter("frameUrl");
		response.setEvent("execScriptEvent", action);
	}
}
