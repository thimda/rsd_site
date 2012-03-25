package nc.uap.portal.mng.page.itf;

import nc.portal.portlet.vo.PtPageDeptVO;
import nc.portal.portlet.vo.PtPageRoleVO;
import nc.portal.portlet.vo.PtPageUserVO;

public interface IPageManagerQryService {
	
	/**
	 * 根据查询条件查询页面用户关系表
	 * @param sqlWhere
	 * @return
	 */
	public PtPageUserVO[] getPageUserVOByCondition(String sqlWhere);
	
	/**
	 * 根据查询条件查询页面角色关系表
	 * @param sqlWhere
	 * @return
	 */
	public PtPageRoleVO[] getPageRoleVOByCondition(String sqlWhere);
	
	/**
	 * 根据查询条件查询页面部门关系表
	 * @param sqlWhere
	 * @return
	 */
	public PtPageDeptVO[] getPageDeptVOByCondition(String sqlWhere);
}
