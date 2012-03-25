package nc.portal.search;

import java.net.MalformedURLException;
import java.util.List;

import nc.uap.lfw.core.exception.LfwBusinessException;

/**
 * @author renxh
 * 
 */
public interface ILfwIndexService{	
	
	/**
	 * 根据参数对索引进行查询
	 * @param params
	 * @return
	 * @throws LfwBusinessException
	 */
	
	public SearchResultVO search(SearchParams searchParams) throws LfwBusinessException;	
	
}
