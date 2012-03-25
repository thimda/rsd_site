package test;

import java.io.IOException;

import junit.framework.Assert;

import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.XmlUtil;

import org.xml.sax.SAXException;

/**
 * “≥√Ê¡˜≤‚ ‘”√¿˝
 * 
 * @author licza
 * 
 */
public class PageFlowTester {


	public void testReadPortletAppDef() throws IOException, SAXException, PortalServiceException {
		PortletApplicationDefinition  pa=	XmlUtil.readPortletAppDef("D:/views/views60/licza_NC_UAP_MODULES6.0_int/NC6_UAP_VOB/NC_UAP_MODULES/modules/portaltest1");
		Assert.assertNotNull(pa);
	}

	 

}
