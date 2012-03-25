package nc.uap.portal.cache;

/**
 * Portal缓存通知消息处理插件
 * @author licza
 *
 */
public interface IPortalCacheClusterHandle {
	public static final String PID = "CacheClusterHandle";
	/**
	 * 处理通知消息
	 * @param message
	 */
	public void doIt(ClusterCacheMessage message);
}
