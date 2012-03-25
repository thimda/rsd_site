package nc.uap.portal.cache;

/**
 * 可通知的缓存
 * 
 * @version NC6.1
 * @since NC6.0
 * @author licza
 * 
 */
public interface INotifyAbleCache {
	/**
	 * 缓存命名空间
	 * 
	 * @return
	 */
	String getNameSpace();

	/**
	 * 缓存键
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * 构造缓存内容
	 * 
	 * @return
	 */
	Object build();
}
