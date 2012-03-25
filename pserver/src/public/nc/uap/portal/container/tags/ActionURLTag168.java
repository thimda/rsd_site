package nc.uap.portal.container.tags;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

/**
 * 
 * A tag handler for the <CODE>actionURL</CODE> tag as defined in the JSR 168.
 * Creates a url that points to the current Portlet and triggers an 
 * action request with the supplied parameters.
 * 
 * @version 2.0
 */
public class ActionURLTag168 extends PortletURLTag168 {
	
	private static final long serialVersionUID = 286L;

	/**
     * Creates an action PortletURL 
     * @param portletResponse PortletResponse
     * @return PortletURL
     */
	@Override
	protected PortletURL createPortletUrl(PortletResponse portletResponse){
		if (portletResponse instanceof RenderResponse) {
			return ((RenderResponse)portletResponse).createActionURL();			
		}		
		throw new IllegalArgumentException();
	}
    
    
}

