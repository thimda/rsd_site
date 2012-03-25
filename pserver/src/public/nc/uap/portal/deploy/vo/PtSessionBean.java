package nc.uap.portal.deploy.vo;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.UserExit;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.portal.user.entity.IUserVO;
import nc.vo.pub.lang.UFDate;
/**
 * Portal会话Bean
 *
 */
public class PtSessionBean extends LfwSessionBean {
	private static final long serialVersionUID = -886197543648091760L;
	private IUserVO user;
	private Integer userType;
	private String groupNo;
	private UFDate loginDate;
	private String datasource;
	public IUserVO getUser() {
		return user;
	}
	public void setUser(IUserVO user) {
		this.user = user;
	}
	@Override
	public String getPk_unit() {
		return user.getPk_group();
	}
	
 	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	@Override
	public String getLangId() {
		return user.getLangcode();
	}
	@Override
	public UFDate getLoginDate() {
		return this.loginDate;
	}
	@Override
	public String getPk_user() {
		return user.getPk_user();
	}
	@Override
	public String getThemeId() {
//		return "webclassic";
		return null;
	}
	@Override
	public String getUser_code() {
		return user.getUserid();
	}
	@Override
	public String getUser_name() {
		return user.getUsername();
	}
	public void setLoginDate(UFDate loginDate) {
		this.loginDate = loginDate;
	}
	@Override
	public String getDatasource() {
		return datasource;
	}
	
	public void setDatasource(String datasource){
		this.datasource = datasource;
	}
	/**
	 * 更新本地信息，有些系统并不是从LfwRuntimeEnvironment中取环境变量，这种情况需要将变量映射到具体环境中。
	 * 本方法每个非资源请求都会调用
	 */
	public void fireLocalEnvironment() {
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);
		InvocationInfoProxy.getInstance().setGroupId(getPk_unit());
		InvocationInfoProxy.getInstance().setBizDateTime(UserExit.getInstance().getBizDateTime());
	}
}
