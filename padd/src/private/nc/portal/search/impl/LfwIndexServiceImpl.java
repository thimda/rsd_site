package nc.portal.search.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.portal.search.ILfwIndexService;
import nc.portal.search.SearchIndexVO;
import nc.portal.search.SearchParams;
import nc.portal.search.SearchResultBuilder;
import nc.portal.search.SearchResultVO;
import nc.pubitf.bdsearch.IBDSearcher;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.vo.bdsearch.IndexBasDoc;
import nc.vo.pub.BusinessException;


public class LfwIndexServiceImpl implements ILfwIndexService {



	public SearchResultVO search(SearchParams searchParams) throws LfwBusinessException {
		return search(searchParams.toMap());
	}

	public SearchResultVO search(Map params) throws LfwBusinessException {
		
		SearchResultVO resultvo = new SearchResultVO();
		IBDSearcher searcher = NCLocator.getInstance().lookup(IBDSearcher.class);
		if (searcher == null) {
			throw new LfwBusinessException("无法加载搜索服务");
		}
		try{
		String docname = "bi_pivot_search";
		int page = Integer.parseInt(params.get(SearchParams.Page).toString());
		int rows = Integer.parseInt( params.get(SearchParams.ROWS).toString());
		String query = params.get(SearchParams.QUERY).toString();
		Date begintime = new Date();
		List<IndexBasDoc> docs =   searcher.search(docname, query, page, rows);
		//List<IndexBasDoc> docs =   searcher.search(docname, query);
		
		int numFound = searcher.getCount(docname, query);
		int totalPage = (numFound % rows) == 0 ? (numFound / rows) : (numFound / rows + 1);		
		int currentPage = page + 1; 
		Date endtime = new Date();
		double qtime = ((double)(endtime.getTime() - begintime.getTime())) /1000.0;
		resultvo.setQTime(qtime);
		resultvo.setNumFound(numFound);
		resultvo.setTotalPage(totalPage);
		resultvo.setCurrentPage(currentPage);
		List<SearchIndexVO> serachindeies  =  new ArrayList<SearchIndexVO>(); 
		List<PtExtension> extens =  PluginManager.newIns().getExtensions("SearchExtension");
		PtExtension curextent = null;
		if(extens != null)
				for(PtExtension ext : extens){
					if(ext.getId().equals(docname)){
						curextent = ext;
						break;
					}
				}
		if(curextent != null && docs != null){
			SearchResultBuilder service =curextent.newInstance(SearchResultBuilder.class);
			for(IndexBasDoc doc : docs){
				SearchIndexVO vo = service.Build(doc);
				serachindeies.add(vo);			
			}
		}
		resultvo.setList(serachindeies);
		return resultvo;
		}
		catch(Exception ex){
			LfwLogger.error(ex);
			throw new LfwBusinessException(ex);			
		}
	}


}
