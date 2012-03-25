
package nc.uap.portal.container.portlet;

/**
 * This class represents a portlet preference, which is a name-value pair.
 *
 */
public interface PortletPreference {

    /**
     * Returns the name of this portlet preference.
     * @return the name of this portlet preference.
     */
    String getName();

    /**
     * Returns the values of this portlet preference, which is a string array.
     * @return the values of this portlet preference as a string array.
     */
    String[] getValues();

    /**
     * Sets values of this portlet preference.
     * @param values  values of this portlet preference to set.
     */
    void setValues(String[] values);

    /**
     * Returns true if this portlet preference is marked as read-only.
     * @return true if this portlet preference is marked as read-only.
     */
    boolean isReadOnly();

    /**
     * Clone a copy of itself.
     * @return a copy of itself.
     */
    PortletPreference clone();

}
