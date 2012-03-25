package nc.uap.portal.om;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.PmlUtil;
import nc.vo.jcom.xml.XMLUtil;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

 
/**
 * portal页面操作.
 * <pre>功能包含XML转换及Portal的子元素拖放</pre>
 * @author licza
 * 
 */
public class Page extends PageBase implements Comparable<Page>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7279112002831286083L;
	
	/**Portlet坐标系**/
	private Map<String, List<Integer>> portletTree;
	/**Layout坐标系**/
	private Map<String, List<Integer>> layoutTree;

	/**
	 * 查找Layout
	 * @param layoutId
	 * @return
	 */
	private Layout findLayout(String layoutId) {
		List<Integer> layoutPlace = getLayoutTree(layoutId);
		return findLayout(layoutPlace);
	}

	/**
	 * 根据Portlet名获得Portlet
	 * @param name
	 * @return
	 */
	public Portlet getPortlet(String name){
		return getPortlet(name, getLayout());
	}
	/**
	 * 获得布局下的Portlet
	 * @param name
	 * @param layout
	 * @return
	 */
	private Portlet getPortlet(String name,Layout layout){
		List<ElementOrderly>  cds = layout.getChild();
		if(cds == null)
			return null;
		for(ElementOrderly eo : cds){
			if(eo instanceof Portlet && name.equals(eo.getName()))
				return (Portlet) eo;
			else if(eo instanceof Layout){
				Portlet let = getPortlet(name, (Layout) eo);
				if (let != null) 
					return let;
			}
		}
		return null;
	}
	/**
	 * 查找Layout
	 * @param layoutPlace Layout坐标系
	 * @return
	 */
	private Layout findLayout(List<Integer> layoutPlace) {
		Layout lay = null;
		for (Integer i : layoutPlace) {
			if (lay == null) {
				lay = (Layout) getLayout().getChild().get(i);
			} else {
				lay = (Layout) lay.getChild().get(i);
			}
		}
		return lay;
	}

	/**
	 * 查找Portlet
	 * <pre>注意:使用此方法必须先调用plantCoordTrees()以建立坐标系</pre>
	 * @param portletId
	 * @return
	 */
	private Portlet findPortlet(String portletId) {
		List<Integer> portletPlace = getPortletTree(portletId);
		Integer portletIndex = portletPlace.get(portletPlace.size() - 1);
		portletPlace.remove(portletPlace.size() - 1);
		Layout lay=findLayout(portletPlace);
		if(lay==null){
			throw new IllegalStateException("当前布局不支持拖放!");
		}
		return (Portlet) lay.getChild().get(portletIndex);
	}

	/**
	 * 删除一个Portlet
	 * @param portletId
	 */
	public void removePortletElement(String portletId) {
		List<Integer> portletPlace = getPortletTree(portletId);
		Integer portletIndex = portletPlace.get(portletPlace.size() - 1);
		portletPlace.remove(portletPlace.size() - 1);
		Layout lay = findLayout(portletPlace);
		lay.setChild(removeChild(lay, portletIndex));
		layoutReSet(lay, portletPlace);
	}

	/**
	 * 递归设置父Layout
	 * @param lay
	 * @param portletPlace lay的父级Layout坐标系
	 */
	private void layoutReSet(Layout lay, List<Integer> portletPlace) {
		Layout layout = lay;
		for (int j = portletPlace.size() - 1; j > 0; j--) {
			Layout _lay = findLayout(portletPlace.subList(0, j));
			int jx = portletPlace.get(j);
			_lay.child.set(jx, layout);
			layout = _lay;
		}
		Layout mainlay = getLayout();
		mainlay.child.set(portletPlace.get(0), layout);
		setLayout(mainlay);
	}

	/**
	 * 强制删除Layout中的Child
	 * @param lay
	 * @param portletIndex
	 * @return
	 */
	private List<ElementOrderly> removeChild(Layout lay, Integer portletIndex) {
		List<ElementOrderly> layChild = new ArrayList<ElementOrderly>();
		for (int ci = 0; ci < lay.getChild().size(); ci++) {
			if (ci != portletIndex) {
				ElementOrderly eo = lay.getChild().get(ci);
				layChild.add(eo);
			}
		}
		return layChild;
	}

	/**
	 *  将Portlet插入空Layout
	 * @param portlet
	 * @param layoutId
	 * @throws PortalServiceException 
	 */
	public void addPortletToBlankLayout(String portletId, String layoutId)   {
		Layout lay = findLayout(layoutId);
		
		/***
		不应该抛异常  可能Layout里面是没有Portlet了  但是有一个Layout
		if (lay.getChild() != null) {
			Logger.debug("layout:" + layoutId + "Not Null");
			throw new PortalServiceException("layout:" + layoutId + "不是一个空元素");
		}
		 */
		
		Portlet portlet = cutPortlet(portletId);
		
		/**
		 * 获得Layout坐标系
		 */
		List<Integer> layoutPlace = getLayoutTree(layoutId);
		List<ElementOrderly> layChild = new ArrayList<ElementOrderly>();
		layChild.add(portlet);
		lay.setChild(layChild);
		layoutReSet(lay, layoutPlace);
	}

	/**
	 * 在目标元素前插入Portlet
	 * @param portletId
	 * @param targetId
	 * @throws PortalServiceException
	 */
	public void addPortletBeforeElement(String portletId, String targetId) throws PortalServiceException {
		movePortlet(portletId, targetId, false);
	}

	/**
	 * 在目标元素后插入Portlet
	 * @param portletId
	 * @param targetId
	 * @throws PortalServiceException
	 */
	public void addPortletAfterElement(String portletId, String targetId) throws PortalServiceException {
		movePortlet(portletId, targetId, true);
	}

	/**
	 * 在目标周围插入Portlet
	 * @param portletId
	 * @param destinationId
	 * @param isAfter
	 * @throws PortalServiceException
	 */
	public void movePortlet(String portletId, String destinationId, Boolean isAfter)   {
		if (destinationId.equals(portletId) || getPortletTree(portletId) == null || getPortletTree(destinationId) == null) {
			Logger.error("portletId:" + portletId + " targetId:" + destinationId + "Not Found");
			return;
		}
		Portlet portlet = cutPortlet(portletId);
		List<Integer> portletPlace = new ArrayList<Integer>(getPortletTree(destinationId));
		/**
		 * 获取目标坐标
		 */
		Integer portletIndex = portletPlace.get(portletPlace.size() - 1);
		/**
		 * 获取父Layout坐标
		 */
		portletPlace.remove(portletPlace.size() - 1);
		/**
		 * 获得父Layout
		 */
		Layout lay = findLayout(portletPlace);
		/**
		 * 拷贝一份Layout的Child
		 */
		List<ElementOrderly> layChild = new ArrayList<ElementOrderly>(lay.getChild());
		/**
		 * 如果在目标之后,坐标+1
		 */
		if (isAfter) {
			portletIndex++;
		}
		/**
		 * 加入拷贝
		 */
		layChild.add(portletIndex, portlet);
		/**
		 * 设置子元素
		 */
		lay.setChild(layChild);
		/**
		 * 递归设置父Layout
		 */
		layoutReSet(lay, portletPlace);
	}

	/**
	 * 插入一个新的Portlet
	 * @param portlet
	 */
	@SuppressWarnings("unchecked")
	public void insertNewPortlet(Portlet portlet){
		initCoordTrees();
		List<Integer> portletPlace = new ArrayList<Integer>();
		portletPlace.addAll((List<Integer>)portletTree.values().toArray()[0]);
		/**
		 * 获取目标坐标
		 */
		Integer portletIndex = portletPlace.get(portletPlace.size() - 1);
		/**
		 * 获取父Layout坐标
		 */
		portletPlace.remove(portletPlace.size() - 1);
		/**
		 * 获得父Layout
		 */
		Layout lay = findLayout(portletPlace);
		/**
		 * 拷贝一份Layout的Child
		 */
		List<ElementOrderly> layChild = new ArrayList<ElementOrderly>(lay.getChild());

		/**
		 * 加入拷贝
		 */
		layChild.add(portletIndex, portlet);
		/**
		 * 设置子元素
		 */
		lay.setChild(layChild);
		/**
		 * 递归设置父Layout
		 */
		layoutReSet(lay, portletPlace);
	}
	
	
	/**
	 * 剪切Portlet
	 * @param portletId
	 * @return
	 */
	private Portlet cutPortlet(String portletId) {
		Portlet portlet = findPortlet(portletId);
		removePortletElement(portletId);
		//生成剪切后的树
		plantCoordTrees();
		return portlet;
	}

	/**
	 * 生成layout及Portal坐标
	 */
	public void plantCoordTrees() {
		portletTree = new LinkedHashMap<String, List<Integer>>();
		layoutTree = new LinkedHashMap<String, List<Integer>>();
		plantLayout(getLayout());
	}

	/**
	 * 初始化坐标系
	 */
	private void initCoordTrees() {
		if (portletTree == null || portletTree.size() == 0) {
			portletTree = new LinkedHashMap<String, List<Integer>>();
		}
		if (layoutTree == null || layoutTree.size() == 0) {
			layoutTree = new LinkedHashMap<String, List<Integer>>();
		}
	}

	/**
	 * 生成layout及Portal坐标
	 * @param layout
	 */
	private void plantLayout(Layout layout) {
		if (layout != null) {
			List<ElementOrderly> childs = layout.child;
			for (int i = 0; i < childs.size(); i++) {
				List<Integer> stump = getLayoutTree(layout.id);
				if (stump == null) {
					stump = new ArrayList<Integer>();
				}
				ElementOrderly child = childs.get(i);
				if (child instanceof Layout) {
					Layout lay = (Layout) child;
					List<Integer> _tmp = new ArrayList<Integer>(stump);
					_tmp.add(i);
					layoutTree.put(lay.getId(), _tmp);
					plantLayout((Layout) child);
				} else {
					Portlet _portlet = (Portlet) child;
					List<Integer> _tmp = new ArrayList<Integer>(stump);
					_tmp.add(i);
					portletTree.put(_portlet.getId(), _tmp);
				}
			}
		}
	}

	/**
	 * 获得Portlet坐标系
	 * @param id
	 * @return
	 */
	private List<Integer> getPortletTree(String id) {
		initCoordTrees();
		List<Integer> place = new ArrayList<Integer>();
		if (portletTree.get(id) != null) {
			place.addAll(portletTree.get(id));
		}
		return place;
	}

	/**
	 *  获得Layout坐标系
	 * @param id
	 * @return
	 */
	private List<Integer> getLayoutTree(String id) {
		initCoordTrees();
		List<Integer> place = new ArrayList<Integer>();
		if (layoutTree.get(id) != null) {
			place.addAll(layoutTree.get(id));
		}
		return place;
	}

	/**
	 * 生成XML
	 * @return
	 */
	public String toXml() {
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = doc.createElement("page");
		doc.appendChild(rootNode);
		if (StringUtils.isNotBlank(getTemplate())) {
			rootNode.setAttribute("template", getTemplate());
		}
		if(StringUtils.isNotBlank(getPagename())){
			rootNode.setAttribute("pagename", getPagename());
		}
		if (StringUtils.isNotBlank(getLable())) {
			rootNode.setAttribute("lable", getLable());
		}
		if (StringUtils.isNotBlank(getVersion())) {
			rootNode.setAttribute("version", getVersion());
		}
		if(StringUtils.isNotBlank(getDevices())){
			rootNode.setAttribute("devices", getDevices());
		}else{
			rootNode.setAttribute("drivers", "PC");
		}
		if (StringUtils.isNotBlank(getSkin())) {
			rootNode.setAttribute("skin", getSkin());
		}
		if (StringUtils.isNotBlank(getIcon())) {
			rootNode.setAttribute("icon", getIcon());
		}
		if(getReadonly() != null){
			rootNode.setAttribute("readonly", getReadonly().toString());
		}
		if(getKeepstate() != null){
			rootNode.setAttribute("keepstate", getKeepstate().toString());
		}
		if(getResourcebundle() != null){
			rootNode.setAttribute("resourcebundle", getResourcebundle());
		}
		
		if (getLevel()!=null) {
			rootNode.setAttribute("level", getLevel().toString());
		}
		if (getOrdernum()!=null) {
			rootNode.setAttribute("ordernum", getOrdernum().toString());
		}
		if (StringUtils.isNotBlank(getModule())) {
			rootNode.setAttribute("module", getModule());
		}
		if (StringUtils.isNotBlank(getLinkgroup())) {
			rootNode.setAttribute("linkgroup", getLinkgroup());
		}
		
		if (getIsdefault() != null) {
			rootNode.setAttribute("isdefault", getIsdefault().toString());
		} else {
			rootNode.setAttribute("isdefault", Boolean.FALSE.toString());
		}
		if (getUndercontrol() != null) {
			rootNode.setAttribute("undercontrol", getUndercontrol().toString());
		} else {
			rootNode.setAttribute("undercontrol", Boolean.FALSE.toString());
		}
		if (StringUtils.isNotBlank(getTitle())) {
			Element titleNode = doc.createElement("title");
			titleNode.setTextContent(getTitle());
			rootNode.appendChild(titleNode);
		}

		if (getLayout() != null) {
			rootNode.appendChild(PmlUtil.layoutToXML(getLayout(), doc.createElement("layout"), doc));
		}

		Writer wr = new StringWriter();
		XMLUtil.printDOMTree(wr, doc, 0, "UTF-8");
		return wr.toString();
	}

	/**
	 * 获得所有的Portlet
	 * @return
	 */
	public Portlet[] getAllPortlet(){
		List<Portlet> plist=new ArrayList<Portlet>();
		 if(getLayout()!=null){
			 plist=getPortletChild(getLayout());
		 }
		return plist.toArray(new Portlet[0]);
	}
	/**
	 * 获得所有Portlet的名称
	 * @return
	 */
	public String[][] getAllPortletNames(){
		 Portlet[] portlets= getAllPortlet();
		if(portlets!=null){
			String[][] portletNames=new String [portlets.length][2];
			for(int i=0;i<portlets.length;i++){
				Portlet pt = portlets[i];
				String[] _ts =new String[2];
					_ts[0]= getModule();
					String ptName = pt.getName();
					if (ptName.indexOf(":") > 0) {
						 _ts = ptName.split(":");
					}else{
						_ts[1]= ptName;
					}
					portletNames[i]=_ts;
			}
			return portletNames;
		}
		return null;
	}
	/**
	 * 递归得Portlet
	 * @param layout
	 * @return
	 */
	private List<Portlet> getPortletChild(Layout layout){
		List<Portlet> plist=new ArrayList<Portlet>();
		List<ElementOrderly> ele=layout.getChild();
		for(ElementOrderly el:ele){
			if(el instanceof Portlet){
				plist.add((Portlet)el);
			}else{
				plist.addAll(getPortletChild((Layout)el));
			}
		}
		return plist;
	}

	@Override
	public int compareTo(Page o) {
		return this.getOrdernum()-o.getOrdernum();
	}
}
