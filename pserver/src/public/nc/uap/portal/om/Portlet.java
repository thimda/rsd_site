package nc.uap.portal.om;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Portlet.
 * 
 * @author licza.
 * 
 */
public class Portlet implements ElementOrderly, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5283899938260883683L;
	/** PortletID.*/
	public String id;
	/**portlet����*/
	public String name;
	/**��ʽ*/
	public String theme;
	/**����*/
	public String title;
	/**������*/
	public Integer column = 0;

	/**���*/
	private Boolean maxable;
	/**��С��*/
	private Boolean minable;
	/**����*/
	private Boolean normal;

	/**��Ⱦͷ�� */
	private Boolean drawhead;
	/**�Ϸ�*/
	private Boolean draggable;
	
	
	public Boolean getDraggable() {
		return draggable;
	}

	public void setDraggable(Boolean draggable) {
		this.draggable = draggable;
	}

	
	/**�ر�*/
	private Boolean closeable;

	//	/**�鿴*/
	//	private Boolean view;
	//	/**�༭*/
	//	private Boolean edit;
	//	/**����*/
	//	private Boolean help;

	public Boolean getMaxable() {
		return maxable;
	}

	public void setMaxable(Boolean maxable) {
		this.maxable = maxable;
	}

	public Boolean getMinable() {
		return minable;
	}

	public void setMinable(Boolean minable) {
		this.minable = minable;
	}

	public Boolean getNormal() {
		return normal;
	}

	public void setNormal(Boolean normal) {
		this.normal = normal;
	}

	public Boolean getDrawhead() {
		return drawhead;
	}

	public void setDrawhead(Boolean drawhead) {
		this.drawhead = drawhead;
	}

	public Boolean getCloseable() {
		return closeable;
	}

	public void setCloseable(Boolean closeable) {
		this.closeable = closeable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	/**
	 * ���ɿɹ�FreeMarker���ݵĶ���
	 * @return 
	 */
	public Map<String, Object> getSummary() {
		Map<String, Object> summary = new HashMap<String, Object>();
		summary.put("name", getName());
		summary.put("theme", getTheme());
		summary.put("id", getId());
		summary.put("title", getTitle());
		summary.put("column", getColumn());
		summary.put("maxable", getMaxable());
		summary.put("normal", getNormal());
		summary.put("drawhead", getDrawhead());
		summary.put("draggable", getDraggable());
		return summary;
	}

	/**
	 * ����
	 */
	public int compareTo(ElementOrderly o) {
		return getColumn().compareTo(o.getColumn());
	}

	public String toString() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
}