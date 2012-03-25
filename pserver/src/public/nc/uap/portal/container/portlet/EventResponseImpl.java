
package nc.uap.portal.container.portlet;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;

import nc.uap.portal.util.ArgumentUtility;


/**
 * Implementation of JSR-286 <code>EventResponse</code>.
 *
 * @since 2.0
 */

public class EventResponseImpl extends StateAwareResponseImpl implements EventResponse
{
    public EventResponseImpl(StateAwareResponseContext context) {
		super(context);
	}

	protected void checkSetStateChanged()
    {
        // nothing to check or do for EventResponse
    }

	public void setRenderParameters(EventRequest request)
	{
        ArgumentUtility.validateNotNull("request", request);
        setRenderParameters(request.getParameterMap());
	}
}
