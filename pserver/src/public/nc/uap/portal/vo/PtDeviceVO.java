package nc.uap.portal.vo;
import nc.vo.pub.SuperVO;
public class PtDeviceVO extends SuperVO {
	private static final long serialVersionUID = 8204242105119703375L;
	private String pk_device;
	private String code;
	private String name;
	private String memo;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	public String getPk_device() {
		return pk_device;
	}
	public void setPk_device(String pk_device) {
		this.pk_device = pk_device;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getPKFieldName() {
		return "pk_device";
	}
	public String getTableName() {
		return "pt_device";
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
