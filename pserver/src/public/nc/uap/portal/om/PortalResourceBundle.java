package nc.uap.portal.om;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Portal资源包
 * @author licza
 *
 */
@XmlRootElement(name = "resource-bundle")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalResourceBundle", propOrder = { "id" , "title" , "i18nname" , "images" , "linkgroup" , "icon"  })

public class PortalResourceBundle {
	
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private String i18nname;
	
	/**
	 * 图片
	 */
	@XmlElement
	private List<String> images;
	
	/**
	 * 链接组
	 */
	@XmlElement
	private List<String> linkgroup;
	
	/**
	 * 图标
	 */
	@XmlElement
	private List<String> icon;
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<String> getLinkgroup() {
		return linkgroup;
	}
	public void setLinkgroup(List<String> linkgroup) {
		this.linkgroup = linkgroup;
	}
	public List<String> getIcon() {
		return icon;
	}
	public void setIcon(List<String> icon) {
		this.icon = icon;
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
}
