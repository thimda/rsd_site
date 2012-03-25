package nc.portal.search;

import nc.vo.bdsearch.IndexBasDoc;

public interface SearchResultBuilder {
	public SearchIndexVO Build(IndexBasDoc doc);
}
