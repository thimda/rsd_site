package nc.uap.portal.container.tags;


import java.io.IOException;

import javax.portlet.BaseURL;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;


/**
 * A tag handler for the <CODE>resourceURL</CODE> tag as defined in the JSR 286.
 * Creates a url that points to the current Portlet and triggers a 
 * resource request with the supplied parameters.
 * 
 * @version 2.0
 */

public class ResourceURLTag286 extends BaseURLTag {
	
	private static final long serialVersionUID = 286L;	
	
	private String id = null;
	
	private String cachability = null;
	
	private ResourceURL resourceURL = null;
	

	public ResourceURLTag286() {
		super();
		this.escapeXml = true;
	}
	

	/* (non-Javadoc)
 	 */
	@Override
	public int doStartTag() throws JspException {
		       
        PortletResponse portletResponse = (PortletResponse) pageContext.getRequest()
            .getAttribute(Constants.PORTLET_RESPONSE);
        
        if (portletResponse != null) {
        	ResourceURL resourceURL = createResourceURL(portletResponse);
                 
            if(id != null){
            	resourceURL.setResourceID(id);
            }
           
            if(cachability != null){
            	try{
            		resourceURL.setCacheability(cachability);
            	}
            	catch(IllegalArgumentException e){
            		throw new JspException(e);
            	}
            	catch(IllegalStateException e){
            		throw new JspException(e);
            	}
            }
            
            setUrl(resourceURL);
        }
        
        return super.doStartTag();
    }
	
	public int doEndTag() throws JspException{
		BaseURL url = getUrl();
		if(url == null){
			throw new IllegalStateException("internal error: url not set");
		}
		setUrlParameters(url);		
	//	setUrlProperties(url);
		
//		HttpServletResponse response = 
//			(HttpServletResponse) pageContext.getResponse();
		
		//	properly encoding urls to allow non-cookie enabled sessions - 
		HttpServletRequest request=(HttpServletRequest) pageContext.getRequest();
		String port=request.getServerPort()==80?"":":"+request.getServerPort();
		String requestScheme=request.getScheme();
	 
		if(getSecureBoolean()){
			requestScheme="https";
		}
		String urlString = requestScheme+"://"+request.getServerName()+port+request.getContextPath()+url.toString();

 		if(escapeXml)
 		{
		 	 urlString = doEscapeXml(urlString);
		}
		
	    if (var == null) {
            try {            	
                JspWriter writer = pageContext.getOut();
                writer.print(urlString);
            } catch (IOException ioe) {
                throw new JspException(
                    "Portlet/ResourceURL-Tag Exception: cannot write to the output writer.");
            }
        } 
	    else {
            pageContext.setAttribute(var, urlString,PageContext.PAGE_SCOPE);
        }
	    
	    /*cleanup*/
	    propertiesMap.clear();
	    parametersMap.clear();
	    removedParametersList.clear();
	    
	    setUrl(null);
	    
        return EVAL_PAGE;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the cachability
	 */
	public String getCachability() {
		return cachability;
	}


	/**
	 * @param cachability the cachability to set
	 */
	public void setCachability(String cachability) {
		this.cachability = cachability;
	}
	

    /* (non-Javadoc)
      */
    @Override
    protected ResourceURL getUrl() {
        return resourceURL;
    }
	
    
    @Override
    protected void setUrl(BaseURL url) {
        this.resourceURL = (ResourceURL)url;
    }
	
    
	/**
	 * Creates a resourceURL.
	 * 
	 * @param portletResponse
	 * @return a resourceURL
	 * @throws JspException
	 */
	protected ResourceURL createResourceURL(PortletResponse portletResponse) throws JspException{
		ResourceURL result = null;
		if(portletResponse instanceof RenderResponse){
    		result = ((RenderResponse)portletResponse).createResourceURL();	
    	}
    	else if(portletResponse instanceof ResourceResponse){
    		result = ((ResourceResponse)portletResponse).createResourceURL();
    	}	
    	else{
    		throw new JspException();
    	}
		return result;
	}
}
