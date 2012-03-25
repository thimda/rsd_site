package nc.uap.portal.util;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.sfapp.IAppendProductConfService;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.BusinessException;

/**
 * Portal使用数据源获取
 *
 */
public final class PortalDsnameFetcher {
	private static String dsName;
	public static String getPortalDsName() {
		if(dsName == null){
			IAppendProductConfService proService = NCLocator.getInstance().lookup(IAppendProductConfService.class);
			try {
				dsName = proService.getNCPortalDsName();
				LfwLogger.debug("获取到Portal数据源:" + dsName);
			} 
			catch (BusinessException e) {
				LfwLogger.error(e);
			}
		}
		return dsName;
	}
}
