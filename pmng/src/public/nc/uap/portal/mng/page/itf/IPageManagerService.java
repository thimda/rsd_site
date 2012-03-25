package nc.uap.portal.mng.page.itf;

import nc.portal.portlet.vo.PtPageDeptVO;
import nc.portal.portlet.vo.PtPageRoleVO;
import nc.portal.portlet.vo.PtPageUserVO;

public interface IPageManagerService {

	/**
	 * 创建页面-部门关系vo
	 * @param vo
	 */
	public void createPtPageDeptVO(PtPageDeptVO vo);
	/**
	 * 创建页面-用户关系vo
	 * @param vo
	 */
	public void createPtPageUserVO(PtPageUserVO vo);
	/**
	 * 创建页面-角色关系vo
	 * @param vo
	 */
	public void createPtPageRoleVO(PtPageRoleVO vo);
}
