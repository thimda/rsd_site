package nc.uap.portal.integrate.credential;

import java.util.Enumeration;
import java.util.Map;


/**
 * 凭证Reference接口，SSO的凭证除了用户名和密码对这个必须条件之外，还有
 * 	一些别的信息，如NC的帐套、公司等。凭证Reference是这些信息的接口
 * @author yzy
 *	Created on 2006-02-26
 */
public interface ICredentialReference {
	/**
	 * 设置元素对
	 * @param key 
	 * @param value
	 */
	public void setValue(String key,String value);
	
	/**
	 * 设置元素对（单KEY可多Value）
	 * @param key
	 * @param values
	 */
	public void setValues(String key,String[] values);
	
	/**
	 * 通过KEY获得VALUE（如果多个VALUE，返回第一个）
	 * @param key
	 * @return
	 */
	public String getValue(String key);
	
	/**
	 * 通过KEY获得VALUES
	 * @param key
	 * @return
	 */
	public String[] getValues(String key);
	
	/**
	 * 获得KEY的Enumeration
	 * @return
	 */
	public Enumeration getNames();
	
	/**
	 * 获得元素对的MAP
	 * @return
	 */
	public Map<String, String[]> getMap();
	
	/**
	 * 从存储中重新获取Reference
	 * @param key
	 */
	public void reset(String key);
	
	/**
	 * 更新Reference到存储中去
	 *
	 */
	public void store();
}
