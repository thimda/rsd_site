package nc.uap.portal.deploy.itf;

import nc.uap.portal.util.PtUtil;

/**
 * Portal部署抽象实现
 * @author licza
 *
 */
public abstract class AbstractPtDeploy implements IPtDeploy{
	
//	/**
//	 * 资源部署
//	 * @param resourcefuls
//	 */
//	public void deployResource(Resourceful[] resourcefuls,String module,Integer type){
//		PortalServiceUtil.getServiceProvider().getResourceService().doDeploy(resourcefuls, module, type);
//	}
	
	/**
	 * 比较本地版本与数据库中的版本
	 * 
	 * @param localVersion
	 * 
	 * @param dbversion
	 * 
	 * @return 本地与数据库中版本的新旧
	 */
	protected static boolean versionCompare(String localVersion, String dbversion) {
		if (PtUtil.isNumbic(localVersion) && PtUtil.isNumbic(dbversion))
			return Integer.parseInt(localVersion) > Integer.parseInt(dbversion);
		else
			return false;
	}
}
