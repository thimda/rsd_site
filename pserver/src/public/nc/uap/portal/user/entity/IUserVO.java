package nc.uap.portal.user.entity;

import java.io.Serializable;

/**
 * Portal用户
 * 
 * @author licza
 * @since NC6.1
 */
public interface IUserVO extends Serializable {
	

	/** 身份认证方式 - 静态密码。 */
	public static final Integer AUTHENMODE_STATICPWD = Integer.valueOf(0);

	/** 身份认证方式 - CA。 */
	public static final Integer AUTHENMODE_CAAUTHEN = Integer.valueOf(1);
	
//	// 集团用户
	public final static Integer USERTYPE_GROUP = Integer.valueOf(1);
//	// 公司用户
//	public final static Integer USERTYPE_CORP = 1;
	// 系统管理员
	public final static Integer USERTYPE_SYSADMIN = Integer.valueOf(2);
	// 集团管理员
	public final static Integer USERTYPE_GROUPADMIN = Integer.valueOf(0);
//	// 普通管理员
//	public final static Integer USERTYPE_ADMIN = 4;
	/**
	 * 获得用户主键
	 * 
	 * @return
	 */
	String getPk_user();
	
	/**
	 * 获得用户集团
	 * @return
	 */
	String getPk_group();
	
	/**
	 * 获得语言
	 * @return
	 */
	String getLangcode();
	
	/**
	 * 获得登录名
	 * @return
	 */
	String getUserid();
	/**
	 * 获得用户名称
	 * @return
	 */
	String getUsername();
	/**
	 * 获得用户密码
	 * @return
	 */
	String getPassword();
	/**
	 * 获得用户类型
	 * @return
	 */
	Integer getUsertype();
	
}
