package nc.uap.portal.integrate.ref;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.deft.DefaultDatasetServerListener;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.message.MessageCenter;
import nc.uap.portal.integrate.message.impl.NCNoticeMessageExchange;
import nc.uap.portal.util.PtUtil;
import nc.vo.org.GroupVO;
import nc.vo.sm.UserVO;

/**
 * 集成的NC参照
 * @author licza
 *
 */
public class NCRefUserDsLoader extends DefaultDatasetServerListener{

	public NCRefUserDsLoader(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onAfterRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getSelectedRow();
		if(row == null)
			return;
		String pk_group = row.getString(ds.nameToIndex("pk_group"));
		String pluginid = "ncnoticemessage";
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			NCNoticeMessageExchange pm = (NCNoticeMessageExchange)MessageCenter.lookup(pluginid);
			PtCredentialVO credential = pm.getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = pm.getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			IUserManageQuery umq = (IUserManageQuery)pm.getLocator().lookup(IUserManageQuery.class.getName());
			UserVO[] vos = umq.queryUserByClause("  pk_group = '"+ pk_group +"'");
			if(!PtUtil.isNull(vos)){
				Dataset masterDs = getCurrentContext().getWidget().getViewModels().getDataset("masterDs");
				masterDs.setCurrentKey(Dataset.MASTER_KEY);
				//userid username pk_user
				for(UserVO vo : vos){
					Row ro = masterDs.getEmptyRow();
					ro.setString(masterDs.nameToIndex("userid"), vo.getUser_code());
					ro.setString(masterDs.nameToIndex("username"), vo.getUser_name());
					ro.setString(masterDs.nameToIndex("pk_user"), vo.getCuserid());
					masterDs.addRow(ro);
				}
			}
		} catch (Exception ex) {
			LfwLogger.error(ex.getMessage(),ex);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		String pluginid = "ncnoticemessage";
		String currentDs = LfwRuntimeEnvironment.getDatasource();
		try {
			NCNoticeMessageExchange pm = (NCNoticeMessageExchange)MessageCenter.lookup(pluginid);
			PtCredentialVO credential = pm.getCredentialVO();
			/**
			 * 获得NC端数据源
			 */
		    String bc = credential.getCredentialReference().getValue("accountcode");
			String rmtDs = pm.getLocator().lookup(IBusiCenterManageService.class).getBusiCenterByCode(bc).getDataSourceName();
			InvocationInfoProxy.getInstance().setUserDataSource(rmtDs);
			nc.itf.org.IGroupQryService groupQ = (nc.itf.org.IGroupQryService)pm.getLocator().lookup(nc.itf.org.IGroupQryService.class.getName());
			GroupVO[] vos = groupQ.queryAllGroupVOs();
			Dataset ds = se.getSource();
			if(vos != null)
			for(GroupVO vo : vos){
				Row row = ds.getEmptyRow();
				row.setString(ds.nameToIndex("pk_group"), vo.getPk_group());
//				row.setString(ds.nameToIndex("pk_parent"), vo.getPk_fathergroup());
				row.setString(ds.nameToIndex("groupcode"), vo.getGroupno());
				row.setString(ds.nameToIndex("groupname"), vo.getName());
				ds.addRow(row);
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 把本地的DataSource设置回来
			 */
			InvocationInfoProxy.getInstance().setUserDataSource(currentDs);
		}
	}

	
	
}
 