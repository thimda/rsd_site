package nc.portal.action;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import nc.portal.search.ILfwIndexService;
import nc.portal.search.IndexImplHelper;
import nc.portal.search.SearchParams;
import nc.portal.search.SearchResultVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;

import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.lfw.util.StringUtil;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

/**
 * @author renxh
 * 全文索引的查询action
 */
@Servlet(path = "/search")
public class SearchAction extends BaseAction {

	private long newPageTurnNum = 0; // 默认翻页的页数
	private long rowsNum = 10; // 默认的最大条数
	

	/**
	 * 查询主方法
	 * @throws MalformedURLException 
	 * @throws LfwBusinessException
	 */
	@Action
	public void doSearch() throws PortalServiceException {		
		ILfwIndexService indexService;
		try {
			indexService = IndexImplHelper.getIndexServiceforPortal();
		} catch (MalformedURLException e2) {
				LfwLogger.error(e2);
				throw new PortalServiceException(e2);
		}
		long currows = rowsNum;
		if(LfwRuntimeEnvironment.getBrowserInfo().isIpad())
			currows = 5;
		long currentPage = request.getParameter("currentPage") == null ? newPageTurnNum : Long.parseLong(request.getParameter("currentPage"));
		currentPage -= 1;
		// 获得当前页码，如果是next则，当前页码加1，如果是front，则减1，
		// 如果是first表是第一个，last表示最后一个
		// 如果是数字则表示页码
		// 如果是null，表示初次进入 默认为0
		String pageTurn = request.getParameter("pageTurn"); // 页码数，第一次为null
		newPageTurnNum = this.getNextPage(currentPage, pageTurn);

		// 如何获得最大页码数？
		String queryString = this.transCode(request.getParameter("queryString"), "iso-8859-1", "UTF-8"); // 查询条件
		
		String pagename = request.getParameter("pageName");
		if(null == pagename)
			pagename = "";
		String pagemodule = request.getParameter("pageModule");
		if(null == pagemodule)
			pagemodule = "";
		
		SearchResultVO result = null;
		if(queryString == null || queryString.equals("") || queryString.trim().equals("")){
			result = new SearchResultVO();
		}
		else{
	//		将所有这些符号"+ - && || ! ( ) { } [ ] ^ " ~ * ? : \"转义
			queryString = queryString.replaceAll("([\\+\\-\\!\\(\\)\\{\\}\\[\\]\\\\\\\"\\*\\?~\\^\\:\\&\\|])", "\\\\$1");
			
			SearchParams params = new SearchParams();			
			params.setPageNum(newPageTurnNum);//设置新的页数
			params.setRows(currows); // 设置每页的条数
			params.setFacedFields("category"); // 设置切面
			params.setQueryString(queryString);			
			
			try {
				result = indexService.search(params);							
			} catch (LfwBusinessException e1) {
				LfwLogger.error(e1.getMessage());
				alert("搜索过程发生异常!");
				return;
			}
			//result = new SearchResultVO();
		}
		
		String ftlName = "nc/portal/ftl/portalsearch.ftl";	
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("currentPage", result.getCurrentPage());
		root.put("totalPage", result.getTotalPage());
		root.put("indexVOList", result.getList());		
		root.put("numFound", result.getNumFound());
		root.put("queryTime", result.getQTime());
		root.put("queryString", queryString);
		root.put("ispad", LfwRuntimeEnvironment.getBrowserInfo().isIpad());
		root.put("ispage", true);
		
		root.put("CUR_PAGE_NAME", pagename);
		root.put("CUR_PPAGE_MODULE", pagemodule);
		
		print(FreeMarkerTools.contextTemplatRender(ftlName, root));
	}

	/**
	 * 进行值转换，将String转为int类型,转型不成功则用默认值
	 * @param rawValue 需要转换的值
	 * @param defValue 默认值
	 * @return
	 */
	public int transToNewValue(String rawValue, int defValue) {
		if (rawValue != null && rawValue.trim().matches("[0-9]+")) {
			return Integer.parseInt(rawValue);
		}
		return defValue;
	}

	/**
	 * 获得下一个翻页的页码
	 * @param currentPage
	 * @param pageTurn
	 * @return
	 */
	public long getNextPage(long currentPage, String pageTurn) {
		if (pageTurn != null) {
			if (pageTurn.equals("first")) {// 首页
				newPageTurnNum = 1;
			} else if (pageTurn.equals("next")) { // 上一页
				newPageTurnNum = currentPage + 1;
			} else if (pageTurn.equals("front")) { // 下一页
				newPageTurnNum = currentPage - 1;
			} else if (pageTurn.matches("[1-9][0-9]*")) { // 具体的某一页
				newPageTurnNum = Integer.parseInt(pageTurn) -1;
			}
		}
		return newPageTurnNum;
	}
	
	/**
	 * 进行字符编码的转换
	 * @param str
	 * @param srcCharSet
	 * @param destCharSet
	 * @return
	 */
	public String transCode(String str,String srcCharSet,String destCharSet){
		if(str == null || str== "")
			return "";
		try {
			return new String(str.getBytes(srcCharSet),destCharSet);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

}
