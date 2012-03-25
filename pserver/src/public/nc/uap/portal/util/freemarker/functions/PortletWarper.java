package nc.uap.portal.util.freemarker.functions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.portlet.PortletContainer;
import nc.uap.portal.container.portlet.PortletResponseContext;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.portlet.PortletWindowImpl;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.servlet.StringServletResponse;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PortletDataWrap;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * Portlet内容包装输出
 * @author licza
 *
 */
public class PortletWarper implements TemplateDirectiveModel {

	@SuppressWarnings({ "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		String portletKey =  params.get("portletName").toString();
		String pageModule =    params.get("pageModule").toString();
		String pageName =   params.get("pageName").toString();
		String[] keys=portletKey.split(":");
		StringBuffer portletBody = new StringBuffer();
		/**
		 * 未显式指定模块,则认定与当前页面模块相同
		 */
		String portletModule = keys.length==2? keys[0]:pageModule;
		String portletName =  keys.length==2? keys[1]:portletKey;
		
		WebContext ctx = LfwRuntimeEnvironment.getWebContext();
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		//用户超时
		if(ses == null)
			return;
		
		
		/**
		 * 构造Response
		 */
		StringServletResponse response=new StringServletResponse(ctx.getResponse());
		try {
			String originalid = PortalPageDataWrap.modModuleName(portletModule, portletName);
			PortletWindowID winId = new PortletWindowID(pageModule, pageName, portletModule, portletName);
			/**
			 * 权限校验
			 */
			boolean hasPower = PortalServiceUtil.getServiceProvider().getPortalPowerService().hasPower(originalid);
			Page page = PortalRenderEnv.getCurrentPage();
			if((!hasPower) && page.getUndercontrol()){
				portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").setExposed();</script>");
				return;
			}
			
			/**
			 * Portlet窗口
			 */
 
			/**
			 * Portlet定义
			 */
			PortletWindow window = new PortletWindowImpl(winId, WindowState.NORMAL, PortletMode.VIEW);
			/**
			 * 查找Portlet配置
			 */
			Preferences pf =  PortalCacheManager.getPreferences(window);
			
			boolean isLazy = false;
			String refresh = null;
			Preference refreshCircle = pf.getPortletPreference("refresh-circle");
			if(refreshCircle != null){
				List<String> circle = refreshCircle.getValues();
				if(circle != null && circle.size() > 0){
					String c = circle.get(0);
					if(c != null && !c.equals("") && !c.equals("0") && StringUtils.isNumeric(c))
						refresh = c;
				}
			}
			
			Preference lazyload = pf.getPortletPreference("lazyload");
			if(lazyload != null){
				List<String> lazy = lazyload.getValues();
				if(lazy != null && lazy.size() > 0){
					isLazy = ("true").equals(lazy.get(0));
				}
			}
			
			if(isLazy){
				/**
				 * 懒加载
				 */
				portletBody.append("加载中...");
				portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").doView();</script>");
			}else{
				/**
				 * 正常加载
				 */
				
				PortletContainer ctn = new PortletContainer();
				PortletResponseContext resCtx = ctn.doRender(window,  ctx.getRequest(), response);
				portletBody.append(response.getString());
				/**
				 * 设置标题
				 */
				String title = resCtx.getTitle();
				if(false)//StringUtils.isNotEmpty(title))
					portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").setTitle('"+title+"');</script>");
			}
			
			/**
			 * 启用Portlet定时刷新
			 */
			if(refresh != null)
				portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").doRefresh("+refresh+");</script>");
			
	 		/**
	 		 * 向容器写入Portlet信息,以便前台使用
	 		 */
			
			//Portlet支持的模式
			String mimeType = ctx.getRequest().getContentType();
			if(mimeType == null)
				mimeType = "text/html";
			List<String> sm = PortletDataWrap.getSupportModes(window.getPortletDefinition(), mimeType);
			String sms = StringUtils.join(sm.iterator(), "','");
			portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").setSupportModes(['"+sms+"']);</script>");
			//Portlet支持显示模式
			List<String> windowStates = PortletDataWrap.getSupportWindowStates(window.getPortletDefinition(), mimeType);
			String windowState = StringUtils.join(windowStates.iterator(), "','");
			portletBody.append("<script>getContainer(\"#"+winId.getStringId()+"\").addDoMaxResize(['"+windowState+"']);</script>");
		} catch (Exception e) {
			LfwLogger.error("Portlet渲染异常！", e);
		}finally{
			/**
			 * 写入FreeMarker输出流
			 */
	 		env.getOut().write(portletBody.toString());
		}
	}

}
