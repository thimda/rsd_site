package nc.uap.portal.integrate.ldap.plugins.impl;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import nc.bs.logging.Logger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.ldap.plugins.itf.ILdapUserVerify;

/**
 * LDAP用户验证实现
 * @author licza
 *
 */
public class LdapUserVerifyImpl implements ILdapUserVerify{
	public boolean verifyPasswd(String userdn, String password, Map<String, Object> param){
		try {
			getLdapContext(userdn, password);
			return true;
		} 
		catch (PortalServiceException e) {
			return false;
		}
	}
	
	private DirContext getLdapContext(String userdn, String password) throws PortalServiceException{
		String root = "DC=airchinaf,DC=com" ; //root 
		Hashtable<String, String> env = new Hashtable<String, String>(); 
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory" ); 
		env.put(Context.PROVIDER_URL, "ldap://192.168.19.11:389/" + root); 
		env.put(Context.SECURITY_AUTHENTICATION, "simple" ); 
		env.put(Context.SECURITY_PRINCIPAL, userdn); 
		env.put(Context.SECURITY_CREDENTIALS, password); 
		try {
			return new InitialDirContext(env);
		} catch (NamingException e) {
			Logger.error(e.getMessage(), e);
			throw new PortalServiceException(e.getMessage());
		}
	}
}
