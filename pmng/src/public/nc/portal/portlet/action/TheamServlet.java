package nc.portal.portlet.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.vo.PtThemeVO;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * theamº”‘ÿ
 * 2011-2-18 …œŒÁ08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/theamServlet")
public class TheamServlet extends PortletBaseServlet {
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(PtThemeVO[] vos){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Theams");
		doc.appendChild(root);		
		if(vos==null)return doc;
		
		for(int i=0;i<vos.length;i++){
			PtThemeVO vo = vos[i];
			Element child = doc.createElement("Theam");
			child.setAttribute("id", vo.getThemeid());
			child.setAttribute("title", vo.getTitle());
			root.appendChild(child);
		}		
		return doc;
	}
	
	@Action
	public void doPost()throws ServletException, IOException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
        String pk_group = request.getParameter("groupid");
        PtThemeVO[] vos = new PtThemeVO[]{};
		try {
			vos = PortalServiceUtil.getThemeQryService().getThemeByGroup();
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}

        Document doc = buildXml(vos);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}