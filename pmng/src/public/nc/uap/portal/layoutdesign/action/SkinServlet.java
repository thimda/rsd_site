package nc.uap.portal.layoutdesign.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.om.Skin;
import nc.uap.portal.service.PortalServiceUtil;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * skinº”‘ÿ
 * 2011-2-18 …œŒÁ08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/skinServlet")
public class SkinServlet extends PortletBaseServlet {
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(Skin[] skins,String type){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Skins");
		root.setAttribute("type", type);
		doc.appendChild(root);		
		if(skins==null)return doc;
		
		for(int i=0;i<skins.length;i++){
			Element child = doc.createElement("Skin");
			child.setAttribute("id", skins[i].getId());
			child.setAttribute("name", skins[i].getName());
			root.appendChild(child);
		}		
		return doc;
	}
	@Action 
	public void doPost()throws ServletException, IOException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
        String type = request.getParameter("type");
        Skin[] skins = new Skin[]{};
        if("page".equals(type))
        	skins = PortalServiceUtil.getSkinQryService().getSkinCache(PortalEnv.TYPE_PAGE);
        else if("layout".equals(type))
        	skins = PortalServiceUtil.getSkinQryService().getSkinCache(PortalEnv.TYPE_LAYOUT);
        else if("portlet".equals(type))
        	skins = PortalServiceUtil.getSkinQryService().getSkinCache(PortalEnv.TYPE_PORTLET);
        Document doc = buildXml(skins,type);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}