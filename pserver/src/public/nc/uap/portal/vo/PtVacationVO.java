package nc.uap.portal.vo;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
/**
 * 休息日VO
 * @author licza
 *
 */
public class PtVacationVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2716906040079620538L;
	private String pk_vacation;
	private UFDateTime startday;
	private String remark;
	private UFDateTime endday;
	private String title;
	private Integer type;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	/**
	 * 周末
	 */
	public static final String WEEKEND = "nc.portal.weekend";
	/**
	 * 特殊工作日
	 */
	public static final String SPECIALWORKDAY = "nc.portal.SpecialWorkDay";
	/**
	 * 假期
	 */
	public static final String HOLIDAYS = "nc.portal.Holidays";
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
	public String getPk_vacation(){
		return pk_vacation;
	}
	public void setPk_vacation(String pk_vacation) {
		this.pk_vacation=pk_vacation;
	}
 	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark) {
		this.remark=remark;
	}
 	public String getTitle(){
		return title;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public String getPrimaryKey(){
		return pk_vacation;
	}
	public String getTableName() {
		return "pt_vacation";
	}
	@Override
	public String getPKFieldName() {
		return "pk_vacation";
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public UFDateTime getStartday() {
		return startday;
	}
	public void setStartday(UFDateTime startday) {
		this.startday = startday;
	}
	public UFDateTime getEndday() {
		return endday;
	}
	public void setEndday(UFDateTime endday) {
		this.endday = endday;
	}
	
}