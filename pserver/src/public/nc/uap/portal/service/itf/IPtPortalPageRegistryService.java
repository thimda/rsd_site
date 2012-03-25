package nc.uap.portal.service.itf;

import java.util.Set;

import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.vo.PtPageVO;

/**
 * Portal页面缓存注册服务
 * 
 * @author licza
 * 
 */
public interface IPtPortalPageRegistryService {
	/**
	 * 将Portal页面布局描述信息添加到缓存中
	 * 
	 * @throws PortalServiceException Portal页面布局描述信息加载异常
	 */
	public void loadPortalPages() throws PortalServiceException;

	/**
	 * 将存在更新的页面描述信息添加到缓存中
	 * 
	 * @param pageVOs Portal页面布局描述信息
	 */
	public void cachePrepareUpdatePages(Set<PtPageVO> pageVOs);
	/**
	 * 注册用户缓存
	 * @param userid 用户编号
	 * @param page 页面
	 */
	public void registryUserPageCache( Page page);
	
 	
 	/**
 	 * 从缓存中获得最新版的Portal页面
 	 * @param module 模块
 	 * @param pagename 页面名称
 	 * @return
 	 */
 	public PtPageVO getPreUpdatePageFromCache(String module,String pagename);
}
