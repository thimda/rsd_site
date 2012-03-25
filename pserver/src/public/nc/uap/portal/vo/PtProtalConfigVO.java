package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * Protal≈‰÷√vo
 * 2010-12-1 …œŒÁ09:18:35  limingf
 */

public class PtProtalConfigVO extends SuperVO{
	private static final long serialVersionUID = -4850124988256630534L;
	
	private String pk_portalconfig;
	private String config_key;
	private String config_value;
	private String name;
	private String description;
	private String config_type;
	private Integer dr;
	private UFDateTime ts;
	
	@Override
	public String getPKFieldName() {
		return "pk_portalconfig";
	}
	@Override
	public String getTableName() {
		return "pt_portalconfig";
	}
	
	public String getPk_portalconfig() {
		return pk_portalconfig;
	}
	public void setPk_portalconfig(String pk_portalconfig) {
		this.pk_portalconfig = pk_portalconfig;
	}
	

	public String getConfig_key() {
		return config_key;
	}
	public void setConfig_key(String config_key) {
		this.config_key = config_key;
	}
	public String getConfig_value() {
		return config_value;
	}
	public void setConfig_value(String config_value) {
		this.config_value = config_value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getConfig_type() {
		return config_type;
	}
	public void setConfig_type(String config_type) {
		this.config_type = config_type;
	}
	
}
