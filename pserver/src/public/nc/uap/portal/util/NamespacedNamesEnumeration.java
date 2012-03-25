package nc.uap.portal.util;


import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:ate@douma.nu">Ate Douma</a>
 * @version $Id: NamespacedNamesEnumeration.java 763164 2009-04-08 10:10:39Z cziegeler $
 */
public class NamespacedNamesEnumeration implements Enumeration<String>
{
    private Enumeration<String> namesEnumeration;
    private String      namespace;

    private String nextName;
    private boolean done;

    public NamespacedNamesEnumeration(Enumeration<String> namesEnumeration, String namespace)
    {
        this.namesEnumeration = namesEnumeration;
        this.namespace = namespace;
        hasMoreElements();
    }

    public boolean hasMoreElements()
    {
        if (!done)
        {
            if (nextName == null)
            {
                while (namesEnumeration.hasMoreElements())
                {
                    String name = namesEnumeration.nextElement();
                    if ( name.startsWith(namespace))
                    {
                        nextName = name.substring(namespace.length());
                        break;
                    }
                }
                done = nextName == null;
            }
        }
        return !done;
    }

    public String nextElement()
    {
        if (done)
        {
            throw new NoSuchElementException();
        }
        String name = nextName;
        nextName = null;
        return name;
    }
}
