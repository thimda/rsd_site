package nc.uap.portal.integrate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;


 

/**
 * 单点集成接口
 * @author gd 
 * @version NC5.6
 * @since NC5.0
 */
public interface IWebAppLoginService {
	
	/**
	 * 获取最后进入web系统的实际URL
	 * @param req 
	 * @param res 
	 * @param credential 进入系统的凭证信息
	 * @param providerVO 应用对应的provider,如果没有为null
	 * @return 进入web application的实际URL
	 * @throws CredentialValidateException 
	 */
	public String getGateUrl(HttpServletRequest req, HttpServletResponse res, PtCredentialVO credential, SSOProviderVO providerVO) throws CredentialValidateException;
	
	/**
	 * 进入第三方集成系统的凭证制作
	 * @param req
	 * @param providerVO 应用对应的provider,如果没有为null
	 * @return 生成的凭证
	 * @throws CredentialValidateException
	 */
	public PtCredentialVO credentialProcess(HttpServletRequest req, SSOProviderVO providerVO) throws CredentialValidateException;
	
	/**
	 * 返回所有第三方系统需要验证的字段定义
	 * @param req 当前请求对象,可以从中获得用户之前输入的信息作为默认值
	 * @param providerVO 应用对应的provider,如果没有为null
	 * @param userVO 当前登录用户的UserVO
	 * @param credential 进入系统的凭证
	 * @param isLoginProvider 本系统使用的provider是否是系统登录的provider
	 * @return
	 * @throws CredentialValidateException
	 */
	public ExtAuthField[] getCredentialFields(HttpServletRequest req, SSOProviderVO providerVO, IUserVO userVO, PtCredentialVO credential) throws CredentialValidateException;
	
	/**
	 * 用户登录信息校验,如果用户登录信息不合法以异常抛出.
	 * 凭证制作,获取登录URL前均需要验证用户的单点登录信息
	 * @param credentialVO
	 * @param providerVO 应用对应的provider,如果没有为null
	 * @throws CredentialValidateException
	 */
	public String verifyUserInfo(HttpServletRequest req, PtCredentialVO credentialVO, SSOProviderVO providerVO) throws CredentialValidateException;
	
	/**
	 * 获取集成系统指定节点的登陆URL
	 * @param req
	 * @param res
	 * @return
	 */
	public String getNodeGateUrl(HttpServletRequest req, HttpServletResponse res, String nodeId, PtCredentialVO credential, SSOProviderVO providerVO) throws CredentialValidateException;
}
