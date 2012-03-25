
package nc.uap.portal.container.portlet;

import javax.portlet.CacheControl;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of the <code>javax.portlet.RenderRequest</code> interface.
 * 
 */
public class RenderRequestImpl extends PortletRequestImpl implements RenderRequest 
{
    private CacheControl cacheControl;
    
    public RenderRequestImpl(HttpServletRequest request, PortletWindow portletWindow, CacheControl cacheControl) 
    {
        super(request, portletWindow, PortletRequest.RENDER_PHASE);
        this.cacheControl = cacheControl;
    }

    @Override
    public String getProperty(String name)
    {
        String result = getMimeRequestProperty(name, cacheControl);
        return result != null ? result : super.getProperty(name);
   }

    public String getETag()
    {
        return cacheControl.getETag();
    }

	@Override
	public String getContextPath() {
		return request.getContextPath()+"/apps/"+getPortletWindow().getId().getModule() ;
	}
}
