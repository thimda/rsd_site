package nc.uap.portal.integrate.ref;

import nc.uap.lfw.reference.base.TreeGridRefModel;

/**
 * 2010-8-18 上午09:32:46  limingf
 */

public class PtUserRefModel extends TreeGridRefModel{
		
	private String wherePart;
	private String classwherePart;
	private String classJoinValue;
	
	@Override
	public int getFieldIndex(String field) {
		return -1;
	}

	@Override
	public String[] getFilterFields() {
	    
		return null;
	}

	@Override
	public String getFixedWherePart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGroupPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getHiddenFieldCodes() {
		return new String[]{"pk_user"};
	}

	@Override
	public String getOrderPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuerySql() {
		return null;
	}

	@Override
	public String getRefCodeField() {
		return "userid";
	}

	@Override
	public String getRefNameField() {
		return "username";
	}

	@Override
	public String getRefPkField() {
		return "pk_user";
	}
	
    
	@Override
	public String getStrPatch() {
		return "";
	}

	@Override
	public String getTablesString() {
		return "pt_user";
	}

	@Override
	public String[] getVisibleFieldCodes() {
		String[] visibleFields = {"userid","username"};
		return visibleFields;
	}

	@Override
	public String getWherePart() {
		return wherePart;
	}

	@Override
	public void setWherePart(String wherePart) {
		this.wherePart = wherePart;
	}

	@Override
	public String getClassChildField() {
		// TODO Auto-generated method stub
		return "pk_group";
	}

	@Override
	public String getClassFatherField() {
		// TODO Auto-generated method stub
		return "pk_parent";
	}

	@Override
	public String getClassJoinField() {
		// TODO Auto-generated method stub
		return "pk_group";
	}

	@Override
	public String getClassJoinValue() {
		// TODO Auto-generated method stub
		return classJoinValue;
	}

	@Override
	public String getClassRefCodeField() {
		// TODO Auto-generated method stub
		return "groupcode";
	}

	@Override
	public String getClassRefNameField() {
		// TODO Auto-generated method stub
		return "groupname";
	}

	@Override
	public String getClassRefPkField() {
		// TODO Auto-generated method stub
		return "pk_group";
	}

	@Override
	public String getClassRootName() {
		// TODO Auto-generated method stub
		return "集团";
	}

	@Override
	public String getClassTableName() {
		// TODO Auto-generated method stub
		return "pt_group";
	}

	@Override
	public String getDocJoinField() {
		// TODO Auto-generated method stub
		return "pk_group";
	}

	@Override
	public void setClassJoinValue(String keyValue) {
		this.classJoinValue = keyValue;
	}

	@Override
	public String[] getHiddenFieldNames() {
		return new String[]{"用户主键"};
	}

	@Override
	public String[] getVisibleFieldNames() {
		String[] visibleNames = {"用户编码", "用户名称"};
		return visibleNames;
	}

	@Override
	public String getClassWherePart() {
		return classwherePart;
	}

	@Override
	public void setClassWherePart(String classwherePart) {
		this.classwherePart = classwherePart;
	}

}
