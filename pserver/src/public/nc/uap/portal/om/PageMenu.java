package nc.uap.portal.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 页签
 * 
 * @author licza
 * 
 */
public class PageMenu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7095078537822999863L;
	private List<PageMenuItem> simpleItems;
	
	private List<PageMenuItem> indexItems;
	
	
	private Boolean keepState = null;
	
	/**
	 * 获得页签项目
	 * @return
	 */
	public List<PageMenuItem> getItems() {
		Collections.sort(getIndexItems());
		Collections.sort(getSimpleItems());
		List<PageMenuItem> all = new ArrayList<PageMenuItem>(getIndexItems());
		all.addAll(getSimpleItems());
		return all;
	}

	private List<PageMenuItem> getSimpleItems() {
		if (simpleItems == null)
			simpleItems = new ArrayList<PageMenuItem>();
		return simpleItems;
	}
 
	private List<PageMenuItem> getIndexItems() {
		if (indexItems == null)
			indexItems = new ArrayList<PageMenuItem>();
		return indexItems;
	}
	/**
	 * 新增一个项
	 * 
	 * @param item
	 */
	public void addItems(PageMenuItem item) {
		if (item.getKeepstate())
			this.keepState = Boolean.TRUE;
		if(item.getIsdefault())
			getIndexItems().add(item);
		else
			getSimpleItems().add(item);
	}

	/**
	 * 是否保持页面状态
	 * 
	 * <pre>
	 * 至少有一个页面需要保持状态,返回true
	 * </pre>
	 * 
	 * @return
	 */
	public boolean isKeepState() {
		if (keepState != null)
			return keepState;
		if (!getItems().isEmpty())
			for (PageMenuItem cmi : getItems()) {
				if (cmi.getKeepstate())
					return true;
			}
		return false;
	}
}