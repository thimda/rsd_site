package nc.uap.portal.util.freemarker.functions;

import java.util.List;
import java.util.Map;

import nc.uap.portal.cache.PortalCacheProxy;
import nc.uap.portal.cache.impl.PtModuleCacheBuilder;
import nc.uap.portal.vo.PtModuleVO;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 检查Portlet权限
 * 
 * @author licza
 * 
 */
public class CheckPower implements TemplateMethodModel {

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		String portletid = (String) arguments.get(0);
		String module = (String) arguments.get(1);
		if(portletid.indexOf(":") != -1)
			module = portletid.split(":")[0];
		
		return checkModulePower(module) && checkRight();
	}
	
	/**
	 * 检查模块是否启用
	 * @param module
	 * @return
	 */
	boolean checkModulePower(String module) {
		Map<String,PtModuleVO> mc = (Map<String,PtModuleVO>)PortalCacheProxy.newIns().get(new PtModuleCacheBuilder());
		return mc.containsKey(module) && mc.get(module).getActiveflag().booleanValue();
	}

	boolean checkRight() {
		return true;
	}
}
