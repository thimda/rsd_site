package nc.uap.portal.action.linktree;


import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.cpb.org.itf.ICpMenuQry;
import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.uap.portal.menu.util.LinkGroupDataWarp;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

/**
 * 链接树节点显示action
 * 
 */
@Servlet(path="/link/tree")
public class LinkTreeAction extends BaseAction{
	/**
	 * 显示分类
	 */
	@Action
	public void category(@Param(name="id") String parentid,@Param(name="tp")String tp){
		try {
			ICpMenuQry menuQry = NCLocator.getInstance().lookup(ICpMenuQry.class);
			MenuItemAdapterVO[] vos = menuQry.getMenuItemsByParent(parentid);
			if("outlook".equals(tp)){
				print(LinkGroupDataWarp.renderNode(vos));
			}else{
				Map root = new HashMap();
				root.put("nodes", vos);
				String html = null;
				html = FreeMarkerTools.contextTemplatRender("nc/uap/portal/portlets/linktree/floatTreeNode.html", root);
				print(html);
			}
		} catch (Exception e1) {
			LfwLogger.error(e1.getMessage(),e1);
		}
	}
	
	
//	/**
//	 * 安全用户比对过滤
//	 */
//	private PtLinknodeVO[] filterSecurityRole(PtLinknodeVO[] vos, String[] pks) {
//
//		List<PtLinknodeVO> secRoles = new ArrayList<PtLinknodeVO>();
//		for (int i = 0; i < vos.length; i++) {
//			PtLinknodeVO linkvo = vos[i];
//			if(!(linkvo.getPk_role() == null || linkvo.getPk_role().length() == 0 )){
//				if(filterRole(pks , linkvo))
//					secRoles.add(linkvo);
//			}else{
//				secRoles.add(linkvo);
//			}
//		}
//		return secRoles.toArray(new PtLinknodeVO[0]);
//
//	}
//
//	/**
//	 * 过滤角色
//	 */
//	private boolean filterRole(String[] pks,  PtLinknodeVO linkvo) {
//
//		for (int j = 0; j < pks.length; j++) {
//			if (linkvo.getPk_role().equals(pks[j])) {
//				return true;
//			}
//		}
//		return false;
//	}
	@Action
	public void menubar(){
		
	}
}
