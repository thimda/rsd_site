package nc.uap.portal.integrate.system;

import java.util.List;
import nc.bs.logging.Logger;

/**
 * 对SSOProviderVO进行ip mapping 处理
 * 
 * @author lkp
 * 
 */
public class SsoProviderMappingUtil {
	private static final String replaceSymbol = "$";

	public static SSOProviderVO mapping(SSOProviderVO provider) {
		return mapping(new SSOProviderVO[] { provider })[0];
	}

	public static SSOProviderVO[] mapping(SSOProviderVO[] providers) {
		if (providers == null || providers.length == 0)
			return providers;

		String sourceIp = fetchSourceIp();
		for (int i = 0; i < providers.length; i++) {
			processProviderVO(providers[i], sourceIp);
		}
		return providers;
	}

	private static void processProviderVO(SSOProviderVO provider, String sourceIp) {
		String gateUrl = provider.getGateUrl();
		List<IpReference> mappings = provider.getMappingReference();
		String targetIp = null;
		if (mappings != null) {
			for (int i = 0; i < mappings.size(); i++) {
				if (mappings.get(i).getSourceIp().equals(sourceIp))
					targetIp = mappings.get(i).getTargetIp();
			}
		}
		Logger.debug("===portal:SsoProviderMappingUtil类processProviderVO方法,gateUrl:" + gateUrl + ",获取目标IP:" + targetIp);

		if (targetIp != null) {
			if (gateUrl != null)
				provider.setGateUrl(gateUrl.replace(replaceSymbol, targetIp));

			List<Reference> providerReference = provider.getProviderReference();
			for (int i = 0; i < providerReference.size(); i++) {
				if (providerReference.get(i).getValue().contains(replaceSymbol))
					providerReference.get(i).setValue(providerReference.get(i).getValue().replace(replaceSymbol, targetIp));
			}
		}
	}

	private static String fetchSourceIp() {
		// HttpServletRequest req = PortalRequestThreadLocal.getRequest();
		// String serverName = req.getServerName();
		//		
		//		
		//		
		// return serverName;

		String serverName = nc.uap.lfw.core.LfwRuntimeEnvironment.getFromServerName();
		Logger.debug("===portal:进行IP mapping,获取当前请求的serverName:" + serverName);
		return serverName;
		// Logger.debug("进行IP mapping，获取当前请求的serverName::" + serverName);
		//		
		// try {
		// String ip = "";
		// byte[] address = InetAddress.getByName(serverName).getAddress();
		// if(address == null)
		// return defaultSourceIp;
		// for(int i = 0;i < address.length; i++)
		// {
		// if(i == 0)
		// ip += address[i];
		// else
		// ip += ("." + address[i]);
		// }
		// if(address.length < 4)
		// for(int i = 0;i < 4 - address.length; i++)
		// ip += ("." + address[i]);
		//			
		// return ip;
		// } catch (UnknownHostException e) {
		// Logger.error("通过serverName获取IP地址时出现错误：" + e.getMessage());
		// return defaultSourceIp;
		// }
		//		
	}
}
