package nc.uap.portal.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.cil.ICilService;
import nc.login.bs.IServerEnvironmentService;
import nc.login.vo.FuncNodeRegInfo;
import nc.login.vo.ISystemIDConstants;
import nc.login.vo.NCSession;
import nc.uap.cpb.log.LoginLogHelper;
import nc.uap.cpb.log.loginlog.LoginLogVO;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.impl.AbstractSsoServiceImpl;
import nc.uap.lfw.login.itf.ILfwSsoService;
import nc.uap.lfw.login.itf.ILoginHandler;
import nc.uap.lfw.login.itf.LoginHelper;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.util.LfwSsoUtil;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalPageQryService;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.user.entity.IUserVO;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/**
 * Portal单点登陆注册
 * @author licza
 *
 */
public class PortalSSOServiceImpl extends AbstractSsoServiceImpl<PtSessionBean> {
	
	private static final String USERID = "ui";
	private static final String GROUPNO = "gn";

	
	@Override
	public void addSsoSign(PtSessionBean sesbean, String sysid) {
			HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
			HttpSession session = request.getSession();
			session.setAttribute(SessionConstant.LOGIN_SESSION_BEAN, sesbean);
			LfwRuntimeEnvironment.setLfwSessionBean(sesbean);
			LfwRuntimeEnvironment.setDatasource(sesbean.getDatasource());
			String sesid = session.getId();
			// 创建一个token
			LfwTokenVO token = new LfwTokenVO();
			token.setDatasource(sesbean.getDatasource());
			token.setPkcrop(sesbean.getPk_unit());
			token.setUsername(sesbean.getUser_name());
			token.setSessionid(sesid);
			token.setUserpk(sesbean.getPk_user());
			token.setThemeid(sesbean.getThemeId());
			token.setUsertype(String.valueOf(sesbean.getUserType()));
			token.setPasswd(sesbean.getUser().getPassword());
			token.setUserid(sesbean.getUser_code());
			token.setLoginip(request.getRemoteAddr());
			token.setSessionid(sesid);
			token.setSystemid(sysid);
			token.setExt1(LfwRuntimeEnvironment.getRootPath());
//			token.setAccountcode(sesbean.getAccount());
			token.setLogindate(new UFDateTime(System.currentTimeMillis()));
			String uuid = UUID.randomUUID().toString();
			token.setTokenid(uuid);
			ILfwSsoService ssoService = NCLocator.getInstance().lookup(ILfwSsoService.class);
			try {
				ssoService.createToken(token);
				// Token编号设置到session bean中
				//设置根Token编号
				session.setAttribute(SessionConstant.ROOT_TOKEN_ID,LfwSsoUtil.getTokenID());
				sesbean.setSsotoken(uuid);
			} catch (LfwBusinessException e) {
				LfwLogger.error("单点登陆信息注册失败", e);
			}
	}

	@Override
	protected Map<String, String> getInfoMap(PtSessionBean ssoInfo) {
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put(USERID, ssoInfo.getPk_user());
		infoMap.put(GROUPNO, ssoInfo.getGroupNo());
		return infoMap;
	}

	@Override
	protected PtSessionBean restoreByInfoMap(Map<String, String> info) {
		try {
			String userid = info.get(USERID);
			String groupno = info.get(GROUPNO);
			CpUserVO userVO = CpbServiceFacility.getCpUserQry().getGlobalUserByCode(userid);
			if(userVO == null)
				return null;
			CpUser user = new CpUser(userVO);
			PtSessionBean ses = new PtSessionBean();
			ses.setUser(user);
			ses.setGroupNo(groupno);
			return ses;
		} catch (CpbBusinessException e) {
			throw new PortalServerRuntimeException("恢复用户信息时出现错误");
		}
	}

	public void afterLogin(LfwSessionBean sbean)   {
		PtSessionBean ptBean = (PtSessionBean) sbean;
		IUserVO userVO = (IUserVO)ptBean.getUser();
		try {
			/**
			 * License检查
			 */
			//checkLicense(userVO.getPk_user(), userVO.getPassword());
			/**
			 * 初始化用户信息
			 */
			initUser(ptBean);
			UFBoolean loginResult = UFBoolean.TRUE;
			doLoginLog(sbean, loginResult);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	/**
	 * 记录登录日志
	 * @param sbean
	 * @param loginResult
	 * @throws LfwBusinessException
	 */
	public void doLoginLog(LfwSessionBean sbean, UFBoolean loginResult)
			throws LfwBusinessException {
		HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		LoginLogVO vo = new LoginLogVO();
		vo.setLogintime(new UFDateTime());
		vo.setClientip(request.getRemoteAddr());
		vo.setLogingrop(sbean.getPk_unit());
		vo.setUsername(sbean.getUser_code());
		vo.setUsername(sbean.getUser_name());
		vo.setLoginresult(loginResult);
		vo.setPk_user(sbean.getPk_user());
		vo.setSessionid(request.getSession().getId());
//			vo.setLogingropcode(newLogingropcode)
//		LoginLogHelper.login(vo);
	}
	/**
	 * 初始化用户信息
	 * @param sbean
	 * @throws PortalServiceException
	 */
	public void initUser(LfwSessionBean sbean) throws PortalServiceException {
		String groupId = sbean.getPk_unit();
		String userId = sbean.getPk_user();
		Integer userType = ((PtSessionBean)sbean).getUserType();
		IPtPortalPageQryService ppq = NCLocator.getInstance().lookup(IPtPortalPageQryService.class);
		PtPageVO[] currPages = new PtPageVO[0];
		if (userType.equals(IUserVO.USERTYPE_GROUP) ) {
			currPages = ppq.getGroupsPage(groupId);
			/**
			 *  过滤掉非用户类型Page
			 */
			currPages = PortalPageDataWrap.filterPagesByUserType(currPages, userType);
		} else {
			currPages = PortalServiceUtil.getPageQryService().getPageByLevel(userType) ;
		}
		PtPageVO[] userPages = ppq.getUserPages(userId);
		/**
		 *  对比是否有新增加
		 */
		PtPageVO[] newPages = PortalPageDataWrap.checkNews(userPages, currPages);
		/**
		 *  生成缓存对象
		 */
		List<Page> userPageList = PortalPageDataWrap.praseUserPages(userPages);
		/**
		 *  有页面
		 */
		if (newPages != null && newPages.length > 0) {
			/**
			 *  追加到缓存对象
			 */
			userPageList.addAll(PortalPageDataWrap.praseUserPages(newPages));
			/**
			 * 追加到数据库中
			 */
			IPtPortalPageService pageService = NCLocator.getInstance().lookup(IPtPortalPageService.class);
			pageService.addUserNewPages(userId, groupId, newPages);
		}
		/**
		 * 转换为用户缓存 
		 */
		Map<String, Page> userPagesCache = PortalPageDataWrap.praseUserPages(userPageList.toArray(new Page[0]));
		PortalCacheManager.getUserPageCache().clear();
		PortalCacheManager.getUserPageCache().putAll(userPagesCache);
	}
	/**
	 * 检测Portal授权数目
	 * 
	 * @param request
	 * @throws BusinessException
	 */
	private void checkLicense(String pkUser, String password) throws BusinessException {
		
		ServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String dsName = LfwRuntimeEnvironment.getDatasource();
		IServerEnvironmentService ses = NCLocator.getInstance().lookup(IServerEnvironmentService.class);
		ICilService cliService = NCLocator.getInstance().lookup(ICilService.class);
		String ownModule = cliService.getProductCode(PortalEnv.PORTALMODULECODE);
		boolean canOpen = ses.canOpenNode(PortalEnv.PORTALMODULECODE, ownModule, ISystemIDConstants.NCPORTAL, dsName, pkUser);
		if(!canOpen)
			throw new BusinessException("无法打开此产品");
		HttpServletRequest req = (HttpServletRequest)request;
		
		FuncNodeRegInfo nodeInfo = new FuncNodeRegInfo();
		nodeInfo.setCode(ISystemIDConstants.NCPORTAL);
		nodeInfo.setName("UAP-PORTAL");
		nodeInfo.setOwnModule(ownModule);
		
		ses.registerOpenedNode(ISystemIDConstants.NCPORTAL, dsName, req.getSession().getId(), nodeInfo);
		NCSession session = new NCSession();
		session.setDsName(dsName);
		session.setSessionID(req.getSession().getId());
		session.setUserCode(pkUser);
		session.setUserID(pkUser);
		ses.registerUserSession(ISystemIDConstants.NCPORTAL, session, true);
		
		 
		return;
	}
 
	@Override
	public PtSessionBean restoreSsoSign(String sysid) {
		try {
			if(LfwRuntimeEnvironment.getWebContext() != null){
				String token=	LfwRuntimeEnvironment.getWebContext().getParameter("token");
				if(token==null||token.equals(""))
					return null;
				getLoginHelper().processLogin();
			}
		} catch (LoginInterruptedException e) {
			LfwLogger.error("登陆过程出现异常", e);
			throw new LfwRuntimeException("登陆过程出现异常");
		}
		return (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
	}
	private LoginHelper<PtSessionBean> getLoginHelper() {
		LoginHelper<PtSessionBean> helper = new LoginHelper<PtSessionBean>() {
			@Override
			public ILoginHandler<PtSessionBean> createLoginHandler() {
				PortalSSOLoginHandler handler = new PortalSSOLoginHandler();
				return handler;
			}
		};
		return helper;
	}
}
