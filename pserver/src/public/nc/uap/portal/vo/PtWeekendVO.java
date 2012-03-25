package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;

/**
 * ÖÜÄ©
 * 
 * @author licza
 * 
 */
public class PtWeekendVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -783323362541120101L;

	private String pk_weekend;
	private String weekendday;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public String getPk_weekend() {
		return pk_weekend;
	}

	public void setPk_weekend(String pk_weekend) {
		this.pk_weekend = pk_weekend;
	}

	public String getWeekendday() {
		return weekendday;
	}

	public void setWeekendday(String weekendday) {
		this.weekendday = weekendday;
	}

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_weekend";
	}

	@Override
	public String getTableName() {
		return "pt_weekend";
	}

}
