
package nc.uap.portal.container.portlet;


/**
 * TODO: javadoc
 * @version 1.0
 * @since Sep 20, 2004
 */
public class PortletPreferenceImpl implements PortletPreference {

	// Private Member Variables ------------------------------------------------

	/** The preference name. */
    private String name;

    /** The preference values. */
    private String[] values;

    /** Flag indicating if this preference is read-only. */
    private boolean readOnly = false;


    // Constructors ------------------------------------------------------------

    public PortletPreferenceImpl(String name, String[] values) {
        this.name = name;
        this.values = values;
        this.readOnly = false;
    }

    public PortletPreferenceImpl(String name,
                                 String[] values,
                                 boolean readOnly) {
        this.name = name;
        this.values = values;
        this.readOnly = readOnly;
    }

    /**
     * Private constructor that is used only within the <code>clone()</code>
     * method.
     * @see #clone()
     */
    private PortletPreferenceImpl() {
    	// Do nothing.
    }


    // PortletPreference Impl --------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public PortletPreference clone() {
    	PortletPreferenceImpl copy = new PortletPreferenceImpl();
    	copy.name = this.name;
    	if (this.values != null) {
    		copy.values = this.values.clone();
    	}
    	copy.readOnly = this.readOnly;
    	return copy;
    }


    // Object Methods ----------------------------------------------------------

    /**
     * Override of toString() that prints out name and values of fields.
     * @see java.lang.Object#toString()
     */
    public String toString(){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(getClass().getName());
    	buffer.append("[name=").append(name).append(";");
    	if (values != null) {
    		for (int i = 0; i < values.length; i++) {
    			buffer.append("value[").append(i).append("]=")
    					.append(values[i]).append(";");
    		}
    	} else {
    		buffer.append("values=NULL;");
    	}
    	buffer.append("readOnly=").append(readOnly).append("]");
    	return buffer.toString();
    }

}

