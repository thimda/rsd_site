package nc.uap.portal.container.driver;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import nc.bs.ml.NCLangResOnserver;
import nc.uap.portal.util.ArgumentUtility;
import nc.vo.ml.Language;

/**
 * Portlet默认资源 <br/>
 * 这里使用NC翻译来实现
 * <pre>
 * 多语资源规则: 
 * 多语文件名:&quot;/lang/语种id/模块名/模块名res.properties&quot;
 * 键:portlet名称+&quot;.&quot;+key
 * 
 * TCK PORTLET：SPEC:24:假如使用 ResourceBundle 资源文件来定义， 则Portlet container
 * 必须先查找在ResourceBundle里定义的信息。 若是 ResourceBundle没有这些信息，或是没有使用
 * ResourceBundle来定， 则 portlet container 必须查找写在定义文件里的信息。 假如
 * ResourceBundle 和定义文件里都没有定义这些信息， 则 portlet container 必须以空字符串来回传。
 * 假如在定义portlet时，没有定义resource bundle，而是把信息定义在部署定义文件中， 此时 portlet
 * container 则必须产生一个 ResourceBundle ，并且把这些信息放进来。
 * </pre>
 * 
 * @author licza
 * 
 */
public class DefaultPortletResourceBundle extends ResourceBundle {
	private static final String DOT = ".";
	/**
	 * 语言
	 */
	private String lan;
	/**
	 * Portlet ID
	 */
	private String portlet;
	/**
	 * 模块
	 */
	private String module;
	/**
	 * 存储资源
	 */
	private Map<String, Object> contents = new LinkedHashMap<String, Object>();
	
	/**
	 * Portal默认资源束
	 * @param dft 默认资源
	 * @param locale 国际
	 * @param module 模块
	 * @param portlet portlet名
	 */
	public DefaultPortletResourceBundle(InlinePortletResourceBundle dft,
			Locale locale, String module, String portlet) {
		this.module = module;
		this.portlet = portlet;
		/**
		 * 导入
		 */
		dump(dft);
		/**
		 * 设置语言编码
		 */
		getLangCode(locale);
	}

	/**
	 * 初始化当前语言编码
	 * 
	 * @param locale
	 */
	private void getLangCode(Locale locale) {
		/**
		 * 获得所有支持的语言
		 */
		Language[] langs = getLangRes().getAllLanguages();
		if (langs != null && langs.length > 0) {
			for (Language lang : langs) {
				/**
				 * 如果相同
				 */
				if (lang.getLocal().equals(locale)) {
					this.lan = lang.getCode();
				}
			}
		}
		/**
		 * 默认简体中文
		 */
		if (this.lan == null)
			this.lan = Language.SIMPLE_CHINESE_CODE;
	}

	@Override
	public Enumeration<String> getKeys() {
		/**
		 * !这个地方获得的键可能不正确
		 */
		return Collections.enumeration(contents.keySet());
	}

	@Override
	protected Object handleGetObject(String key) {
		ArgumentUtility.validateNotNull("key", key);
		/**
		 * 生成真正的NC多语key
		 */
		String resId = portlet + DOT + key;
		
		/**
		 * 从默认bundle中获得的值
		 */
		String dftval = (String) contents.get(key);
		String value = getLangRes().getString(lan, module, dftval, resId);
		return value;
	}

	/**
	 * 获取NC语言资源类实例
	 * @return
	 */
	private NCLangResOnserver getLangRes() {
		return NCLangResOnserver.getInstance();
	}

	/**
	 * 导入资源
	 * 
	 * @param bundle
	 */
	private void dump(ResourceBundle bundle) {
		Enumeration<String> e = bundle.getKeys();
		while (e.hasMoreElements()) {
			String value = e.nextElement().toString();
			contents.put(value, bundle.getObject(value));
		}
	}
}
