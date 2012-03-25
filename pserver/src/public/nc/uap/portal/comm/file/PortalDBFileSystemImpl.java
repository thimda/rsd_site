package nc.uap.portal.comm.file;

import java.util.HashMap;
import java.util.Map;

import nc.file.core.IFileStoreView;
import nc.file.fsv.db.DBFileStoreViewFactory;
import nc.file.fsv.db.IDBFileStoreViewParamKeys;
import nc.uap.lfw.file.filesystem.DocumentCenterFileSystem;

/**
 * Portal数据库文件系统实现
 * @author licza
 *
 */
public class PortalDBFileSystemImpl extends DocumentCenterFileSystem{

	public PortalDBFileSystemImpl(String filePath) {
		super(filePath);
	}

	@Override
	public IFileStoreView getFsv() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(IDBFileStoreViewParamKeys.DBFS_DSNAME, filePath);
		return new DBFileStoreViewFactory().createfileStoreView(params);
	}

	
	
//	@Override
//	public void deleteFile(Serializable fileVO) throws Exception {
//		String pk = getFilePK(fileVO);
//		/**
//		 * 从数据库中删除文件
//		 */
//		try {
//			CRUDHelper.getCRUDService().deleteVo(new PtDbfileVO(pk));
//		} catch (LfwBusinessException e) {
//			LfwLogger.error(e.getMessage(),e);
//			throw new FileNotFoundException();
//		}
//	}
//
//	@Override
//	public Object download(Serializable fileVO, OutputStream out)
//			throws Exception {
//		String pk = getFilePK(fileVO);
//		PtDbfileVO[] vos = null;
//		try {
//			vos = CRUDHelper.getCRUDService().queryVOs(" pk_dbfile ='"+ pk +"' ", PtDbfileVO.class, null, " ", null);
//		} catch (Exception e) {
//			LfwLogger.error(e.getMessage(),e);
//		}
//		if(vos == null || vos.length == 0){
//			throw new FileNotFoundException();
//		}
//		vos[0].doGetOutPutStream(out);
//		return null;
//	}
//
//	@Override
//	public boolean existInFs(String fileNo) throws Exception {
//		PtDbfileVO[] vos = null;
//		try {
//			vos = CRUDHelper.getCRUDService().queryVOs(new PtDbfileVO(fileNo), null, null, null, null);
//		} catch (Exception e) {
//			LfwLogger.error(e.getMessage(),e);
//		}
//		return !(vos == null || vos.length == 0);
//	}
//
//	@Override
//	public void upload(Serializable fileVO, InputStream input) throws Exception {
//		String pk = getFilePK(fileVO);
//		PtDbfileVO vo = new PtDbfileVO(pk);
//		vo.doSetInputStream(input);
//		try {
//			/**
//			 * 如果已经存在,更新
//			 * 否则新增
//			 */
//			if(existInFs(pk)){
//				vo.setStatus(VOStatus.UPDATED);
//			}else{
//				vo.setStatus(VOStatus.NEW);
//			}
//			LfwExAggVO obj = new LfwExAggVO();
//			obj.setParentVO(vo);
//			CRUDHelper.getCRUDService().saveBusinessVO(obj);
//		} catch (Exception e) {
//			LfwLogger.error(e.getMessage(),e);
//		}
//	}
//
//	private String getFilePK(Serializable fileVO) throws FileNotFoundException{
//		String pk = null;
//		if(fileVO instanceof String){
//			pk = (String)fileVO;
//		}
//		if(fileVO instanceof LfwFileVO){
//			pk = ((LfwFileVO)fileVO).getPk_lfwfile();
//		}
//		if(pk != null && pk.length() != 0)
//			return pk;
//		else
//			throw new FileNotFoundException();
//	}
}
