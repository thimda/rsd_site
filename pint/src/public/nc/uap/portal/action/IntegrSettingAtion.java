package nc.uap.portal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.exception.UserAccessException;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portal.vo.PtSlotVO;

import org.apache.commons.lang.StringUtils;

@Servlet(path = "/integr/setting")
public class IntegrSettingAtion extends BaseAction {
	@SuppressWarnings("unchecked")
	@Action
	public void home() {
		Map root = new HashMap();
		String userId = LfwRuntimeEnvironment.getLfwSessionBean()
				.getUser_code();
		Map<String, PtSlotVO> slotmap = new HashMap<String, PtSlotVO>();
		try {
			List<SSOProviderVO> providers = PintServiceFactory
					.getSsoConfigQryService().getAllConfig();
			if (providers == null) {
				print("系统没有集成配置.");
				return;
			}
			root.put("providers", providers);

			PtSlotVO[] slots = PintServiceFactory.getSsoQryService().getSlots(
					userId, null, null, null);
			if (slots != null)
				for (PtSlotVO slot : slots) {
					slotmap.put(slot.getClassname(), slot);
				}
			root.put("slots", slotmap);
			String ftlName = "nc/portal/ftl/integrSetting.ftl";
			try {
				print(FreeMarkerTools.contextTemplatRender(ftlName, root));
			} catch (PortalServiceException e) {
				LfwLogger.error(e.getMessage());
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	@Action
	public void renew(@Param(name = "id") String id) {
		LfwSessionBean  ses = LfwRuntimeEnvironment.getLfwSessionBean();
		try {
			if(ses == null) 
				throw new UserAccessException("会话超时,请重新登陆!");
 
			PtSlotVO slot = PintServiceFactory.getSsoQryService().getSlotByPK(id);
			if(slot == null)
				return;
			if(StringUtils.equals(ses.getUser_code(), slot.getUserid())){
				PintServiceFactory.getSsoService().removeSlot(id);
				addExecScript("alert('操作成功!');parent.document.location.reload();");
				return;
			}else
				throw new UserAccessException("操作异常,请重新登陆再试!");

		} catch (Exception e) {
			addExecScript("alert('"+e.getMessage()+"');");
		}
	}

}
