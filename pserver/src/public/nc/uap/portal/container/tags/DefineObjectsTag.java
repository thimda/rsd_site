package nc.uap.portal.container.tags;

/**
 * Backwards compatibility JSR-168 DefineObjectsTag provided for old
 * nc portal 1.0.1 portlet.tld usages.
 * <p>
 * Although a portlet.tld should not be provided by Portlet Applications
 * themselves but "injected" by the target portlet container, fact is many
 * have them embedded in the application anyway. This class ensures those
 * applications still can use their old portlet.tld.
 * </p>
 * @version $Id: DefineObjectsTag.java 759873 2009-03-30 08:35:37Z cziegeler $
 * @deprecated
 */
public class DefineObjectsTag extends DefineObjectsTag168
{
    private static final long serialVersionUID = 286L;

    public static class TEI extends DefineObjectsTag168.TEI
    {
    }
}
