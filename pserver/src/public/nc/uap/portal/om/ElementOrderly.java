package nc.uap.portal.om;

/**
 * 组件排序接口. Layout Portlet实现此接口.
 * 
 * @author licza
 * 
 */

public interface ElementOrderly extends  Comparable<ElementOrderly>{
	/**
	 * 获得所在的列.
	 * 
	 * @return 所在列.
	 */
	public Integer getColumn();
	public void setColumn(Integer column);
	public String getName()  ;
}
