package nc.uap.portal.vo;

import java.util.UUID;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * 第三方系统登录Portal信息对象
 * 
 * @author licza
 * 
 */
public class PtTrdauthVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8848788216478228257L;
	/**
	 * 主键
	 */
	private String pk_trdauth;
	/**
	 * 用户主键
	 */
	private String pk_user;
	/**
	 * 有效期
	 */
	private UFDateTime ttl;
	/**
	 * 目标URL
	 */
	private String url;
	/**
	 * 创建时间
	 */
	private UFDateTime creattime;
	/**
	 * 令牌号
	 */
	private String akey;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	/**
	 * 标题
	 */
	private String title;
	
	public PtTrdauthVO(){
		setPk_trdauth(UUID.randomUUID().toString().replaceAll("-", ""));
		setAkey(UUID.randomUUID().toString().replaceAll("-", ""));
	}

	public String getPk_trdauth() {
		return pk_trdauth;
	}

	public void setPk_trdauth(String pk_trdauth) {
		this.pk_trdauth = pk_trdauth;
	}

	public String getAkey() {
		return akey;
	}

	public void setAkey(String akey) {
		this.akey = akey;
	}

	public String getPk_user() {
		return pk_user;
	}

	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

	public UFDateTime getTtl() {
		return ttl;
	}

	public void setTtl(UFDateTime ttl) {
		this.ttl = ttl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UFDateTime getCreattime() {
		return creattime;
	}

	public void setCreattime(UFDateTime creattime) {
		this.creattime = creattime;
	}

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		return "pk_trdauth";
	}

	@Override
	public String getTableName() {
		return "pt_trdauth";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
