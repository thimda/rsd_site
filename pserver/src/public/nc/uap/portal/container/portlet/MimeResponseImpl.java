package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.CacheControl;
import javax.portlet.MimeResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.container.om.Supports;


public class MimeResponseImpl extends PortletResponseImpl implements MimeResponse
{
    public MimeResponseImpl(PortletResponseContext context) {
		super(context);
	}

	/** Response content types. */
    private ArrayList<String> responseContentTypes;
    
    private boolean usingWriter;
    private boolean usingStream;
    private boolean committed;
    private String contentType;

	 
	
    protected List<String> getResponseContentTypes() 
    {
        if (responseContentTypes == null) 
        {
            responseContentTypes = new ArrayList<String>();
            PortletDefinition dd = getPortletWindow().getPortletDefinition();
            for (Supports sup : dd.getSupports())
            {
                responseContentTypes.add(sup.getMimeType());
            }
            if (responseContentTypes.isEmpty()) 
            {
                responseContentTypes.add("text/html");
            }
        }
        return responseContentTypes;
    }
    
    public PortletURL createActionURL()
    {
        return new PortletURLImpl(getPortletWindow());
    }
    
    public PortletURL createRenderURL()
    {
        return new PortletURLImpl(getPortletWindow());
    }


    public ResourceURL createResourceURL()
    {
        return new PortletURLImpl(getPortletWindow());
    }
    
    public void flushBuffer() throws IOException
    {
        committed = true;
        getServletResponse().flushBuffer();
    }

    public int getBufferSize()
    {
        return getServletResponse().getBufferSize();
    }
    
	public CacheControl getCacheControl() {
		return new CacheControlImpl();
	}
	
    public String getCharacterEncoding()
    {
        return getServletResponse().getCharacterEncoding();
    }

    public String getContentType()
    {
        return contentType;
    }
    
    public Locale getLocale()
    {
        return getServletResponse().getLocale();
    }

    public OutputStream getPortletOutputStream()
    throws IllegalStateException, IOException
    {
        if (usingWriter)
        {
            throw new IllegalStateException("getPortletOutputStream can't be used after getWriter was invoked.");
        }
        if (getContentType() == null)
        {
            setContentType(getResponseContentTypes().get(0));
        }
        usingStream = true;
        return getServletResponse().getOutputStream();
    }
    
    public PrintWriter getWriter()
    throws IllegalStateException, IOException
    {
        if (usingStream)
        {
            throw new IllegalStateException("getWriter can't be used after getOutputStream was invoked.");
        }
        if (getContentType() == null)
        {
            setContentType(getResponseContentTypes().get(0));
        }
        usingWriter = true;
        return getServletResponse().getWriter();
    }

    public boolean isCommitted()
    {
        return committed ? true : getServletResponse().isCommitted();
    }

    public void reset()
    {
        if (isCommitted())
        {
            throw new IllegalStateException("Response is already committed");
        }
        getServletResponse().reset();
    }

    public void resetBuffer()
    {
        if (isCommitted())
        {
            throw new IllegalStateException("Response is already committed");
        }
        getServletResponse().resetBuffer();
    }

    public void setBufferSize(int size)
    {
        if (isCommitted())
        {
            throw new IllegalStateException("Response is already committed");
        }
        getServletResponse().setBufferSize(size);
    }
    
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
        getServletResponse().setContentType(contentType);
    }
    
    @Override
    public void setProperty(String name, String value)
    {
        if (USE_CACHED_CONTENT.equals(name))
        {
            getCacheControl().setUseCachedContent(value != null);
        } 
        else if (EXPIRATION_CACHE.equals(name))
        {
            int expirationTime;
            try 
            {
                expirationTime = Integer.parseInt(value);
            } 
            catch (NumberFormatException e)
            {
                expirationTime = 0;
            }
            getCacheControl().setExpirationTime(expirationTime);
        } 
        else if (ETAG.equals(name)) 
        {
            getCacheControl().setETag(value);
        } 
        else if (CACHE_SCOPE.equals(name))
        {
            getCacheControl().setPublicScope(PUBLIC_SCOPE.equals(value));
        } 
        else 
        {
            super.setProperty(name, value);
        }
    }
}
