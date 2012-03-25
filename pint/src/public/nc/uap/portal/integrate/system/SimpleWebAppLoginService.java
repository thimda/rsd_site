package nc.uap.portal.integrate.system;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.portal.integrate.IWebAppLoginService;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.user.entity.IUserVO;

/**
 * 简单实现，方便无凭证登录系统使用 
 * @author yzy Created on 2006-8-29
 *
 */
public abstract class SimpleWebAppLoginService implements IWebAppLoginService {
	
	public PtCredentialVO credentialProcess(HttpServletRequest req,
			SSOProviderVO providerVO)
			throws CredentialValidateException {
		return null;
	}

	public ExtAuthField[] getCredentialFields(HttpServletRequest req, SSOProviderVO providerVO,
			IUserVO userVO, PtCredentialVO credential)
			throws CredentialValidateException {
		return null;
	}
}
