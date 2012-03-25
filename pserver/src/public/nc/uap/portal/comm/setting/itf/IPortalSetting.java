package nc.uap.portal.comm.setting.itf;

import nc.uap.portal.comm.setting.PtSettingVO;

/**
 * Portal设置接口
 * 
 * @author licza
 * 
 */
public interface IPortalSetting {
	public static final String PID = "PortalSetting";

	/**
	 * 获得设置项
	 * 
	 * @return
	 */
	PtSettingVO[] getSettings();

	 
}
