package nc.uap.portal.comm.schedule;

/**
 * Portal任务调度插件接口
 * 
 * <pre>
 * 在Portal中注册任务调度需要实现此接口
 * </pre>
 * 
 * @author licza
 * 
 */
public interface IPtSchedule {
	/**
	 * 插件扩展点
	 */
	public static final String PID = "PtSchedule";

	/**
	 * 设置调度的时间段
	 * 
	 * <pre>
	 * 以毫秒计数
	 * </pre>
	 * 
	 * @return
	 */
	public long getTimeSpan();

	/**
	 * 开始执行
	 */
	public void go();

	/**
	 * 获得编号
	 * 
	 * @return
	 */
	public String getID();

}
