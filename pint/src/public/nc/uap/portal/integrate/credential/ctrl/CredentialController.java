package nc.uap.portal.integrate.credential.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.credential.constant.SsoConstant;
import nc.uap.portal.integrate.credential.pagemodel.CredentialEditPageModel;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.vo.PtSlotVO;

import org.apache.commons.lang.StringUtils;

/**
 * 凭据制作页面控制类
 * @author licza
 *
 */
public class CredentialController implements IController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8065841004081332923L;
	
	public void onDataLoad(DataLoadEvent se) {
		WebSession websession = LfwRuntimeEnvironment.getWebContext().getWebSession();
		Map<Integer, Object> defaultValueMap = (HashMap<Integer, Object>) websession.getAttribute(CredentialEditPageModel.DEFAULT_VALUE_MAP);
		Dataset ds = se.getSource();
		ds.setCurrentKey(Dataset.MASTER_KEY);
		Row row = ds.getEmptyRow();
		if (defaultValueMap != null) {
			for (int i = 0; i < ds.getFieldCount(); i++) {
				if (defaultValueMap.containsKey(i) && defaultValueMap.get(i)!=null)
					row.setValue(i, defaultValueMap.get(i));
			}
		}
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowIndex(row));
		ds.setEnabled(true);
		ViewComponents wcs = AppLifeCycleContext.current().getViewContext().getView().getViewComponents();
		WebComponent wc = wcs.getComponent(CredentialEditPageModel.CREDENTIAL_FORM);
		wc.setEnabled(true);
		if(!StringUtils.equals((String)websession.getAttribute("wmode"), "dialog")){
			ButtonComp bc = (ButtonComp)wcs.getComponent("giveup");
			bc.setVisible(false);
		}
	}
	
	
	public void onclick(MouseEvent<ButtonComp> e) {
		ButtonComp button = e.getSource();
		// 从websession中获得数据
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String wmode = (String) session.getAttribute("wmode");
		String userIdField = (String) session.getAttribute(SsoConstant.USER_ID_FIELD);
		String pwdField = (String) session.getAttribute(SsoConstant.PWD_FIELD);
		boolean dlgMode = StringUtils.equals(wmode, "dialog");
		PtSlotVO slotVO = getSlot();
		// 当前数据集
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(CredentialEditPageModel.CREDENTIAL_DS);
		// 提交事件
		if (button.getId().equals("okbt")) {
			IUserVO userDetails = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
			// 获得应用名
			String portalUser = userDetails.getUserid();
			Logger.debug("===WebApplicationPortlet类processAction方法:为登录用户" + portalUser + " 制造凭证.");

			String systemCode = (String) session.getAttribute(SsoConstant.SYSTEM_CODE);
			String portletWinId = (String) session.getAttribute(SsoConstant.PORTLETID);
			String sharelevel = (String) session.getAttribute(SsoConstant.SHARELEVEL);
			// 构造凭证槽VO
			 
			HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
			SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
			// 设置PortletID
			request.setAttribute("$portletId", portletWinId);
			// 必填项校验
			IDataValidator dataValdator = new DefaultDataValidator();
			dataValdator.validate(ds, new LfwWidget());

			// 将登陆表单信息放到request中
			FieldSet fs = ds.getFieldSet();
			int count = fs.getFieldCount();
			RowData rd = ds.getCurrentRowData();
			if (rd == null)
				return;
			Row row = ds.getSelectedRow();
			if (row == null || row instanceof EmptyRow)
				return;
			List<String> fds = new ArrayList<String>();
			
			for (int i = 0; i < count; i++) {
				Field field = fs.getField(i);
				Object value = row.getValue(i);
				String fid = field.getId();
				fds.add(fid);
//				if(!StringUtils.equals(userIdField, fid) && !StringUtils.equals(userIdField, fid)){
//				}
//					if (field.isPrimaryKey()) {
						request.setAttribute(field.getId(), value);
//					}
			}
			
			request.setAttribute(SsoConstant.USER_ID_FIELD, userIdField);
			request.setAttribute(SsoConstant.PWD_FIELD, pwdField);
			request.setAttribute(SsoConstant.VERIFY_FIELDS, StringUtils.join(fds.iterator(), ","));
			try {
				PtCredentialVO credentialVO = ProviderFetcher.getInstance().getAuthService(systemCode).credentialProcess(request, provider);
				if(credentialVO==null)
					throw new LfwRuntimeException("登陆失败!");
				Logger.debug("===SsoLoginMouseListener:调用相应的Provider的制造凭证方法成功!");
				// 存储凭证(修改,增加)
				PintServiceFactory.getSsoService().createCredentials(credentialVO, slotVO);
				Logger.debug("===SsoLoginMouseListener:制作完凭证并进行了保存,转向View视图!");
				// 制作完凭证后,转向view模式
			} catch (Exception e2) {
				LfwLogger.error("===SsoLoginMouseListener:存储凭证失败", e2);
				throw new LfwRuntimeException(e2.getMessage());
			}
		}
		// 取消事件
		if (button.getId().equals("cancelbt")) {
			// 清空数据集
			Row row = ds.getEmptyRow();
			ds.addRow(row);
			ds.setSelectedIndex(ds.getRowIndex(row));
			ds.setEnabled(true);
		}
		// 放弃集成
		if (button.getId().equals("giveup")) {
			//放弃
			slotVO.setActive(0);
			try {
				PintServiceFactory.getSsoService().addSlot(slotVO);
			} catch (PortalServiceException e1) {
				LfwLogger.error(e1.getMessage(),e1);
			}
		}
		authComplite(slotVO, dlgMode);
	}
	/**
	 * 构造凭证槽
	 * @return
	 */
	private PtSlotVO getSlot(){
		IUserVO userDetails = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		// 获得应用名
		String portalUser = userDetails.getUserid();
		// 从websession中获得数据
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String systemCode = (String) session.getAttribute(SsoConstant.SYSTEM_CODE);
		String portletWinId = (String) session.getAttribute(SsoConstant.PORTLETID);
		String sharelevel = (String) session.getAttribute(SsoConstant.SHARELEVEL);
		// 构造凭证槽VO
		PtSlotVO slotVO = new PtSlotVO();
		slotVO.setClassname(systemCode);
		slotVO.setPk_group(userDetails.getPk_group());
		slotVO.setPortletid(portletWinId);
		slotVO.setSharelevel(new Integer(sharelevel));
		slotVO.setUserid(portalUser);
		return slotVO;
	}
	/**
	 * 验证完毕
	 * @param ctx
	 * @param slot
	 * @param dlgMode
	 */
	private void authComplite( PtSlotVO slot ,boolean dlgMode){
		if(dlgMode){
			AppLifeCycleContext.current().getApplicationContext().addExecScript("parent.authCorrect('"+slot.getPortletid()+"','"+slot.getClassname()+"',"+slot.getSharelevel()+");");
		}else{
			AppLifeCycleContext.current().getApplicationContext().addExecScript("parent.document.location.reload();");
		}
	}
}
