package nc.uap.portal.deploy.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
/**
 * Portal皮肤对象
 * @author licza
 *
 */
public class PortalskinVO extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3682131130813192868L;

	/**编码**/
	private String id;
	/**模块**/
	private String module;
	/**名称**/
	private String title;
	/**图标**/
	private String icon;
	/**是否激活**/
	private UFBoolean isactive;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public UFBoolean getIsactive() {
		return isactive;
	}
	public void setIsactive(UFBoolean isactive) {
		this.isactive = isactive;
	}
	@Override
	public String getPKFieldName() {
		return "";
	}
	@Override
	public String getTableName() {
		return "";
	}
	
}
