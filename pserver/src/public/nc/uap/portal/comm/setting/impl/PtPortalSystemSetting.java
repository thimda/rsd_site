package nc.uap.portal.comm.setting.impl;

import nc.uap.portal.comm.setting.PtSettingVO;
import nc.uap.portal.comm.setting.itf.IPortalSetting;
/**
 * Portal设置项
 * @author licza
 *
 */
public class PtPortalSystemSetting implements IPortalSetting {

	@Override
	public PtSettingVO[] getSettings() {
		return new  PtSettingVO[]{
				new PtSettingVO("changePassWd","修改密码","changePassWd","/portal/pages/changePasswd.jsp"),
				new PtSettingVO("langSetting","语言设置","langSetting","/portal/pages/langSetting.jsp"),
				new PtSettingVO("themeSetting","主题设置","themeSetting","/portal/pt/setting/themeList"),
				new PtSettingVO("templateSetting","皮肤设置","templateSetting","/portal/pt/setting/templateList"),
				new PtSettingVO("addPortlet","增加Portlet","addPortlet","/portal/pages/addPortlet.jsp")

				
		};
	}

}
