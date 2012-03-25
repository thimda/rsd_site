package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;

/**
 * Portlet应用定义
 * 
 * @author licza
 * 
 */
public class PtModuleAppVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1487323017677670789L;

	private String pk_moduleapp;
	/** Portal定义 **/
	private String appsdef;
	/** 默认命名空间 **/
	private String defaultns;
	/** 模块 **/
	private String module;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	@Override
	public String getPKFieldName() {
		return "pk_moduleapp";
	}

	@Override
	public String getTableName() {
		return "pt_moduleapp";
	}

	public String getPk_moduleapp() {
		return pk_moduleapp;
	}

	public void setPk_moduleapp(String pk_moduleapp) {
		this.pk_moduleapp = pk_moduleapp;
	}

	public String getAppsdef() {
		return appsdef;
	}

	public void setAppsdef(String appsdef) {
		this.appsdef = appsdef;
	}

	public String getDefaultns() {
		return defaultns;
	}

	public void setDefaultns(String defaultns) {
		this.defaultns = defaultns;
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
