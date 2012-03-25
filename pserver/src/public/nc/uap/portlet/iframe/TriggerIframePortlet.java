package nc.uap.portlet.iframe;


import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * IframePortlet事件触发Portlet
 * <br/>触发switchIframeContentEvent,常见的监听Portlet是MonitorIFramePortlet及其子类
 * @author licza
 *
 */
public class TriggerIframePortlet extends BaseIframePortlet {

	/**
	 * 默认的Action处理方式
	 * 自定义Action时需要重载此方法
	 * 
	 * @param request
	 * @param response
	 */
	public void processAction(ActionRequest request, ActionResponse response) {
		String action = request.getParameter(ActionRequest.ACTION_NAME);
		if("doMaxAction".equals(action)){
			response.setEvent("doMaxEvent", "doMax");
		}else{
			String url = request.getParameter("frameUrl");
			response.setEvent("switchIframeContentEvent", url);
		}
	}
	
	 
 }

