package nc.uap.portal.util.freemarker.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.msg.provide.IMsgProvide;
import nc.uap.portal.plugins.PluginManager;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 计算新消息数量的方法
 * @author licza
 *
 */
public class GetNewMessageCount implements TemplateMethodModel{

	@Override
	public Object exec(List arg0) throws TemplateModelException {
	 
		String pluginId = (String)arg0.get(0);
		String category = (String)arg0.get(1);
		int count = 0;
		Map<String, Object> returnVal = new HashMap<String, Object>();
		try {
			 count = PluginManager.newIns().getExtension(pluginId).newInstance(IMsgProvide.class).getNewMessageCount(category);
		} catch (CpbBusinessException e) {
			LfwLogger.error("新消息数量计算失败 plugin:" + pluginId + " category : " + category, e);
			 
		}
		return count;
	}

}
