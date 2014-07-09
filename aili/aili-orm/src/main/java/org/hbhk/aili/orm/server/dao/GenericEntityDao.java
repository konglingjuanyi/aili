package org.hbhk.aili.orm.server.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hbhk.aili.orm.server.annotation.NativeSave;
import org.hbhk.aili.orm.server.service.impl.DaoService;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface GenericEntityDao<T, PK extends Serializable> {
	
	@Transactional
	@NativeSave
	T save(T model);

	@Transactional
	void delete(T model);

	@Transactional
	void deleteAll(List<T> models);

	@Transactional
	void deleteByPrimaryKey(PK id);

	@Transactional
	void deleteAllByPrimaryKey(List<PK> ids);
	
	@Transactional(propagation=Propagation.SUPPORTS)
	void flush();
	
	@Transactional(propagation=Propagation.SUPPORTS)
	void evict(T model);

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	T getByPrimaryKey(PK id);
	
	@Transactional
	int updateByNamedQuery(String queryName, Map<String,Object> params);
	
	@Transactional
	int updateByQuery(String query, Map<String,Object> params);
	
	@Transactional
	int updateByNativeQuery(String query, Object[] params, Class<?>[] types);
	
	@Transactional
	void executeDDL(String ddl);
	
	@Transactional
	Map<String,Object> executeSP(String spName);
	
	@Transactional
	Map<String,Object> executeSp(String spName, SqlParameter[] sqlParameters, Map<String,Object> params);
	
	DaoService getDaoService();

	void setDaoService(DaoService daoService);
}
