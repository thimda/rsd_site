package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 获得绝对链接
 * @author licza
 *
 */
public class Href implements TemplateMethodModel {

	@SuppressWarnings("unchecked")
	@Override
	public Object exec(List urls) throws TemplateModelException {
		String url = urls.get(0).toString();
		return LfwRuntimeEnvironment.getRootPath() + url;
	}
}
