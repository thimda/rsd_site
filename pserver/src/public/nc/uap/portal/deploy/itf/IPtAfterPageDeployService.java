package nc.uap.portal.deploy.itf;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortalPageService;
import nc.uap.portal.vo.PtPageVO;

/**
 * 元素部署后服务
 * 
 * @author licza
 * 
 */
public interface IPtAfterPageDeployService {
	/**
	 * 增加
	 * 
	 * @param eles
	 */
	void addPages(PtPageVO[] pages, IPtPortalPageService prs,
			String[] pks) throws PortalServiceException;
	

	/**
	 * 更新
	 * 
	 * @param eles
	 */

	/**
	 * 删除
	 * 
	 * @param eles
	 */
}
