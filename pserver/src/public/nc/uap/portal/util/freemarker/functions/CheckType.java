package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 取参数的类名(FreeMarker模板方法)
 * 
 * @author licza.
 * 
 */
public class CheckType implements TemplateMethodModel {
	@SuppressWarnings("unchecked")
	@Override
	public Object exec(final List arg) throws TemplateModelException {
		final String typeName = arg.get(0).toString().toLowerCase();
		return typeName;
	}
}
