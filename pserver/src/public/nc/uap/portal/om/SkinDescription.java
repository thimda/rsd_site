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
 * ∆§∑Ù√Ë ˆ
 * 
 * @author dingrf
 * 
 */
@XmlRootElement(name = "description")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkinDescription", propOrder = { "skin" })
public class SkinDescription implements Serializable{

	private static final long serialVersionUID = 871895269237796111L;
	/**
	 * skin
	 */
	@XmlElement(name = "skin")
	protected List<Skin> skin;

	public List<Skin> getSkin() {
		if (skin == null){
			skin = new ArrayList<Skin>();
		}
		return skin;
	}

	public void setSkin(List<Skin> skin) {
		this.skin = skin;
	}

}
