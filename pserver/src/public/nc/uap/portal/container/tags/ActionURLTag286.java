package nc.uap.portal.container.tags;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import javax.servlet.jsp.JspException;

/**
 * 
 * A tag handler for the <CODE>actionURL</CODE> tag as defined in the JSR 286,
 * which creates a url that points to the current Portlet and triggers 
 * an action request with the supplied parameters.
 * 
 * @version 2.0
 */
public class ActionURLTag286 extends PortletURLTag286 {
	
	private static final long serialVersionUID = 286L;

	private String 	name = null;
	
	
	
    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	 
	@Override
	public int doEndTag() throws JspException {
		
		if(name != null){
			addParameter("javax.portlet.action", name);
		}
		
		return super.doEndTag();
	}



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
		else if (portletResponse instanceof ResourceResponse) {
			return ((ResourceResponse)portletResponse).createActionURL();			
		}
		throw new IllegalArgumentException();
	}
    
    
}

