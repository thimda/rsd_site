package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;

public class PtPortletPreferencesVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5127232640657595178L;
	private String pk_portletpreferences;
	private String pk_portlet;
	private String fk_portaluser;
	private String fk_portalpage;
	private String preferences;
	private String module;
	private String portletid;

	public String getPk_portletpreferences() {
		return pk_portletpreferences;
	}

	public void setPk_portletpreferences(String pk_portletpreferences) {
		this.pk_portletpreferences = pk_portletpreferences;
	}

	public String getPk_portlet() {
		return pk_portlet;
	}

	public void setPk_portlet(String pk_portlet) {
		this.pk_portlet = pk_portlet;
	}

	public String getFk_portaluser() {
		return fk_portaluser;
	}

	public void setFk_portaluser(String fk_portaluser) {
		this.fk_portaluser = fk_portaluser;
	}

	public String getFk_portalpage() {
		return fk_portalpage;
	}

	public void setFk_portalpage(String fk_portalpage) {
		this.fk_portalpage = fk_portalpage;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getPortletid() {
		return portletid;
	}

	public void setPortletid(String portletid) {
		this.portletid = portletid;
	}

	public String getTableName() {
		return "pt_portletpreferences";
	}

	public String getPrimaryKey() {
		return "pk_portletpreferences";
	}

}
