package nc.portal.sso.pagemodel;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.portal.sso.SsoConstant;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.event.listener.PageServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.config.RefNodeConf;
import nc.uap.lfw.core.processor.EventRequestProcessor;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.login.authfield.ComboExtAuthField;
import nc.uap.lfw.login.authfield.DateExtAuthField;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.authfield.ExtAuthFiledTypeConst;
import nc.uap.lfw.login.authfield.HiddenExtAuthField;
import nc.uap.lfw.login.authfield.PasswordExtAuthField;
import nc.uap.lfw.login.authfield.RefExtAuthField;
import nc.uap.lfw.login.authfield.TextExtAuthField;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.PortletSessionUtil;

/**
 * 凭证制作页面模型
 * 
 * @author licza
 * 
 */
public class CredentialEditPageModel extends PageModel {

	public static final String CREDENTIAL_DS = "credential_ds";
	public static final String CREDENTIAL_FORM = "credential_form";
	public static final String ERROR_INFO_DS = "errorInfoDs";
	public static final String DEFAULT_VALUE_MAP = "default_value_map";
	public static final String WMODE = "wmode";
	public static final String WMODE_DIALOG = "dialog";
	/**
	 * 创建PageMeta对象
	 */
	protected PageMeta createPageMeta() {
		try {
			HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
			String portletId = getParam(SsoConstant.PORTLETID);
			String systemCode = getParam(SsoConstant.SYSTEM_CODE);
			String sharelevel = getParam(SsoConstant.SHARELEVEL);
			String windowState = getParam(SsoConstant.PORTLETWINDOWSTATE);
			String wmode = getParam(WMODE);
			
			WebSession websession = getWebContext().getWebSession();
			/**
			 * 构造页面数据
			 */
			PageMeta meta = new PageMeta();
			meta.setProcessorClazz(EventRequestProcessor.class.getName());
			/**
			 * 构造片段信息
			 */
			LfwWidget wd = new LfwWidget();
			wd.setId("main");
			meta.addWidget(wd);

			/**
			 * 获取登陆表单项信息
			 */
			ExtAuthField[] authFields = (ExtAuthField[]) PortletSessionUtil.get(SsoConstant.AUTHFIELDS);
			if (authFields == null) {

				IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
				ISSOQueryService qs = PintServiceFactory.getSsoQryService();
				PtCredentialVO credential = qs.getCredentials(userVO.getPk_user(), portletId, systemCode, new Integer(sharelevel));
				SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
				IWebAppLoginService was = ProviderFetcher.getInstance().getAuthService(systemCode);
				if(was == null)
					throw new LfwRuntimeException("此系统无集成配置!");
				authFields = ProviderFetcher.getInstance().getAuthService(systemCode).getCredentialFields(request, provider, userVO, credential);
				if (authFields == null)
					return meta;
			}

			/**
			 * 获取表单模板里面的表单项信息
			 */

			String dsId = CREDENTIAL_DS;
			FormComp form = new FormComp(CREDENTIAL_FORM);
			form.setRowHeight(26);
			form.setColumnCount((1));
			form.setDataset(dsId);
			form.setRenderType(1);
			form.setEnabled(true);
			Dataset ds = new Dataset(dsId);
			ds.setLazyLoad(false);
			ds.setPageSize(-1);
			wd.getViewModels().addDataset(ds);
			Map<Integer, Object> defaultValueMap = new HashMap<Integer, Object>();
			wd.getViewComponents().addComponent(form);
			for (int i = 0; i < authFields.length; i++) {
				FormElement formElement = new FormElement(authFields[i].getName());
				form.addElement(formElement);
				formElement.setField(authFields[i].getName());
				formElement.setLabel(authFields[i].getLabel());
//				formElement.setWidth(String.valueOf(180));

				Field dsField = new Field(authFields[i].getName());
				ds.getFieldSet().addField(dsField);
				dsField.setI18nName(authFields[i].getLabel());
				dsField.setText(authFields[i].getLabel());
				if (authFields[i].isRequired())
					dsField.setNullAble(false);
				else
					dsField.setNullAble(true);

				switch (authFields[i].getType()) {
				case ExtAuthFiledTypeConst.TYPE_USERID:
					formElement.setDataType(StringDataTypeConst.STRING);
					defaultValueMap.put((i), ((TextExtAuthField) authFields[i]).getDefaultValue());
					dsField.setDataType(StringDataTypeConst.STRING);
					formElement.setEditorType(EditorTypeConst.STRINGTEXT);
					websession.setAttribute(SsoConstant.USER_ID_FIELD, authFields[i].getName());
					break;
				case ExtAuthFiledTypeConst.TYPE_TEXT:
					formElement.setDataType(StringDataTypeConst.STRING);
					defaultValueMap.put((i), ((TextExtAuthField) authFields[i]).getDefaultValue());
					dsField.setDataType(StringDataTypeConst.STRING);
					formElement.setEditorType(EditorTypeConst.STRINGTEXT);
					break;
				case ExtAuthFiledTypeConst.TYPE_PASSWORD:
					formElement.setEditorType(EditorTypeConst.PWDTEXT);
					defaultValueMap.put((i), ((PasswordExtAuthField) authFields[i]).getDefaultValue());
					dsField.setDataType(StringDataTypeConst.STRING);
					websession.setAttribute(SsoConstant.PWD_FIELD, authFields[i].getName());
					break;
				case ExtAuthFiledTypeConst.TYPE_INTEGER:
					formElement.setDataType(StringDataTypeConst.INT);
					dsField.setDataType(StringDataTypeConst.INT);
					formElement.setEditorType(EditorTypeConst.INTEGERTEXT);
					break;
				case ExtAuthFiledTypeConst.TYPE_DATE:
					formElement.setDataType(StringDataTypeConst.UFDATE);
					defaultValueMap.put((i), ((DateExtAuthField) authFields[i]).getDefaultValue());
					dsField.setDataType(StringDataTypeConst.UFDATE);
					formElement.setEditorType(EditorTypeConst.DATETEXT);
					break;
				case ExtAuthFiledTypeConst.TYPE_CHOOSE:
					dsField.setDataType(StringDataTypeConst.STRING);
					formElement.setEditorType(EditorTypeConst.COMBODATA);

					ComboExtAuthField combo = (ComboExtAuthField) authFields[i];
					String[][] options = combo.getOptions();
					String refCombId = combo.getName() + "_refComboData";
					formElement.setRefComboData(refCombId);

					StaticComboData comboData = new StaticComboData();
					comboData.setId(refCombId);
					wd.getViewModels().addComboData(comboData);
					if (options != null) {
						for (int j = 0; j < options.length; j++) {
							CombItem item = new CombItem();
							item.setI18nName(options[j][1]);
							item.setValue(options[j][0]);
							comboData.addCombItem(item);
						}
					}
					if (combo.getSelectedIndex() != -1)
						defaultValueMap.put((i), options[combo.getSelectedIndex()][0]);
					break;

				case ExtAuthFiledTypeConst.TYPE_REF:
					dsField.setDataType(StringDataTypeConst.STRING);
					formElement.setEditorType(EditorTypeConst.REFERENCE);
					formElement.setEditable(true);
					RefExtAuthField ref = (RefExtAuthField) authFields[i];
					String refNodeId = authFields[i].getName() + "_refNode";
					
					RefNode refNode = new RefNode();
					refNode.setId(refNodeId);
					refNode.setRefId(refNodeId);
					refNode.setRefModel(ref.getRefmodel());
					refNode.setReadDs(ref.getReadDs());
					refNode.setReadFields(ref.getReadFields());
					refNode.setText(ref.getLabel());
					refNode.setDataListener(ref.getDsloaderclass());
					refNode.setWriteDs("credential_ds");
					refNode.setPath(ref.getPath());
					refNode.setPagemeta(ref.getPageMeta());
					refNode.setWriteFields(ref.getWriteFields());
					wd.getViewModels().addRefNode(refNode);
					formElement.setRefNode(refNodeId);
					defaultValueMap.put((i), ref.getDefaultValue());
					break;
				case ExtAuthFiledTypeConst.TYPE_HIDDEN:
					formElement.setDataType(StringDataTypeConst.STRING);
					formElement.setVisible(false);
					defaultValueMap.put((i), ((HiddenExtAuthField) authFields[i]).getDefaultValue());
					dsField.setDataType(StringDataTypeConst.STRING);
				}
			}
//			form.setHeight(String.valueOf(40 + form.getRowHeight() * form.getElementCountWithoutHidden()));

			DatasetListener dsl = new DatasetListener();
			dsl.setId("dslistener");
			EventHandlerConf dsevent = DatasetListener.getOnDataLoadEvent();
			dsevent.setOnserver(true);
			dsl.setServerClazz(nc.portal.sso.listener.DefaultCredentialDatasetListener.class.getName());
			dsl.addEventHandler(dsevent);
			ds.addListener(dsl);
			/**
			 * 设置服务器监听类
			 */
			MouseListener ml = new MouseListener();
			ml.setId("alistener");
			ml.setServerClazz(nc.portal.sso.listener.SsoLoginMouseListener.class.getName());
			EventHandlerConf event = MouseListener.getOnClickEvent();
			event.setOnserver(true);
			ml.addEventHandler(event);
			/**
			 * 登录按钮处理
			 */
			ButtonComp save = new ButtonComp();
			save.setId("submit");
			save.setText("登录");
			wd.getViewComponents().addComponent(save);
			save.addListener(ml);
			/**
			 * 取消按钮处理
			 */
			ButtonComp cancel = new ButtonComp();
			cancel.setId("cancel");
			cancel.setText("取消");
			wd.getViewComponents().addComponent(cancel);
			cancel.addListener(ml);
			
			/**
			 * 放弃集成
			 */
//			if(StringUtils.equals(wmode, WMODE_DIALOG)){
				ButtonComp giveup = new ButtonComp();
				giveup.setId("giveup");
				giveup.setText("不再询问");
				wd.getViewComponents().addComponent(giveup);
				giveup.addListener(ml);
//			}
			 
//			PageListener afterPageInitListener = new PageListener();
//			afterPageInitListener.setId("afterInitListener");
//			afterPageInitListener.setServerClazz("nc.portal.sso.listener.CredentialPageInitListener");
//			EventHandlerConf pageInitEvent = PageListener.getAfterPageInitEvent();
//			EventSubmitRule submitRule = new EventSubmitRule();
//			WidgetRule widgetRule = new WidgetRule();
//			widgetRule.setId("main");
//			DatasetRule dsRule = new DatasetRule();
//			dsRule.setId("credential_ds");
//			widgetRule.addDsRule(dsRule);
//			submitRule.addWidgetRule(widgetRule);
//			pageInitEvent.setSubmitRule(submitRule);
//			pageInitEvent.setOnserver(true);
//			afterPageInitListener.addEventHandler(pageInitEvent);
//			meta.addListener(afterPageInitListener);
			
			// Field默认值
			websession.setAttribute(DEFAULT_VALUE_MAP, (HashMap<Integer, Object>) defaultValueMap);
			// Portlet窗口
			websession.setAttribute(SsoConstant.PORTLETID, portletId);
			websession.setAttribute(SsoConstant.SYSTEM_CODE, systemCode);
			websession.setAttribute(SsoConstant.SHARELEVEL, sharelevel);
			// Portlet窗口状态
			websession.setAttribute(SsoConstant.PORTLETWINDOWSTATE, windowState);
			websession.setAttribute(WMODE, wmode);
			return meta;
		}catch(LfwRuntimeException ex){
			throw ex;
		}catch (Exception e) {
			LfwLogger.error("pagemeta生成失败!", e);
		}
		return new PageMeta(); 
	}

	/**
	 * 获得传入的参数
	 * 
	 * @param para
	 * @return
	 */
	private String getParam(String para) {
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		return request.getParameter(para);
	}
}
