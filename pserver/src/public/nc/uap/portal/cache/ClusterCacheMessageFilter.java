package nc.uap.portal.cache;

import nc.bs.framework.cluster.itf.ClusterMessage;
import nc.bs.framework.cluster.itf.ClusterMessageFilter;

/**
 * 缓存通知消息过滤器
 * @author licza
 *
 */
public class ClusterCacheMessageFilter implements ClusterMessageFilter {

	@Override
	public boolean accept(ClusterMessage message) {
		return (message instanceof ClusterCacheMessage);
	}

}
