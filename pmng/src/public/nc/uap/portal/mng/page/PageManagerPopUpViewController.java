package nc.uap.portal.mng.page;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.om.Page;
import nc.uap.portal.service.PortalServiceUtil;
import nc.uap.portal.service.itf.IPtPortalPageQryService;
import nc.uap.portal.util.PmlUtil;
import nc.uap.portal.vo.PtPageVO;
import nc.vo.pub.lang.UFBoolean;

import org.xml.sax.SAXException;

/**
 * @author chouhl
 */
public class PageManagerPopUpViewController implements IController {
	private static final long serialVersionUID = 1L;

	public void beforeShow_popupView(DialogEvent dialogEvent) {
		String pk = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("pk_portalpage");
		IPtPortalPageQryService pageQry = NCLocator.getInstance().lookup(IPtPortalPageQryService.class);
		try {
			PtPageVO pagevo = pageQry.getPortalPageVOByPK(pk);
			String pml = pagevo.doGetSettingsStr();
			Page page = PmlUtil.parser(pml);
			Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("settingds");
			Row row = ds.getEmptyRow();
			row.setString(ds.nameToIndex("linkgroup"), page.getLinkgroup());
			row.setString(ds.nameToIndex("icon"), page.getIcon());
			row.setUFBoolean(ds.nameToIndex("activeflag"), pagevo.getActiveflag() == null ? false : pagevo.getActiveflag().booleanValue());
			ds.addRow(row);
			ds.setRowSelectIndex(ds.getRowIndex(row));
			ds.setEnabled(true);
		} catch (PortalServiceException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("查询数据库失败", e);
		} catch (SAXException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("数据类型转换失败", e);
		}
	}

	public void onclick_cancel(MouseEvent mouseEvent) {
		AppLifeCycleContext.current().getWindowContext().closeView("popup");
	}

	public void onclick_save(MouseEvent mouseEvent) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("settingds");
		Row row = ds.getSelectedRow();
		String linkgroup = row.getString(ds.nameToIndex("linkgroup"));
		String icon = row.getString(ds.nameToIndex("icon"));
		UFBoolean activeflag = row.getUFBoolean(ds.nameToIndex("activeflag"));
		String pk = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("pk_portalpage");
		IPtPortalPageQryService pageQry = NCLocator.getInstance().lookup(IPtPortalPageQryService.class);
		try {
			PtPageVO pagevo = pageQry.getPortalPageVOByPK(pk);
			String settings = pagevo.doGetSettingsStr();
			Page page = PmlUtil.parser(settings);
			page.setLinkgroup(linkgroup);
			page.setIcon(icon);
			settings = page.toXml();
			pagevo.doSetSettingsStr(settings);
			pagevo.setActiveflag(activeflag);
			PortalServiceUtil.getPageService().update(pagevo);
			AppLifeCycleContext.current().getWindowContext().closeView("popup");
			ds.setEnabled(false);
		} catch (PortalServiceException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("查询数据库失败", e);
		} catch (SAXException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("数据类型转换失败", e);
		}
	}

}
