package nc.uap.portal.util;

import java.io.File;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


import nc.bs.framework.common.NCLocator;
import nc.itf.uap.sf.IConfigFileService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.util.HttpUtil;
import nc.uap.lfw.util.ParamUtil;
import nc.uap.lfw.util.StringPool;
import nc.uap.lfw.util.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.sm.config.Account;
/**
 * Portal工具
 * @author dengjt 2006-2-6
 */
public class PortalUtil {
	/**
	 * 获得服务器地址
	 * @return
	 */
	public static String getServerUrl(){
		String serverIp = LfwRuntimeEnvironment.getServerAddr();
		if (serverIp == null || serverIp.equals("")) {
			serverIp = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerName();
			int port = LfwRuntimeEnvironment.getWebContext().getRequest()
					.getServerPort();
			if (port != 80)
				serverIp = serverIp + ":" + port;
		}
		return serverIp;
	}
	
	
	/**
	 * 对于tomcat服务器要转换编码才能得到正确的中文,对于was不需要转换编码
	 * @param oriString
	 * @return
	 */
	public static String convertToCorrectEncoding(String oriString)
	{
		return StringUtil.convertToCorrectEncoding(oriString);
	}
	
	/**
	 * 运行时替换用户名,密码,当前时间
	 * 
	 * @param url
	 * @param ses
	 * @return
	 */
	public static String processURL(String url)
	{
		if(url == null)
			return null;
		
		
//		url = url.replaceAll("\\[name\\]", SecurityUtil.getPtUser().getUserid());
//		url = url.replaceAll("\\[date\\]", new UFDateTime(new Date()).toString());
//		url = url.replaceAll("\\[password\\]", SecurityUtil.getPtUser().getPassword());
//		// 密码加密处理
//		url = url.replaceAll("\\[password_encode\\]", new BASE64Encoder().encode(SecurityUtil.getPtUser().getPassword().getBytes()));
//		String staffCode = SecurityUtil.getPtUser().getStaffcode() == null ? "" : SecurityUtil.getPtUser().getStaffcode();
//		url = url.replaceAll("\\[staffcode\\]", staffCode);
//		// 员工号加密处理
//		url = url.replaceAll("\\[staffcode_encode\\]", new BASE64Encoder().encode(staffCode.getBytes()));
//		return url;
		return null;
	}
	
	
	
	/**
	 * 判断是否ajax请求.根据参数 ajax=1
	 * 
	 * @param req
	 * @return
	 */
	public static boolean isInAjax(ServletRequest req)
	{
		return ParamUtil.getString(req, "ajax") != null && ParamUtil.getString(req, "ajax").equals("1");
	}

	public static String getPortalURL(HttpServletRequest req, boolean secure) {
		StringBuffer sb = new StringBuffer();
		sb.append(HttpUtil.HTTP_WITH_SLASH);
		sb.append(req.getServerName());
		sb.append(StringPool.COLON).append(req.getServerPort());
		return sb.toString();
	}
	
	/**
	 * 获得命名空间值
	 */
	public static String getPortletNamespace(String portletName) {
		return portletName + "_";
	}
	
//	public static PreferencesValidator getPreferencesValidator(PortletVO portlet) {
//		PreferencesValidator prefsValidator = null;
//		if (Validator.isNotNull(portlet.getPreferencesValidator())) {
//			try {
//				prefsValidator = (PreferencesValidator) Class.forName(portlet.getPreferencesValidator()).newInstance();
//			} catch (Exception e) {
//				Logger.error(e.getMessage(), e);
//			}
//		}
//		return prefsValidator;
//	}

//	/**
//	 * 判定是否系统参数
//	 * @param name
//	 * @return
//	 */
//	public static boolean isReservedParameter(String name) {
//		if (name.equals(IParamKeys.LAYOUT_ID)
//				|| name.equals(IParamKeys.PORTLET_ID)
//				|| name.equals(IParamKeys.ACTION)
//				|| name.equals(IParamKeys.P_STATE)
//				|| name.equals(IParamKeys.P_MODE)
//				|| name.equals(IParamKeys.P_COL_ORDER)
//				|| name.equals(IParamKeys.P_COL_POS)
//				|| name.equals(IParamKeys.P_COL_COUNT))
//			return true;
//		return false;
//	}
	
//	
//	/**
//	 * 给PortletURL添加参数
//	 */
//	public static void setParameters(PortletURLImpl portletURL, String queryString) {
//		String[] params = StringUtil.split(queryString, "&");
//		for (int i = 0; i < params.length; i++) {
//			int pos = params[i].indexOf("=");
//			if (pos != -1) {
//				String param = params[i].substring(0, pos);
//				String value = params[i].substring(pos + 1, params[i].length());
//
//				if (param.equals("windowState")) {
//					try {
//						portletURL.setWindowState(new WindowState(value));
//					} catch (WindowStateException wse) {
//						Logger.error(wse);
//					}
//				} else if (param.equals("portletMode")) {
//					try {
//						portletURL.setPortletMode(new PortletMode(value));
//					} catch (Exception pme) {
//						Logger.error(pme);
//					}
//				} else if (param.equals("actionURL")) {
//					portletURL.setAction(GetterUtil.getBoolean(value));
//				} else {
//					portletURL.setParameter(param, HttpUtil.decodeURL(value), true);
//				}
//			}
//		}
//	}
	
//	public static void setParameters(PortletURLImpl portletURL, Map paramMap)
//	{
//		Iterator it = paramMap.entrySet().iterator();
//		while(it.hasNext())
//		{
//			Map.Entry entry = (Entry) it.next();
//			String param =  (String)entry.getKey();
//			Object value =  entry.getValue();
//
//			if (param.equals("windowState")) 
//			{
//				try 
//				{
//					portletURL.setWindowState(new WindowState(value.toString()));
//				} 
//				catch (WindowStateException wse) 
//				{
//					Logger.error(wse);
//				}
//			}
//			else if (param.equals("portletMode")) 
//			{
//				try {
//						portletURL.setPortletMode(new PortletMode(value.toString()));
//				} 
//				catch (Exception pme) 
//				{
//					Logger.error(pme);
//				}
//			} 
//			else if (param.equals("actionURL")) 
//			{
//				portletURL.setAction(GetterUtil.getBoolean(value.toString()));
//			} 
//			else 
//			{
//				portletURL.setParameter(param, HttpUtil.decodeURL(value.toString()), true);
//			}
//		}
//	}
//
//	public static PortletVO getPortletById(String portletName) throws PortalServiceException{
//		String userId = SecurityUtil.getPtUser().getPk_user();
//		try {
//			return PortalServiceFacility.getPortletService().getPortlet(portletName, userId, SecurityUtil.getPk_org());
//		} catch (BusinessException e) {
//			throw new PortalServiceException(e);
//		}
//	}
	

	/**
	 * 删除用户缓存
	 * @param portletId
	 * @param mode
	 * @param layoutId
	 * @param userId
	 * @param theme
	 * @param langId
	 */
//	public static void removeUserCache(String portletId, String mode, String layoutId, String userId, String theme, String langId)
//	{
//		Map map = (Map) PortalServiceFacility.getClusterCatheService().getUserCachePortletState();
//		if(map != null)
//		{
//			StringBuffer idBuffer = new StringBuffer();
//			idBuffer.append(langId).append(StringPool.UNDERLINE).append(portletId).append(StringPool.UNDERLINE)
//		                                      .append(mode)
//		                                      .append(StringPool.UNDERLINE)
//		                                      .append(theme).append(StringPool.UNDERLINE).append(layoutId)
//		                                      .append(StringPool.UNDERLINE).append(userId);
//			map.remove(idBuffer.toString());
//			// 修改完成需要同步全局缓存,否则将丢失修改信息
//			PortalServiceFacility.getClusterCatheService().setUserCachePortletState(map);
//		}
//	}
	
//	public static boolean isPortlet(Object obj)
//	{
//		return (obj instanceof PortletVO);
//	}
	
	
	/**
	 * 获取新闻组发布为portlet的id
	 * @param pk_newsgroup
	 * @return
	 */
	public static String getPortletIdByNewsGroup(String pk_newsgroup)
	{
		if(pk_newsgroup != null && !pk_newsgroup.equals(""))
			return "NewsGroup_" + pk_newsgroup + "_Portlet";
		else
			return null;
	}
	
	/**
	 * 获取新闻组发布为layout的id
	 * @param pk_newsgroup
	 * @return
	 */
	public static String getLayoutIdByNewsGroup(String pk_newsgroup)
	{
		if(pk_newsgroup != null && !pk_newsgroup.equals(""))
			return "NewsGroup_" + pk_newsgroup + "_Layout";
		else
			return null;
	}
	
	/**
	 * 按照给定路径创建目录,不存在该目录才创建
	 * @param path
	 */
	public static void createDir(String path)
	{
		if(path == null || path.equals(""))
			return;
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
	}
	
	/**
	 * 根据帐套获取数据源
	 * @param account
	 * @param locator
	 * @return
	 * @throws BusinessException
	 */
	public static String fetchDatasourceName(String account, NCLocator locator) throws BusinessException
	{
		IConfigFileService file = (IConfigFileService)locator.lookup(IConfigFileService.class.getName());
		Account acc = file.getAccountByCode(account);
		if(acc == null)
			return null;
		
		return acc.getDataSourceName();
	}
}
