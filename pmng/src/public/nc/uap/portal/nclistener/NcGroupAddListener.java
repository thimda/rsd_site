package nc.uap.portal.nclistener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.vo.pub.BusinessException;

public class NcGroupAddListener implements IBusinessListener{
 
	@Override
	public void doAction(IBusinessEvent event) throws BusinessException {
// 
//		BDCommonEvent bevent = (BDCommonEvent) event;
//		BDCommonUserObj ncgroupvo = (BDCommonUserObj) bevent.getUserObject();
//		Object[] groupvos = (Object[]) ncgroupvo.getNewObjects();		
//		if (groupvos == null || groupvos.length == 0) {
//			return;
//		}
//		for(int i=0;i<groupvos.length;i++){
//			GroupVO group = ((GroupVO)groupvos[i]);
//			String pk_group = group.getPk_group();
//			IPtPageService pageService = PortalServiceUtil.getPageService();
//			IPtPortletService portletService = PortalServiceUtil.getPortletService();
//			try {
//			//同步page
//			pageService.sync(pk_group);		
//			//同步portlet
//			portletService.sync(pk_group);
//			
//			//通知清空缓存
//			PortalCacheManager.notify(CacheKeys.PORTLETS_CACHE, CacheKeys.GROUP_PORTLETS_CACHE);
//			
//			CpbExtentionUtil.notifyAfterAction(ICpbExtentionService.GROUPMANAGE, ICpbExtentionService.ADD, group);
//			} catch (PortalServiceException e) {
//				LfwLogger.error(e.getMessage(), e);
//			}
//		}
		}
	
}
