package nc.uap.portal.integrate.message;

import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;

/**
 * 消息中心常量
 * 
 * @author licza
 * 
 */
public class MCConstant {
	public static final PtMessagecategoryVO TYPE_NOTICE = new PtMessagecategoryVO("notice", null, "通知消息", "Notice",null);
	public static final PtMessagecategoryVO TYPE_TASK = new PtMessagecategoryVO("task", null, "工作任务", "Task",null);
	public static final PtMessagecategoryVO TYPE_WARING = new PtMessagecategoryVO("warning", null, "预警消息", "Warning",null);
	public static final PtMessagecategoryVO TYPE_SENT = new PtMessagecategoryVO("sent", null, "已发送消息", "Sent",null);
	public static final PtMessagecategoryVO TYPE_TRASH = new PtMessagecategoryVO("trash", null, "已删除消息", "Trash",null);
	/** 消息中心插件扩展点 **/
	public static final String MC_EXPOINT = "messagecenter";
	/** 消息状态:未读 **/
	public static final String STATE_NEW = "0";
	/** 消息状态:已读 **/
	public static final String STATE_READ = "1";
	/** 消息状态:已删除 **/
	public static final String STATE_TRASH = "-1";
	/** 消息状态:彻底删除 **/
	public static final String STATE_DELETE = "-2";
	
	/** 工作流状态:任务已创建 **/
	public static final String STATE_WFL_CREATE = "3";
	
	/** 工作流状态:任务执行中 **/
	public static final String STATE_WFL_DOING = "4";

	/** 工作流状态:任务执行完毕 **/
	public static final String STATE_WFL_DONE = "5";
	
	/** 收件箱 **/
	public static final String MSG_INBOX = "in";
	/** 垃圾箱 **/
	public static final String MSG_TRASH = "trash";
	/** 私信 **/
	public static final String PERSON_MESSAGE = "personmessage";
	/**
	 * 任务信息
	 */
	public static final String TASK_MESSAGE = "taskmessage";
	
	/** 是否包括已读信息 **/
	public static final String INCLUDE_READED_MESSAGE = "INCLUDE_READED_MESSAGE";
	/** 消息查询参数 **/
	public static final String MESSAGE_QRY_PARAM = "Message_Qry_Param";
	

}
