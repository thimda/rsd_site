package nc.uap.portal.integrate.message.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.MessageQueryParam;
import nc.uap.portal.integrate.message.itf.AbstractIntergrateNoticeMessageExchage;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;
import nc.uap.portal.integrate.othersystem.nc5x.Nc5xIntConstent;
import nc.uap.portal.integrate.othersystem.nc5x.Nc5xMessageVO;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.util.ToolKit;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.msg.MessageStatus;
import nc.vo.pub.msg.MessageTypes;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.msg.MessageinfoVO;

import org.apache.commons.lang.StringUtils;

/**
 * NC5.x消息交换类
 * @author licza
 *
 */
public class NC5xMessageExchange extends AbstractIntergrateNoticeMessageExchage{

	private static final String EMPTY_STR = "";
	private static final String SEMICOLON = ";";
	private static final String ORDER_BY_SENDDATE = " order by senddate desc ";

	@Override
	public String getId() {
		return "nc57";
	}

	@Override
	public void compose(LfwPageContext ctx) {
		
	}

	@Override
	public void delInbox(String[] pks) {
		
	}

	@Override
	public void delSent(String[] sentpks) {
		
	}

	@Override
	public void fwd(LfwPageContext ctx) {
		
	}

	@Override
	public void read(String pk) {
		
	}

	@Override
	public void reply(LfwPageContext ctx) {
		
	}

	@Override
	public void view(String pk, LfwPageContext ctx) {
		ArrayList aryMsgInfo =  qryNcMsg(buildPkFilterSQL(pk));;
		MessageinfoVO mio = (MessageinfoVO) aryMsgInfo.get(0);
		String  sb = buildMessageString(pk, mio);
		ctx.openNewNormalWindow("/portal/pt/nc5x/execNCAppletFunction?systemCode="+getSystemCode()+"&param=" + (sb.toString()), pk, "1", "1", false);
	}

	@Override
	public List<PtMessagecategoryVO> getCategory() {
		PtMessagecategoryVO notice = new PtMessagecategoryVO("nc5xnotice",	MCConstant.TYPE_NOTICE.getSyscode(), "NC5x通知消息", "ncnoticemessage","nc5xnoticemessage");
		PtMessagecategoryVO worklist = new PtMessagecategoryVO("nc5xworklist",	MCConstant.TYPE_TASK.getSyscode(), "NC5x工作任务", "ncnoticemessage","nc5xnoticemessage");
		PtMessagecategoryVO prealert = new PtMessagecategoryVO("nc5xprealert",	MCConstant.TYPE_WARING.getSyscode(), "NC5x预警", "ncnoticemessage","nc5xnoticemessage");
		List<PtMessagecategoryVO> cl = new ArrayList<PtMessagecategoryVO>();
		cl.add(notice);
		cl.add(worklist);
		cl.add(prealert);
		return cl;
	}

	@Override
	public PtMessageVO[] getMessage(String category, int timeSection,
			String[] states, PaginationInfo pi) {
		
		PtCredentialVO credential = getCredentialVO();
		
		StringBuffer wherePartSQL = new StringBuffer();
		/**
		 * 过滤用户
		 */
		wherePartSQL.append(buildUserFilterSQL(credential));
		
		/**
		 * 过滤分类
		 */
		wherePartSQL.append(buildCategoryFilterSQL(category));
		
		/**
		 * 过滤时间
		 */
		wherePartSQL.append(buildTimeFilterSQL(timeSection));
		
		/**
		 * 过滤消息状态
		 */
		wherePartSQL.append(buildStateFilterSQL(states));
		
		String orderByPart = ORDER_BY_SENDDATE;
	
		try {
			Nc5xMessageVO[] msgs = qryMsg4Pagination(pi, wherePartSQL, orderByPart);
			/**
			 * 创建分页条件
			 */
			String paginationSQL = buildPaginationSQL(msgs);
			
			/**
			 * 查询消息信息
			 */
			ArrayList aryMsgInfo = qryNcMsg(paginationSQL);
			
			if(ToolKit.notNull(aryMsgInfo)){
				return transf(category, aryMsgInfo);
			}

		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 将MessageinfoVO集合转换为portal消息数组
	 * @param category
	 * @param aryMsgInfo
	 * @return
	 */
	PtMessageVO[] transf(String category, ArrayList aryMsgInfo) {
		ArrayList aryResult = new ArrayList();
		for (int i = 0; i < aryMsgInfo.size(); i++) {
			MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) aryMsgInfo.get(i));
			aryResult.add(transf(msgvo, category));
		}
		return (PtMessageVO[]) aryResult.toArray(new PtMessageVO[0]);
	}
	
	/**
	 * 为分页查询消息
	 * @param pi
	 * @param wherePartSQL
	 * @param orderByPart
	 * @return
	 * @throws PortalServiceException
	 */
	Nc5xMessageVO[] qryMsg4Pagination(PaginationInfo pi, StringBuffer wherePartSQL, String orderByPart) throws PortalServiceException {
		SSOProviderVO provider = getProvider();
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = provider.getValue(Nc5xIntConstent.NC5XDs);
		InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
		try {
			Nc5xMessageVO[] msgs = CRUDHelper.getCRUDService().queryVOs(new Nc5xMessageVO(), pi, " 1=1 " + wherePartSQL.toString(), null, orderByPart);
			return msgs;
		} catch (Exception e) {
			throw new PortalServiceException(e);
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
	}
	
	/**
	 * 根据消息主键创建查询条件
	 * @param pk
	 * @return
	 */
	String buildPkFilterSQL(String pk) {
		return " and a.pk_messageinfo='" + pk + "' ";
	}
	
	/**
	 * 根据消息状态创建查询条件
	 * @param states
	 * @return
	 */
	String buildStateFilterSQL(String[] states) {
		StringBuffer inSql = new StringBuffer();
		if(ToolKit.notNull(states)){
			inSql.append(" and state in ('");
			inSql.append(StringUtils.join(states, "','"));
			inSql.append("'  ) ");
		}
		return inSql.toString();
	}
	

	
	/**
	 * 加入时间段选择
	 * @param type
	 * @return
	 */
	String buildTimeFilterSQL(Integer type){
		String sql = EMPTY_STR;
		if (type != null) {
			switch (type) {
			case 1:
				sql=(" and senddate > '"+ (new UFDate()).getDateBefore(7) +"' ");
				break;
			case 2:
				sql=(" and senddate > '"+ (new UFDate()).getDateBefore(30) +"' ");
				break;
			case 3:
				sql=(" and senddate > '"+ (new UFDate()).getDateBefore(90) +"' ");
				break;
			default:
				break;
			}
		}
		return sql;
	}
	
	/**
	 * 根据用户信息创建查询条件
	 * @param credential
	 * @return
	 */
	String buildUserFilterSQL(PtCredentialVO credential) {
		if(credential == null)
			return " AND 1 =2 ";
		String receiver = credential.getCredentialReference().getValue("ncuserpk"); 

		return  " AND (checkman='" + receiver + "' ) and (isnull(receivedeleteflag,'N')='N') ";
	}
	
	/**
	 * 	根据消息分类创建查询条件
	 * @param category
	 * @return
	 */
	String buildCategoryFilterSQL(String category){
		if(StringUtils.equals(category, "nc5xnotice")){
			return " and type in (" + MessageTypes.MSG_TYPE_P2P + "," + MessageTypes.MSG_TYPE_PUBLIC	+ ")  and state<>" + MessageStatus.SEALED.getValue() + " ";
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 用于分页的根据消息对象产生查询条件
	 * 
	 * @param msgs
	 * @return SQL like  pk in ('',''....'')
	 */
	String buildPaginationSQL(nc.uap.portal.integrate.othersystem.nc5x.Nc5xMessageVO[] msgs){
		if(ToolKit.notNull(msgs)){
			List<String> pks = new ArrayList<String>();
			for(nc.uap.portal.integrate.othersystem.nc5x.Nc5xMessageVO msg : msgs){
				if(msg != null && ToolKit.notNull(msg.getPrimaryKey()))
					pks.add(msg.getPrimaryKey());
			}
			StringBuffer sb = new StringBuffer();
			sb.append("AND  a.pk_messageinfo in ( '");
			sb.append(StringUtils.join(pks.toArray(new String[0]),"','"));
			sb.append("') ");
			return sb.toString();
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 查询NC消息
	 * @param wherePart
	 * @return
	 */
	ArrayList qryNcMsg(String wherePart){
		final String sqlHead = "select a.pk_messageinfo,a.senderman,b.user_name,a.checkman,a.pk_corp,a.type,a.state,a.url,a.title,a.content,a.senddate,a.priority,a.dealdate,"
			+ "a.billid,a.billno,a.pk_billtype,a.pk_srcbilltype,a.actiontype,a.titlecolor,a.filecontent "
			+ "from pub_messageinfo a left join sm_user b on a.senderman=b.cuserid ";
		String orderByPart = (" order by senddate desc");
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = getProvider().getValue(Nc5xIntConstent.NC5XDs);
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
			ArrayList aryMsgInfo = (ArrayList) CRUDHelper.getCRUDService().query(sqlHead + " where 1=1 " + wherePart, new MessageInfoProcessor());
			return aryMsgInfo;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			return null;
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
	}
	
	
	
	
	
	@Override
	public PtMessageVO getMessage(String pk) {
		MessageQueryParam param = MessageCenter.getMessageQueryParam();
		ArrayList aryMsgInfo = qryNcMsg(buildPkFilterSQL(pk));
		if(!ToolKit.notNull(aryMsgInfo))
			return null;
		MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO((MessageinfoVO) aryMsgInfo.get(0));
		return (transf(msgvo, param.getCategory()));
		 
	}

	@Override
	public Integer getNewMessageCount(String category) {
		PtCredentialVO credential = getCredentialVO();
		
		StringBuffer wherePartSQL = new StringBuffer();
		/**
		 * 过滤用户
		 */
		wherePartSQL.append(buildUserFilterSQL(credential));
		
		/**
		 * 过滤分类
		 */
		wherePartSQL.append(buildCategoryFilterSQL(category));
		
		
		/**
		 * 过滤消息状态
		 */
		wherePartSQL.append(buildStateFilterSQL(new String[]{"0"}));
		
		String countSql = "select count(1) as c from (select * from pub_messageinfo where 1=1 "+ wherePartSQL +") as a";
		String oldDs = LfwRuntimeEnvironment.getDatasource();
		String nc5xDs = getProvider().getValue(Nc5xIntConstent.NC5XDs);
		try {
			InvocationInfoProxy.getInstance().setUserDataSource(nc5xDs);
			Map obj = (Map) CRUDHelper.getCRUDService().query(countSql, new MapProcessor()) ;
			return (Integer)obj.get("c");
		} catch (Throwable e) {
			LfwLogger.error(e.getMessage(),e);
			return 0;
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(oldDs);
		}
	}
	/**
	 * 将NC5x的消息对象转换为Portal消息对象
	 * @param msg
	 * @param syscode
	 * @return
	 */
	PtMessageVO transf(MessageVO msg, String syscode){
		PtMessageVO m = new PtMessageVO();
		m.setPk_message(msg.getPrimaryKey());
		m.setPk_sender(msg.getSenderCode());
		m.setPk_user(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
		m.setState((msg.isCheck() != null && msg.isCheck().booleanValue()) ? "1" : "0");
		m.setSystemcode(syscode);
		m.setPk_user(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
		m.setTitle(msg.getTitle());
		m.doSetContent(msg.getMessageNote());
		m.setSendtime(msg.getSendDateTime());
		m.setUsername(LfwRuntimeEnvironment.getLfwSessionBean().getUser_name()); 
		m.setSendername(msg.getSenderName());
		return m;
	}
	/**
	 * 将消息转换为字符串
	 * @param pk
	 * @param mio
	 * @return
	 */
	String buildMessageString(String pk, MessageinfoVO mio) {
		MessageVO msgvo = MessageinfoVO.transMsgInfoVO2MsgVO(mio);
		StringBuffer sb = new StringBuffer();
		sb.append(pk);
		sb.append(SEMICOLON).append(mio.getPk_billtype());
		sb.append(SEMICOLON).append(mio.getPk_srcbilltype());
		sb.append(SEMICOLON).append(mio.getBillno());
		sb.append(SEMICOLON).append(mio.getBillid());
		sb.append(SEMICOLON).append(EMPTY_STR);
		sb.append(SEMICOLON).append(mio.getPk_corp());
		sb.append(SEMICOLON).append(mio.getActiontype());
		//mio.getSenderman()
		sb.append(SEMICOLON).append(EMPTY_STR);
		//mio.getSendermanName()
		sb.append(SEMICOLON).append(EMPTY_STR);
		//mio.getCheckman()
		sb.append(SEMICOLON).append(EMPTY_STR);
		//mio.getCheckmanName()
		sb.append(SEMICOLON).append(EMPTY_STR);
		//isCheck
		sb.append(SEMICOLON).append(msgvo.isCheck().toString());
		//CheckNote
		sb.append(SEMICOLON).append(msgvo.getCheckNote());
		//SendDateTime
		sb.append(SEMICOLON).append(msgvo.getSendDateTime());
		 //DealDateTime
		sb.append(SEMICOLON).append(msgvo.getDealDateTime());
		//setMessageNote
		sb.append(SEMICOLON).append(EMPTY_STR);
		//URLEncoder.encode(mio.getTitle(), "UTF-8")
		sb.append(SEMICOLON).append(EMPTY_STR);
		//MailAddress
		sb.append(SEMICOLON).append(msgvo.getMailAddress());
		sb.append(SEMICOLON).append(mio.getType());
		sb.append(SEMICOLON).append(mio.getPriority());
		return sb.toString();
	}
	
}

/**
 * 消息SQL处理器
 * @author licza
 *
 */
class MessageInfoProcessor extends BaseProcessor {

	public Object processResultSet(ResultSet rs) throws SQLException {
		if (null == rs) { return null; }
		ArrayList al = new ArrayList();
		while (rs.next()) {
			MessageinfoVO mivo = new MessageinfoVO();
			String key = rs.getString(1);
			mivo.setPrimaryKey(key == null ? null : key.trim());
			String sendman = rs.getString(2);
			mivo.setSenderman(sendman == null ? null : sendman.trim());
			String senderName = rs.getString(3);
			mivo.setSendermanName(senderName == null ? sendman : senderName.trim());
			mivo.setCheckmanName(senderName == null ? null : senderName.trim());
			String checkman = rs.getString(4);
			mivo.setCheckman(checkman == null ? null : checkman.trim());
			String pkcorp = rs.getString(5);
			mivo.setPk_corp(pkcorp == null ? null : pkcorp.trim());
			int type = rs.getInt(6);
			mivo.setType(new Integer(type));
			mivo.setMessagestate(new Integer(rs.getInt(7)));

			String url = rs.getString(8);
			mivo.setUrl(url == null ? null : url.trim());
			String title = rs.getString(9);
			mivo.setTitle(title == null ? null : title.trim());
			String content = rs.getString(10);
			//mivo.setContent(content == null ? null : content.trim());
			mivo.setContent(content == null ? null : content);
			String senddate = rs.getString(11);
			mivo.setSenddate(senddate == null ? null : new UFDateTime(senddate.trim()));
			mivo.setPriority(new Integer(rs.getInt(12)));
			String dealdate = rs.getString(13);
			mivo.setDealdate(dealdate == null ? null : new UFDateTime(dealdate.trim()));

			// lj+2006-6-19
			// billid
			mivo.setBillid(rs.getString(14));
			// billno
			mivo.setBillno(rs.getString(15));
			// pk_billtype
			mivo.setPk_billtype(rs.getString(16));
			// pk_srcbilltype
			mivo.setPk_srcbilltype(rs.getString(17));
			//actiontype
			String actionTypeCode = rs.getString(18);
			mivo.setActiontype(actionTypeCode == null ? null : actionTypeCode.trim());

			// titlecolor
			mivo.setTitlecolor(rs.getString(19));

			// 设置发送人为预警平台时的发送人名
			if (type == MessageTypes.MSG_TYPE_PA && sendman.equals(MessageVO.getPAMutliLangName())) {
				mivo.setSendermanName(MessageVO.getPAMutliLangName());
			}
			//filecontent
			mivo.setFilecontent(rs.getBytes(20));
			/**
			mivo.setNeedFlowCheck("Y".equals(rs.getString(21)));
			mivo.setPk_wf_msg(rs.getString(22));
			mivo.setPk_wf_task(rs.getString(23));
			**/
			al.add(mivo);
		}
		return al;
	}
}
