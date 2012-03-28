package nc.uap.portlet.iframe;

import java.io.IOException;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PortletSessionUtil;
import nc.uap.portlet.base.PtBasePortlet;

import org.apache.commons.lang.StringUtils;

/**
 * IframePortlet基类
 * 
 * @author licza
 * @since 2010-8-10
 */
public class BaseIframePortlet extends PtBasePortlet {
	/** 默认的Iframe页面 **/
	protected static final String IFRAME_PORTLET_PAGE = "/pages/iframe.jsp";
	protected static final String SRC_PARAM = "if_src";
	protected static final String WIDTH_PARAM = "if_width";
	protected static final String HEIGHT_PARAM = "if_height";
	protected static final String SRC_TYPE = "if_src_type";
	protected static final String srcVal = "scr";
	protected static final String ALLOW_SCROLL = "scrolling";
	/**
	 * 获得FrameURL
	 * 
	 * @return
	 */
	protected String getFrameURL() {
		return "/";
	}

	/**
	 * IframePortlet的默认实现
	 * 
	 * @see javax.portlet.GenericPortlet
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType("text/html");
		PortletPreferences preference = request.getPreferences();
		String frameURL = getFrameURL();
		frameURL = preference.getValue(SRC_PARAM, frameURL);
		String scroll = preference.getValue(ALLOW_SCROLL, "no");
		if (StringUtils.isNotBlank(request.getParameter(SRC_PARAM)))
			frameURL = request.getParameter(SRC_PARAM);
		String frameHeight = "0";
		frameHeight = preference.getValue(HEIGHT_PARAM, frameHeight);
		if (StringUtils.isNotBlank(request.getParameter(HEIGHT_PARAM)))
			frameHeight = request.getParameter(HEIGHT_PARAM);
		frameURL=PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		request.setAttribute(SRC_PARAM, frameURL);
		request.setAttribute(HEIGHT_PARAM, frameHeight);
		request.setAttribute(ALLOW_SCROLL, scroll);
		String srcType = StringUtils.defaultIfEmpty(request.getParameter(SRC_TYPE), srcVal);
		if(PortalRenderEnv.RENDER_REQ_TYPE_AJAX.equals(PortalRenderEnv.getPortletRenderType())){
			String iframeId = request.getWindowID().replaceAll("\\.","_") + "_iframe";
			String script = "if($('#" + iframeId + "').attr('fullHeight')){ $('#" + iframeId + "').height(parseInt($('#" + iframeId + "').attr('fullHeight'))); }\n document.getElementById('"+iframeId+"').src='"+frameURL+"';";
			addExecScript(response, script);
			//			frameURL
		}else{
			request.setAttribute(SRC_TYPE, srcType);
			include(request, response);
		}
	}
	/**
	 * 设置Frame的地址
	 * @param request
	 * @param frameURL
	 */
	protected void setFrameURL(RenderRequest request, String frameURL){
		frameURL=PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		request.setAttribute(SRC_PARAM, frameURL);
	}
	
	
	/**
	 * 设置Frame的环境信息
	 * @param request
	 */
	protected void setFrameEnv(RenderRequest request){
		PortletPreferences preference = request.getPreferences();
		String frameHeight = "0";
		frameHeight = preference.getValue(HEIGHT_PARAM, frameHeight);
		if (StringUtils.isNotBlank(request.getParameter(HEIGHT_PARAM)))
			frameHeight = request.getParameter(HEIGHT_PARAM);
		request.setAttribute(HEIGHT_PARAM, frameHeight);
	}

	protected void include(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		PortletContext context = getPortletContext();
		PortletRequestDispatcher requestDispatcher = context.getRequestDispatcher(getFramePage());
		requestDispatcher.include(request, response);
	}
	/**
	 * 获得Iframe的ID
	 * @param request
	 * @return
	 */
	protected String getIFrameId(PortletRequest request){
		return request.getWindowID() + "_iframe";
	}
	
	protected String getFramePage(){
		return IFRAME_PORTLET_PAGE;
	}
}
