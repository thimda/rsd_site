package nc.uap.portal.container.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Event;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.ParameterKey;
import nc.uap.portal.container.exception.PortletContainerException;
import nc.uap.portal.container.om.EventDefinition;
import nc.uap.portal.container.om.EventDefinitionReference;
import nc.uap.portal.container.om.ModuleApplication;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.portlet.EventImpl;
import nc.uap.portal.container.portlet.PortletContainer;
import nc.uap.portal.container.portlet.PortletWindow;
import nc.uap.portal.container.portlet.PortletWindowID;
import nc.uap.portal.container.portlet.PortletWindowImpl;
import nc.uap.portal.container.service.itf.EventCoordinationService;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Portlet;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PortalPageDataWrap;


/**
 * 事件处理服务实现
 * 
 * @author licza
 * 
 */
public class EventCoordinationServiceImpl implements EventCoordinationService {
	/**
	 * 处理事件
	 * 
	 * @see nc.uap.portal.container.service.itf.EventCoordinationService
	 */
	public void processEvents(PortletContainer container, PortletWindow portletWindow, HttpServletRequest request, HttpServletResponse response,
			List<Event> events) {
		String pageModule = request.getParameter(ParameterKey.PAGE_MODULE);
		String pageName = request.getParameter(ParameterKey.PAGE_NAME);
		LfwSessionBean sbean = LfwRuntimeEnvironment.getLfwSessionBean();
		if(sbean == null)
			return ;
		String userId = sbean.getPk_user();
		String groupId = sbean.getPk_unit();
		// 获得所有页面
		Page[] myPages = null;
			try {
				myPages = PortalPageDataWrap.getUserPages();
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(),e);
			}
		if(myPages == null)
			return ;
		List<PortletDefinition> pds = new ArrayList<PortletDefinition>();
			// 找出当前页面
			Page page = PortalPageDataWrap.getUserPage(myPages, pageModule, pageName);
			Portlet[] portlets = page.getAllPortlet();
			IPtPortletQryService portletQry = PortalServiceUtil.getPortletQryService();
			for (Portlet pt : portlets) {
				String ptModule = pageModule;
				String ptName = pt.getName();
				if (ptName.indexOf(":") > 0) {
					String[] _ts = ptName.split(":");
					ptModule = _ts[0];
					ptName = _ts[1];
				}
				PortletDefinition _ptDef = portletQry.findPortlet(userId, groupId, ptName, ptModule, pageModule, pageName);
				pds.add(_ptDef);
			}
		for (Event event : events) {
			for (PortletDefinition pd : pds) {
				if(pd != null){
					List<EventDefinitionReference> supportEvents = pd.getSupportedProcessingEvent();
					if (supportEvents != null) {
						for (EventDefinitionReference supportEvent : supportEvents) {
							PortletWindowID wid = new PortletWindowID(portletWindow.getId().getPageModule(), portletWindow.getId().getPageName(), pd.getModule(), pd.getPortletName());
							PortletWindowImpl pw = new PortletWindowImpl(wid);
							pw.setPortlet(pd);
							pw.setPortletMode(PortletMode.VIEW);
							pw.setWindowState(WindowState.NORMAL);
							if (supportEvent.getName().equals(event.getName())) {
								doEvent(container, pw, event, request, response);
							}
						}
					}
				}
					 
				
			}
		}
	}

	/**
	 * 调用容器对事件进行处理
	 * 
	 * @param container
	 * @param portletWindow
	 * @param event
	 * @param request
	 * @param response
	 */
	protected void doEvent(PortletContainer container, PortletWindow portletWindow, Event event, HttpServletRequest request, HttpServletResponse response) {
		try {
			Object value = event.getValue();
			XMLStreamReader xml = null;
			try {
				if (value instanceof String) {
					String in = (String) value;
					xml = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(in));
				}
			} catch (XMLStreamException e1) {
				throw new IllegalStateException(e1);
			} catch (FactoryConfigurationError e1) {
				throw new IllegalStateException(e1);
			}
			if (xml != null) {
				try {
					//JAXB解析参数
					EventDefinition eventDefinitionDD = getEventDefintion(portletWindow, event.getQName());
					ClassLoader loader = Thread.currentThread().getContextClassLoader();
					Class<? extends Serializable> clazz = loader.loadClass(eventDefinitionDD.getValueType()).asSubclass(Serializable.class);
					Serializable result =	JaxbMarshalFactory.newIns().lookupUnMarshaller(clazz).unmarshal(xml, clazz).getValue();
					event = new EventImpl(event.getQName(), result);
				} catch (JAXBException e) {
					throw new IllegalStateException(e);
				} catch (ClassCastException e) {
					throw new IllegalStateException(e);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
			container.doEvent(portletWindow, request, response, event);
		} catch (PortletException e) {
			LfwLogger.warn(e.getMessage());
		} catch (IOException e) {
			LfwLogger.warn(e.getMessage());
		} catch (PortletContainerException e) {
			LfwLogger.warn(e.getMessage());
		}
	}

	/**
	 * 获得事件定义
	 * 
	 * @param portletWindow
	 * @param name
	 * @return
	 */
	private EventDefinition getEventDefintion(PortletWindow portletWindow, QName name) {
		String ns = name.getNamespaceURI();
		ModuleApplication[] apps = PortalCacheManager.getModuleAppByModuleNs(ns);
		if(apps != null && apps.length > 0){
			for(ModuleApplication appDD : apps){
				for (EventDefinition def : appDD.getEventDefinitions()) {
					if (def.getQName() != null) {
						if (def.getQName().equals(name))
							return def;
					} else {
						QName tmp = new QName(appDD.getDefaultNameSpace(), def.getName());
						if (tmp.equals(name)) {
							return def;
						}
					}
				}
			}
		}
		LfwLogger.error("事件:" + name + "未被定义");
		throw new IllegalStateException();
	}
}
