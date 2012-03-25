package nc.uap.portal.util.freemarker.functions;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.ILfwSsoQryService;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 在线人数数量
 * @author licza
 *
 */
public class OnlineUserCount implements TemplateMethodModel{

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		Integer count = null;
		try {
			String context = LfwRuntimeEnvironment.getRootPath();
			count = NCLocator.getInstance().lookup(ILfwSsoQryService.class).getOnlineUserCount(context);
		} catch (Exception e) {
			LfwLogger.error("获取在线人数错误!");
		}
		return count == null ? 1 : count;
	}
}
