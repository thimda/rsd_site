package nc.portal.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import nc.uap.lfw.core.exception.LfwBusinessException;

public class SearchParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2586631096250444601L;
	public static final String QUERY = "q"; // 查询参数此项
	public static final String RETURN = "fl"; // 返回字段指定参数
	public static final String START = "start";
	public static final String Page = "page";
	public static final String ROWS = "rows";
	public static final String FILTER_QUERY = "fq";
	public static final String SORT = "sort"; // 排序参数
	public static final String FACET = "facet";
	public static final String FACETFIELD = "facet.field";

	public static final char SPLITC = ':';

	/**
	 * 查询字符串，此项必须 格式：fieldName:queryValue 支持 多字段查询 fieldName1:queryValue1
	 * [fieldName2:queryValue2] 支持 字段多值查询 fieldName:queryValue1 [queryValue2]
	 * 支持范围查询：fieldName:[0 TO *] 支持模糊查询：roam~ "jakerta apache"~ 支持通配符查询：单字符：
	 * te?t,多字符：test*，te*t 注意:字段间空格默认条件为 OR，如需AND 则需要明确写出 如
	 * fieldName1:queryValue1 AND fieldName2:queryValue2
	 */
	private String queryString;
	/**
	 * 查询结果过滤条件 格式：格式同queryString
	 */
	private String filterQuery;
	/**
	 * 指定需要返回的字段 格式：field[,fiedl2,field]
	 */
	private String returnFields;
	/**
	 * 返回第一条记录在完成找到结果中的偏移量，0开始，分页使用
	 * 
	 */
	private Long start;
	/**
	 * 返回结果最多有多少条记录，配合start来实现分页,默认为10
	 */
	private Long rows = (long)10;
	
	/**
	 * 表示需要查询的页码
	 */
	private Long pageNum;
	
	/**
	 * 排序的字段 格式： fieldName [desc|asc][,fieldName [desc|asc]]
	 */
	private String sortFields;

	/**
	 * 切面参数 field[,fiedl2,field]
	 */
	private String facedFields;// 

	private String userPermissions; // 控制用户的权限

	private String category; // 控制内容的分类

	/**
	 * 将参数的字段转换成map
	 * 
	 * @return
	 * @throws LfwBusinessException
	 */
	public Map toMap() throws LfwBusinessException {
		this.formatQueryString();
		
		Map map = new HashMap();		
		this.setToMap(map, QUERY, queryString, true);
		this.setToMap(map, FILTER_QUERY, filterQuery, false);		
		this.setToMap(map, SORT, sortFields, false);
		this.setToMap(map, Page, pageNum+"", false);
		this.setToMap(map, ROWS, rows+"", false);
		
		return map;
	}

	/**
	 * 将字段值添加到map中
	 * 
	 * @param map
	 *            存储参数的map
	 * @param key
	 *            取自上面定义的静态常量
	 * @param value
	 *            取自字段的值
	 * @param isRequired
	 *            // 该字段是否是必须的
	 * @throws LfwBusinessException
	 */
	private void setToMap(Map map, String key, String value,boolean isRequired) throws LfwBusinessException {
		if (value != null && !value.equals("")) {
			map.put(key, value);
		} else {
			if (isRequired) {
				throw new LfwBusinessException(key+ " can't be null or blank!");
			}
		}
	}

	/**
	 * 设置用户的权限
	 * @throws LfwBusinessException 
	 */
	public void setUserPermissions(String[] userPermissions) {
		StringBuffer sb = new StringBuffer("");
		if (userPermissions.length == 1) {
			sb.append(SearchIndexVO.ROLES).append(SPLITC).append(userPermissions[0]);
		}
		if (userPermissions.length > 1) {
			for (int i = 0; i < userPermissions.length; i++) {
				sb.append(SearchIndexVO.ROLES).append(SPLITC).append(userPermissions[i]);
				if (i < userPermissions.length - 1) {
					sb.append(" OR ");
				}
			}
		}
		
		if(sb.length()>0)
			this.userPermissions = "(" + sb.append(" OR ").append(SearchIndexVO.ROLES).append(SPLITC).append(SearchIndexVO.PUBLIC_ROLE).toString() + ")";
		else 
			this.userPermissions = "";

	}

	/**
	 * 格式化查询字符串，带上权限 和 分类
	 * @throws LfwBusinessException 
	 */
	private void formatQueryString() throws LfwBusinessException {
		if(queryString == null || queryString.trim().equals("")){
			throw new LfwBusinessException("queryString can't be null");
		}
		
		//queryString = (new StringBuffer()).append("(").append(queryString).append(")").toString();
		
		
//		// 用户权限添加
//		if(userPermissions!=null && !userPermissions.trim().equals("")){ // 进行权限控制
//			queryString = (new StringBuffer()).append(queryString).append(" AND ").append(userPermissions).toString();
//		}
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
	}

	public void setReturnFields(String returnFields) {
		this.returnFields = returnFields;
	}

	public void setRows(Long rows) {
		this.rows = rows;
		this.setStart();
	}
	
	public void setPageNum(Long pageNum) {
		this.pageNum = pageNum;
		this.setStart();
	}
	
	private void setStart(){
		if(pageNum!=null && rows!=null){
			this.start = (pageNum-1)*this.rows;
		}else{
			start = null;
		}
	}
	public void setSortFields(String sortFields) {
		this.sortFields = sortFields;
	}

	public void setFacedFields(String facedFields) {
		this.facedFields = facedFields;
	}

	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getFilterQuery() {
		return filterQuery;
	}

	public String getReturnFields() {
		return returnFields;
	}

	public Long getStart() {
		return start;
	}

	public Long getRows() {
		return rows;
	}

	public String getSortFields() {
		return sortFields;
	}

	public String getFacedFields() {
		return facedFields;
	}

	public String getUserPermissions() {
		return userPermissions;
	}
	public String getCategory() {
		return category;
	}
}
