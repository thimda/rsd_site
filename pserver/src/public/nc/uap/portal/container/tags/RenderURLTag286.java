package nc.uap.portal.container.tags;


import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;


/**
 * A tag handler for the <CODE>renderURL</CODE> tag as defined in the JSR 286.
 * Creates a url that points to the current Portlet and triggers a render request 
 * with the supplied parameters.
 * 
 * @version 2.0
 */
public class RenderURLTag286 extends PortletURLTag286 {
	
	private static final long serialVersionUID = 286L;

    /**
     * Creates a render PortletURL
     * @param portletResponse PortletResponse
     * @return PortletURL
     */
	@Override
	protected PortletURL createPortletUrl(PortletResponse portletResponse){
		
		if (portletResponse instanceof RenderResponse) {
			return ((RenderResponse)portletResponse).createRenderURL();			
		}
		else if (portletResponse instanceof ResourceResponse) {
			return ((ResourceResponse)portletResponse).createRenderURL();			
		}
		
		throw new IllegalArgumentException();
	}
    
}

