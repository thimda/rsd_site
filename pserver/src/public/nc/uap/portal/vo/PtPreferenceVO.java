package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * Portlet配置对象
 * 
 * @author licza
 * 
 */
public class PtPreferenceVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4319959229613318367L;
	/** 主键 **/
	private String pk_preference;
	/** portlet名称 **/
	private String portletname;
	/** 页面名称 **/
	private String pagename;
	/** 集团编码 **/
	private String pk_group;
	/*** 用户编码 */
	private String pk_user;
	/** 配置项 **/
	private byte[] preferences;

	public String getPk_preference() {
		return pk_preference;
	}

	public void setPk_preference(String pk_preference) {
		this.pk_preference = pk_preference;
	}

	public String getPortletname() {
		return portletname;
	}

	public void setPortletname(String portletname) {
		this.portletname = portletname;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getPk_user() {
		return pk_user;
	}

	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

	public String doGetPreferences() {
		return new String(preferences);
	}

	public void doSetPreferences(String preferences) {
		this.preferences = preferences.getBytes();
	}

	@Override
	public String getPKFieldName() {
		return "pk_preference";
	}

	@Override
	public String getTableName() {
		return "pt_preference";
	}

	public byte[] getPreferences() {
		return preferences;
	}

	public void setPreferences(byte[] preferences) {
		this.preferences = preferences;
	}
	/** ts */
	private UFDateTime ts; // char(20)
	/** dr */
	private UFBoolean dr  ;

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public UFBoolean getDr() {
		return dr;
	}

	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}
	
}
