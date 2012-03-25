package nc.uap.portal.container.portlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;




/**
 * <code>javax.portlet.PortletPreferences</code>接口的实现
 * 
 * @author licza
 * @see PortletPreferences
 */
public class PortletPreferencesImpl implements PortletPreferences {
	/**
	 * 从portlet.xml里得到到的默认配置 用来重置配置
	 */
	private Map<String, PortletPreference> defaultPreferences;

	/**
	 * 当前配置.
	 */
	private final Map<String, PortletPreference> preferences = new HashMap<String, PortletPreference>();

 
	public PortletPreferencesImpl() {
	}

	
	public boolean isReadOnly(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key can not be null");
		}
		PortletPreference pref = preferences.get(key);
		return (pref != null && pref.isReadOnly());
	}

	public String getValue(String key, String defaultValue) {
		String[] values = getValues(key, new String[] { defaultValue });
		String value = null;
		if (values != null && values.length > 0) {
			value = values[0];
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public String[] getValues(String key, String[] defaultValues) {
		if (key == null) {
			throw new IllegalArgumentException("key can not be null");
		}
		String[] values = null;
		PortletPreference pref = preferences.get(key);
		if (pref != null) {
			values = pref.getValues();
		}
		if (values == null) {
			values = defaultValues;
		}
		return values;
	}

	public void setValue(String key, String value) throws ReadOnlyException {
		if (isReadOnly(key)) {
			throw new ReadOnlyException("the preference is readonly");
		}
		PortletPreference pref = preferences.get(key);
		if (pref != null) {
			pref.setValues(new String[] { value });
		} else {
			pref = new PortletPreferenceImpl(key, new String[] { value });
			preferences.put(key, pref);
		}
	}

	public void setValues(String key, String[] values) throws ReadOnlyException {
		if (isReadOnly(key)) {
			throw new ReadOnlyException("the preference is readonly");
		}
		PortletPreference pref = preferences.get(key);
		if (pref != null) {
			pref.setValues(values);
		} else {
			pref = new PortletPreferenceImpl(key, values);
			preferences.put(key, pref);
		}
	}

	public Enumeration<String> getNames() {
		return new Vector<String>(preferences.keySet()).elements();
	}

	public Map<String, String[]> getMap() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (PortletPreference pref : preferences.values()) {
			map.put(pref.getName(), pref.getValues() != null ? pref.getValues()
					.clone() : null);
		}
		return Collections.unmodifiableMap(map);
	}

	public void reset(String key) throws ReadOnlyException {
		if (isReadOnly(key)) {
			throw new ReadOnlyException("属性"+key+"为只读");
		}
		PortletPreference p = defaultPreferences.get(key);
		if (p != null) {
			preferences.put(key, p.clone());
		}
		else {
			preferences.remove(key);
		}
	}

	/**
	 * 排序 若不使用Hash排序请重载此方法
	 * 
	 * @throws IllegalStateException
	 * @throws ValidatorException
	 * @throws IOException
	 */
	@Override
	public void store() throws IOException, ValidatorException {

	}
}
