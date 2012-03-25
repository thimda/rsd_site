package nc.uap.portal.integrate.credential;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 凭证Reference的实�?
 * @author yzy Created on 2006-02-26
 */
public class CredentialReferenceImpl implements Cloneable, ICredentialReference,
		Serializable {

	static final long serialVersionUID = -5698348841696420785L;
	private static String NULL_VALUE = "NULL";
	//凭证ID
	private Long credentialId;
	//凭证的reference的实体MAP
	private Map preferences = null;

	public CredentialReferenceImpl() {
		this(null, new HashMap());
	}

	public CredentialReferenceImpl(Long credentialId, Map preferences) {
		this.credentialId = credentialId;
		this.preferences = preferences;
	}

	public Enumeration getNames() {
		return Collections.enumeration(preferences.keySet());
	}

	public String getValue(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		Preference preference = (Preference) preferences.get(key);
		String[] values = null;
		if (preference != null) {
			values = preference.getValues();
		}
		if (values != null && values.length > 0) {
			return getActualValue(values[0]);
		} else {
			return null;
		}
	}

	public void setValue(String key, String value) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		value = getXmlSafeValue(value);
		Preference preference = (Preference) preferences.get(key);
		if (preference == null) {
			preference = new Preference(key, value);
			preferences.put(key, preference);
		}
		preference.setValues(new String[] { value });
	}

	public String[] getValues(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		Preference preference = (Preference) preferences.get(key);
		String[] values = null;
		if (preference != null) {
			values = preference.getValues();
		}
		if (values != null && values.length > 0) {
			return getActualValues(values);
		} else {
			return null;
		}
	}

	public void setValues(String key, String[] values) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		values = getXmlSafeValues(values);
		Preference preference = (Preference) preferences.get(key);
		if (preference == null) {
			preference = new Preference(key, values);
			preferences.put(key, preference);
		}
		preference.setValues(values);
	}

	public Map getMap() {
		Map map = new HashMap();
		Iterator itr = preferences.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			String key = (String) entry.getKey();
			Preference preference = (Preference) entry.getValue();
			map.put(key, getActualValues(preference.getValues()));
		}
		return Collections.unmodifiableMap(map);
	}

	public void reset(String key) {
	}

	public void store() {
	}

	private String getActualValue(String value) {
		if ((value == null) || (value.equalsIgnoreCase(NULL_VALUE))) {
			return null;
		} else {
			return value;
		}
	}

	private String[] getActualValues(String[] values) {
		if (values == null) {
			return null;
		}
		if ((values.length == 1) && (getActualValue(values[0]) == null)) {
			return null;
		}
		String[] actualValues = new String[values.length];
		System.arraycopy(values, 0, actualValues, 0, values.length);
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] = getActualValue(actualValues[i]);
		}
		return actualValues;
	}

	private String getXmlSafeValue(String value) {
		if (value == null) {
			return NULL_VALUE;
		} else {
			return value;
		}
	}

	private String[] getXmlSafeValues(String[] values) {
		if (values == null) {
			return new String[] { getXmlSafeValue(null) };
		}
		String[] xmlSafeValues = new String[values.length];
		System.arraycopy(values, 0, xmlSafeValues, 0, values.length);
		for (int i = 0; i < xmlSafeValues.length; i++) {
			if (xmlSafeValues[i] == null) {
				xmlSafeValues[i] = getXmlSafeValue(xmlSafeValues[i]);
			}
		}
		return xmlSafeValues;
	}

	public Object clone() {
		Map preferencesClone = new HashMap();
		Iterator itr = preferences.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			String key = (String) entry.getKey();
			Preference preference = (Preference) entry.getValue();
			preferencesClone.put(key, preference.clone());
		}
		return new CredentialReferenceImpl(credentialId, preferencesClone);
	}

	public Long getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(Long credentialId) {
		this.credentialId = credentialId;
	}

	public Map getPreferences() {
		return preferences;
	}

	public void setPreferences(Map preferences) {
		this.preferences = preferences;
	}
}
