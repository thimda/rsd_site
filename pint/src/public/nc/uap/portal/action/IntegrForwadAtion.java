package nc.uap.portal.action;

import java.io.IOException;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.inte.PintServiceFactory;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.exception.CredentialValidateException;
import nc.uap.portal.integrate.system.ProviderFetcher;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.PtUtil;

/**
 * 集成的功能节点转发类
 * 
 * @author licza
 * 
 */
@Servlet(path = "/integr")
public class IntegrForwadAtion extends BaseAction {
	public void goSSOGate(){
		print("<h1>错误! </h1> <b>信息</b>:没有查找到关联的用户信息!" );
	};
	
	@Action(url="/nodes/forward")
	public void forward(@Param(name = "node") String node ,@Param(name = "systemCode") String systemCode) throws IOException {
		if(PtUtil.isNull(node))
			throw new IllegalArgumentException("节点为空!");
		if(PtUtil.isNull(systemCode))
			throw new IllegalArgumentException("系统编码为空!");
		IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		String userId = userVO.getUserid();
		PtCredentialVO credential = null;
		try {
			credential = PintServiceFactory.getSsoQryService().getCredentials(userId, this.getClass().getName(), systemCode, Integer.valueOf(1));
		} catch (PortalServiceException e) {
			goSSOGate();
			return;
		}
		if(credential == null){
			goSSOGate();
			return;
		}
		SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
		try {
			String location = ProviderFetcher.getInstance().getAuthService(systemCode).getNodeGateUrl(request, response, node, credential, provider);
			response.sendRedirect(location);
			return;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			print("<h1>ERROR!</h1> message : " + e.getMessage());
		}
	}
	@Action(url="/nodes/redirect")
	public void redirect(@Param(name = "returnurl") String node ,@Param(name = "systemCode") String systemCode){
		if(PtUtil.isNull(systemCode))
			throw new IllegalArgumentException("系统编码为空!");
		
		IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		String userId = userVO.getUserid();
		PtCredentialVO credential = null;
		try {
			credential = PintServiceFactory.getSsoQryService().getCredentials(userId, this.getClass().getName(), systemCode, Integer.valueOf(1));
		} catch (PortalServiceException e) {
			goSSOGate();
			return;
		}
		if(credential == null){
			goSSOGate();
			return;
		}
		SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
		try {
			String location = ProviderFetcher.getInstance().getAuthService(systemCode).getNodeGateUrl(request, response, node, credential, provider);
			response.sendRedirect(location);
			return;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			print("<h1>ERROR!</h1> message : " + e.getMessage());
		}
	}
	/**
	 * 集成系统跳转
	 * @param systemCode
	 * @throws IOException 
	 */
	@Action
	public void system(@Param(name="syscode") String systemCode) throws IOException{
		if(PtUtil.isNull(systemCode))
			throw new IllegalArgumentException("系统编码为空!");
		IUserVO userVO = ((PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean()).getUser();
		String userId = userVO.getUserid();
		
		PtCredentialVO credential = null;
		try {
			credential = PintServiceFactory.getSsoQryService().getCredentials(userId, this.getClass().getName(), systemCode, Integer.valueOf(1));
		} catch (PortalServiceException e) {
			goSSOGate();
			return;
		}
		if(credential == null){
			goSSOGate();
			return;
		}
		SSOProviderVO provider = ProviderFetcher.getInstance().getProvider(systemCode);
		try {
			String location = ProviderFetcher.getInstance().getAuthService(systemCode).getGateUrl(request, response, credential, provider);
			response.sendRedirect(location +"&redirect=N");
			return;
		} catch (CredentialValidateException e) {
			goSSOGate();
			return;
		} catch(Exception e){
			LfwLogger.error(e.getMessage(),e);
			print("<h1>ERROR!</h1> message : " + e.getMessage());
			print(e.getMessage());
		}

	}
}
