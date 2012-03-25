package nc.uap.portal.msg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ÏûÏ¢ºÐ
 * 
 * @author licza
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MsgBox", propOrder = { "id", "i18nname", "title", "idx",
		"msgcmd" })
public class MsgBox implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5870403624581393885L;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private String i18nname;
	@XmlAttribute
	private Integer idx;
	@XmlElement
	private List<MsgCmd> msgcmd;

	public List<MsgCmd> getMsgcmd() {
		if (msgcmd == null)
			msgcmd = new ArrayList<MsgCmd>();
		return msgcmd;
	}

	public void setMsgcmd(List<MsgCmd> msgcmd) {
		this.msgcmd = msgcmd;
	}

	public void addMsgCmdList(MsgCmd msgcmd) {
		getMsgcmd().add(msgcmd);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

}
