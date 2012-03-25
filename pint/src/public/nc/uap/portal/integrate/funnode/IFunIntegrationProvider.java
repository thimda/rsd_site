package nc.uap.portal.integrate.funnode;

/**
 * 功能集成生产者
 * 
 * @author licza
 * 
 */
public interface IFunIntegrationProvider {

	public static final String PID = "FUN_INT";
	/**
	 * 是否可见 
	 * @return
	 */
	Boolean isVisibility();
	
	/**
	 * 获得ID
	 * 
	 * @return
	 */
	String getId();
	/**
	 * 获得图标
	 * @return
	 */
	String getIcon();
	
	/**
	 * 获得标题
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * 获得详细信息
	 * 
	 * @return
	 */
	String getDetail();

	/**
	 * 获得统计信息
	 * 
	 * @return
	 */
	Integer getStat();

}
