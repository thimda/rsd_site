package nc.uap.portal.integrate;

import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.funnode.SsoSystemNode;
import nc.uap.portal.integrate.system.SSOProviderVO;

/**
 * Portal集成功能节点接口
 * 
 * @author licza
 * 
 */
public interface IWebAppFunNodesService {
	/**
	 * 获得用户功能节点
	 * 
	 * @param credential
	 * @param provider
	 * @return
	 */
	public SsoSystemNode[] getUserNodes(SSOProviderVO provider,PtCredentialVO credential) throws CredentialValidateException;

	/**
	 * 获得所有节点
	 * 
	 * @param provider
	 * @return
	 * @throws Exception
	 */
	public SsoSystemNode[] getAllFunNodes(SSOProviderVO provider);
}
