package nc.uap.portal.om;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nc.uap.portal.vo.PtDisplayVO;

/**
 * PortletÐ¡×é
 * @author licza
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortletDisplay", propOrder = { "id", "dynamic" ,"module","title"})
public class PortletDisplay implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6798017021276934983L;
	@XmlAttribute(name = "id")
	@XmlJavaTypeAdapter(value = CollapsedStringAdapter.class)
	protected String id;
	@XmlAttribute(name = "dynamic")
	protected Boolean dynamic;
	@XmlAttribute
	protected String title;
	@XmlAttribute
	protected String module;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getDynamic() {
		return dynamic;
	}

	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public PortletDisplay() {
		
	}
	public PortletDisplay(PtDisplayVO display) {
		super();
		this.dynamic = (display.getDynamic().booleanValue());
		this.id = (display.getId());
		this.module = (display.getModule());
		this.title = (display.getTitle());
	}
	
}