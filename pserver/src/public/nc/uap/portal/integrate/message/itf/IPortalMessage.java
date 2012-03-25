package nc.uap.portal.integrate.message.itf;

import java.util.List;

import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;
import nc.uap.portal.plugins.model.IDynamicalPlugin;

/**
 * Portal消息插件
 * 
 * @author licza
 * 
 */
public interface IPortalMessage extends IDynamicalPlugin {
	
	/**
	 * 获得消息
	 * @param category 分类
	 * @param timeSection 时间段(1= 一周 ,2= 30天内 ,3=三个月内)
	 * @param states 消息状态 (0 =未读,1=已读,-1=已删除,-2=彻底删除)
	 * @param pinfo 分页信息
	 * @return
	 */
	public PtMessageVO[] getMessage(String category, int timeSection, String[] states ,PaginationInfo pi);
	
	/**
	 * 获得支持的分类列表
	 * @return
	 */
	public List<PtMessagecategoryVO> getCategory();
	
	/**
	 * 根据消息主键获得消息
	 * 
	 * @param pk_user
	 * @return
	 */
	public PtMessageVO getMessage(String pk);
	
	/**
	 * 消息操作
	 * 
	 * @param pk 消息主键
	 * @param cmd 命令
	 * @param ctx 当前页面上下文
	 */
	public void execute(String[] pk,String cmd,LfwPageContext ctx);
	
	/**
	 * 获得新消息的数量
	 * @return
	 */
	public Integer getNewMessageCount(String category);

}
