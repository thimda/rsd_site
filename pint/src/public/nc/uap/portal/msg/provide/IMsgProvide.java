package nc.uap.portal.msg.provide;

import java.util.List;

import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.msg.model.Msg;
import nc.uap.portal.msg.model.MsgBox;
import nc.uap.portal.msg.model.MsgCategory;
import nc.vo.pub.lang.UFDateTime;

/**
 * 消息生产者，接入系统提供消息的插件接口
 * 
 * @author licza
 * @since V6.1
 */
public interface IMsgProvide {
	/**
	 * 扩展点编码
	 */
	public static final String PID = "MSG_PROVIDE";

	/**
	 * 获得消息
	 * 
	 * @param category 分类ID
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param box 消息盒
	 * @param pi 分页信息
	 * @return
	 */
	public Msg[] query(String category, UFDateTime start, UFDateTime end,
			MsgBox box, PaginationInfo pi, FromWhereSQL whereSql);

	/**
	 * 获得支持的分类列表
	 * 
	 * @return
	 */
	public List<MsgCategory> getCategory();

	/**
	 * 根据消息主键获得消息
	 * 
	 * @param id
	 * @return
	 */
	public Msg get(String id);

	/**
	 * 获得新消息的数量,无分类显示全部分类
	 * 
	 * @return
	 */
	public Integer getNewMessageCount(String category);

	/**
	 * 获得支持的操作命令
	 * 
	 * @param id
	 * @return
	 */
	public IMsgCmd getCmd(String id);
	
	/**
	 * 在消息分类初始化之前执行的操作
	 * 如设置某列是否显示,列名称等等操作
	 */
	public void beforeCategoryInit(MsgCategory currentCategory);
	
	/**
	 * 在消息分类初始化之后执行的操作
	 */
	public void afterCategoryInit(MsgCategory currentCategory);
	
}
