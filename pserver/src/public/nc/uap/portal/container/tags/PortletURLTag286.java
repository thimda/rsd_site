package nc.uap.portal.container.tags;

import java.util.Map;
import java.util.Set;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.servlet.jsp.JspException;

/**
 * Abstract supporting class for the JSR 286 actionURL 
 * and renderURL tag handlers.
 * 
 * @version 2.0
 */

public abstract class PortletURLTag286 extends PortletURLTag168 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4853187908316856275L;
	protected Boolean copyCurrentRenderParameters = false;
	
	
	public PortletURLTag286() {
		super();
		this.escapeXml = true;
	}
	
	 
	@Override
    public int doStartTag() throws JspException {    	    	  
         
        PortletConfig portletConfig = 
        	(PortletConfig) pageContext.getRequest().getAttribute(Constants.PORTLET_CONFIG);
        Map<String,String[]> containerRuntimeOptions = portletConfig.getContainerRuntimeOptions();
        if (containerRuntimeOptions != null){
        	String[] result = containerRuntimeOptions.get(Constants.ESCAPE_XML_RUNTIME_OPTION);
        	if (result != null){
        		if (result.length > 0){
        			if ("true".equals(result[0]))
        				escapeXml = true;
        			else if ("false".equals(result[0]))
        				escapeXml = false;
        		}
        	}
        }
        
        return super.doStartTag();
    }
	 
	@Override
	public int doEndTag() throws JspException{
				
		if(copyCurrentRenderParameters){
			/*prepend current render parameters*/
			doCopyCurrentRenderParameters();
		}
	    
        return super.doEndTag();
	}
	
        
    /**
     * Returns the copyCurrentRenderParameters property.
     * @return Boolean
     */
    public Boolean getCopyCurrentRenderParameters() {
        return copyCurrentRenderParameters;
    }
         
        
    /**
     * Sets copyCurrentRenderParameters property.
     * @param copyCurrentRenderParameters
     * @return void
     */
    public void setCopyCurrentRenderParameters(Boolean copyCurrentRenderParameters) {
        this.copyCurrentRenderParameters = copyCurrentRenderParameters;
    }
        
    	
	/**
     * Copies the current render parameters to the parameter map.
     * @return void
     */
    protected void doCopyCurrentRenderParameters(){
    	PortletRequest request = 
    		(PortletRequest) pageContext.getRequest().
    		getAttribute(Constants.PORTLET_REQUEST);
		
    	if(request != null){    		
			Map<String,String[]> renderParamsMap =
				request.getPrivateParameterMap();
			
			Set<String> keySet = renderParamsMap.keySet();
			
			for(String key : keySet){
									
				if(!removedParametersList.contains(key)){

					String[] values = renderParamsMap.get(key);

					for(int index = 0; index < values.length; ++index){
						addParameter(key, values[index]);				
					}
				}
			}
		}
    }
    
}
