package nc.uap.portal.integrate.system;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 第三方系统登出类
 * <pre>
 * 如果第三方系统注册了登陆路径，则注销页面生成一组嵌入注销页面的IFrame，实现Portal和其集成的第三方系统的同时登出。
 * </pre>
 * @author licza
 *
 */
public class OtherSystemLogout {
	/** 注销地址 **/
	public static final String LOGOUT_URL = "LOGOUT_URL";
	/**
	 * 获得其他系统登陆注销脚本片段
	 * @param pool
	 * @return
	 */
	 StringBuffer makeOtherSysLogoutPart(Map<String, SSOProviderVO> pool) {
		StringBuffer output = new StringBuffer();
		String head = "<iframe  width='0' height='0'  frameborder='0' vspace='0' hspace='0' scrolling='no' src='";
		String tile = "'></iframe>";
		for(SSOProviderVO provider : pool.values()){
			String logoutUrl = provider.getValue(LOGOUT_URL);
			/**
			 * 如果配置了注销地址
			 */
			if(logoutUrl != null && logoutUrl.length() > 0) 
				output.append(head+logoutUrl+tile);
		}
		return output;
	}
	/**
	 * 获得第三方系统的注销脚本
	 * @return
	 */
	public String getOtherSysLogoutScript(){
		Map<String, SSOProviderVO> pool = ProviderFetcher.getInstance().getProvidersPool();
		if(pool == null || pool.isEmpty()){
			return StringUtils.EMPTY;
		}else{
			return makeOtherSysLogoutPart(pool).toString();
		}
		
	}
	
}
