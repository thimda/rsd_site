

package nc.uap.portal.container.portlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.portlet.ClientDataRequest;
import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of the <code>javax.portlet.ClientDataRequest</code> interface.
 */
public abstract class ClientDataRequestImpl extends PortletRequestImpl implements ClientDataRequest
{
    /** Flag indicating if the HTTP body reader has been accessed. */
    protected boolean readerAccessed = false;

    public ClientDataRequestImpl(HttpServletRequest request, PortletWindow portletWindow, String lifecyclePhase)
    {
        super(request, portletWindow, lifecyclePhase);
    }

    private void checkPostedFormData() 
    {
        if (getMethod().equals("POST"))
        {
            String contentType = getContentType();
            if (contentType == null || contentType.equals("application/x-www-form-urlencoded"))
            {
                throw new IllegalStateException("User request HTTP POST data is of type "
                                                + "application/x-www-form-urlencoded. "
                                                + "This data has been already processed "
                                                + "by the portlet-container and is available "
                                                + "as request parameters.");
            }
        }
    }
        
    public java.lang.String getCharacterEncoding()
    {
        return getServletRequest().getCharacterEncoding();
    }

    public int getContentLength()
    {
        return getServletRequest().getContentLength();
    }

    public java.lang.String getContentType()
    {
        return getServletRequest().getContentType();
    }
    
    public String getMethod()
    {
        return getServletRequest().getMethod();
    }

    public InputStream getPortletInputStream() throws IOException
    {
        checkPostedFormData();
        // the HttpServletRequest will ensure that a IllegalStateException is thrown
        //   if getReader() was called earlier        
        return getServletRequest().getInputStream();
    }

    public BufferedReader getReader()
    throws UnsupportedEncodingException, IOException 
    {
        checkPostedFormData();
        // the HttpServletRequest will ensure that a IllegalStateException is thrown
        //   if getInputStream() was called earlier
        BufferedReader reader = getServletRequest().getReader();
        readerAccessed = true;
        return reader;

    }
    
    public void setCharacterEncoding(String encoding)
    throws UnsupportedEncodingException 
    {
        if (readerAccessed) 
        {
            throw new IllegalStateException("Cannot set character encoding "
                    + "after HTTP body reader is accessed.");
        }
        getServletRequest().setCharacterEncoding(encoding);
    }
}
