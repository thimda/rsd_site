package nc.uap.portal.integrate.message;

import java.io.Serializable;

/**
 * 消息查询条件
 * 
 * @author licza
 * 
 */
public class MessageQueryParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5864704596778904702L;
	String category;
	String parentid;
	int timeSection = 1;;
	String[] states = new String[]{"0","1"};
	String pk_user;
	String pluginid;
	public String getPluginid() {
		return pluginid;
	}
	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public int getTimeSection() {
		return timeSection;
	}
	public void setTimeSection(int timeSection) {
		this.timeSection = timeSection;
	}
	public String[] getStates() {
		return states;
	}
	public void setStates(String[] states) {
		this.states = states;
	}
	public String getPk_user() {
		return pk_user;
	}
	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}
	public MessageQueryParam(){
		
	}
	public MessageQueryParam(String category, String parentid, int timeSection,
			String[] states, String pk_user, String pluginid) {
		super();
		this.category = category;
		this.parentid = parentid;
		this.timeSection = timeSection;
		this.states = states;
		this.pk_user = pk_user;
		this.pluginid = pluginid;
	}
	/**
	 * 是否验证通过
	 * @return
	 */
	public boolean isValdate(){
		return (category != null && pluginid != null);
	}
}
