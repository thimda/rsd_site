package nc.uap.portal.container.driver;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import nc.uap.portal.util.ArgumentUtility;

/**
 *
 * @version 1.0
 * @since Jan 9, 2006
 */
class CombinedPortletResourceBundle extends ResourceBundle {

    private HashMap<String, Object> contents = new HashMap<String, Object>();

    public CombinedPortletResourceBundle(InlinePortletResourceBundle inlineBundle, ResourceBundle resourceBundle) {
       dump(inlineBundle);
       dump(resourceBundle);
    }

    protected Object handleGetObject(String key) {
        ArgumentUtility.validateNotNull("key", key);
        return contents.get(key);
    }

    public Enumeration<String> getKeys() {
       return Collections.enumeration(contents.keySet());
    }

    private void dump(ResourceBundle bundle) {
        Enumeration<String> e = bundle.getKeys();
        while(e.hasMoreElements()) {
            String value = e.nextElement().toString();
            contents.put(value, bundle.getObject(value));
        }
    }
}
