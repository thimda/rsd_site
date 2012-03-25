package nc.uap.portal.container.driver;

import java.util.ArrayList;
import java.util.ListResourceBundle;

/**
 * InlinePortletResourceBundle implementation which provides the
 * inline title, short-title, and keywords as properties from the
 * bundle.
 *
 * @version 1.0
 * @since Jan 9, 2006
 */
class InlinePortletResourceBundle extends ListResourceBundle {

   private final static String TITLE_KEY = "javax.portlet.title";
   private final static String SHORT_TITLE_KEY = "javax.portlet.short-title";
   private final static String KEYWORDS_KEY = "javax.portlet.keywords";

    private Object[][] contents;

    public InlinePortletResourceBundle(Object[][] contents) {
        this.contents = contents;
    }

    public InlinePortletResourceBundle(String title, String shortTitle, String keywords) {
        ArrayList<Object[]> temp = new ArrayList<Object[]>();
        if(title != null)
            temp.add(new Object[] {TITLE_KEY, title});

        if(shortTitle != null)
            temp.add(new Object[] {SHORT_TITLE_KEY, shortTitle});

        if(keywords != null)
            temp.add(new Object[] {KEYWORDS_KEY, keywords});

        contents = temp.toArray(new Object[temp.size()][]);
    }

    protected Object[][] getContents() {
        return contents;
    }
}
