package nc.uap.portal.user.impl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.util.SecurityUtil;
import nc.uap.cpb.org.vos.CpResourceVO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.om.Page;
import nc.uap.portal.user.itf.IPortalPowerService;
import nc.uap.portal.util.PortalPageDataWrap;
import nc.uap.portal.util.PtUtil;

public class PtPortalPowerImpl implements IPortalPowerService {

	@Override
	public List<Page> filterPagesByUserResource(Page[] pages) {
		return filterPagesByUserResource(pages, getUserResource());
	}

	@Override
	public boolean hasPower(String originalid) {
		
		return hasPower(originalid, getUserResource());
	}
	private CpResourceVO[] getUserResource(){
		CpResourceVO[] resouces = null;
		try {
			resouces = CpbServiceFacility.getCpResourceQry().getResourcesByRoles(SecurityUtil.getRolePks()); 
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		return resouces;
	}
	private boolean hasPower(String originalid, CpResourceVO[] resouces) {
		if(!PtUtil.isNull(resouces)){
			for(CpResourceVO resouce : resouces){
				if(originalid.equals(resouce.getOriginalid()))
					return true;
			}
		}
		return false;
	}
	/**
	 * 根据用户获得的授权资源过滤Portal页面
	 * 
	 * @param pages
	 * @param resouces
	 */
	private List<Page> filterPagesByUserResource(Page[] pages, CpResourceVO[] resouces) {
		List<Page> pagelist = new ArrayList<Page>();
		if(PtUtil.isNull(pages))
			return pagelist; 
		
		for (Page page : pages) {
			/**
			 * 1.非个人权限的页面
			 * 2.不受权限控制的页面
			 */
			if((page.getLevel()!=null && page.getLevel().intValue()!=0) || page.getUndercontrol() == null || !page.getUndercontrol().booleanValue()){
				pagelist.add(page);
			}else{
				String originalid = "";
				if(PtUtil.isNull(page.getModule())){
					originalid =  page.getPagename();
				}else{
					originalid =PortalPageDataWrap.modModuleName(page.getModule(), page.getPagename());
				}
				if( hasPower(originalid, resouces))
					pagelist.add(page);
			}
		}
		return pagelist;
	}
}
