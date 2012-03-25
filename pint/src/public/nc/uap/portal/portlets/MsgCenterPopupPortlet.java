package nc.uap.portal.portlets;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portlet.iframe.BaseIframePortlet;

/**
 * 消息中心弹出Portlet
 * @author licza
 *
 */
public class MsgCenterPopupPortlet extends BaseIframePortlet {
	 

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		String frameURL = LfwRuntimeEnvironment.getRootPath() + "/app/msg/center";
		String category = request.getParameter("category");
		String frameHeight = request.getParameter("h");
		if(frameHeight != null && frameHeight.length() > 0 ){
			int realHeight = Integer.parseInt(frameHeight) - 38;
			request.setAttribute(HEIGHT_PARAM, realHeight + "");
		}
		if(category != null)
			frameURL = frameURL + "?category=" + category;
		setFrameURL(request, frameURL);
		request.setAttribute(SRC_TYPE, "src");
		include(request, response);
	}
}
