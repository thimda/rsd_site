package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 匿名用户设置VO
 * 
 * @author licza
 * 
 */
public class PtAnnoyVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2681790948736436347L;
	/** 主键 **/
	private String pk_annoy;
	/** 是否启用 **/
	private UFBoolean isactive;
	/** 匿名集团编码 **/
	private String pk_group;
	/** 匿名集团名称 **/
	private String groupname;

	public String getPk_annoy() {
		return pk_annoy;
	}

	public void setPk_annoy(String pk_annoy) {
		this.pk_annoy = pk_annoy;
	}

	public UFBoolean getIsactive() {
		return isactive;
	}

	public void setIsactive(UFBoolean isactive) {
		this.isactive = isactive;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Override
	public String getPKFieldName() {
		return "pk_annoy";
	}

	@Override
	public String getTableName() {
		return "pt_annoy";
	}

}
