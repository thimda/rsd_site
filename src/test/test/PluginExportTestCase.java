package test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.test.AbstractTestCase;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.plugins.model.ExPoint;
import nc.uap.portal.plugins.model.Extension;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;
import nc.uap.portal.plugins.model.PtPlugin;
import nc.uap.portal.service.itf.IPtPluginQryService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.util.PtUtil;

/**
 * 将数据库中插件导出成xml
 * @author licza
 *
 */
public class PluginExportTestCase extends AbstractTestCase {

	public void testExport() throws PortalServiceException{
		NCLocator loc = NCLocator.getInstance();
		IPtPluginQryService pluginQry = loc.lookup(IPtPluginQryService.class);
		PtExtensionPoint[] exPointArr = pluginQry.getAllExtensionPoint();
		PtExtension[] exArr = pluginQry.getAllExtension();
		PtPlugin plugin = new PtPlugin();
		Map<String,ExPoint> expMap = new HashMap<String,ExPoint>();
		if(exPointArr != null){
			for(PtExtensionPoint exp : exPointArr){
				expMap.put(exp.getPoint(), new ExPoint(exp));
			}
		}
		
		if(exArr != null){
			for(PtExtension ex : exArr){
				if(!PtUtil.isNull(ex.getId())){
					ExPoint pi = expMap.get(ex.getPoint());
					if(pi != null)
						pi.addPtExtension(new Extension(ex));
					else
						System.out.println("++++++++++++++++++" + ex.getId() + "=====" + ex.getPoint() +"++++++++++++++++++");
 				}
			}
		}
		List<ExPoint> points =  new ArrayList<ExPoint>();
		points.addAll(expMap.values());
		plugin.setExtensionPointList(points);
		try {
			Writer sw =new StringWriter();
			JaxbMarshalFactory.newIns().lookupMarshaller(PtPlugin.class).marshal(plugin,sw);
			String xml = sw.toString();
//			xml = xml.replace("plugin>", "plugin>\r\n").replace("</extension>", "</extension>\r\n").replace("<dirty>false</dirty>", "").replace("<status>0</status>", "").replace("<dr>0</dr>", "").replace("</extension-point>", "</extension-point>\r\n");
			System.out.println(xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
