package nc.uap.portal.extention;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.funcres.extention.IFuncResExtentionService;
import nc.uap.cpb.org.vos.CpFuncResVO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortalPageQryService;
import nc.uap.portal.vo.PtPageVO;


/**
 * portalpage扩展实现
 * 2012-1-13 上午09:18:06
 * @author limingf
 *
 */
public class PortalPageExtentionServiceImpl implements IFuncResExtentionService {

	@Override
	public CpFuncResVO[] getFuncResVos() {
		List<CpFuncResVO> list = new ArrayList<CpFuncResVO>();
		PtPageVO[] pagevos = null;
		try {
			pagevos = NCLocator.getInstance().lookup(IPtPortalPageQryService.class).getPageVOList();
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		for(PtPageVO tmp:pagevos){
			CpFuncResVO vo = new CpFuncResVO();
			vo.setPk_busifunc(tmp.getPk_portalpage());
			vo.setCode(tmp.getPagename());
			vo.setName(tmp.getTitle());
			vo.setPk_funcres(tmp.getPk_portalpage());
//			if(tmp.getParentid()!=null&&!"".equals(tmp.getParentid())){
//				vo.setPk_parent(tmp.getParentid());
//			}
		//	else{
				vo.setPk_parent(null);
		//	}
			vo.setType(getFuncResType());
			list.add(vo);
		}
		return list.toArray(new CpFuncResVO[0]);		
	}

	@Override
	public int getFuncResType() {
		return IFuncResExtentionService.TYPE_PORTALPAGE;
	}


}
