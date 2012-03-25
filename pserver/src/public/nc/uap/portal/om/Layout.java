package nc.uap.portal.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * PortalPage布局.
 * 
 * @author licza
 * 
 */
public class Layout implements ElementOrderly,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 97143282043499971L;

	/**
	 * Layout编码.
	 */
	public String id;

	/**
	 * Layout名称.
	 */
	public String name;
	/**
	 * Layout 宽度 格式：百分比 以","隔开。如:33%,66% .
	 */
	public String sizes;

	/**
	 * 布局下的子元素 可以是布局或者portlet.
	 */
	public List<ElementOrderly> child;

	public List<ElementOrderly> getChild() {
		return child;
	}

	public void setChild(List<ElementOrderly> child) {
		Collections.sort(child);
		this.child = child;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Integer column = 0;

	public Layout() {
		child = new ArrayList<ElementOrderly>();

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

	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	public void addChild(ElementOrderly child) {
		this.child.add(child);
	}

	/**
	 * 排序 实现了Comparable接口
	 */
	@Override
	public int compareTo(ElementOrderly o) {
		return getColumn().compareTo(o.getColumn());
	}

	public String toString(){
		return this.getClass().getSimpleName().toLowerCase();
	}
	/**
	 * 生成可供FreeMarker传递的对象
	 * @return 
	 */
	public Map<String, Object> getSummary() {
		Map<String, Object> summary = new HashMap<String, Object>();
		summary.put("name", getName());
		summary.put("sizes", getSizes());
		summary.put("id", getId());
		return summary;
	}
}
