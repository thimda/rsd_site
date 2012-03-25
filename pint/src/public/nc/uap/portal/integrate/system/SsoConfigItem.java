package nc.uap.portal.integrate.system;

import java.io.Serializable;

import nc.vo.pub.SuperVO;

/**
 * ssoø…≈‰÷√œÓvo
 * @author gd 2009-01-03
 * @version NC5.5
 * @since NC5.5
 */
public class SsoConfigItem  extends SuperVO implements Serializable {
	private static final long serialVersionUID = 442517515658992228L;
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private String value;  
}
