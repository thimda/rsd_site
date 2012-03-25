package nc.uap.portal.container.portlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.w3c.dom.Element;

public class ExtendPropertySupport {
	private Map<String, Object> propMap = new HashMap<String, Object>();

	protected void add2Property(String key, Object value) {
		propMap.put(key, value);
	}

	protected void add2Property(String key, Element ele) {
		propMap.put(key, ele);
	}

	protected void add2Property(Cookie cookie) {
		propMap.put(cookie.getName(), cookie);
	}

	protected void set2Property(String key, Object value) {
		propMap.put(key, value);
	}

	protected Object get2Property(String key) {
		return propMap.get(key);
	}

}
