package nc.uap.portal.deploy.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.vo.PtPortletVO;

public interface IPtAfterPortletDeployService {
	void addPortlet(PtPortletVO[] portles, IPtPortletService prs,
			String[] pks) throws PortalServiceException ;
}
