package nc.uap.portal.integrate.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.portal.cache.PortalCacheManager;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.message.itf.IPortalMessage;
import nc.uap.portal.integrate.message.itf.IPortalNoticeMessageExchange;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtMessageCenterQryService;
import nc.uap.portal.util.PtUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 消息中心数据包装类
 * 
 * @author licza
 * 
 */
public class MessageCenter {
	public static final String MESSAGE_CENTER_KEY = "_MESSAGE_CENTER_KEY";
	public static final String PLUGIN_CATEGORY_KEY = "_PLUGIN_CATEGORY_KEY";
	public static final String MESSAGE_CATEGORY_KEY = "_MESSAGE_CATEGORY_KEY";
	public static final String CATEGORY_SYSTEMCODE_KEY = "_CATEGORY_SYSTEMCODE_KEY";
	public static final String MESSAGE_NAV_CATEGORY_KEY = "_MESSAGE_NAV_CATEGORY_KEY";
	public static final String MESSAGE_NAV_DIC_KEY = "_MESSAGE_NAV_DIC_KEY";
	public static final String MESSAGE_MAPING_DIC_KEY = "_MESSAGE_MAPING_DIC_KEY";
	
	/**
	 * 处理消息查询
	 * @param param
	 * @param msgds
	 * @param switchover
	 */
	public static void processMessageQry(MessageQueryParam param , Dataset msgds,boolean switchover){
		msgds.setCurrentKey(param.getCategory() + "~");
		PaginationInfo pg = msgds.getCurrentRowSet().getPaginationInfo();
		/**
		 * 切换
		 */
		if(switchover){
			pg.setPageIndex(0);
			pg.setRecordsCount(-1);
			LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(MCConstant.MESSAGE_QRY_PARAM, param);
		}
		PtMessageVO[] vos = MessageCenter.query(param, pg);
		if(vos != null){
			new SuperVO2DatasetSerializer().serialize(vos, msgds, Row.STATE_NORMAL);
		}
	}
	/**
	 * 获得消息查询参数
	 * @return
	 */
	public static MessageQueryParam getMessageQueryParam(){
		return (MessageQueryParam)LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getAttribute(MCConstant.MESSAGE_QRY_PARAM);
	}
	/**
	 * 查询消息
	 * @param param
	 * @param pg
	 * @return
	 */
	public static PtMessageVO[] query(MessageQueryParam param , PaginationInfo pg){
		PtMessageVO[] vos = null;
		String category = param.getCategory();
		String parentId = param.getParentid();
		String pluginid = param.getPluginid();
		String[] states = param.getStates();
		int type = param.getTimeSection();
		String pk_user = param.getPk_user(); 
		try {
			/**
			 * 垃圾箱
			 */
			if(MCConstant.TYPE_TRASH.getSyscode().equals(category)) {
				pg.setRecordsCount(PortalServiceUtil.getMessageQryService().getTrashMessageCount(pk_user));
				vos = PortalServiceUtil.getMessageQryService().getTrashMessage(pk_user,pg);
			}
			/**
			 * 发件箱
			 */
			else if(MCConstant.TYPE_SENT.getSyscode().equals(category)){
				pg.setRecordsCount( PortalServiceUtil.getMessageQryService().getSentMessageCount(pk_user));
				vos = PortalServiceUtil.getMessageQryService().getSentMessage(pk_user,pg);
			}
			else {
				if(!PtUtil.isNull(parentId)){
					IPortalMessage me =  MessageCenter.lookup(pluginid);
					vos = me.getMessage(category, type, states,pg);

				}
			}
		} catch (CpbBusinessException e1) {
			LfwLogger.error(e1.getMessage(),e1);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
		}
 		
		return vos;
	}
	
	/**
	 * 获得辅助信息
	 * @param param
	 * @return
	 */
	public static String[] getTile(MessageQueryParam param){
		String tabName = null;
		String title = null;
		String syscode = param.getCategory();
		String parentId = param.getParentid();
		String pluginid = param.getPluginid();
		/**
		 * 垃圾箱
		 */
		if(MCConstant.TYPE_TRASH.getSyscode().equals(syscode)) {
 			tabName = MCConstant.TYPE_NOTICE.getSyscode();
			title = MCConstant.TYPE_TRASH.getTitle();
		}
		/**
		 * 发件箱
		 */
		else if(MCConstant.TYPE_SENT.getSyscode().equals(syscode)){
			tabName = MCConstant.TYPE_NOTICE.getSyscode();
			title = MCConstant.TYPE_NOTICE.getTitle();
		}else {
			if(!PtUtil.isNull(parentId)){
				IPortalMessage me;
				try {
					me = MessageCenter.lookup(pluginid);
					title = me.getTitle();
				} catch (CpbBusinessException e) {
					LfwLogger.error(e.getMessage(),e);
				}
				tabName = parentId;
			}
		}
	 return new String[]{tabName,title};
	}
	
	/**
	 * 消息类型与插件对照表
	 * @return
	 */
	public MessageCenter(){
 		
	}
	/**
	 * 获得消息名称字典
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getPluginNameDic(){
 		return (Map<String,String>) PortalCacheManager.getMessageCache(MESSAGE_MAPING_DIC_KEY);
 	}
	
	/**
	 * 获得消息分类对应的插件字典
	 * @return
	 */
 	@SuppressWarnings("unchecked")
	public static Map<String,String> getSys2PluginDic(){
 		return (Map<String,String>) PortalCacheManager.getMessageCache(PLUGIN_CATEGORY_KEY);
 	}
 	/**
 	 * 获得消息分类对应的系统编码字典
 	 * @return
 	 */
 	@SuppressWarnings("unchecked")
	public static Map<String,String> getMessageCategoryDic(){
 		return (Map<String,String>) PortalCacheManager.getMessageCache(MESSAGE_CATEGORY_KEY);
 	}
 	/**
 	 * 获得系统对应的消息分类编码
 	 * @return
 	 */
 	@SuppressWarnings("unchecked")
	public static Map<String,List<String>> getChild2CategoryMap(){
 		return (Map<String,List<String>>) PortalCacheManager.getMessageCache(CATEGORY_SYSTEMCODE_KEY);
 	}
 	/**
 	 * 获得所有消息分类
 	 * @return
 	 */
 	@SuppressWarnings("unchecked")
	public static List<PtMessagecategoryVO> getNavCategory(){
 		return (List<PtMessagecategoryVO>) PortalCacheManager.getMessageCache(MESSAGE_NAV_CATEGORY_KEY);
 	}
 	
 	/**
 	 * 获得导航
 	 * @param id
 	 * @return
 	 */
 	@SuppressWarnings("unchecked")
	public static PtMessagecategoryVO getNav(String id){
 		Map<String,PtMessagecategoryVO> ncp  = (Map<String,PtMessagecategoryVO> ) PortalCacheManager.getMessageCache(MESSAGE_NAV_DIC_KEY);
 		if(ncp != null)
 			return ncp.get(id);
 		return null;
 	}
 

	/**
	 * 获得信息导航数据
	 * 
	 * @param pk_user
	 * @return
	 */
	public static PtMessagecategoryVO[] getNavData(String pk_user) {
		List<PtMessagecategoryVO> navList = new ArrayList<PtMessagecategoryVO>();
		

		int noticeCount =0 ,taskCount =0 ,warnCount= 0;
		
		
		// 节点
		//从缓存中获得导航
		List<PtMessagecategoryVO> categorys = MessageCenter.getNavCategory();
		if(categorys != null && !categorys.isEmpty()){
			List<PtMessagecategoryVO> categorysWithCount = new ArrayList<PtMessagecategoryVO>();
			/**
			 * 将信息的数量加入导航
			 */
			for(PtMessagecategoryVO category : categorys){
				try {
					String pluginid = category.getPluginid();
					IPortalMessage me = (IPortalMessage) PluginManager.newIns().getExtension(pluginid).newInstance();
					PtMessagecategoryVO cate = (PtMessagecategoryVO)category.clone();
					int count = me.getNewMessageCount(category.getSyscode());
					cate.setTitle(cate.getTitle()+"("+count+")");
					categorysWithCount.add(cate);
					if(category.getParentid().equals(MCConstant.TYPE_NOTICE.getSyscode()))
						noticeCount = noticeCount + count;
					if(category.getParentid().equals(MCConstant.TYPE_TASK.getSyscode()))
						taskCount = taskCount + count;
					if(category.getParentid().equals(MCConstant.TYPE_WARING.getSyscode()))
						warnCount = warnCount + count;
					
				} catch (Exception e) {
					LfwLogger.error("查询分类的时候出现异常" + e.getMessage(),e);
				}
			}
			navList.addAll(categorysWithCount);
		}
		
		PtMessagecategoryVO cateNotice = (PtMessagecategoryVO) MCConstant.TYPE_NOTICE.clone();
		cateNotice.setTitle(cateNotice.getTitle() + "("+noticeCount+")");
		navList.add(cateNotice);
		PtMessagecategoryVO cateTask = (PtMessagecategoryVO) MCConstant.TYPE_TASK.clone();
		cateTask.setTitle(cateTask.getTitle() + "("+taskCount+")");
		navList.add(cateTask);
		PtMessagecategoryVO cateWarn = (PtMessagecategoryVO) MCConstant.TYPE_WARING.clone();
		cateWarn.setTitle(cateWarn.getTitle() + "("+warnCount+")");
		navList.add(cateWarn);
		IPtMessageCenterQryService mcq = PortalServiceUtil.getMessageQryService();
		Integer sentCount = 0;
		try {
			sentCount = mcq.getSentMessageCount(pk_user);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		
		PtMessagecategoryVO cateSent = (PtMessagecategoryVO) MCConstant.TYPE_SENT.clone();
		cateSent.setTitle(cateSent.getTitle() + "("+sentCount+")");
		
		// 发件箱
		navList.add(cateSent);
		
		Integer trashCount = 0;
		try {
			trashCount = mcq.getTrashMessageCount(pk_user);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		
		PtMessagecategoryVO cateTrash = (PtMessagecategoryVO) MCConstant.TYPE_TRASH.clone();
		cateTrash.setTitle(cateTrash.getTitle() + "("+trashCount+")");
		// 垃圾箱
		navList.add(cateTrash);
		
		return navList.toArray(new PtMessagecategoryVO[0]);
	}

	/**
	 * 获得分类下的系统编码
	 * 
	 * @param categoryCode
	 * @return
	 */
	public static String[] getSystemCodes(String categoryCode) {
		List<String> codelist = MessageCenter.getChild2CategoryMap().get(categoryCode);
		if(codelist == null)
			return new String[0];
		return codelist.toArray(new String[0]);
	}
	
	/**
	 * 获得分类下的新信息数量
	 * @param categoryCode
	 * @return
	 */
	public static int getSystemNavNewMessageCount(String[] systems){
		String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
		try {
			return PortalServiceUtil.getMessageQryService().getNewMessageCounts(pk_user, systems);
		} catch (PortalServiceException e) {
			LfwLogger.error("获得分类下的新信息数量失败!", e);
			return 0;
		}
	}
	
	/**
	 * 获得系统导航数据
	 * @param categoryCode
	 * @return
	 */
	public static List<Map<String,String>> getSystemNavData(String[] systems){
		Map<String, String> dic = MessageCenter.getSys2PluginDic();
		List< Map<String,String>> codelist = new ArrayList< Map<String,String>>();
		if (systems != null && systems.length > 0) {
			for (String ex : systems) {
				try {
					String pluginid = dic.get(ex);
					IPortalMessage me = (IPortalMessage) PluginManager.newIns().getExtension(pluginid).newInstance();
					PtMessagecategoryVO cate = MessageCenter.getNav(ex);
					if(me != null){
 							Map<String,String> c = new HashMap<String,String>();
							c.put("systemName", cate.getTitle());
							c.put("systemCode", cate.getSyscode());
							c.put("pluginid", pluginid);
							c.put("newMessage", me.getNewMessageCount(ex).toString());
							codelist.add(c);
					}
				} catch (CpbBusinessException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		return codelist;
	}
	
	/**
	 * 获得消息插件
	 * @param systemCode
	 * @return
	 * @throws PortalServiceException
	*/
	public static IPortalMessage lookup(String systemCode) throws CpbBusinessException {
		return   PluginManager.newIns().getExtension(systemCode).newInstance(IPortalMessage.class);
	}
	
	/**
	 * 通知消息命令执行者
	 * @param ex
	 * @param pk
	 * @param cmd
	 * @param ctx
	 */
	public static void noticeMessageCmdExec(IPortalNoticeMessageExchange ex, String[] pk, String cmd, LfwPageContext ctx){
			if(StringUtils.equals(cmd, "delInbox")){
				ex.delInbox(pk);
			}else if(StringUtils.equals(cmd, "delSent")){
				ex.delSent(pk);
			}else if(StringUtils.equals(cmd, "reply")){
				ex.reply(ctx);
			}else if(StringUtils.equals(cmd, "fwd")){
				ex.fwd(ctx);
			}else if(StringUtils.equals(cmd, "read")){
				ex.read(pk[0]);
			}else if(StringUtils.equals(cmd, "compose")){
				ex.compose(ctx);
			}else if(StringUtils.equals(cmd, "view")){
				ex.view(pk[0],ctx);
			}
	}
	
	/**
	 * 获得选中的消息PK
	 * 
	 * @return
	 */
	public static String[] getSelectMessagePks(LfwPageContext ctx) {
		List<String> pks = new ArrayList<String>();
		LfwWidget widget = ctx.getPageMeta().getWidget("main");
		Dataset ds = widget.getViewModels().getDataset("msgds");
		Row[] rs = ds.getSelectedRows();
		if (rs == null)
			throw new LfwRuntimeException("没有选中的行!");
		for (Row row : rs) {
			String pk_message = row.getString(ds.nameToIndex("pk_message"));
			pks.add(pk_message);
		}
		return pks.toArray(new String[0]);
	}

}
