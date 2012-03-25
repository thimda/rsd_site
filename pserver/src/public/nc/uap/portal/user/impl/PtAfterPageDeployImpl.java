package nc.uap.portal.user.impl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.portal.deploy.itf.IPtAfterPageDeployService;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.org.GroupVO;

public class PtAfterPageDeployImpl implements IPtAfterPageDeployService{

	public void addPages(PtPageVO[] pages, IPtPortalPageService prs,
			String[] pks) throws PortalServiceException {
		GroupVO[] groups = null;
		try {
			groups = CpbServiceFacility.getCpGroupQry().queryAllGroupVOs();;
		} catch (Exception e) {
			throw new PortalServiceException(e);
		}
		PtPageVO[] pvc = churnPageVO(pages, groups, pks);
		prs.addPortalPage(pvc);
	}
	

	/**
	 * 给每个Group生成一份PortalPageVO
	 * 
	 * @param addPage
	 * @param groups
	 * @return
	 */
	public static PtPageVO[] churnPageVO(PtPageVO[] pages, GroupVO[] groups, String[] pks) {
		List<PtPageVO> set = new ArrayList<PtPageVO>();
		for (int i = 0; i < pages.length; i++) {

			for (GroupVO group : groups) {
				PtPageVO ppv = pages[i];
				String pk;
				if (pks != null) {
					/**
					 *  新增
					 */
					pk = pks[i];
				} else {
					/**
					 *  修改
					 */
					pk = ppv.getPk_portalpage();
				}
				ppv.setPk_portalpage("");
				ppv.setParentid(pk);
				ppv.setPk_group(group.getPk_group());
				PtPageVO pageVO = new PtPageVO();
				pageVO = (PtPageVO) ppv.clone();
				set.add(pageVO);
			}
		}
		return set.toArray(new PtPageVO[0]);
	}

	
}
