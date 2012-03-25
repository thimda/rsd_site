package nc.uap.portal.mng.portlet;

import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.container.om.Preference;
import nc.uap.portal.container.om.Preferences;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortletQryService;
import nc.uap.portal.service.itf.IPtPortletService;
import nc.uap.portal.util.JaxbMarshalFactory;
import nc.uap.portal.vo.PtPreferenceVO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Portlet设置页面控制类
 * @author licza
 *
 */
public class PortletSettingMainController implements IController{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6838186814262913211L;

	public void dataLoad(DataLoadEvent e){
		Dataset ds = e.getSource();
		Preferences pf = (Preferences)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_PREFERENCE_");
		List<Preference> pflist = pf.getPortletPreferences();
		Row row = ds.getEmptyRow();
		if (CollectionUtils.isNotEmpty(pflist)) {
			for (Preference p : pflist) {
				List<String> value = p.getValues();
				if(value != null && !value.isEmpty()){
					row.setValue(ds.nameToIndex(p.getName()), value.get(0));
				}
			}
		}
		ds.addRow(row);
		ds.setSelectedIndex(ds.getRowIndex(row));
		ds.setEnabled(true);
	}
	@SuppressWarnings("unchecked")
	public void okButtonClick(MouseEvent e){
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("masterDs");
		FieldSet fs = ds.getFieldSet();
		Field[] fields = fs.getFields();
		String pk_group = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		String pageName = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_PAGENAME_");
		String portletName = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_PORTLETNAME_");
		
		try {
			boolean isNew = false;
			IPtPortletQryService portletQry = PortalServiceUtil.getPortletQryService();
			PtPreferenceVO preferenceVO = portletQry.getGroupPortletPreference(pk_group,portletName , pageName); 
			if(preferenceVO == null){
				preferenceVO = new PtPreferenceVO();
				preferenceVO.setPagename(pageName);
				preferenceVO.setPortletname(portletName);
				preferenceVO.setPk_group(pk_group);
				isNew = true;
			}
			Preferences pf = (Preferences)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_PREFERENCE_");
			Row row = ds.getSelectedRow();
			for(Field field : fields){
				Preference p = pf.getPortletPreference(field.getId());
				p.getValues().clear();
				p.addValue(row.getString(ds.nameToIndex(field.getId())));
			}
			String newpreferenceCode = JaxbMarshalFactory.newIns().decodeXML(pf);
			preferenceVO.doSetPreferences(newpreferenceCode);
			IPtPortletService portletService = PortalServiceUtil.getPortletService();
			if(isNew)
				portletService.addPreference(preferenceVO);
			else
				portletService.updatePreference(preferenceVO);
			AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
		} catch (PortalServiceException ex) {
			LfwLogger.error(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cancelButtonClick(MouseEvent e){
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
}
