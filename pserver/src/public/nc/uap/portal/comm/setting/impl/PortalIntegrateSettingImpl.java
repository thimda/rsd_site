package nc.uap.portal.comm.setting.impl;

import nc.uap.portal.comm.setting.PtSettingVO;
import nc.uap.portal.comm.setting.itf.IPortalSetting;

/**
 * Portal集成设置
 * 
 * @author licza
 * 
 */
public class PortalIntegrateSettingImpl implements IPortalSetting {


	@Override
	public PtSettingVO[] getSettings() {
		return new PtSettingVO[] { new PtSettingVO("integrateSetting", "集成凭据管理",
				"integrateSetting", "/portal/pt/integr/setting/home") };
	}

}
