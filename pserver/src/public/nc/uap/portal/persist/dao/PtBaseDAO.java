package nc.uap.portal.persist.dao;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.bs.trade.comdelete.BillDelete;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.portal.deploy.vo.PtSessionBean;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 
 * 针对组织下的基本操作
 * 
 * @author zhangxya
 * 
 */
public class PtBaseDAO {
	private String dataSource = null;

	/**
	 * 默认构造函数，将使用当前数据源
	 */
	public PtBaseDAO() {
	}
	/**
	 * 有参构造函数，将使用指定数据源
	 * 
	 * @param dataSource
	 *            数据源名称
	 */
	public PtBaseDAO(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 根据条件查询数据
	 * @param voClass
	 * @param strWhere
	 * @return
	 * @throws DAOException
	 */
	public SuperVO[] queryByCondition(Class voClass, String strWhere)
			throws DAOException {
		if (strWhere != null && strWhere.length() != 0)
			strWhere = " (isnull(dr,0)=0) and " + strWhere;
		else
			strWhere = "isnull(dr,0)=0";
		
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			List list = (List) manager.retrieveByClause(voClass, strWhere);
			return (SuperVO[]) list.toArray((SuperVO[]) Array.newInstance(voClass, 0));
		} 
		catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} 
		finally {
			if (manager != null)
				manager.release();
		}
	}
	
	/**
	 * 插入一个VO对象，如果该VO的主键值非空则插入VO的原有主键
	 * 
	 * @param vo
	 * @return
	 * @throws DAOException
	 */
	public String insertVOWithPK(SuperVO vo) throws DAOException {
		PersistenceManager manager = null;
		String pk = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			pk = manager.insertWithPK(vo);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return pk;
	}
	
	public String[] insertVOWithPKs(SuperVO[] vos) throws DAOException{
		PersistenceManager manager = null;
		String[] pk = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			pk = manager.insertWithPK(vos);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return pk;
	}

	/**
	 * 根据IMappingMeta插入一个VO对象，该VO的主键值非空则插入VO的原有主键
	 * 
	 * @param vo
	 *            VO对象
	 * @param meta
	 *            IMappingMeta
	 * @return
	 * @throws DAOException
	 */
	public String insertObjectWithPK(Object vo, IMappingMeta meta) throws DAOException {

		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			return manager.insertObjectWithPK(vo, meta);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	
	/**
	 * 根据IMappingMeta插入VO对象集合，该VO的主键值非空则插入VO的原有主键
	 * 
	 * @param vo
	 *            VO对象集合
	 * @param meta
	 *            IMappingMeta
	 * @return
	 * @throws DAOException
	 */
	public String[] insertObjectWithPK(Object[] vo, IMappingMeta meta) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			return manager.insertObjectWithPK(vo, meta);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	/**
	 * 插入vo
	 * 
	 * @param vo
	 * @return
	 * @throws DAOException
	 */
	public String insertVO(SuperVO vo) throws DAOException {
		if (vo == null)
			return null;
		return insertVOs(new SuperVO[] { vo })[0];
	}

	/**
	 * 输入一批vo
	 * 
	 * @param vos
	 * @return
	 * @throws DAOException
	 */
	public String[] insertVOs(SuperVO[] vos) throws DAOException {
		if (vos == null || vos.length == 0)
			return null;
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			String groupno = getGroupNO();
			// 生成pk
			String[] pks = new SequenceGenerator(dataSource).generate(groupno,
					vos.length);
			for (int i = 0; i < vos.length; i++) {
				vos[i].setPrimaryKey(pks[i]);
			}
			return manager.insertWithPK(vos);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}

	private String getGroupNO() {
		PtSessionBean ses = (PtSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
		if(ses == null)
			return "0001";
		return ses.getGroupNo();
	}
	/**
	 * 根据VO对象更新数据库
	 * 
	 * @param vo
	 *            VO对象
	 */
	public int updateVO(SuperVO vo) throws DAOException {
		return updateVOArray(new SuperVO[] { vo });
	}

	/**
	 * 根据VO对象数组更新数据库
	 * 
	 * @param vo
	 *            VO对象
	 */
	public int updateVOArray(SuperVO[] vos) throws DAOException {
		return updateVOArray(vos, null);
	}

	/**
	 * 根据VO对象数组中指定列更新数据库
	 * 
	 * @param vos
	 *            VO对象
	 * @param fieldNames
	 *            指定列
	 */
	public int updateVOArray(SuperVO[] vos, String[] fieldNames)
			throws DAOException {
		return updateVOArray(vos, fieldNames, null, null);

	}

	public int updateVOArray(final SuperVO[] vos, String[] fieldNames,
			String whereClause, SQLParameter param) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			return manager.update(vos, fieldNames, whereClause, param);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}

	/**
	 * 在数据库中删除一个VO对象。
	 * 
	 * @param vo
	 *            VO对象
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteVO(SuperVO vo) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.delete(vo);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}

	}

	/**
	 * 在数据库中删除一组VO对象。
	 * 
	 * @param vos
	 *            VO数组对象
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteVOArray(SuperVO[] vos) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.delete(vos);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}

	}

	/**
	 * 在数据库中根据类名和PK删除一个VO对象集合
	 * 
	 * @param className
	 *            VO类名
	 * @param pk
	 *            PK值
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteByPK(Class className, String pk) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByPK(className, pk);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}

	}
	
	/**
	 * 在数据库中根据类名和条件删除数据
	 * 
	 * @param className
	 *            VO类名
	 * @param wherestr
	 *            条件
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteByClause(Class className, String wherestr)
			throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByClause(className, wherestr);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	
	public void deleteByClause(Class className, String wherestr,
			SQLParameter params) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByClause(className, wherestr, params);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}

	}

	/**
	 * 在数据库中根据类名和PK数组删除一组VO对象集合
	 * 
	 * @param className
	 *            要删除的VO类名
	 * @param pks
	 *            PK数组
	 * @throws DAOException
	 *             如果删除出错抛出DAOException
	 */
	public void deleteByPKs(Class className, String[] pks) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByPKs(className, pks);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}

	/**
	 * 根据SQL 执行数据库查询,并返回ResultSetProcessor处理后的对象 （非 Javadoc）
	 * 
	 * @param sql
	 *            查询的SQL
	 * @param processor
	 *            结果集处理器
	 */
	public Object executeQuery(String sql, ResultSetProcessor processor)
			throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeQuery(sql, processor);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}

	/**
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 * 
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
	 */
	public Object executeQuery(String sql, SQLParameter parameter,
			ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			JdbcSession session = manager.getJdbcSession();
			value = session.executeQuery(sql, parameter, processor);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}

	/**
	 * 根据VO类名和where条件返回vo集合
	 * 
	 * @param className
	 *            Vo类名称
	 * @param condition
	 *            查询条件
	 * @return 返回Vo集合
	 * @throws DAOException
	 *             发生错误抛出DAOException
	 */
	public Collection retrieveByClause(Class className, String condition) throws DAOException {
		PersistenceManager manager = null;
		Collection values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveByClause(className, condition);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	
	/**
	 * 根据PK查询指定VO
	 * 
	 * @param VO类名
	 * @param pk
	 *            主键
	 * 
	 */
	public Object retrieveByPK(Class className, String pk) throws DAOException {
		PersistenceManager manager = null;
		Object values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveByPK(className, pk);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}

	
	/**
	 * 根据指定SQL 执行有参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @param parameter
	 *            更新参数
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter)
			throws DAOException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql, parameter);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}

	/**
	 * 根据指定SQL 执行无参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql) throws DAOException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
	/**
	 * 删除聚合VO
	 * 
	 * @param billVo
	 * @throws BusinessException
	 */
	public void deleteAggVO(AggregatedValueObject billVo)throws BusinessException {
		new BillDelete().deleteBill(billVo);
	}

	

	private PersistenceManager createPersistenceManager(String ds)
			throws DbException {
		PersistenceManager manager = PersistenceManager.getInstance(ds);
		return manager;
	}

	/**
	 * 根据VO类名查询该VO对应表的所有数据
	 * 
	 * @param className
	 *            SuperVo类名
	 * 
	 * @return
	 * @throws DAOException
	 *             发生错误抛出DAOException
	 */
	public Collection retrieveAll(Class className) throws DAOException {
		PersistenceManager manager = null;
		Collection values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveAll(className);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	
	/**
	 * 根据VO对象和MappingMeta信息更新数据库
	 * 
	 * @param vo
	 *            VO对象
	 * @param meta
	 *            MappingMeta信息
	 */
	public int updateObject(Object vo, IMappingMeta meta) throws DAOException {
		return updateObject(vo, meta, null);
	}
	
	/**
	 * 根据 IMappingMeta和条件更新VO对象对应的数据库
	 * 
	 * @param vos
	 *            VO对象
	 * @param meta
	 *            IMappingMeta
	 * @param whereClause
	 *            条件语句
	 * @return
	 * @throws DAOException
	 */
	public int updateObject(Object vo, IMappingMeta meta, String whereClause) throws DAOException {
		PersistenceManager manager = null;

		try {
			manager = createPersistenceManager(dataSource);
			return manager.updateObject(vo, meta, whereClause);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}

	}
	
	/**
	 * 根据IMappingMeta插入一个VO对象
	 * 
	 * @param vo
	 *            VO对象
	 * @param meta
	 *            IMappingMeta
	 */
	public String insertObject(Object vo, IMappingMeta meta) throws DAOException {

		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			;
			return manager.insertObject(vo, meta);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
}
