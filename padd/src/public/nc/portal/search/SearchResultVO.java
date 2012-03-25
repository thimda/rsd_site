package nc.portal.search;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author renxh
 * 查询结果的VO
 */
public class SearchResultVO implements Serializable{
	
	private static final long serialVersionUID = 5275897133690869408L;
	private long totalPage = 0; // 一共有多少页
	private long currentPage = 0; // 当前是第几页
	private long numFound = 0; //一共查询了多少条数据
	private Map groupQuery; // 分组结果
	private double qTime; // 查询时间 单位为 秒
	
	private List<SearchIndexVO> list;  // 查询结果的list
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(final long totalPage) {
		this.totalPage = totalPage;
	}
	public long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(final long currentPage) {
		this.currentPage = currentPage;
	}
	public List<SearchIndexVO> getList() {
		return list;
	}
	public void setList(final List<SearchIndexVO> list) {
		this.list = list;
	}
	public long getNumFound() {
		return numFound;
	}
	public void setNumFound(final long numFound) {
		this.numFound = numFound;
	}
	public Map getGroupQuery() {
		return groupQuery;
	}
	public void setGroupQuery(final Map groupQuery) {
		this.groupQuery = groupQuery;
	}
	public double getQTime() {
		return qTime;
	}
	public void setQTime(final double time) {
		qTime = time;
	}
	
	
	
	
	

}
