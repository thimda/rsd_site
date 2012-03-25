package nc.uap.portal.menu.util;

import java.util.HashMap;
import java.util.Map;

import nc.uap.cpb.org.vos.MenuItemAdapterVO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.util.freemarker.FreeMarkerTools;

/**
 * 链接组数据包装类
 * 
 * @author licza
 * 
 */
public class LinkGroupDataWarp {
	/** 头 **/
	public static final String TYPE_HEAD = "head";
	/** 底 **/
	public static final String TYPE_FOOT = "foot";
	/** 树 **/
	public static final String TYPE_TREE = "tree";
	/** 节点 **/
	public static final String TYPE_NODE = "node";
	public static final String WINDOW_ID = "windowId";
	public static final String ROOT_NAME = "rootName";
	public static final String ROOT_NODE = "ROOT_NODE";
	public static final String RENDER_TYPE = "renderType";
	
	public static final String VIEW_FTL = "nc/uap/portal/portlets/linktree/linkTree.ftl";
	/**
	 * 链接树渲染
	 * @param vos 
	 * @param rendertype  显示模式
	 * @param name 
	 * @return
	 */
	public static String renderLinkTreeView(MenuItemAdapterVO[] vos ,String name , String rendertype){
		Map<String,Object> root = new HashMap<String, Object>();
		if(vos != null){
			root.put(ROOT_NODE, vos);
		}
		root.put(RENDER_TYPE, rendertype);
		if(TYPE_HEAD.equals(rendertype))
			root.put(WINDOW_ID, name);
		
		if(TYPE_TREE.equals(rendertype))
			root.put(ROOT_NAME, name);
		try {
			return (FreeMarkerTools.contextTemplatRender(VIEW_FTL, root));
		} catch (PortalServiceException e) {
			LfwLogger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 渲染树
	 * @param vos
	 * @param title
	 * @return
	 */
	public static String renderTree(MenuItemAdapterVO[] vos ,String title){
		return renderLinkTreeView(vos, title, TYPE_TREE);
	}
	
	/**
	 * 渲染节点
	 * @param vos
	 * @return
	 */
	public static String renderNode(MenuItemAdapterVO[] vos){
		return renderLinkTreeView(vos, null, TYPE_NODE);
	}
	
	/**
	 * 渲染头部
	 * @param windowId
	 * @return
	 */
	public static String renderHead(String windowId){
		return renderLinkTreeView(null, windowId, TYPE_HEAD);
	}
	/**
	 * 渲染底部
	 * @return
	 */
	public static String renderFoot(){
		return renderLinkTreeView(null, null, TYPE_FOOT);
	}
}
