package nc.uap.portal.service;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.cil.ICilService;
import nc.itf.uap.sf.ISMVerifyService;
import nc.itf.uap.sf.IServiceProviderSerivce;
import nc.itf.uap.sfapp.IAppendProductConfService;

/**
 * NC服务查询类
 * @author gd 2008-12-30
 * @version NC5.5
 * @since NC5.5
 */
public class NcServiceFacility {
	private static IServiceProviderSerivce serviceProviderService = null;
	private static IAppendProductConfService appendProductService = null; 
	private static ICilService cilService = null;
	private static ISMVerifyService smVerifyService = null;
	
	public static IServiceProviderSerivce getProviderService()
	{
		if(serviceProviderService == null)
			serviceProviderService = (IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName());
		return serviceProviderService;
	}
	
	public static IAppendProductConfService getAppendProductService()
	{
		if(appendProductService == null)
			appendProductService = (IAppendProductConfService)NCLocator.getInstance().lookup(IAppendProductConfService.class);
		return appendProductService;
	}
	
	public static ICilService getCilService()
	{
		if(cilService == null)
			cilService = (ICilService)NCLocator.getInstance().lookup(ICilService.class);
		return cilService;
	}
	
	public static ISMVerifyService getSmVerifyService()
	{
		if(smVerifyService == null)
			smVerifyService = (ISMVerifyService)NCLocator.getInstance().lookup(ISMVerifyService.class.getName());
		return smVerifyService;
	}
}
