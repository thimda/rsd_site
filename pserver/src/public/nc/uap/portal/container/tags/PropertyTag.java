package nc.uap.portal.container.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * A tag handler for the <CODE>property</CODE> tag. Defines a property that
 * can be added to a <CODE>actionURL</CODE>, a <CODE>resourceURL</CODE> or a <CODE>renderURL</CODE>
 * <BR>The following attributes are mandatory:
 *   <UL>
 *       <LI><CODE>name</CODE>
 *       <LI><CODE>value</CODE>
 *   </UL>
 *   
 *  The parent tag handler must be a subclass of <code>BaseURLTag</code>.   
 *       
 *  @version 2.0
 */
public class PropertyTag extends TagSupport {
	
	private static final long serialVersionUID = 286L;

    private String name = null;
    private String value = null;

    /**
     * Processes the <CODE>param</CODE> tag.
     * @return <CODE>SKIP_BODY</CODE>
     */
    public int doStartTag() throws JspException {
        BaseURLTag urlTag = (BaseURLTag)
                findAncestorWithClass(this, BaseURLTag.class);

        if (urlTag == null) {
            throw new JspException(
                "the 'property' Tag must have a actionURL, renderURL " +
                "or resourceURL tag as a parent");
        }
        
        urlTag.addProperty(getName(), getValue());

        return SKIP_BODY;
    }

    /**
     * Returns the name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value.
     * @return String
     */
    public String getValue() throws JspException {
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
