package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;

/**
 * Portlet persistent preference store. Used in: portlet <p>Java class for portlet-preferencesType complex type. <p>The
 * following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;portlet-preferencesType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;preference&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}preferenceType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;preferences-validator&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}preferences-validatorType&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;id&quot; type=&quot;{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id$
 */
@XmlRootElement(name = "portlet-preferences")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Preferences", propOrder = { "preference", "preferencesValidator" })
public class Preferences implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3643967458694398435L;
	@XmlElement(name = "preference")
    protected List<Preference > preference;
    @XmlElement(name = "preferences-validator")
    protected String preferencesValidator;

    public Preference getPortletPreference(String name)
    {
        for (Preference p : getPortletPreferences())
        {
            if (p.getName().equals(name))
            {
                return p;
            }
        }
        return null;
    }
    
    public List<Preference > getPortletPreferences()
    {
        if (preference == null)
        {
            preference = new ArrayList<Preference >();
        }
        return preference;
    }
    
    public Preference addPreference(String name)
    {
        if (getPortletPreference(name) != null)
        {
            throw new IllegalArgumentException("Portlet preference with name: "+name+" already defined");
        }
        Preference  pref = new Preference ();
        pref.setName(name);
        preference.add(pref);
        return pref;        
    }
    
    public String getPreferencesValidator()
    {
        return preferencesValidator;
    }

    public void setPreferencesValidator(String value)
    {
        preferencesValidator = value;
    }
}
