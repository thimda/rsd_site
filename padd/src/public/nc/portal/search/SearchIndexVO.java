package nc.portal.search;

import java.io.Serializable;
import java.util.Date;

import nc.uap.lfw.util.StringUtil;

public class SearchIndexVO implements Serializable{
	
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String BODY = "complex_text";
	public static final String ROLES = "roles"; // 角色 用于用户权限
	public static final String CATEGORY = "category"; // 内容索引分类
	public static final String URL = "url";
	
	/**
	 * 公共权限，可以被任何人访问
	 */
	public static final String PUBLIC_ROLE = "PUBLICROLE"; 
	
	private String id;	// 主键id
	private String title;	 // 标题
	private String[] briefInfo; // 简介	
	private String simplebriefInfo = "";//
	private String url; // 应用链接	
	private String[] roles; // 角色	
	private String[] category;// 分类
	private Date createDate; // 创建时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getBriefInfo() {
		return briefInfo;
	}
	public void setBriefInfo(String[] briefInfo) {
		if(briefInfo[0] != null){
			if(briefInfo[0].length() > 200){
				setSimplebriefInfo(briefInfo[0].substring(0,200) + "...");
			}
			else 
				setSimplebriefInfo(briefInfo[0]);
		}
		
		this.briefInfo = briefInfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String[] getRoles() {
		return roles;
	}
	
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	public String[] getCategory() {
		return category;
	}
	public void setCategory(String[] category) {
		this.category = category;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setBriefInfo(String briefInfo) {
		if(briefInfo != null){
			if(briefInfo.length() > 200){
				setSimplebriefInfo(briefInfo.substring(0,100) + "...");
			}
			else 
				setSimplebriefInfo(briefInfo);
		}
		this.briefInfo = new String[]{briefInfo};
	}
	public void setCategory(String category) {
		
		this.category = new String[]{category}; 
	}
	public String getSimplebriefInfo() {
		return simplebriefInfo;
	}
	public void setSimplebriefInfo(String info) {
		simplebriefInfo = info;
	}

}
