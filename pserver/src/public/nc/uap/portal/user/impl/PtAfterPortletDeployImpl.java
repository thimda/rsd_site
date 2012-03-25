package nc.uap.portal.user.impl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.portal.deploy.itf.IPtAfterPortletDeployService;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.vo.PtPortletVO;
import nc.vo.org.GroupVO;

public class PtAfterPortletDeployImpl implements IPtAfterPortletDeployService {
	public void addPortlet(PtPortletVO[] portles, IPtPortletService prs,
			String[] pks) throws PortalServiceException {
		GroupVO[] groups = null;
		try {
			groups = CpbServiceFacility.getCpGroupQry().queryAllGroupVOs();;
		} catch (Exception e) {
			throw new PortalServiceException(e);
		}
		// 每个集团生成一份
		PtPortletVO[] pvc = churnPortletVO(portles, groups, pks);
		prs.addPortlets(pvc);
	}

	/**
	 * 给每个集团生成一份Portlet
	 * 
	 * @param portlets
	 *            PortletVOs
	 * @param groups
	 *            集团
	 * @return
	 */
	private static PtPortletVO[] churnPortletVO(PtPortletVO[] portlets,
			GroupVO[] groups, String[] pks) {
		List<PtPortletVO> set = new ArrayList<PtPortletVO>();
		for (int i = 0; i < portlets.length; i++) {
			for (GroupVO group : groups) {
				PtPortletVO ppv = portlets[i];
				String parentid;
				if (pks != null) {
					ppv.setPk_portlet("");
					parentid = pks[i];
					ppv.setParentid(parentid);
					ppv.setPk_group(group.getPk_group());
					PtPortletVO portletVO = (PtPortletVO) ppv.clone();
					set.add(portletVO);
				}
			}
		}
		return set.toArray(new PtPortletVO[0]);
	}
}
