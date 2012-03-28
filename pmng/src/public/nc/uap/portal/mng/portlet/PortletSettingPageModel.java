package nc.uap.portal.mng.portlet;

import java.util.List;

import nc.portal.portlet.plugin.IPreferenceEditor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.event.AppRequestProcessor;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.vo.PtPortletVO;
import nc.uap.portal.vo.PtPreferenceVO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Portlet设置页面模型
 * 访问地址/portal/app/mockapp/portletsetting?model=nc.uap.portal.mng.portlet.PortletSettingPageModel&module=pmng&pagename=admin&pk_portlet=0001AA10000000002W6P
 * @author licza
 *
 */
public class PortletSettingPageModel extends PageModel{
	private static final String FORM = "form1";
	private static final String TREEDS = "masterDs";
	private static final String CANCELBT = "cancelbt";
	private static final String OKBT = "okbt";
	private static final String WIDGET_ID = "main";
	private static final String ID_FIELD = "id";
	private static final String PID_FIELD = "pid";
	private static final String LABEL_FIELD = "label";
	private static final String LOAD_FIELD = "load";

	private Preferences pf;
	@Override
	protected PageMeta createPageMeta() {
		
		try {
			this.initPreferences();
			LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("_PREFERENCE_", pf);
		} catch (PortalServiceException e) {
			throw new LfwRuntimeException("获取Portlet配置信息失败!");
		}

		PageMeta pm = new PageMeta();
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		LfwWidget widget = new LfwWidget();
		widget.setId(WIDGET_ID);
		widget.setControllerClazz(PortletSettingMainController.class.getName());
		pm.addWidget(widget);
		
		Dataset ds = new Dataset();
		ds.setId(TREEDS);
		ds.setLazyLoad(false);
		widget.getViewModels().addDataset(ds);
		
		FormComp form = new FormComp();
		form.setId(FORM);
		form.setDataset(TREEDS);
		form.setEleWidth(250);
		widget.getViewComponents().addComponent(form);
		FieldSet fs = ds.getFieldSet();
		form.setRowHeight(26);
		form.setColumnCount(Integer.valueOf(1));
		form.setRenderType(1);
		form.setEnabled(true);
		
		
		EventConf event = new EventConf();
		event.setName(DatasetListener.ON_DATA_LOAD);
		event.setMethodName("dataLoad");
		event.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(event);
		
		// pm ds form
		
		List<Preference> pflist = pf.getPortletPreferences();
		if (CollectionUtils.isNotEmpty(pflist)) {
			for (Preference p : pflist) {
				
				String name = p.getName();
				String description = p.getDescription();
				Field dsField = new Field();
				dsField.setId(name);
				dsField.setText(description);
				dsField.setDataType(StringDataTypeConst.STRING);
				fs.addField(dsField);
				
				FormElement formElement = new FormElement(name);
				formElement.setId(name);
				formElement.setField(name);
				formElement.setText(description!=null?description:name);
				formElement.setDataType(StringDataTypeConst.STRING);
				formElement.setEditorType(EditorTypeConst.STRINGTEXT);
				if(name.equals("version")){
					formElement.setVisible(false);
				}
				form.addElement(formElement);
				//formElement
			}
		}
		ButtonComp okbt = new ButtonComp();
		constructOkButton(okbt);
		widget.getViewComponents().addComponent(okbt);
		
		ButtonComp cancelbt = new ButtonComp();
		constructCancelButton(cancelbt);
		widget.getViewComponents().addComponent(cancelbt);
		return pm;
		
	}
	protected void constructOkButton(ButtonComp okbt) {
		okbt.setId(OKBT);
		okbt.setText("确定");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("okButtonClick");
		
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(TREEDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		event.setSubmitRule(submitRule);
		
		
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId("main");
		submitRule.setParentSubmitRule(parentSum);
		
		event.setJsEventClaszz(MouseListener.class.getName());
		okbt.addEventConf(event);
	}

	protected void constructCancelButton(ButtonComp cancelbt) {
		cancelbt.setId(CANCELBT);
		cancelbt.setText("取消");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("cancelButtonClick");
		event.setJsEventClaszz(MouseListener.class.getName());
		cancelbt.addEventConf(event);
	}

	/**
	 * 初始化配置信息
	 * @throws PortalServiceException
	 */
	void initPreferences() throws PortalServiceException{
		WebContext ctx = LfwRuntimeEnvironment.getWebContext();
		IPtPortletQryService portletQry = PortalServiceUtil.getPortletQryService();

		String pk_portlet = ctx.getParameter("pk_portlet");
		
		String pk_group = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();

		String pageModule = ctx.getParameter("module") ;
		String pageid = ctx.getParameter("pagename") ;
		PtPortletVO portlet = portletQry.findPortletByPK(pk_portlet);
		String portletModule = portlet.getModule();
		String portletid = portlet.getPortletid() ;
		
		String pageName = PortalPageDataWrap.modModuleName(pageModule, pageid);
		String portletName = PortalPageDataWrap.modModuleName(portletModule,portletid);
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("_PAGENAME_", pageName);
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("_PORTLETNAME_", portletName);
		LfwRuntimeEnvironment.getWebContext().getWebSession().setAttribute("_PKPORTLET_", pk_portlet);

		JaxbMarshalFactory jmf = JaxbMarshalFactory.newIns();

		IPtPortletQryService pq = PortalServiceUtil.getPortletQryService();
		PtPreferenceVO vo = pq.getGroupPortletPreference(pk_group, portletName, pageName);
		/**
		 * 取集团配置
		 */
		if (vo != null) {
			this.pf = jmf.encodeXML(Preferences.class, vo.doGetPreferences());
		} else {
			this.pf = jmf.encodeXML(Preferences.class, portlet.getPreferences());
		}
		List<PtExtension> exs = PluginManager.newIns().getExtensions(IPreferenceEditor.PID);
		for(PtExtension ex :exs){
			IPreferenceEditor e = ex.newInstance(IPreferenceEditor.class);
			if(portletName.equals(e.getPortletId())){
				pe = e;
			}
		}
	}
	
		IPreferenceEditor pe = null;
	
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		UIMeta uimeta = new UIMeta();
		
		UIWidget widget = new UIWidget();
		widget.setId(WIDGET_ID);
		UIMeta wuimeta = new UIMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		
		constructViewUI(wuimeta);
		
		uimeta.setReference(1);
		uimeta.setElement(widget);
		
		
		return uimeta;
	}

	private void constructViewUI(UIMeta wuimeta) {
		
		UIFlowvLayout flvLayout = new UIFlowvLayout();
		flvLayout.setId("flowv1");
		flvLayout.setWidgetId(WIDGET_ID);
		
		UIFlowvPanel flvPanel0 = new UIFlowvPanel();
		flvPanel0.setId("flowvp0");
		flvPanel0.setHeight("30");
		flvLayout.addPanel(flvPanel0);
		
		UIFlowvPanel flvPanel1 = new UIFlowvPanel();
		flvPanel1.setId("flowvp1");
		
			UIFlowhLayout flhlayout1 = new UIFlowhLayout();
			flhlayout1.setId("flowh1");
			flhlayout1.setWidgetId(WIDGET_ID);
				
				UIFlowhPanel flhp01 = new UIFlowhPanel();
				flhp01.setId("flhp01");
				flhp01.setWidth("50");
				flhlayout1.addPanel(flhp01);
				
				UIFlowhPanel flhp02 = new UIFlowhPanel();
				flhp02.setId("flhp02");
					UIFormComp uiForm = new UIFormComp();
					uiForm.setWidgetId(WIDGET_ID);
					uiForm.setId(FORM);
					flhp02.setElement(uiForm);
				flhlayout1.addPanel(flhp02);
				
				UIFlowhPanel flhp03 = new UIFlowhPanel();
				flhp03.setId("flhp03");
				flhp03.setWidth("50");
				flhlayout1.addPanel(flhp03);	
				
		flvPanel1.setElement(flhlayout1);
		flvLayout.addPanel(flvPanel1);
		
		UIFlowvPanel flvPanel2 = new UIFlowvPanel();
		flvPanel2.setId("flowvp2");
		flvPanel2.setHeight("30");
		
			UIFlowhLayout flhLayout2 = new UIFlowhLayout();
			flhLayout2.setId("flowh2");
			flhLayout2.setWidgetId(WIDGET_ID);
			
				UIFlowhPanel flhP1 = new UIFlowhPanel();
				flhP1.setId("flowhp1");
				flhLayout2.addPanel(flhP1);
				
				UIFlowhPanel flhP2 = new UIFlowhPanel();
				flhP2.setId("flowhp2");
				flhLayout2.addPanel(flhP2);
					
				
				UIFlowhPanel flhP3 = new UIFlowhPanel();
				flhP3.setId("flowhp3");
				flhLayout2.addPanel(flhP3);
			
				UIFlowhPanel flhP4 = new UIFlowhPanel();
				flhP4.setId("flowhp4");
				UIButton okbt = new UIButton();
				okbt.setWidgetId(WIDGET_ID);
				okbt.setId(OKBT);
				flhP4.setElement(okbt);	
				flhLayout2.addPanel(flhP4);
				
				UIFlowhPanel flhP5 = new UIFlowhPanel();
				flhP5.setId("flowhp5");
				UIButton cancelbt = new UIButton();
				cancelbt.setWidgetId(WIDGET_ID);
				cancelbt.setId(CANCELBT);
				flhP5.setElement(cancelbt);
				flhLayout2.addPanel(flhP5);
				
				UIFlowhPanel flhP6 = new UIFlowhPanel();
				flhP6.setId("flowhp6");
				flhP6.setWidth("1");
				flhLayout2.addPanel(flhP6);
				
		flvPanel2.setElement(flhLayout2);
		flvLayout.addPanel(flvPanel2);			
				
		wuimeta.setElement(flvLayout);
		//uimeta
	}
}
