package nc.uap.portal.container.portlet;

import java.util.ArrayList;
import java.util.Collection;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;

import nc.uap.portal.util.ArgumentUtility;


/**
 * <code>javax.portlet.RenderResponse</code> 接口实现.
 * @author licza
 * @since portal 6.0
 */
public class RenderResponseImpl extends MimeResponseImpl implements RenderResponse
{	
    
    public RenderResponseImpl(PortletResponseContext context) {
		super(context);
	}

	/**
     * 检查内容类型是否被支持.
     * @param contentType  要检查的类型.
     * @return 是否验证通过.
     */
    protected boolean isValidContentType(String contentType)
    {
        boolean valid = false;
        for (String supportedType : getResponseContentTypes())
        {
            // Content type is supported by an exact match.
            if (supportedType.equals(contentType))
            {
                valid = true;
            }
            // The supported type contains a wildcard.
            else if (supportedType.indexOf("*") >= 0)
            {
                int index = supportedType.indexOf("/");
                String supportedPrefix = supportedType.substring(0, index);
                String supportedSuffix = supportedType.substring(index + 1);
                index = contentType.indexOf("/");
                String typePrefix = contentType.substring(0, index);
                String typeSuffix = contentType.substring(index + 1);
                // Check if the prefixes match AND the suffixes match.
                if (supportedPrefix.equals("*") || supportedPrefix.equals(typePrefix))
                {
                    if (supportedSuffix.equals("*") || supportedSuffix.equals(typeSuffix))
                    {
                        valid = true;
                    }
                }
            }
        }
        // Return the check result.
        return valid;
    }
    
    @Override
    public void setContentType(String contentType)
    {
        ArgumentUtility.validateNotNull("contentType", contentType);
        int index =contentType.indexOf(';');
        if (index != -1)
        {
            contentType = contentType.substring(0, index);
        }
        contentType = contentType.trim();
        if (!isValidContentType(contentType))
        {
            throw new IllegalArgumentException("Specified content type '" + contentType + "' is not supported.");
        }
        super.setContentType(contentType);
    }
    
    public void setNextPossiblePortletModes(Collection<PortletMode> portletModes)
    {
        ArgumentUtility.validateNotNull("portletModes", portletModes);
        if (portletModes.isEmpty())
        {
            throw new IllegalArgumentException("At least one possible PortletMode should be specified.");            
        }
        ArrayList<PortletMode> modes = new ArrayList<PortletMode>();
        for (PortletMode mode : portletModes)
        {
            if (isPortletModeAllowed(mode))
            {
                modes.add(mode);
            }
        }
        if (modes.isEmpty())
        {
            modes.add(getPortletWindow().getPortletMode());
        }
    }
    
    public void setTitle(String title)
    {
        this.context.setTitle(title);
    }
}
