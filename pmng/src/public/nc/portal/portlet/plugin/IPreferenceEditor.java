package nc.portal.portlet.plugin;

import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.data.Dataset;

/**
 * Portlet配置编辑
 * 
 * @author licza
 * 
 */
public interface IPreferenceEditor {
	
	public static final String PID = "IPreferenceEditor";
	
	/**
	 * 可处理的PortletID
	 */
	public String getPortletId();
	
	/**
	 * 处理FormElement
	 * 
	 * @param ele
	 */
	public void processFormElement(FormElement ele);

	/**
	 * 保存前数据操作
	 * 
	 * @param parentDs
	 * @param currentDs
	 */
	public void beforeDataSave(Dataset parentDs, Dataset currentDs);

	/**
	 * 保存后数据操作
	 * 
	 * @param parentDs
	 * @param currentDs
	 */
	public void afterDataSave(Dataset parentDs, Dataset currentDs);
}
