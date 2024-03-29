package com.deppon.${project}.module.${module}.server.dao;

import com.deppon.${project}.module.${module}.shared.domain.${table.typeName}Entity;

import java.util.List;

/**
 * ${table.name}
 * 
 * @author DPAP ${.now}
 * 
 */
public interface I${table.typeName}Dao {
	
	/** 新增 **/
	void insert(${table.typeName}Entity entity);
    
    /** 删除  **/
	void delete(List<String> ids,String modifyUser);
	
	/** 修改 **/
	void update(${table.typeName}Entity entity);
		
	/** 获取所有的记录 **/
	List<${table.typeName}Entity> getAll();
	
	/** 获取符合条件的记录 **/
	List<${table.typeName}Entity> queryByEntity(${table.typeName}Entity entity, int start, int limit);
	
	/** 获取符合条件的记录数 */
	Long queryByEntityCount(${table.typeName}Entity entity);
	
}
