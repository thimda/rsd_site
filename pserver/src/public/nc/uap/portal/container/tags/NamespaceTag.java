package nc.uap.portal.container.tags;

import java.io.IOException;

import javax.portlet.PortletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag produces a unique value for the current portlet.
 * <p/>
 * <p/>
 * A tag handler for the <CODE>namespace</CODE> tag. writes a unique value
 * for the current portlet <BR>This tag has no attributes
 * 
 * @version 2.0
 */
public class NamespaceTag extends TagSupport {
	
	private static final long serialVersionUID = 286L;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
    	
    	PortletResponse portletResponse = (PortletResponse) pageContext.getRequest()
            .getAttribute(Constants.PORTLET_RESPONSE);
    	
        String namespace = portletResponse.getNamespace();
        
        JspWriter writer = pageContext.getOut();
        
        try {
            writer.print(namespace);
        } catch (IOException ioe) {
            throw new JspException(
                "Unable to write namespace", ioe
            );
        }
        
        return SKIP_BODY;
    }
}
