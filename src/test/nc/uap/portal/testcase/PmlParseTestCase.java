package nc.uap.portal.testcase;

import java.io.File;

import junit.framework.TestCase;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PmlUtil;

/**
 * PMLΩ‚Œˆ≤‚ ‘”√¿˝
 * 
 * @author licza
 * 
 */
public class PmlParseTestCase extends TestCase {
	private static final String pml="D:\\views\\views60\\NC_UAP_PORTAL6.0_dev\\NC6_UAP_VOB\\NC_UAP_PORTAL6.0\\pbasapp\\portalspec\\pml\\index.pml";
	public void testParse() throws PortalServiceException {
		Page page=	PmlUtil.parser(new File(pml));
		assertEquals(page.getTemplate(), "onerow");
	}
}
