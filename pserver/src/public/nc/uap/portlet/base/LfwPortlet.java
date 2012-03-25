package nc.uap.portlet.base;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portlet.iframe.BaseIframePortlet;

/**
 * 基于LFW节点的Portlet 
 * @author licza
 * @since 6.1
 */
public class LfwPortlet extends BaseIframePortlet{

	@Override
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		PortletPreferences fs = request.getPreferences();
		String nodeId = fs.getValue("lfwnodeid", StringUtils.EMPTY);
		if(StringUtils.isEmpty(nodeId)){
			response.getWriter().write("Lfw node id is empty!");
			response.getWriter().flush();
			return;
		}
		
		String frameURL = LfwRuntimeEnvironment.getRootPath() + "/core/uimeta.um?pageId=" + nodeId;
		setFrameEnv(request);
		setFrameURL(request, frameURL);
		super.doView(request, response);
	}
	
}
