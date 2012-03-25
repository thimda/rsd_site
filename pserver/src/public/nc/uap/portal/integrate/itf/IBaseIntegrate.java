package nc.uap.portal.integrate.itf;

import nc.uap.portal.plugins.model.IDynamicalPlugin;

/**
 * 集成接口
 * <pre>Portal中有集成需求的程序,统一实现IBaseIntegrate接口</pre>
 * @author licza
 * 
 */
public interface IBaseIntegrate extends IDynamicalPlugin{
	/**
	 * 获得插件名称
	 * 
	 * @return
	 */
	public String getSystemName();

	/**
	 * 获得插件国际化名称
	 * 
	 * @return
	 */
	public String getI18nname();
	
	/**
	 * 获得在sso-prop.xml中配置的集成系统编码
	 * 
	 * <pre>
	 * 如果不需要单点集成,直接返回NULL
	 * </pre>
	 * 
	 * @return 系统编码
	 */
	public String getSystemCode();
	
	/**
	 * 得到共享级别
	 * 
	 * @return
	 */
	public Integer getSharelevel();
	
	/**
	 * 是否外部集成系统
	 * @return
	 */
	public boolean isIntegrateSystem();
	/**
	 * 用户是否放弃此应用
	 * @return
	 */
	public boolean isGiveUp();
 }
