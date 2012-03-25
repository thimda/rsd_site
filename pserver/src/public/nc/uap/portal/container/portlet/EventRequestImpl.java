
package nc.uap.portal.container.portlet;


import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;


/**
 * <code>javax.portlet.EventRequest</code> implementation.
 *
 */
public class EventRequestImpl extends PortletRequestImpl implements EventRequest
{
	private final Event event; 
	
    public EventRequestImpl(HttpServletRequest request, PortletWindow portletWindow, Event event)
    {
        super(request, portletWindow, PortletRequest.EVENT_PHASE);
        this.event = event;
    }
    
    public Event getEvent()
    {
        return event;
    }

    public String getMethod()
    {
        return getServletRequest().getMethod();
    }

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
