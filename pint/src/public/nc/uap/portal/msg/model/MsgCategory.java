package nc.uap.portal.msg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 消息分类
 * 
 * @author licza
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MsgCategory", propOrder = { "id", "i18nname", "title",
		"pluginid", "msgbox", "child" })
public class MsgCategory implements Serializable {

	public static final String ID = "id";
	/**
	 * 父id标记
	 */
	public static final String PARENTID = "parentid";

	public static final String TITLE = "title";
	public static final String I18NNAME = "i18nname";
	public static final String PLUGINID = "pluginid";
	public static final String MSGBOX = "msgbox";
	public static final String CHILD = "child";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5735457420339212325L;
	/**
	 * 分类ID
	 */
	@XmlAttribute
	private String id;
	/**
	 * 分类中文显示值
	 */
	@XmlAttribute
	private String title;
	/**
	 * 分类国际化显示值
	 */
	@XmlAttribute
	private String i18nname;
	/**
	 * 插件id
	 */
	@XmlAttribute
	private String pluginid;

	/**
	 * 消息盒
	 */
	@XmlElement
	private List<MsgBox> msgbox;
	/**
	 * 子分类
	 */
	@XmlElement
	private List<MsgCategory> child;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public List<MsgCategory> getChild() {
		if (child == null)
			child = new ArrayList<MsgCategory>();
		return child;
	}

	public void setChild(List<MsgCategory> child) {
		this.child = child;
	}

	public void addMsgCategoryList(MsgCategory msgCategory) {
		getChild().add(msgCategory);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MsgBox> getMsgbox() {
		if (msgbox == null)
			msgbox = new ArrayList<MsgBox>();

		return msgbox;
	}

	public void setMsgbox(List<MsgBox> msgbox) {
		this.msgbox = msgbox;
	}

	public void addMsgbox(MsgBox e) {
		getMsgbox().add(e);
	}

	public String getPluginid() {
		return pluginid;
	}

	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}

}
