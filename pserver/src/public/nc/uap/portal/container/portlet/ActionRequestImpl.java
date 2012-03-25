
package nc.uap.portal.container.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of the <code>javax.portlet.ActionRequest</code> interface.
 */
public class ActionRequestImpl extends ClientDataRequestImpl implements ActionRequest
{
    public ActionRequestImpl(HttpServletRequest request, PortletWindow portletWindow)
    {
        super(request, portletWindow, PortletRequest.ACTION_PHASE);
    }

	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

}
