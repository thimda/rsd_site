package nc.portal.portlet.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * portlet类别加载
 * 2011-2-18 上午08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/portletLoadServlet")
public class PortletLoadServlet extends PortletBaseServlet {
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(List<PortletDisplay> list){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Portlets");
		doc.appendChild(root);		
		if(list==null)return doc;
		
		for(int i=0;i<list.size();i++){
			PortletDisplay display = list.get(i);
			Element child = doc.createElement("Portlet");
			//modify by licza Porltet名称应该是加入Module的全称
			child.setAttribute("id", PortalPageDataWrap.modModuleName(display.getModule(), display.getId()));
			child.setAttribute("title", display.getTitle());
			root.appendChild(child);
		}		
		return doc;
	}
	
	@Action
	public void doPost()throws ServletException, IOException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
        String cate_id = request.getParameter("id");
        if(cate_id==null||"".equals(cate_id))return;
		Map<String, PortletDisplayCategory>  cates = PortalServiceUtil.getPortletRegistryService().getPortletDisplayCache();
		if(cates==null)return;
		PortletDisplayCategory category = cates.get(cate_id);
		if(category==null)return;
		List<PortletDisplay> portletDisplayList = category.getPortletDisplayList();
		
        Document doc = buildXml(portletDisplayList);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}