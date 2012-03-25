package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class PtIndexConfigVO extends SuperVO {
	
	private String pk_indexconfig;
	private String hostip;
	private String port;
	private String servername;
	private UFDateTime ts;
	private UFBoolean dr;
	
	public String getPk_indexconfig() {
		return pk_indexconfig;
	}
	public void setPk_indexconfig(String pk_indexconfig) {
		this.pk_indexconfig = pk_indexconfig;
	}
	public String getHostip() {
		return hostip;
	}
	public void setHostip(String hostip) {
		this.hostip = hostip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
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
	
	public String getPKFieldName() {
		return "pk_indexconfig";
	}
	public String getTableName() {
		return "pt_indexconfig";
	}

}
