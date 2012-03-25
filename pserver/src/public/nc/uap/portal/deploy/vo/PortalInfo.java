package nc.uap.portal.deploy.vo;

/**
 * Portal容器基本信息
 * @author dengjt
 *
 */
public class PortalInfo {
	static String name = "UFIDA Portal";

	static String version = "70";

	static String date = "July 1, 2010";

	static String serverInfo = name + " Server";
	static String portalInfo = name + " v" + version + " (" + date + ")";
	public static String getVersion()
	{
		return version;
	}
	public static String getServerInfo()
	{
		return serverInfo;
	}
	public static String getPortalInfo()
	{
		return portalInfo;
	}
}
