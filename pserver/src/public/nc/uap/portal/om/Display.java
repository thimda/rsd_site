package nc.uap.portal.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/***
 * Portlet分组显示类
 * @author licza
 *
 */
@XmlRootElement(name = "display")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Display", propOrder = { "category" })
public class Display implements Serializable{
	private static final long serialVersionUID = -8836211104183647254L;
	@XmlElement(name = "category")
	protected List<PortletDisplayCategory> category;

	public List<PortletDisplayCategory> getCategory() {
		if (category == null) {
			category = new ArrayList<PortletDisplayCategory>();
		}
		return category;
	}

	public void setCategory(List<PortletDisplayCategory> category) {
		this.category = category;
	}

	/**
	 * 增加一个Portlet分组类别
	 * @param portletDisplay
	 */
	public void addPortletDisplayList(PortletDisplayCategory portletDisplayCategory) {
		if (category == null) {
			category = new ArrayList<PortletDisplayCategory>();
		}
		this.category.add(portletDisplayCategory);
	}

}
