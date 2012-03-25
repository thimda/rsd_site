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
 * The description element is used to provide text describing the parent element. The description element should include
 * any information that the portlet application war file producer wants to provide to the consumer of the portlet
 * application war file (i.e., to the Deployer). Typically, the tools used by the portlet application war file consumer
 * will display the description when processing the parent element that contains the description. It has an optional
 * attribute xml:lang to indicate which language is used in the description according to RFC 1766
 * (http://www.ietf.org/rfc/rfc1766.txt). The default value of this attribute is English(â€œenâ€?. Used in: init-param,
 * portlet, portlet-app, security-role <p>Java class for Description  complex type. <p>The following schema
 * fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Description &quot;&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base=&quot;&lt;http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd&gt;string&quot;&gt;
 *       &lt;attribute ref=&quot;{http://www.w3.org/XML/1998/namespace}lang&quot;/&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * @version $Id: Description .java 947094 2010-05-21 17:50:35Z edalquist $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Description ", propOrder = { "value" })
public class Description implements   Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3046809957881067711L;
	@XmlValue
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    protected String value;
    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    protected String lang = null;
    @XmlTransient
    protected Locale locale;

    public String getDescription()
    {
        return value;
    }

    public void setDescription(String value)
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
        deriveLocale();
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
