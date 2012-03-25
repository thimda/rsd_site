package nc.portal.portlet.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpDeviceQry;
import nc.uap.cpb.org.vos.CpDeviceVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *设备加载
 * 2011-2-18 上午08:50:45 
 * @author limingf
 */
@Servlet(path="/servlet/deviceServlet")
public class DevicesServlet extends PortletBaseServlet {
	private static final long serialVersionUID = -8417057858342060664L;

	public Document buildXml(CpDeviceVO[] vos){
		Document doc = XMLUtil.getNewDocument();
		Element root = doc.createElement("Devices");
		doc.appendChild(root);		
		if(vos==null)return doc;
		
		for(int i=0;i<vos.length;i++){
			CpDeviceVO vo = vos[i];
			Element child = doc.createElement("Device");
			child.setAttribute("code", vo.getCode());
			child.setAttribute("name", vo.getName());
			root.appendChild(child);
		}		
		return doc;
	}
	
	@Action
	public void doPost()throws ServletException, IOException {		
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");   
        PrintWriter out = response.getWriter();   
        
        CpDeviceVO[] vos = new CpDeviceVO[]{};
		try {
			ICpDeviceQry service = (ICpDeviceQry) NCLocator.getInstance().lookup(ICpDeviceQry.class.getName());
			vos = service.getAllDevice();
		} catch (CpbBusinessException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}

        Document doc = buildXml(vos);
        XMLUtil.printDOMTree(out, doc, 0, "UTF-8");
        out.println();         		
	}
}