package nc.uap.portal.menu.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.util.PortletSessionUtil;
import nc.uap.portlet.iframe.TriggerIframePortlet;

import org.apache.commons.lang.StringUtils;

/**
 * Ê÷×´ÏÔÊ¾µÄ²Ëµ¥Portlet
 * @author licza
 *
 */
public class TreeViewMenuPortlet extends TriggerIframePortlet{

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		PortletPreferences preference =	request.getPreferences();
		String category = preference.getValue("linkgroup", null);
		
		String frameHeight = "0";
		frameHeight = preference.getValue(HEIGHT_PARAM, frameHeight);
		if (StringUtils.isNotBlank(request.getParameter(HEIGHT_PARAM)))
			frameHeight = request.getParameter(HEIGHT_PARAM);
		String frameURL = LfwRuntimeEnvironment.getRootPath() + "/app/mockapp/outlook?model=nc.uap.portal.view.menu.OutLookMenuPageModel&category=" + category;
		frameURL=PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		request.setAttribute(SRC_PARAM, frameURL);
		request.setAttribute(HEIGHT_PARAM, frameHeight);
		String srcType = StringUtils.defaultIfEmpty(request.getParameter(SRC_TYPE), srcVal);
		request.setAttribute(SRC_TYPE, srcType);
		include(request, response);
	}

	@Override
	protected String getFramePage() {
		return "/pages/leoframe.jsp";
	}
	
}
