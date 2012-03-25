package nc.uap.portal.deploy.impl;

import java.util.ArrayList;
import java.util.List;

import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.deploy.itf.IPtDeploy;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtDisplayVO;
import nc.uap.portal.vo.PtDisplaycateVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

/**
 * 部署Portlet分组
 * 
 * @author licza
 * 
 */
public class PtDisplayDepoly implements IPtDeploy {
	/** Portlet显示信息列表 **/
	private List<PtDisplayVO> portletDisplayList = null;
	
	/** Portlet分组列表 **/
	private List<PtDisplaycateVO> portletDisplayCateList = null;
	
	@Override
	public void deploy(PortalDeployDefinition define) {
		Display display = define.getDisplay();
		if(display == null)
			return;
		String module = define.getModule();
		List<PortletDisplayCategory> cates = display.getCategory();
		if (cates == null || cates.isEmpty())  
			return;
		for (PortletDisplayCategory cate : cates) {
			addPortletDisplay(cate, module);
		}
		clearPortletDisplayByModule(module);
		addAllDisplay();
	}
	/**
	 * 新增所有的portlt分类显示信息
	 */
	private void addAllDisplay(){
		addAllPortletDisplay();
		addAllPortletDisplayCategory();
	}
	
	/**
	 * 将Portlet显示添加到数据库
	 */
	private void addAllPortletDisplay(){
		if(getPortletDisplayList().isEmpty())
			return;
		try {
			CRUDHelper.getCRUDService().saveBusinessVOs(PtUtil.setSuperVO2AggVOParent(getPortletDisplayList(),VOStatus.NEW));
		} catch (Exception e) {
			LfwLogger.error("==="+this.getClass().getName()+"#===添加Portlet显示发生异常" ,e.getCause());
		}
	}
	
	/**
	 * 将Portlet分组显示添加到数据库
	 */
	private void addAllPortletDisplayCategory(){
		if(getPortletDisplayCateList().isEmpty())
			return;
		try {
			CRUDHelper.getCRUDService().saveBusinessVOs(PtUtil.setSuperVO2AggVOParent(getPortletDisplayCateList(),VOStatus.NEW));
		} catch (Exception e) {
			LfwLogger.error("==="+this.getClass().getName()+"#===添加Portlet显示发生异常" ,e.getCause());
		}
	}
	
	
	/**
	 * 清除模块下的Portlet显示信息
	 */
	private void clearPortletDisplayByModule(String module){
		/**
		 * Portal核心模块,不清理
		 */
		if(PortalEnv.getPortalCoreName().equals(module))
			return;
		String sql = "delete from pt_display where module = ? ";
		SQLParameter param = new SQLParameter();
		param.addParam(module);
		try {
			CRUDHelper.getCRUDService().executeUpdate(sql, param);
		} catch (Exception e) {
			LfwLogger.error("==="+this.getClass().getName()+"#===清除模块:"+module+"下的Portlet分组发生异常" ,e.getCause());
		}
	}
	
	/**
	 * 增加Portal分组到列表中
	 * @param category
	 * @param module
	 */
	private void addPortletDisplay(PortletDisplayCategory category, String module){
		if(category == null || category.getPortletDisplayList().isEmpty())
			return;
		String cateid = category.getId();
		/**
		 * 新分类
		 */
		if(!isCategoryNotExist(cateid)){
			PtDisplaycateVO dc = new PtDisplaycateVO();
			dc.setId(cateid);
			dc.setI18nname(category.getI18nName());
			dc.setTitle(category.getText());
			getPortletDisplayCateList().add(dc);
		}
		/**
		 * portlet显示
		 */
		for(PortletDisplay display : category.getPortletDisplayList()){
			display.setModule(module);
			getPortletDisplayList().add(new PtDisplayVO(display,cateid));
		}
	}
	
	/**
	 * 判断分类是否存在
	 * @param moduleid
	 * @return
	 */
	private boolean isCategoryNotExist(String cateid){
		PtDisplaycateVO displayVO = new PtDisplaycateVO();
		displayVO.setId(cateid);
		try {
			SuperVO[] result = CRUDHelper.getCRUDService().queryVOs(displayVO, null, null);
			return result != null && result.length > 0;
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(),e);
			return false;
		}
	}
	
	/**
	 * getter
	 * @return
	 */
	protected List<PtDisplayVO> getPortletDisplayList() {
		if(portletDisplayList == null)
			portletDisplayList = new ArrayList<PtDisplayVO>();
		return portletDisplayList;
	}
	
	/**
	 * getter
	 * @return
	 */
	protected List<PtDisplaycateVO> getPortletDisplayCateList() {
		if(portletDisplayCateList == null)
			portletDisplayCateList = new ArrayList<PtDisplaycateVO>();
		return portletDisplayCateList;
	}
	
}
