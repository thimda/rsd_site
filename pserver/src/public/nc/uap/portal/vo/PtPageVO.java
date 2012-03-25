package nc.uap.portal.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * PortalPage实体
 * 
 * @author licza
 * 
 */
public class PtPageVO extends SuperVO {
	private static final long serialVersionUID = -1053941326364539256L;

	/** 页面名称 */
	private String pagename;
	/** 用户编号 */
	private String fk_pageuser;
	/** 版本 */
	private String version;
	/** 页面配置 */
	private byte[] settings;
	
	/** 源页面 */
	private String parentid;
	/** 创建时间 */
	private UFDate createdate;
	/** 页面标题 **/
	private String title;
	/** 是否主页 **/
	private UFBoolean isdefault;
	/** 模块名称 */
	private String module;
	/** 组织机构 **/
	private String pk_group;
	/** 新版本 **/
	private String newversion;
	/** 级别 **/
	private Integer levela;
	/** 级别 **/
	private Integer ordernum;
	/** 接受权限控制 **/
	private UFBoolean undercontrol;
	/** 是否可用 **/
	private UFBoolean activeflag;

	
	public static final String PAGENAME = "pagename";
	public static final String FK_PAGEUSER = "fk_pageuser";
	public static final String VERSION = "version";
	public static final String PARENTID = "parentid";
	public static final String CREATEDATE = "createdate";
	public static final String TITLE = "title";
	public static final String ISDEFAULT = "isdefault";
	public static final String MODULE = "module";
	public static final String PK_GROUP = "pk_group";
	public static final String NEWVERSION = "newversion";
	public static final String LEVELA = "levela";
	public static final String ORDERNUM = "ordernum";
	public static final String UNDERCONTROL = "undercontrol";
	public static final String PK_PORTALPAGE = "pk_portalpage";
	public static final String ACTIVE = "active";
	
	
	public String getNewversion() {
		return newversion;
	}

	public void setNewversion(String newversion) {
		this.newversion = newversion;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	/** ts */
	private UFDateTime ts; // char(20)
	/** dr */
	private UFBoolean dr;

	/** pk_portalpage */
	private String pk_portalpage;

	@Override
	public String getPKFieldName() {
		return "pk_portalpage";
	}

	@Override
	public String getTableName() {
		return "pt_portalpage";
	}

	public String getPk_portalpage() {
		return pk_portalpage;
	}

	public void setPk_portalpage(String pk_portalpage) {
		this.pk_portalpage = pk_portalpage;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getFk_pageuser() {
		return fk_pageuser;
	}

	public void setFk_pageuser(String fk_pageuser) {
		this.fk_pageuser = fk_pageuser;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public byte[] getSettings() {
		return settings;
	}

	public void setSettings(byte[] settings) {
		this.settings = settings;
	}

	public String doGetSettingsStr() {
		return new String(this.settings);
	}
	
	public void doSetSettingsStr(String settings){
		this.settings = settings.getBytes();
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public UFDate getCreatedate() {
		return createdate;
	}

	public void setCreatedate(UFDate createdate) {
		this.createdate = createdate;
	}

	public UFBoolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(UFBoolean isdefault) {
		this.isdefault = isdefault;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public UFBoolean getDr() {
		return dr;
	}

	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLevela() {
		return levela;
	}

	public void setLevela(Integer level) {
		this.levela = level;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public UFBoolean getUndercontrol() {
		return undercontrol;
	}

	public void setUndercontrol(UFBoolean undercontrol) {
		this.undercontrol = undercontrol;
	}

	public UFBoolean getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(UFBoolean activeflag) {
		this.activeflag = activeflag;
	}

}
