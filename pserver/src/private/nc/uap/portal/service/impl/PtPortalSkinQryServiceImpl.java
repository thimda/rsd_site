package nc.uap.portal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.constant.CacheKeys;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Skin;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPtPortalSkinQryService;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.vo.PtSkinVo;

/**
 * 样式查询实现类
 * 
 * @author dingrf
 * 
 */

public class PtPortalSkinQryServiceImpl implements IPtPortalSkinQryService {

	@Override
	public PtSkinVo find(String module, String themeId, String type ,String skinId)
			throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtSkinVo> list = (List<PtSkinVo>)dao.retrieveByClause(PtSkinVo.class, "modulename = '"+ module +"' and themeid = '"+themeId+"' and type = '"+ type +"' and skinid='" + skinId + "'" );
			if(!PtUtil.isNull(list))
				return list.toArray(new PtSkinVo[0])[0];
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new  PortalServiceException("查询样式失败!");
		}
		return null;
	}
	
	@Override
	public PtSkinVo[] findByType(String type)throws PortalServiceException {
		PtBaseDAO dao = new PtBaseDAO();
		try {
			List<PtSkinVo> list = (List<PtSkinVo>)dao.retrieveByClause(PtSkinVo.class, "type = '"+ type +"'" );
			if(!PtUtil.isNull(list))
				return list.toArray(new PtSkinVo[0]);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(),e);
			throw new  PortalServiceException("查询样式失败!");
		}
		return null;
	}
	
	@Override
	public Skin[] getSkinCache(String type) {
		String dsName = LfwRuntimeEnvironment.getDatasource();
		ILfwCache cache = LfwCacheManager.getStrongCache(CacheKeys.PORTAL_SKINS_CACHE, dsName);
		Skin[] skins =	(Skin[])cache.get(type);
		if (skins==null){
			PtSkinVo[] skinVo = null;
			try {
				skinVo = findByType(type);
			} catch (PortalServiceException e) {
				LfwLogger.error("===PtPortalSkinQryServiceImpl#getSkinCache===获得skin错误:" + e.getMessage() ,e );
			}
			if (skinVo==null)
				return null;
			List<Skin> skinList = new ArrayList<Skin>();
			for (int i = 0; i< skinVo.length ; i ++){
				Skin s = new Skin();
				s.setId(skinVo[i].getSkinid());
				s.setName(skinVo[i].getSkinname());
				skinList.add(s);
			}
			skins = skinList.toArray(new Skin[0]);
			cache.put(type, skins);
		}
		return skins;
	}
	

}
