package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * »ñµÃPortletId
 * 
 * @author licza
 * 
 */
public class GetPortletId implements TemplateMethodModel {
	@SuppressWarnings("unchecked")
	public Object exec(List param) throws TemplateModelException {
		String pageModule = (String) param.get(0);
		String pageName = (String) param.get(1);
		String portletName = (String) param.get(2);
		String portletModule = pageModule;
		if (portletName.indexOf(":") > 0) {
			String[] sp = portletName.split(":");
			if (StringUtils.isNotBlank(sp[0])) {
				portletModule = sp[0];
			}
			portletName = sp[1];
		}
		StringBuffer sb = new StringBuffer();
		sb.append(pageModule);
		sb.append("_");
		sb.append(pageName);
		sb.append("_");
		sb.append(portletModule);
		sb.append("_");
		sb.append(portletName);
		return sb.toString();
	}
}
