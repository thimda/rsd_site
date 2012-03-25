package nc.uap.portal.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.cache.INotifyAbleCache;
import nc.uap.portal.vo.PtModuleVO;

/**
 * PortalÄ£¿é»º´æ
 * @author licza
 *
 */
public class PtModuleCacheBuilder implements INotifyAbleCache{

	@Override
	public Object build() {
		Map<String,PtModuleVO> mc = new ConcurrentHashMap<String, PtModuleVO>();
		try {
			PtModuleVO[] vos = CRUDHelper.getCRUDService().queryVOs(new PtModuleVO(), null, null);
			if(vos == null || vos.length < 1)
				throw new NullPointerException("²éÑ¯PortalÄ£¿éÊ§°Ü!");
			for(PtModuleVO vo : vos){
				mc.put(vo.getModuleid(), vo);
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return mc;
	}

	@Override
	public String getKey() {
		return "PT_MODULE_CACHE_KEY";
	}

	@Override
	public String getNameSpace() {
		return "PT_MODULE_CACHE_NS";
	}

}
