package nc.uap.portal.extention;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.funcres.extention.IFuncResExtentionService;
import nc.uap.cpb.org.vos.CpFuncResVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.vo.PtPortletVO;

/**
 * portlet扩展实现
 * 2012-1-13 上午09:18:41
 * @author limingf
 *
 */
public class PortletExtentionServiceImpl implements IFuncResExtentionService {

	@Override
	public CpFuncResVO[] getFuncResVos() {
		List<CpFuncResVO> list = new ArrayList<CpFuncResVO>();
		PtPortletVO[] portletvos = null;
		try {
			String pk_group = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
			portletvos = NCLocator.getInstance().lookup(IPtPortletQryService.class).getGroupPortlets(pk_group);
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		for(PtPortletVO tmp:portletvos){
			CpFuncResVO vo = new CpFuncResVO();
			vo.setPk_busifunc(tmp.getPk_portlet());
			vo.setCode(tmp.getParentid());
			vo.setName(tmp.getDisplayname());
			vo.setPk_funcres(tmp.getPk_portlet());
//			if(tmp.getParentid()!=null&&!"".equals(tmp.getParentid())){
//				vo.setPk_parent(tmp.getParentid());
//			}
			//else{
				vo.setPk_parent(null);
			//}
			vo.setType(getFuncResType());
			list.add(vo);
		}
		return list.toArray(new CpFuncResVO[0]);
	}

	@Override
	public int getFuncResType() {
		return IFuncResExtentionService.TYPE_PORTLET;
	}

}
