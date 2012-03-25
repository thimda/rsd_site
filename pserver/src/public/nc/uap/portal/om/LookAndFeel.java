package nc.uap.portal.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Portal主题集合
 * 
 * @author licza
 * 
 */
@XmlRootElement(name = "look-and-feel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LookAndFeel", propOrder = { "theme" })
public class LookAndFeel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4284297066904291848L;
	/** 主题 **/
	@XmlElement(name = "theme")
	protected List<Theme> theme;

	public List<Theme> getTheme() {
		return theme;
	}

	public void setTheme(List<Theme> theme) {
		this.theme = theme;
	}

	/**
	 * 增加一个Theme
	 * 
	 * @param newTheme
	 */
	public void addTheme(Theme newTheme) {
		if (theme == null) {
			theme = new ArrayList<Theme>();
		}
		theme.add(newTheme);
	}
}
