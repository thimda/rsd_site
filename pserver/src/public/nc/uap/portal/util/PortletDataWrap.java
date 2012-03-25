package nc.uap.portal.util;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.xml.bind.JAXBException;

import nc.bs.logging.Logger;
import nc.uap.portal.container.om.CustomPortletMode;
import nc.uap.portal.container.om.CustomWindowState;
import nc.uap.portal.container.om.Description;
import nc.uap.portal.container.om.DisplayName;
import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.container.om.InitParam;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletAdjunct;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.PortletInfo;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.container.om.Supports;
import nc.uap.portal.container.portlet.PortletPreferencesImpl;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.vo.PtPortletPreferencesVO;
import nc.uap.portal.vo.PtPortletVO;

import org.apache.commons.lang.StringUtils;

/**
 * PortletVO与PortletDefinition绑定类
 * 
 * @author licza
 * 
 */
public class PortletDataWrap {

	/**
	 * 将PortletDefinition内容拷贝到PortletVO中
	 * 
	 * @param target PortletVO
	 * @param src Portlet定义
	 * @return PortletVO
	 * @throws JAXBException
	 * @throws PortalServiceException
	 * @throws Exception
	 */
	public static PtPortletVO warpVO(PtPortletVO target, PortletDefinition src) throws JAXBException, PortalServiceException {

		if (target == null) {
			target = new PtPortletVO();
		}
		List<? extends DisplayName> displayNames = src.getDisplayNames();
		if (displayNames != null && (!displayNames.isEmpty())) {
			target.setDisplayname(displayNames.get(0).getDisplayName());
		}
		target.setExpirecache(src.getExpirationCache());
		target.setInfo(buildPortletInfo(src.getPortletInfo()));
		target.setInitparams(buildInitParams(src));
		List<? extends Description> descriptions = src.getDescriptions();
		if (descriptions != null && (!descriptions.isEmpty())) {
			target.setMemo(descriptions.get(0).getDescription());
		}
		target.setPortletclass(src.getPortletClass());
		target.setPortletid(src.getPortletName());
		target.setPreferences(XmlUtil.preferencesWriter(src.getPortletPreferences()));
		target.setResourcebundle(src.getResourceBundle());
		target.setSupportlocales(buildSupportedLocale(src.getSupportedLocales()));
		target.setSupportmodes(buildSupports(src.getSupports()));
		target.setVersion(getVersion(src));
		PortletAdjunct pa = new PortletAdjunct();
		pa.setSupportedProcessingEvent(src.getSupportedProcessingEvent());
		pa.setSupportedPublicRenderParameter(src.getSupportedPublicRenderParameter());
		pa.setSupportedPublishingEvent(src.getSupportedPublishingEvent());
		String setting = JaxbMarshalFactory.newIns().decodeXML(pa);
		target.setSetting(setting);
		return target;
	}

	/**
	 * 将PortletVO内容拷贝到PortletDefinition中
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static PortletDefinition warpDefinition(PtPortletVO src) {
		PortletDefinition target = new PortletDefinition();
		try {
			target.setExpirationCache(src.getExpirecache() == null ? 0 : src.getExpirecache());
			target.addDescription(src.getMemo());
			target.addDisplayName(src.getDisplayname());
			target.setInitParam(restoreInitParams(src.getInitparams()));
			target.setPortletClass(src.getPortletclass());
			target.setPortletInfo(restorePortletInfo(src.getInfo()));
			target.setPortletName(src.getPortletid());
			if (src.getPreferences() != null) {
				Reader reader = new StringReader(src.getPreferences());
				target.setPortletPreferences(XmlUtil.preferencesReader(reader));
			}
			target.setResourceBundle(src.getResourcebundle());
			target.setSupportedLocale(restoreSupportedLocale(src.getSupportlocales()));
			target.setSupports(restoreSupports(src.getSupportmodes()));
			target.setModule(src.getModule());
			String setting = src.getSetting();
			PortletAdjunct pa = JaxbMarshalFactory.newIns().encodeXML(PortletAdjunct.class, setting);
			if (pa != null) {
				target.setSupportedPublishingEvent(pa.getSupportedPublishingEvent());
				target.setSupportedProcessingEvent(pa.getSupportedProcessingEvent());
				target.setSupportedPublicRenderParameter(pa.getSupportedPublicRenderParameter());
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return target;
	}
	/**
	 * 获得模块Portal应用定义
	 * @param define
	 * @return
	 */
	public static ModuleApplication getModuleApplication(PortalDeployDefinition define){
		ModuleApplication ma = new ModuleApplication();
		ma.setModule(define.getModule());
		PortletApplicationDefinition pad = define.getPortletDefineModule();
		if(pad == null)
			return ma;
		List<? extends CustomPortletMode> cpms = pad.getCustomPortletModes();
		if(cpms != null && !cpms.isEmpty()){
			for(CustomPortletMode cpm : cpms){
				ma.addCustomPortletMode(cpm.getPortletMode());
			}
		}
		
		ma.setCustomWindowState((List<CustomWindowState>) pad.getCustomWindowStates());
		ma.setDefaultNameSpace(pad.getDefaultNamespace());
		ma.setEventDefinition((List<EventDefinition>) pad.getEventDefinitions());
		return ma;
	}
	
	/**
	 * 获得模块PortletApp定义信息
	 * 
	 * @param module
	 * @return
	 * @throws PortalServiceException
	 */
//	@SuppressWarnings("unchecked")
//	private   PortletApplicationDefinition getPortletAppDef(String module) {
//		String dsName = LfwRuntimeEnvironment.getDatasource();
//		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.DEPLOY_MODULE_DEFINITION_CACHE, dsName);
//		Map<String, PortalDeployDefinition> moduleFolders = (Map<String, PortalDeployDefinition>) cache.get(CacheKeys.DEPLOY_MODULE_DEFINITION_CACHE);
//		if (moduleFolders != null && (!moduleFolders.isEmpty()) && moduleFolders.containsKey(module)) {
//			return ((PortalDeployDefinition) moduleFolders.get(module)).getPortletDefineModule();
//		} else {
//			return null;
//		}
//	}

	/**
	 * 将InitParam实例转换为InitParams字符串
	 * 
	 * @param pt
	 * @return
	 */
	private static String buildInitParams(PortletDefinition pt) {
		List<? extends InitParam> initParams = pt.getInitParams();
		if (initParams == null)
			return null;

		StringBuffer paramBuf = new StringBuffer();
		for (int i = 0; i < initParams.size(); i++) {
			InitParam param = initParams.get(i);
			String key = param.getParamName();
			String value = param.getParamValue();
			paramBuf.append(key).append("#,#").append(value);
			if (i != initParams.size() - 1)
				paramBuf.append("$,$");
		}
		return paramBuf.toString();
	}

	/**
	 * 将InitParams字符串还原为InitParam实例
	 * 
	 * @param param
	 * @return
	 */
	private static List<InitParam> restoreInitParams(String param) {
		List<InitParam> initParams = new ArrayList<InitParam>();
		if (param == null || param.equals(""))
			return initParams;
		String[] params = param.split("\\$,\\$");
		for (int i = 0; i < params.length; i++) {
			InitParam initParam = new InitParam();

			String[] pair = params[i].split("#,#");
			if (pair.length == 2) {
				initParam.setParamName(pair[0]);
				initParam.setParamValue(pair[1]);
			} else {
				initParam.setParamName(pair[0]);
				initParam.setParamValue("");
			}
			initParams.add(initParam);
		}
		return initParams;
	}

	/**
	 * 将PortletInfo转换为字符串
	 * 
	 * @param pit
	 * @return
	 */
	private static String buildPortletInfo(PortletInfo pit) {
		StringBuffer infoBuf = new StringBuffer();
		infoBuf.append(StringUtils.defaultIfEmpty(pit.getTitle(), "")).append(";;;").append(StringUtils.defaultIfEmpty(pit.getShortTitle(), "")).append(";;;")
				.append(StringUtils.defaultIfEmpty(pit.getKeywords(), ""));
		return infoBuf.toString();
	}

	/**
	 * 将字符串还原为PortletInfo实例
	 * 
	 * @param infoBuf
	 * @return
	 */
	private static PortletInfo restorePortletInfo(String infoBuf) {
		PortletInfo pit = new PortletInfo();
		if (infoBuf == null)
			return pit;
		String[] infos = infoBuf.split(";;;");
		if (infos != null && infos.length > 0) {
			pit.setTitle(infos[0]);
			if (infos.length > 1) {
				pit.setShortTitle(infos[1]);
				if (infos.length > 2) {
					pit.setKeywords(infos[2]);
				}
			}
		}
		return pit;
	}

	/**
	 * 将Supports列表转换为字符串
	 * 
	 * @param supports
	 * @return
	 */
	private static String buildSupports(List<? extends Supports> supports) {
		if (supports != null && (!supports.isEmpty())) {
			List<String> strs = new ArrayList<String>();
			for (Supports st : supports) {
				strs.add(buildSupports(st));
			}
			return StringUtils.join(strs.toArray(), ";");
		}
		return "";
	}

	/**
	 * 将Supports 转换为字符串
	 * 
	 * @param st
	 * @return
	 */
	private static String buildSupports(Supports st) {
		List<String> strs = new ArrayList<String>();
		strs.add(st.getMimeType());
		strs.addAll(st.getPortletModes());

		String[] supports = strs.toArray(new String[0]);
		return StringUtils.join(supports, ",");
	}

	/**
	 * 将字符串还原为Supports列表
	 * 
	 * @param modes
	 * @return
	 */
	private static List<Supports> restoreSupports(String modes) {
		List<Supports> supports = new ArrayList<Supports>();
		if (modes != null) {
			String[] modesStr = modes.split(";");
			for (int i = 0; i < modesStr.length; i++) {
				String[] mds = modesStr[i].split(",");
				Supports support = new Supports();
				if (mds != null && mds.length > 0) {
					support.setMimeType(mds[0]);
					if (mds.length > 1) {
						for (int j = 1; j < mds.length; j++) {
							support.addPortletMode(mds[j]);
						}
					}
				}
				supports.add(support);
			}
		}
		return supports;
	}

	/**
	 * 获得版本号
	 * 
	 * @param pt
	 * @return
	 * @throws PortalServiceException
	 */
	public static String getVersion(PortletDefinition pt) throws PortalServiceException {
		Preference pf = pt.getPortletPreferences().getPortletPreference("version");
		if (pf == null) {
			Logger.error("Portlet:" + pt.getPortletName() + "未设置正确的版本号");
			throw new PortalServiceException("Portlet:" + pt.getPortletName() + "未设置正确的版本号");
		}
		List<String> versions = pf.getValues();
		if (versions != null && (!versions.isEmpty())) {
			return versions.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 将supportedLocale转换为字符串
	 * 
	 * @param supportedLocale
	 * @return
	 */
	private static String buildSupportedLocale(List<String> supportedLocale) {
		if (supportedLocale != null && (!supportedLocale.isEmpty())) {
			return StringUtils.join(supportedLocale.toArray(), ",");
		} else {
			return "";
		}
	}

	/**
	 * 将字符串还原为supportedLocale
	 * 
	 * @param supportedLocale
	 * @return
	 */
	private static List<String> restoreSupportedLocale(String locales) {
		List<String> supportedLocales = new ArrayList<String>();
		if (locales != null) {
			String[] lcls = locales.split(",");
			for (int i = 0; i < lcls.length; i++) {
				supportedLocales.add(lcls[i]);
			}
		}
		return supportedLocales;
	}

	/**
	 * 从缓存中获得系统级Portlet
	 * 
	 * @return
	 * @throws PortalServiceException
	 */
	public static Map<String, PortletDefinition> getSystemPortlets(PtPortletVO[] portletVOs) throws PortalServiceException {
		Map<String, PortletDefinition> systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
		if (portletVOs == null)
			return systemPortlets;
		for (PtPortletVO portletVO : portletVOs) {
			PortletDefinition pd = PortletDataWrap.warpDefinition(portletVO);
			systemPortlets.put(PortalPageDataWrap.modModuleName(portletVO.getModule(), portletVO.getPortletid()), pd);
		}
		return systemPortlets;
	}

	/**
	 * 获得用户的Portlet
	 * 
	 * @param portletVOs
	 * @throws PortalServiceException
	 */
	public static Map<String, Map<String, PortletDefinition>> getUsersDiyPortlets(PtPortletVO[] portletVOs) throws PortalServiceException {
		Map<String, Map<String, PortletDefinition>> map = new ConcurrentHashMap<String, Map<String, PortletDefinition>>();
		Map<String, PortletDefinition> systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
		String portalUser = null;
		if (portletVOs == null)
			return map;
		for (PtPortletVO portletVO : portletVOs) {
			if (StringUtils.isBlank(portalUser)) {
				portalUser = portletVO.getFk_portaluser();
			}
			PortletDefinition pd = PortletDataWrap.warpDefinition(portletVO);
			systemPortlets.put(PortalPageDataWrap.modModuleName(portletVO.getModule(), portletVO.getPortletid()), pd);
			if (!portalUser.equals(portletVO.getFk_portaluser())) {
				map.put(portalUser, systemPortlets);
				portalUser = portletVO.getFk_portaluser();
				systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
			}
		}
		map.put(portalUser, systemPortlets);
		return map;
	}

	/**
	 * 获得集团的Portlet
	 * 
	 * @param portletVOs
	 * @throws PortalServiceException
	 */
	public static Map<String, Map<String, PortletDefinition>> getGroupsPortlets(PtPortletVO[] portletVOs) throws PortalServiceException {
		Map<String, Map<String, PortletDefinition>> map = new ConcurrentHashMap<String, Map<String, PortletDefinition>>();
		if (portletVOs == null)
			return map;
		Map<String, PortletDefinition> systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
		String portalUser = null;
		for (PtPortletVO portletVO : portletVOs) {
			if (StringUtils.isBlank(portalUser)) {
				portalUser = portletVO.getPk_group();
			}
			PortletDefinition pd = PortletDataWrap.warpDefinition(portletVO);
			if (!portalUser.equals(portletVO.getPk_group())) {
				map.put(portalUser, systemPortlets);
				portalUser = portletVO.getPk_group();
				systemPortlets = new ConcurrentHashMap<String, PortletDefinition>();
			}
			systemPortlets.put(PortalPageDataWrap.modModuleName(portletVO.getModule(), portletVO.getPortletid()), pd);
		}
		// 最后一个
		map.put(portalUser, systemPortlets);
		return map;
	}


	/**
	 * 将PortletDefine中Preferences转换为Portlet 2.0规范中的Preferences
	 * 
	 * @param prefs 原始配置信息
	 * @return
	 */
	public static PortletPreferences convertPreferences(Preferences prefs) {
		PortletPreferencesImpl portletPrefs = new PortletPreferencesImpl();
		List<? extends Preference> list = prefs.getPortletPreferences();
		if (list != null && list.size() > 0) {
			Iterator<? extends Preference> it = list.iterator();
			while (it.hasNext()) {
				Preference pref = it.next();
				try {
					portletPrefs.setValues(pref.getName(), pref.getValues() == null ? null : pref.getValues().toArray(new String[0]));
				} catch (ReadOnlyException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return portletPrefs;
	}
	
	/**
	 * 为PortletDisplay设置Module
	 * @param portlets
	 * @param module
	 * @return
	 */
	public static List<PortletDisplay> addModuleInfo(List<PortletDisplay> portlets, String module) {
		List<PortletDisplay> target = new ArrayList<PortletDisplay>();
		if(!PtUtil.isNull(portlets)){
			for (PortletDisplay portlet : portlets) {
				portlet.setModule(module);
				target.add(portlet);
			}
		}
		return target;
	}
	
	/**
	 * 检查Portal页面中是否存在此Portlet
	 * @param page
	 * @param module
	 * @param name
	 * @return
	 */
	public static boolean hasPortlet(Page page,String module,String name){
		String[][] names = page.getAllPortletNames();
		if(names != null && names.length >0){
			for(String[] pn : names){
				if(pn[0].equals(module) && pn[1].equals(name))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得Portlet支持的模式
	 * @param portlet Portlet定义
	 * @param mimeType MIME类型
	 * @return
	 */
	public static List<String> getSupportModes(PortletDefinition portlet,String mimeType){
		List<String> portletModes = new ArrayList<String>();
		List<? extends Supports> supports = portlet.getSupports();
		if(!PtUtil.isNull(supports)){
			for(Supports support : supports){
				List<String> _portletModes = support.getPortletModes();
				if(StringUtils.equals(mimeType,support.getMimeType()) && _portletModes != null)
					portletModes.addAll(_portletModes);
			}
		}
		if(portletModes.isEmpty())
			portletModes.add("view");
		return portletModes;
	}

	/**
	 * 获得Portlet支持的窗口模式
	 * @param portlet Portlet定义
	 * @param mimeType MIME类型
	 * @return
	 */
	public static List<String> getSupportWindowStates(PortletDefinition portlet,String mimeType){
		List<String> windowStates = new ArrayList<String>();
		List<? extends Supports> supports = portlet.getSupports();
		if(!PtUtil.isNull(supports)){
			for(Supports support : supports){
				List<String> _windowStates = support.getWindowStates();
				if(StringUtils.equals(mimeType,support.getMimeType()) && _windowStates != null)
					windowStates.addAll(_windowStates);
			}
		}
		if(windowStates.isEmpty())
			windowStates.add("normal");
		return windowStates;
	}

}
