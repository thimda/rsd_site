package nc.uap.portal.integrate.message.vo;

import org.apache.commons.lang.StringUtils;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * 消息VO
 * 
 * @author licza
 * 
 */
public class PtMessageVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344799316318775907L;

	private String pk_message;
	/** 用户编码 **/
	private String pk_user;
	/** 用户名称 **/
	private String username;
	/** 信息状态 **/
	private String state = "0";
	/** 优先级 **/
	private Integer priority = 0;
	/** 住题 **/
	private String title;
	/** 内容 **/
	private byte[] content;
	/** 发件人编码 **/
	private String pk_sender;
	/** 发件人名称 **/
	private String sendername;
	/** 源系统编码 **/
	private String pk_from;
	/** 系统编码 **/
	private String systemcode;
	/** 发送时间 **/
	private UFDateTime sendtime;
	/** 阅读时间 **/
	private UFDateTime readtime;
	/** 消息类型 **/
	private Integer msgtype;
	/**链接地址**/
	private String targeturl;
	/**所有者状态**/
	private Integer ownerstate;
	
	/** 扩展项 **/
	private String ext0;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private String ext8;
	private String ext9;
	/** 扩展项 **/
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPk_message() {
		return pk_message;
	}

	public void setPk_message(String pk_message) {
		this.pk_message = pk_message;
	}

	public String getPk_user() {
		return pk_user;
	}

	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getContent() {
		return content;
	}

	public void doSetContent(String content) {
		if(content != null)
			this.content = content.getBytes();
	}
	public String doGetContent() {
		return content == null ? StringUtils.EMPTY : new String(content);
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getPk_sender() {
		return pk_sender;
	}

	public void setPk_sender(String pk_sender) {
		this.pk_sender = pk_sender;
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode;
	}

 
	public UFDateTime getSendtime() {
		return sendtime;
	}

	public void setSendtime(UFDateTime sendtime) {
		this.sendtime = sendtime;
	}

	public UFDateTime getReadtime() {
		return readtime;
	}

	public void setReadtime(UFDateTime readtime) {
		this.readtime = readtime;
	}

	public Integer getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(Integer msgtype) {
		this.msgtype = msgtype;
	}

	@Override
	public String getPKFieldName() {
		return "pk_message";
	}

	@Override
	public String getTableName() {
		return "pt_message";
	}

	public String getSendername() {
		return sendername;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getPk_from() {
		return pk_from;
	}

	public void setPk_from(String pk_from) {
		this.pk_from = pk_from;
	}

	public String getTargeturl() {
		return targeturl;
	}

	public void setTargeturl(String targeturl) {
		this.targeturl = targeturl;
	}


	public Integer getOwnerstate() {
		return ownerstate;
	}

	public void setOwnerstate(Integer ownerstate) {
		this.ownerstate = ownerstate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getExt0() {
		return ext0;
	}

	public void setExt0(String ext0) {
		this.ext0 = ext0;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

}
