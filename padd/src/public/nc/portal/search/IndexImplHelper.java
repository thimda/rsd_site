package nc.portal.search;

import java.net.MalformedURLException;
import nc.bs.framework.common.NCLocator;
import nc.uap.portal.exception.PortalServiceException;

public final class IndexImplHelper {

	/**
	 * 获得索引模块服务
	 * @return
	 * @throws PortalServiceException 
	 * @throws MalformedURLException 
	 */
	public static ILfwIndexService getIndexServiceforPortal() throws PortalServiceException, MalformedURLException{
		ILfwIndexService indexService = NCLocator.getInstance().lookup(ILfwIndexService.class);		
		return indexService;
	}
	
	private static ILfwIndexService getIndexService() throws PortalServiceException
	{
		ILfwIndexService indexService = NCLocator.getInstance().lookup(ILfwIndexService.class);		
		return indexService;
	}
	
	/**
	 * 处理Index相关操作
	 * @param actionType
	 * @param indexVO
	 * @param indexService
	 */
	public static void DoIndex(String actionType,SearchIndexVO indexVO)
	{
		
	}
}
