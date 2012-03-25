package nc.uap.portal.util;

import java.io.File;
import java.io.FileFilter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.ElementOrderly;
import nc.uap.portal.om.Layout;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Portlet;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * PML解析及生成的java类
 * 
 * @author licza.
 * 
 */
public class PmlUtil {
	
	//PML解析器
	private static Digester pmlDigester;

	/**
	 * 获得Digester
	 * 
	 * @return Digester
	 */
	public static Digester getPortletDigester() {
		if (pmlDigester == null) 
			initPsmlDigester();
		return pmlDigester;
	}

	/**
	 * 初始化Digester
	 */
	private static void initPsmlDigester() {
		pmlDigester = new Digester();
		pmlDigester.setValidating(false);
		pmlDigester.addObjectCreate("page", Page.class.getName());
		pmlDigester.addSetProperties("page");
		pmlDigester.addCallMethod("page/title", "setTitle", 0);
		String layoutClazz = Layout.class.getName();
		String portletClazz = Portlet.class.getName();
		pmlDigester.addObjectCreate("page/layout", layoutClazz);
		pmlDigester.addSetProperties("page/layout");
		pmlDigester.addSetNext("page/layout", "setLayout", layoutClazz);
		String layoutPath = "page/layout";
		for (int i = 0; i < 10; i++) {
			String _layoutPath = layoutPath + "/layout";
			pmlDigester.addObjectCreate(_layoutPath, layoutClazz);
			pmlDigester.addSetProperties(_layoutPath);
			pmlDigester.addSetNext(_layoutPath, "addChild", layoutClazz);
			String portletPath = layoutPath + "/portlet";
			pmlDigester.addObjectCreate(portletPath, portletClazz);
			pmlDigester.addSetProperties(portletPath);
			pmlDigester.addSetNext(portletPath, "addChild", portletClazz);
			layoutPath = _layoutPath;
		}
	}

	/**
	 * 重设Layout下所有Child的排序信息
	 * 
	 * @param child
	 */
	public static void resetColumn(List<ElementOrderly> child) {
		ListIterator<ElementOrderly> i = child.listIterator();
		for (int j = 0; j < child.size(); j++) {
			child.get(j).setColumn(0);
			i.set(child.get(j));
		}
	}

	/**
	 * 重设元素的排序信息
	 * 
	 * @param element
	 */
	public static void resetElement(ElementOrderly element) {
		element.setColumn(0);
	}

	/**
	 * 生成布局的XML片段
	 * 
	 * @param layout
	 * @param node
	 * @param doc
	 * @return
	 */
	public static Element layoutToXML(Layout layout, Element node, Document doc) {
		if (layout != null) {
			if (StringUtils.isNotBlank(layout.getId())) {
				node.setAttribute("id", layout.getId());
			}
			if (StringUtils.isNotBlank(layout.getName())) {
				node.setAttribute("name", layout.getName());
			}
			if (StringUtils.isNotBlank(layout.getSizes())) {
				node.setAttribute("sizes", layout.getSizes());
			}
			node.setAttribute("column", layout.getColumn().toString());
			if (layout.child != null) {
				for (ElementOrderly child : layout.getChild()) {
					if (child instanceof Layout) {
						node.appendChild(layoutToXML((Layout) child, doc
								.createElement("layout"), doc));
					} else {
						Portlet portlet = (Portlet) child;
						node.appendChild(portletToXML(portlet, doc));
					}
				}
			}
		}
		return node;
	}

	/**
	 * 生成Portlet的布局片段
	 * 
	 * @param portlet
	 * @param doc
	 * @return
	 */
	public static Element portletToXML(Portlet portlet, Document doc) {
		Element portletNode = doc.createElement("portlet");
		if (StringUtils.isNotBlank(portlet.id)) {
			portletNode.setAttribute("id", portlet.id);
		}
		if (StringUtils.isNotBlank(portlet.name)) {
			portletNode.setAttribute("name", portlet.name);
		}
		if (StringUtils.isNotBlank(portlet.theme)) {
			portletNode.setAttribute("theme", portlet.theme);
		}
		if (StringUtils.isNotBlank(portlet.title)) {
			portletNode.setAttribute("title", portlet.title);
		}
		portletNode.setAttribute("column", portlet.column.toString());
		if (portlet.getCloseable() != null) {
			portletNode.setAttribute("closeable", portlet.getCloseable()
					.toString());
		}
		if (portlet.getDraggable() != null) {
			portletNode.setAttribute("draggable", portlet.getDraggable()
					.toString());
		}
		if (portlet.getDrawhead() != null) {
			portletNode.setAttribute("drawhead", portlet.getDrawhead()
					.toString());
		}
		if (portlet.getMaxable() != null) {
			portletNode
					.setAttribute("maxable", portlet.getMaxable().toString());
		}
		if (portlet.getMinable() != null) {
			portletNode
					.setAttribute("minable", portlet.getMinable().toString());
		}
		if (portlet.getNormal() != null) {
			portletNode.setAttribute("normal", portlet.getNormal().toString());
		}
		return portletNode;
	}

	/**
	 * 解析PML
	 * 
	 * @param PML文件
	 * @return Portal页面对象.
	 * @throws PortalServiceException
	 */
	public static Page parser(File pml) throws PortalServiceException {
		Digester digester = PmlUtil.getPortletDigester();
		try {
			Page page = null;
			synchronized (digester) {
				page = (Page) digester.parse(pml);
			}
			String pmlName = pml.getName();
			// 设置pageName
			page.setPagename(pmlName.substring(0, pmlName.length() - 4));
			return page;
		} catch (Exception e) {
			throw new PortalServiceException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * 解析PML
	 * @param pml
	 * @return
	 * @throws PortalServiceException
	 */
	public static Page parser(String pml) throws SAXException{
		Digester digester = PmlUtil.getPortletDigester();
		Reader reader = null;
		try {
			Page page = null;
			reader = new StringReader(pml);
			synchronized (digester) {
				page = (Page) digester.parse(reader);
			}
			return page;
		} catch (Exception e) {
			LfwLogger.error("PML解析异常!",e);
			throw new SAXException(e.getMessage());
		}finally{
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * 获得Page对象列表
	 * 
	 * @return Portal页面对象数组.
	 */
	public static Page[] getPages(String dirPath) {
		List<Page> pages = new ArrayList<Page>();
		File dir = new File(dirPath);
		FileFilter filter = FileFilterUtils.suffixFileFilter("pml");
		File[] files = dir.listFiles(filter);
		if (files == null)  
			return null;
		for (File pml : files) {
			try {
				pages.add(PmlUtil.parser(pml));
			} catch (PortalServiceException e) {
				Logger.warn("PML文件错误：" + pml.getPath(), e.getCause());
			}
		}
		return pages.toArray(new Page[0]);
	}
}
