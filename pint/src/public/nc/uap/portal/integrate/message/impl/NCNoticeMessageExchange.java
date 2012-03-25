package nc.uap.portal.integrate.message.impl;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.MapProcessor;
import nc.message.Attachment;
import nc.message.itf.IMessageQueryService;
import nc.message.itf.IMessageService;
import nc.message.vo.AttachmentSetting;
import nc.message.vo.AttachmentVO;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.crud.itf.ILfwQueryService;
import nc.uap.portal.exception.PortalServerRuntimeException;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.message.MCConstant;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.itf.AbstractIntergrateNoticeMessageExchage;
import nc.uap.portal.integrate.message.vo.PtMessageVO;
import nc.uap.portal.integrate.message.vo.PtMessagecategoryVO;
import nc.uap.portal.integrate.system.PortletRuntimeEnv;
import nc.uap.portal.integrate.system.SSOProviderVO;
import nc.uap.portal.util.PtUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;
/**
 * NC消息交换类
 * </BR>不支持分页
 * @author licza
 *
 */
public class NCNoticeMessageExchange extends AbstractIntergrateNoticeMessageExchage{
	String MSG_BOX_URL = "core/uimeta.um?pageId=ncmessage";

	@Override
	public String getSystemCode() {
		return "NC";
	}
	@Override
	public void view(String pk, LfwPageContext ctx) {
		ctx.downloadFileInIframe("/portal/pt/integr/nodes/forward?systemCode="+getSystemCode()+"&node=0,"+pk); 
		//ctx.openNewWindow("/portal/pt/integr/nodes/forward?systemCode="+getSystemCode()+"&node=0,"+pk, "查看",  "320", "200");
	}
	@Override
	public void compose(LfwPageContext ctx) {
		ctx.openNewWindow(MSG_BOX_URL + "&cmd=compose", "新信息", "660", "560");
	}
	@Override
	public void delSent(String[] sentpks) {
		
	}

	@Override
	public void fwd(LfwPageContext ctx) {
		String pk_message = MessageCenter.getSelectMessagePks(ctx)[0];
		ctx.openNewWindow(MSG_BOX_URL + "&cmd=fwd&pk_message=" + pk_message,
				"转发信息", "660", "560" );

	}
	@Override
	public void execute(String[] pk, String cmd, LfwPageContext ctx) {
		super.execute(pk, cmd, ctx);
		if(StringUtils.equals(cmd, "exec")){
			this.view(pk[0],ctx);
		}
	}
	@Override
	public void read(String pk) {
		if(pk == null)
			return;
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			IMessageService ims = (IMessageService)getLocator().lookup(IMessageService.class.getName());
			IMessageQueryService msq = (IMessageQueryService)getLocator().lookup(IMessageQueryService.class.getName());
			NCMessage msg = msq.queryNCMessageByPk(pk);
			if(msg == null)
				return;
			msg.getMessage().setIsread(UFBoolean.TRUE);
			ims.udpateMessage(new NCMessage[]{msg});
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
	}

	
	
	@Override
	public void reply(LfwPageContext ctx) {
		String pk_message = MessageCenter.getSelectMessagePks(ctx)[0];
		ctx.showModalDialog(
				MSG_BOX_URL + "&cmd=reply&pk_message=" + pk_message, "答复信息",
				"680", "560", "compose", true, true);
	}
	
	@Override
	public void delInbox(String[] pk) {
		if(pk == null)
			return;
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			IMessageService ims = (IMessageService)getLocator().lookup(IMessageService.class.getName());
			IMessageQueryService msq = (IMessageQueryService)getLocator().lookup(IMessageQueryService.class.getName());
			NCMessage[] msgs = msq.queryNCMessages("pk_message in('" + StringUtils.join(pk,"','") + "')");
			if(msgs == null || msgs.length == 0)
				return;
			for(NCMessage msg : msgs){
				msg.getMessage().setIsdelete(UFBoolean.TRUE);
			}
			ims.udpateMessage(msgs);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
	}



	@Override
	public List<PtMessagecategoryVO> getCategory() {
		PtMessagecategoryVO notice = new PtMessagecategoryVO("ncnotice",	MCConstant.TYPE_NOTICE.getSyscode(), "NC通知消息", "ncnoticemessage","ncnoticemessage");
		PtMessagecategoryVO worklist = new PtMessagecategoryVO("worklist",	MCConstant.TYPE_TASK.getSyscode(), "NC工作任务", "ncnoticemessage","ncnoticemessage");
		PtMessagecategoryVO prealert = new PtMessagecategoryVO("prealert",	MCConstant.TYPE_WARING.getSyscode(), "NC预警", "ncnoticemessage","ncnoticemessage");
		List<PtMessagecategoryVO> cl = new ArrayList<PtMessagecategoryVO>();
		cl.add(notice);
		cl.add(worklist);
		cl.add(prealert);
		return cl;
	}

	public PtMessageVO[] getMessage(String syscode, int timeSection,String[] states, PaginationInfo pi) {
		NCMessage[] msgs = getNCMessage(syscode, timeSection, states ,pi);
		if(msgs != null){
			return transf(msgs,syscode);
		}
		return new PtMessageVO[0];
	}
	
	public NCMessage[] getNCMessage(String syscode, int timeSection,String[] states ,PaginationInfo pg){
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			PtCredentialVO credential = getCredentialVO();
			if(credential == null)
				return null;
			if(syscode.equals("ncnotice"))
				syscode = "notice";
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			String receiver = credential.getCredentialReference().getValue("ncuserpk"); 
			IMessageQueryService msq = (IMessageQueryService)getLocator().lookup(IMessageQueryService.class.getName());
			ILfwQueryService qs = getLocator().lookup(ILfwQueryService.class);
			
			StringBuffer whereSB = new StringBuffer("  receiver = '"+ receiver +"' and msgsourcetype = '"+ syscode +"' and destination = 'inbox' ");
			whereSB.append(timeSection(timeSection));
			
			if(states != null){
				List<String> isReadArr = new ArrayList<String>();
				List<String> isDelArr = new ArrayList<String>();
				for(int i = 0;i < states.length ;i++){
					if(StringUtils.equals(states[i], "0")){
						isReadArr.add("N");
					}
					if(StringUtils.equals(states[i], "1")){
						isReadArr.add("Y");
					}
 					if(StringUtils.equals(states[i], "-1")){
 						isDelArr.add("Y");
					}
				}
				if(!isReadArr.isEmpty())
					whereSB.append(" and isread in('"+ StringUtils.join(isReadArr.iterator(),"','") +"') ");
				if(!isDelArr.isEmpty())
					whereSB.append(" and isdelete in('"+ StringUtils.join(isReadArr.iterator(),"','") +"') ");
				else
					whereSB.append(" and isdelete = 'N' and msgtype='nc'"); //modify by licza : add  msgtype='nc'  只查询nc消息
			}
			
			MessageVO[] vos = qs.queryVOs(new MessageVO(), pg,whereSB.toString(), null	, " order by sendtime desc ");
			Map obj = (Map) qs.queryVOs("select count(1) as c from (select pk_message from sm_msg_content where " + whereSB.toString()+") as a",  new MapProcessor());
			if(obj!= null) 
				pg.setRecordsCount((Integer)obj.get("c"));
			List<String> pks = new ArrayList<String>();
			if(vos != null && vos.length > 0){
				for(MessageVO vo : vos){
					pks.add(vo.getPk_message());
				}
				return msq.queryNCMessages(" pk_message in ('"+StringUtils.join(pks.toArray(new String[0]),"','")+"') ");
			} 
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		return null;
	}
	/**
	 * 根据消息附件PK获得消息附件VO
	 * @param pk_attachment
	 * @return
	 */
	public AttachmentVO qryMsgAttachmentByPK(String pk_attachment){
		AttachmentVO av = new AttachmentVO();
		av.setPk_attachment(pk_attachment);
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			 
			ILfwQueryService qs = getLocator().lookup(ILfwQueryService.class);
			AttachmentVO[] avs = qs.queryVOs(av,null,null);
			if(avs != null && avs.length > 0){
				return avs[0];
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		} finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		return av;
	}

	@Override
	public PtMessageVO getMessage(String pk) {
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		NCMessage nm = null;
		String rmtDs = null;
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			IMessageQueryService msq = getLocator().lookup(IMessageQueryService.class);
			nm = msq.queryNCMessageByPk(pk);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		String nccode = null;
		if(nm != null){
			nccode = nm.getMessage().getMsgsourcetype();
			if(StringUtils.equals(nccode, "notice")){
				nccode = "ncnotice";
			}
		}
		StringBuffer content = new StringBuffer();
		content.append(""+nm.getMessage().getContent());
		Attachment[] attachs = nm.getAttachmentSetting().getAttachments();
		if(attachs != null && attachs.length > 0){

			SSOProviderVO provider = getProvider();
			String ncUrl = provider.getValue("runtimeUrl");
			
			content.append("<hr style='height:1px;color:#000000'>");
			content.append("<dl>");
			content.append("<dt><b>此信息包含以下附件:</b></dt>");
			content.append("<dd><ul>");
			for(Attachment attach : attachs){
				
				content.append("<li><a href='").append(ncUrl).append("/service/DownloadServlet?pk_file="+qryMsgAttachmentByPK(attach.getPk_attachment()).getPk_file()+"&dsname=").append(rmtDs)
				.append("&plugin=").append(getId()).append("'>")
				.append(attach.getName()).append("</a></li>");
			}
			content.append("</ul></dd>").append("</dl>");
		}
		
		nm.getMessage().setContent(content.toString());
		return transf(new NCMessage[]{nm},nccode)[0];
	}

	@Override
	public Integer getNewMessageCount(String category) {
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			 
			String receiver = credential.getCredentialReference().getValue("ncuserpk"); 

			String nccode = null;
				if(StringUtils.equals(category, "ncnotice")){
					nccode = "notice";
				}else{
					nccode = category;
				}
			ILfwQueryService qs = getLocator().lookup(ILfwQueryService.class);
			Map obj = (Map) qs.queryVOs("select count(1) as c from (select pk_message from sm_msg_content where   isread = 'N' and  receiver = '"+ receiver +"' and msgsourcetype = '"+nccode+"' and destination = 'inbox'  and isdelete = 'N' and msgtype='nc'  ) as a",  new MapProcessor());
			if(obj!= null) 
				return ((Integer)obj.get("c"));
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		} finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		
	return 0;
	}

	/**
	 * 获得用户
	 * @param spk
	 * @return
	 */
	private Map<String,UserVO> findUser(Set<String> spk){
		Map<String,UserVO> sm = new HashMap<String,UserVO>();
		IUserManageQuery ncuqs = (IUserManageQuery)getLocator().lookup(IUserManageQuery.class.getName());
		try {
			UserVO[] users = ncuqs.findUserByIDs(spk.toArray(new String[0]));
			if(!PtUtil.isNull(users)){
				for(UserVO user : users){
					if(user != null)
						sm.put(user.getCuserid(), user);
				}
			}
		} catch (BusinessException e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return sm;
	}
	/**
	 * 获得集成NC的Locator
	 * @return
	 */
	public NCLocator getLocator(){
		SSOProviderVO provider = getProvider();
		if(provider == null)
			throw new PortalServerRuntimeException("获取集成系统配置信息失败!");
		String ncUrl = provider.getValue("runtimeUrl");
	    return NCLocator.getInstance(PortletRuntimeEnv.getInstance().getNcProperties(ncUrl));
	}
	/**
	 * 将NCMessage转换为Portal消息对象
	 * @param msgs
	 * @return
	 */
	private PtMessageVO[] transf(NCMessage[] msgs,String syscode){
		List<PtMessageVO> pmsgs = new ArrayList<PtMessageVO>();
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		Map<String,UserVO> us = null;
		try {
			PtCredentialVO credential = getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			us = findUser(getUserPKs(msgs));;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
		if(us != null && msgs != null)
			for(NCMessage msg: msgs){
				MessageVO m = msg.getMessage();
				PtMessageVO p = new PtMessageVO();
				p.setPk_message(m.getPk_message());
				p.setPk_sender(m.getSender());
				p.setPriority(m.getPriority() == null ? 0 : m.getPriority().intValue() == 5 ? 0 : 1 );
				p.setState(Integer.valueOf(m.getIsread() == null ? 1 : m.getIsread().booleanValue() ? 1 : 0).toString());
				p.setSystemcode(syscode);
				p.setPk_user(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
				p.setTitle(m.getSubject());
				p.doSetContent(m.getContent());
				p.setSendtime(m.getSendtime());
 				UserVO user = us.get(m.getReceiver());
				if(user != null)
					p.setUsername(user.getUser_name()); 
				user = us.get(m.getSender());
				if(user != null)
					p.setSendername(user.getUser_name());
				else
					p.setSendername(m.getSender());
				pmsgs.add(p);
			}
		return pmsgs.toArray(new PtMessageVO[0]);
	}
	/**
	 * 获得用户PK
	 * @param msgs
	 * @return
	 */
	private Set<String> getUserPKs(NCMessage[] msgs){
		Set<String> spk = new HashSet<String>();
		for(NCMessage msg: msgs){
			MessageVO m = msg.getMessage();
			spk.add(m.getSender());
			spk.add(m.getReceiver());
		}
		return spk;
	}
	/**
	 * 加入时间段选择
	 * @param type
	 * @return
	 */
	private String timeSection(Integer type){
		String sql = "";
		if (type != null) {
			switch (type) {
			case 1:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(7) +"' ");
				break;
			case 2:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(30) +"' ");
				break;
			case 3:
				sql=(" and sendtime > '"+ (new UFDate()).getDateBefore(90) +"' ");
				break;
			default:
				break;
			}
		}
		return sql;
	}

	/**
	 * 发送NC消息
	 * @param msg
	 * @throws Exception
	 */
	public void sendMessage(NCMessage msg) throws Exception{
		String gateUrl = getProvider().getValue("runtimeUrl");
		String currentDS = InvocationInfoProxy.getInstance().getUserDataSource();
		try {
			String sbUrl = gateUrl +"/service/ncmsgservlet";
//			String sbUrl = "http://127.0.0.1/service/ncmsgservlet";
			URL url = new URL(sbUrl.toString());
			URLConnection conn = url.openConnection();
			/* step1 */
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).setChunkedStreamingMode(2048);
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type",
					"application/x-java-serialized-object,charset=utf-8");
			OutputStream out = conn.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			byte[] buf = new byte[2048];
			int len = -1;

			// 写入数据源
			oos.writeObject(currentDS);
			
			InvocationInfoProxy.getInstance().setUserDataSource(LfwRuntimeEnvironment.getDatasource());
			PtCredentialVO  cd = getCredentialVO();
			InvocationInfoProxy.getInstance().setUserDataSource(currentDS);
			// 写入集团id
 			oos.writeObject(cd.getCredentialReference().getValue("ncgrouppk"));
			// 写入用户
			oos.writeObject(cd.getCredentialReference().getValue("ncuserpk"));
			oos.writeObject(null);
			oos.writeObject(msg.getMessage());
			AttachmentSetting attachmentSetting = msg.getAttachmentSetting();
			Attachment[] attachments = attachmentSetting.getAttachments();
			oos.writeInt(attachments.length);
			for (int i = 0; i < attachments.length; i++) {
				long fileLen = attachments[i].getInputStream().available();
				String name = attachments[i].getName();
				oos.writeUTF(name);
				oos.flush();
				oos.writeLong(fileLen);
				oos.flush();
				InputStream in = attachments[i].getInputStream();
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
					out.flush();
				}
				in.close();
				out.flush();
			}
			oos.close();
			ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
			// 读取返回结果
			Object obj = ois.readObject();
			if (obj instanceof Exception) {
				throw (Exception) obj;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}
}
