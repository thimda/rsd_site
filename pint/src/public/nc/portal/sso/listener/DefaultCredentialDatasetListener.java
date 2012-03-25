package nc.portal.sso.listener;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.portal.sso.pagemodel.CredentialEditPageModel;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.page.ViewComponents;

/**
 * 默认的SSO认证页面数据集监听器
 * 给数据集中的field设置默认值
 * @author licza
 * 
 */
public class DefaultCredentialDatasetListener extends DefaultDatasetServerListener {

	public DefaultCredentialDatasetListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		// 从websession中拿到各项默认值
		WebSession websession = getGlobalContext().getWebSession();
		Map<Integer, Object> defaultValueMap = (HashMap<Integer, Object>) websession.getAttribute(CredentialEditPageModel.DEFAULT_VALUE_MAP);
		Dataset ds = se.getSource();
		ds.setCurrentKey(Dataset.MASTER_KEY);
		Row row = ds.getEmptyRow();
		if (defaultValueMap != null) {
			for (int i = 0; i < ds.getFieldCount(); i++) {
				if (defaultValueMap.containsKey(i) && defaultValueMap.get(i)!=null)
					row.setValue(i, defaultValueMap.get(i));
			}
		}
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowIndex(row));
		ds.setEnabled(true);
		ViewComponents wcs = getGlobalContext().getPageMeta().getWidget("main").getViewComponents();
		WebComponent wc = wcs.getComponent(CredentialEditPageModel.CREDENTIAL_FORM);
		wc.setEnabled(true);
		if(!StringUtils.equals((String)websession.getAttribute("wmode"), "dialog")){
			ButtonComp bc = (ButtonComp)wcs.getComponent("giveup");
			bc.setVisible(false);
		}
	}

}
