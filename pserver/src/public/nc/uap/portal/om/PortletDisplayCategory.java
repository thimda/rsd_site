package nc.uap.portal.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import nc.uap.portal.vo.PtDisplaycateVO;

/**
 * Portlet小组类别
 * @author licza
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortletDisplayCategory", propOrder = { "id", "text", "i18nName", "portletDisplayList" })
public class PortletDisplayCategory implements Serializable{

	private static final long serialVersionUID = -6149981116086880339L;
	
	@XmlAttribute(name = "id")
	protected String id;
	@XmlAttribute(name = "text")
	protected String text;
	@XmlAttribute(name = "i18nName")
	protected String i18nName;
	@XmlElement(name = "portlet")
	protected List<PortletDisplay> portletDisplayList;

	public List<PortletDisplay> getPortletDisplayList() {
		if (portletDisplayList == null) {
			portletDisplayList = new ArrayList<PortletDisplay>();
		}
		return portletDisplayList;
	}

	public void setPortletDisplayList(List<PortletDisplay> portletDisplayList) {
		this.portletDisplayList = portletDisplayList;
	}

	/**
	 * 增加一个Portlet小组
	 * @param portletDisplay
	 */
	public void addPortletDisplayList(PortletDisplay portletDisplay) {
		if (portletDisplayList == null) {
			portletDisplayList = new ArrayList<PortletDisplay>();
		}
		this.portletDisplayList.add(portletDisplay);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}
	public PortletDisplayCategory(){
		
	}
	public PortletDisplayCategory(PtDisplaycateVO vo) {
		super();
		this.id = vo.getId();
		this.text = vo.getTitle();
		this.i18nName = vo.getI18nname();
	}

}
