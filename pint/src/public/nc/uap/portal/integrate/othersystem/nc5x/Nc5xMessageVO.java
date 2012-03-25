package nc.uap.portal.integrate.othersystem.nc5x;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * NC5x消息的代理
 * 
 * @author licza
 * 
 */
public class Nc5xMessageVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pk_messageinfo;
	private String pk_corp;
	private String checkman;
	private String type;
	private String messagestate;
	private String receivedeleteflag;
	
	private UFDateTime senddate;
	@Override
	public String getPKFieldName() {
		return "pk_messageinfo";
	}
	@Override
	public String getTableName() {
		return "pub_messageinfo";
	}
	public String getPk_messageinfo() {
		return pk_messageinfo;
	}
	public void setPk_messageinfo(String pk_messageinfo) {
		this.pk_messageinfo = pk_messageinfo;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public String getCheckman() {
		return checkman;
	}
	public void setCheckman(String checkman) {
		this.checkman = checkman;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessagestate() {
		return messagestate;
	}
	public void setMessagestate(String messagestate) {
		this.messagestate = messagestate;
	}
	public String getReceivedeleteflag() {
		return receivedeleteflag;
	}
	public void setReceivedeleteflag(String receivedeleteflag) {
		this.receivedeleteflag = receivedeleteflag;
	}
	public UFDateTime getSenddate() {
		return senddate;
	}
	public void setSenddate(UFDateTime senddate) {
		this.senddate = senddate;
	}
	
}
