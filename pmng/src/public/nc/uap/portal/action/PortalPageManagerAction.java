package nc.uap.portal.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PmlUtil;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtPageVO;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

/**
 * 页面设计器Action
 * @author licza
 *
 */
@Servlet(path="/portalpage")
public class PortalPageManagerAction extends BaseAction{
	
	/**
	 * 保存某集团的PML
	 * @param pk_group 集团编码
	 * @param pml 页面描述
	 */
	@Action(method="POST")
	public void doNew(@Param(name="groupid")String pk_group,@Param(name="pml")String pml){
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		if(pml == null || pk_group == null || ses == null)
			return;
		/**
		 * 管理员是否有权限
		 */
//		if(!SecurityHelper.hasPower(ses.getPk_user(), pk_group))
//			return;
		
		try {
			/**
			 * PML转换为页面描述模型
			 */
			Page page = PmlUtil.parser(URLDecoder.decode(pml,"UTF-8"));
			String pagename = page.getPagename();
			if(page == null || PtUtil.isNull(pagename))
				return;
			/**
			 * 检查是否存在此页面
			 */
			boolean isPageExist = false;//PortalServiceUtil.getPageQryService().isPortalPageExist(PortalEnv.getPortalCoreName(), pagename);
			
			if(isPageExist)
				return;
			/**
			 * 保存
			 */
			PortalServiceUtil.getPageService().add(pml2vo(page, pk_group));
			
			/**
			 * 增加资源
			 */
			/**
			 * 更新集团缓存
			 */
			//PortalServiceUtil.getRegistryService().updateGroupCache(pk_group , Boolean.TRUE);
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(),e);
		}   catch (SAXException e) {
			LfwLogger.error(e.getMessage()	, e);
			print("PML格式错误");
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
 	}
	/**
	 * 转换描述模型为数据库存储模型
	 * @param page 页面描述模型
	 * @param pk_group 集团编码
	 * @return
	 */
	private PtPageVO pml2vo(Page page,String pk_group){
		PtPageVO vo = PortalPageDataWrap.copyPage2PageVO(page, null);
		vo.setPk_group(pk_group);
		vo.setFk_pageuser("*");
		vo.setModule(PortalEnv.getPortalCoreName());
		return vo;
	}
	
	 
	
	/**
	 * 编辑PML
	 * @param pk 页面主键
	 * @param pml 页面描述
	 */
	@Action(method="POST")
	public void doEdit(@Param(name="pk")String pk,@Param(name="pml")String pml){
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		if(pml == null || pk == null || ses == null)
			return;
		try {
			/**
			 * 获得旧版本
			 */
			PtPageVO oldVersion = PortalServiceUtil.getPageQryService().getPageByPk(pk);
			String pk_group = oldVersion.getPk_group();
			/**
			 * 管理员是否有权限
			 */
//			if(!SecurityHelper.hasPower(ses.getPk_user(), pk_group))
//				return;
			Page page = PmlUtil.parser(URLDecoder.decode(pml,"UTF-8"));
			if(page == null)
				return;
			boolean pageNameHasModify = !StringUtils.equals(page.getPagename(), oldVersion.getPagename());
			/**
			 * 页面ID是否修改
			 */
			if(pageNameHasModify)
				return;
			
			/**
			 * 应用修改
			 */
			PtPageVO vo = PortalPageDataWrap.copyPage2PageVO(page,oldVersion);
			/**
			 * 更新
			 */
			PortalServiceUtil.getPageService().update(vo);
			/**
			 * 更新缓存
			 */
			//PortalServiceUtil.getRegistryService().updateGroupCache(vo.getPk_group() , Boolean.TRUE);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(),e);
		} catch (SAXException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException("PML格式错误");
		}
	}
	/**
	 * 根据PK打印出PML
	 * @param pk 
	 */
	@Action(method="GET")
	public void getPml(@Param(name="pk")String pk){
		if(pk == null)
			return;
		try {
			PtPageVO vo = PortalServiceUtil.getPageQryService().getPageByPk(pk);
			String pml = vo.doGetSettingsStr();
			print(pml);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}
}
