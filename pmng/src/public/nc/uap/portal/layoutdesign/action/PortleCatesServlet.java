package nc.uap.portal.layoutdesign.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;

import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.service.PortalServiceUtil;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * portlet分类的加载
 * 2011-2-18 上午08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/portletCatesServlet")
public class PortleCatesServlet extends PortletBaseServlet { 
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(Map<String,PortletDisplayCategory> cates){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Cates");
		doc.appendChild(root);		
		if(cates==null)return doc;
		
		Iterator<String> ite = cates.keySet().iterator();
		while(ite.hasNext()){
			String category = ite.next();
			PortletDisplayCategory cate = cates.get(category);
			Element node = doc.createElement("Cate");
			node.setAttribute("id", category);
			node.setAttribute("text", cate.getText());
			node.setAttribute("i18name", cate.getI18nName());
			root.appendChild(node);			
		}			
		return doc;
	}
	
	@Action
	public void doPost()throws ServletException, IOException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
		Map<String, PortletDisplayCategory>  cates = PortalServiceUtil.getPortletRegistryService().getPortletDisplayCache();
			
        Document doc = buildXml(cates);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}