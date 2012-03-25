/**
 * 
 */
package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;


/**
 * 凭证库槽VO
 * 
 * @author yzy Created on 2006-2-23
 * @version NC5.6
 */
public class PtSlotVO extends  SuperVO {

	static final long serialVersionUID = 3231940865220987860L;
	private String pk_slot;
	private String userid;
	private String portletid;
	private Integer sharelevel;
	private Integer active;
	private String classname;
	// 所属集团(since NC5.6)
	private String pk_group;
	
	/**
	 * @return Returns the active.
	 */
	public Integer getActive() {
		return active;
	}
	
	/**
	 * @param active The active to set.
	 */
	public void setActive(Integer active) {
		this.active = active;
	}
	
	/**
	 * @return Returns the classname.
	 */
	public String getClassname() {
		return classname;
	}
	
	/**
	 * @param classname The classname to set.
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	
	/**
	 * @return Returns the userid.
	 */
	public String getUserid() {
		return userid;
	}

	
	/**
	 * @param userid The userid to set.
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return Returns the pk_slot.
	 */
	public String getPk_slot() {
		return pk_slot;
	}
	
	/**
	 * @param pk_slot The pk_slot to set.
	 */
	public void setPk_slot(String pk_slot) {
		this.pk_slot = pk_slot;
	}
	
	/**
	 * @return Returns the portletid.
	 */
	public String getPortletid() {
		return portletid;
	}
	
	/**
	 * @param portletid The portletid to set.
	 */
	public void setPortletid(String portletid) {
		this.portletid = portletid;
	}
	
	/**
	 * @return Returns the sharelevel.
	 */
	public Integer getSharelevel() {
		return sharelevel;
	}
	
	/**
	 * @param sharelevel The sharelevel to set.
	 */
	public void setSharelevel(Integer sharelevel) {
		this.sharelevel = sharelevel;
	}	
	
	public String getTableName() {
		return "pt_slot";
	}
	
	public String getPrimaryKey() {
		return pk_slot;
	}

	@Override
	public String getPKFieldName() {
		return "pk_slot";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
}
