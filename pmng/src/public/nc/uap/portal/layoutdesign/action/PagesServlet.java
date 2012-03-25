package nc.uap.portal.layoutdesign.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * pageº”‘ÿ
 * 2011-6-18 …œŒÁ08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/pagesServlet")
public class PagesServlet extends PortletBaseServlet {
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(PtPageVO[] vos){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Pages");
		doc.appendChild(root);		
		if(vos==null)return doc;
		
		for(int i=0;i<vos.length;i++){
			PtPageVO vo = vos[i];
			Element child = doc.createElement("Page");
			child.setAttribute("id", vo.getParentid());
			child.setAttribute("name", vo.getPagename());
			child.setAttribute("pk_page", vo.getPk_portalpage());
			root.appendChild(child);
		}		
		return doc;
	}
	
	@Action
	public void doPost()throws ServletException, IOException, PortalServiceException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
        String pk_group = request.getParameter("pk_group");
        PtPageVO[] pages = new PtPageVO[]{};
		pages = PortalServiceUtil.getPageQryService().getPageByGroup(pk_group);
        Document doc = buildXml(pages);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}