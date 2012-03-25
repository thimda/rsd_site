package nc.uap.portal.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.portal.persist.dao.PtBaseDAO;
import nc.uap.portal.service.itf.IPortalSpService;
import nc.uap.portal.vo.PtPortletVO;
import nc.vo.pub.BusinessException;

/**
 * 获取Portal数据源名称
 * @author dengjt
 *
 */
public class PortalSpServiceImpl implements IPortalSpService {
	private static final String DSNAME_KEY = "dsname";
	//portal数据源名称
	private String dsName;
	
	/**
	 * 获取数据源名称。如果没有主动配置，尝试从所有现有帐套中取初始化了portal表的数据源，并将匹配结果写入到文件中
	 */
	public String getPortalDsName(){
		if(dsName == null){
			Properties props = new Properties();
			InputStream is = null;
			String confDir = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/portal/WEB-INF/conf";
			String confPath = confDir + "/dsname.properties";
			try {
				is = new FileInputStream(confPath);
				if(is != null)
					props.load(is);
				dsName = props.getProperty(DSNAME_KEY);
			} 
			catch (Exception e) {
				LfwLogger.error("获取数据源出现异常", e);
			}
			finally{
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
					LfwLogger.error(e);
				}
			}
			
			OutputStream out = null;
			try{
				if(dsName == null || dsName.equals("")){
					LfwLogger.debug("没有配置数据源，尝试匹配");
					dsName = tryConfig();
					if(dsName != null){
						LfwLogger.debug("尝试匹配数据源:" + dsName);
						props.setProperty(DSNAME_KEY, dsName);
						File f = new File(confDir);
						if(!f.exists())
							f.mkdirs();
						f = new File(confPath);
						if(!f.exists())
							f.createNewFile();
						out = new FileOutputStream(f);
						props.store(out, null);
					}
					else
						LfwLogger.debug("没有匹配数据源");
				}
			}
			catch(Exception e){
				LfwLogger.error(e);
			}
			finally{
				if(out != null){
					try {
						out.close();
					} 
					catch (IOException e) {
						LfwLogger.error(e);
					}
				}
			}
		}
		return dsName;
	}

	/**
	 * 尝试取所有帐套对应数据源中，初始化了portal表的数据源
	 * @return
	 */
	private String tryConfig() {
		IBusiCenterManageService bs = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
		String name = null;
		try {
			BusiCenterVO[] vos = bs.getBusiCenterVOs();
			if(vos != null && vos.length > 0){
				for (int j = 0; j < vos.length; j++) {
					BusiCenterVO vo = vos[j];
					if(vo.isLocked())
						continue;
					String testDsName = vo.getDataSourceName();
					PtBaseDAO dao = new PtBaseDAO(testDsName);
					//测试表
					String testSql = "select * from pt_portlet";
					try{
						dao.executeQuery(testSql, new BeanListProcessor(PtPortletVO.class));
						name = testDsName;
						break;
					}
					catch(Throwable e){
						LfwLogger.error(e);
						continue;
					}
				}
			}
		} 
		catch (BusinessException e) {
			LfwLogger.error(e);
		}
		return name;
	}

}
