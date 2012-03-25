package nc.uap.portal.integrate.system;

import java.util.Properties;

import nc.bs.framework.common.NCLocator;
import nc.uap.portal.exception.PortalServiceException;

/**
 * @author yzy
 */
public class PortletRuntimeEnv {

	private static PortletRuntimeEnv instance = new PortletRuntimeEnv();
	private static String defaultUrl = "http://127.0.0.1:80";
	public static PortletRuntimeEnv getInstance() {
		return instance;
	}

	public Properties getNcProperties(String ncUrl) {
		if (ncUrl == null || ncUrl.trim().length() <= 0) 
			ncUrl = defaultUrl;
		Properties props = new Properties();
//		props.setProperty("CLIENT_COMMUNICATOR",
//				"nc.bs.framework.comn.cli.JavaURLCommunicator");
		props.setProperty("SERVICEDISPATCH_URL", ncUrl
				+ "/ServiceDispatcherServlet");
		return props;
	}
	
	public NCLocator getTargetNCLocator(String systemCode) throws PortalServiceException {
		if(systemCode == null || systemCode.equals(""))
			systemCode = "NC";
		String ncUrl = ProviderFetcher.getInstance().getProvider(systemCode).getValue("runtimeUrl");
		return NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
	}
	
	public NCLocator getTargetIUFOLocator() throws PortalServiceException {
		String iufoUrl = ProviderFetcher.getInstance().getProvider("IUFO").getValue("runtimeUrl");
		return NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(iufoUrl));
	}
}
