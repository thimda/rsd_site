package nc.uap.portal.integrate.credential;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Preference implements Cloneable, Serializable {
	private static final long serialVersionUID = -7052965674195343952L;
	private String name;
	private String[] values;
	//ÃèÊö
	private String description;
	private boolean readOnly;
	//×ÊÔ´id
	private String resourceid;
	
	public Preference()
	{}

	public Preference(String name, String value) {
		this(name, new String[] {value}, false, null, null);
	}
	
	public Preference(String name, String value, boolean readOnly) {
		this(name, new String[] {value}, readOnly, null, null);
	}

	public Preference(String name, String value, boolean readOnly, String desc, String resourceid) {
		this(name, new String[] {value}, readOnly, desc, resourceid);
	}

	public Preference(String name, String[] values) {
		this(name, values, false, null, null);
	}

	public Preference(String name, String[] values, boolean readOnly, String desc, String resourceid) {
		this.name = name;
		this.values = values;
		this.readOnly = readOnly;
		this.description = desc;
		this.resourceid = resourceid;
	}

	public String getName() {
		return name;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public boolean getReadOnly() {
		return readOnly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public Object clone() {
		return new Preference(name, values, readOnly, description, resourceid);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Preference) || (((Preference)obj).getName() == null))
			return false;
		return StringUtils.equals(((Preference)obj).getName(), this.name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String isResourceid() {
		return resourceid;
	}

	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
}
