package nc.uap.portal.vo;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBException;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.integrate.system.SSOReference;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * SSO配置VO
 * 
 * @author licza
 * 
 */
public class PtSsopropVO extends SuperVO {

	private static final long serialVersionUID = -8315434627599144409L;
	
	private String pk_ssoprop;
	
	/**
	 * 系统编码
	 */
	private String systemcode;
	
	/**
	 * 系统名称
	 */
	private String systemname;
	
	/**
	 * 是否启用南北网mapping功能 默认不启用,启用后才会进行IP映射替换
	 */
	private UFBoolean enablemapping;

	/**
	 * 验证类
	 */
	private String authclass;

	/**
	 * 集成功能节点类
	 */
	private String nodesclass;

	/**
	 * 验证网关
	 */
	private String gateurl;

	/**
	 * 扩展项
	 */
	private String reference;
	
	private SSOReference ref;
 
	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode;
	}

	public String getSystemname() {
		return systemname;
	}

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}

	public UFBoolean getEnablemapping() {
		return enablemapping;
	}

	public void setEnablemapping(UFBoolean enablemapping) {
		this.enablemapping = enablemapping;
	}

	public String getAuthclass() {
		return authclass;
	}

	public void setAuthclass(String authclass) {
		this.authclass = authclass;
	}

	public String getNodesclass() {
		return nodesclass;
	}

	public void setNodesclass(String nodesclass) {
		this.nodesclass = nodesclass;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getGateurl() {
		return gateurl;
	}

	public void setGateurl(String gateurl) {
		this.gateurl = gateurl;
	}

	@Override
	public String getPKFieldName() {
		return "pk_ssoprop";
	}

	@Override
	public String getTableName() {
		return "pt_ssoprop";
	}

	public SSOReference getRef() {
		if(ref == null){
			ref = (SSOReference) JaxbMarshalFactory.newIns().encodeXML(SSOReference.class, getReference());
		}
		return ref;
	}

	public void setRef(SSOReference ref) {
		this.ref = ref;
	}

	public String getPk_ssoprop() {
		return pk_ssoprop;
	}

	public void setPk_ssoprop(String pk_ssoprop) {
		this.pk_ssoprop = pk_ssoprop;
	}
	
}
