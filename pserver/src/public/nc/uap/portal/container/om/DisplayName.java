package nc.uap.portal.container.om;

import java.io.Serializable;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

 
/**
 * The display-name type contains a short name that is intended to be displayed by tools. It is used by display-name
 * elements. The display name need not be unique. Example: ...
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;display-name xmlns="http://www.w3.org/2001/XMLSchema" xmlns:portlet="http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd" xmlns:xs="http://www.w3.org/2001/XMLSchema" xml:lang="en"&gt;Employee Self Service&lt;/display-name&gt;
 * </pre>
 * 
 * It has an optional attribute xml:lang to indicate which language is used in the description according to RFC 1766
 * (http://www.ietf.org/rfc/rfc1766.txt). The default value of this attribute is English(â€œenâ€?. <p>Java class for
 * display-nameType complex type. <p>The following schema fragment specifies the expected content contained within this
 * class.
 * 
 * <pre>
 * &lt;complexType name=&quot;display-nameType&quot;&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base=&quot;&lt;http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd&gt;string&quot;&gt;
 *       &lt;attribute ref=&quot;{http://www.w3.org/XML/1998/namespace}lang&quot;/&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id: DisplayName .java 947094 2010-05-21 17:50:35Z edalquist $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "display-nameType", propOrder = { "value" })
public class DisplayName implements  Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6252398551118810577L;
	@XmlValue
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String value;
    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang = null;
    @XmlTransient
    protected Locale locale;

    public String getDisplayName()
    {
        return value;
    }

    public void setDisplayName(String value)
    {
        this.value = value;
    }

    public String getLang()
    {
        return lang == null ? Locale.ENGLISH.toString() : lang;
    }

    public void setLang(String value)
    {
        lang = value;
    }

    public Locale getLocale()
    {
        return locale == null ? deriveLocale() : locale;
    }
    
    protected Locale deriveLocale()
    {
        Locale locale = null;
        String lang = this.getLang();
        if (lang != null)
        {
            String country = "";
            String variant = "";
            String[] localeArray = lang.split("[-|_]");
            for (int i = 0; i < localeArray.length; i++)
            {
                if (i == 0)
                {
                    lang = localeArray[i];
                }
                else if (i == 1)
                {
                    country = localeArray[i];
                }
                else if (i == 2)
                {
                    variant = localeArray[i];
                }
            }
            locale = new Locale(lang, country, variant);
        }
        return this.locale = locale;
    }
}
