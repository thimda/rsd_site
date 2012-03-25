package nc.uap.portal.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.servlet.StringServletResponse;


/**
 * 工具
 * @author licza
 *
 */
public final class ToolKit {
	/**
	 * 判断字符串是否不为空
	 * @param str
	 * @return
	 */
	public static boolean notNull(String str) {
		return str != null && str.trim().length() > 0;
	}
	
	/**
	 * 判断数组是否为空
	 * @param str
	 * @return
	 */
	public static final boolean notNull(Object[] os){
		return os != null && os.length > 0;
	}
	/**
	 * 判断集合是否为空
	 * @param cs
	 * @return
	 */
	public static final boolean notNull(Collection<?> cs){
		return cs != null && !cs.isEmpty();
	}
	
	/**
	 * 在其他数据源中执行方法
	 * @param o
	 * @param m
	 * @param params
	 * @param targetDs
	 * @param currentDs
	 * @return
	 * @throws Throwable
	 */
	public static final Object execMethodWithOtherDataSource(Object o, Method m, Object[] params, String targetDs, String currentDs) throws Throwable{
		InvocationInfoProxy.getInstance().setUserDataSource(targetDs);
		try {
			return m.invoke(o, params);
		} catch (IllegalArgumentException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		} catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		} catch (InvocationTargetException e) {
			LfwLogger.error(e.getMessage(), e);
			return null;
		}catch (Throwable e) {
			throw e;
		}finally{
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
	}
	/**
	 * 提取上下文中响应内容
	 * @param url
	 * @return
	 */
	/**
	 * 提取上下文中响应内容
	 * @param url
	 * @return
	 * @deprecated 请不要使用这个方法,即将删除
	 */
	public static final String contextResponseFetcher(String url){
		StringServletResponse stringResponse = new StringServletResponse(LfwRuntimeEnvironment.getWebContext().getResponse());
		HttpServletRequest request =  (LfwRuntimeEnvironment.getWebContext().getRequest());
		String html = null;
		try {
			request.getRequestDispatcher(url).forward(request, stringResponse);
			html =  new String(stringResponse.getString().getBytes("gbk"));
		} catch (Throwable e) {
			LfwLogger.error("尝试获取资源时出现异常" + e.getMessage(),e);
		}  finally{
			
		}
		return html;
	}
}

