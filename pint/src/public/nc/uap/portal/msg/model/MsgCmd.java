package nc.uap.portal.msg.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 消息操作命令
 * 
 * @author licza
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MsgCmd", propOrder = { "id", "i18nname", "title" })
public class MsgCmd implements Serializable {

	private static final long serialVersionUID = -977745852378793077L;
	/**
	 * 命令id
	 */
	@XmlAttribute
	String id;
	/**
	 * 命令的中文显示值
	 */
	@XmlAttribute
	String title;
	/**
	 * 命令的国际化显示值
	 */
	@XmlAttribute
	String i18nname;

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

}
