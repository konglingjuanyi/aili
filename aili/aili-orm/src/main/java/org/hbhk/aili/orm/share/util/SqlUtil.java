package org.hbhk.aili.orm.share.util;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.hbhk.aili.orm.server.handler.INameHandler;
import org.hbhk.aili.orm.share.model.SqlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sql辅助为类
 * 
 * User: liyd Date: 2/13/14 Time: 10:03 AM
 */
public class SqlUtil {

	/** 日志对象 */
	private static final Logger LOG = LoggerFactory.getLogger(SqlUtil.class);

	/**
	 * 构建insert语句
	 * 
	 * @param entity
	 *            实体映射对象
	 * @param INameHandler
	 *            名称转换处理器
	 * @return
	 */
	public static SqlContext buildInsertSql(Object entity,
			INameHandler nameHandler) {
		Class<?> clazz = entity.getClass();
		String tableName = nameHandler.getTableName(clazz);
		String primaryName = nameHandler.getPrimaryName(clazz);
		StringBuilder sql = new StringBuilder("insert into ");
		List<Object> params = new ArrayList<Object>();
		sql.append(tableName);

		// 获取属性信息
		BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (PropertyDescriptor pd : pds) {
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (value == null) {
				continue;
			}
			sql.append(nameHandler.getColumnName(clazz, pd.getName()));
			args.append("?");
			params.add(value);
			sql.append(",");
			args.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		args.deleteCharAt(args.length() - 1);
		args.append(")");
		sql.append(")");
		sql.append(" values ");
		sql.append(args);
		return new SqlContext(sql, primaryName, params);
	}

	/**
	 * 构建更新sql
	 * 
	 * @param entity
	 * @param INameHandler
	 * @return
	 */
	public static SqlContext buildUpdateSql(Object entity,
			INameHandler nameHandler) {
		Class<?> clazz = entity.getClass();
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		String tableName = nameHandler.getTableName(clazz);
		String primaryName = nameHandler.getPrimaryName(clazz);
		// 获取属性信息
		BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

		sql.append("update ");
		sql.append(tableName);
		sql.append(" set ");
		Object primaryValue = null;
		for (PropertyDescriptor pd : pds) {
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (value == null) {
				continue;
			}
			String columnName = nameHandler.getColumnName(clazz, pd.getName());
			if (!primaryName.equalsIgnoreCase(columnName)) {
				sql.append(columnName);
				sql.append(" = ");
				sql.append("?");
				params.add(value);
				sql.append(",");
			}else{
				primaryValue= value;
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" where ");
		sql.append(primaryName);
		sql.append(" = ?");
		params.add(primaryValue);
		return new SqlContext(sql, primaryName, params);
	}
	
	/**
	 * 构建更新sql
	 * 
	 * @param entity
	 * @param INameHandler
	 * @return
	 */
	public static SqlContext buildDeleteSql(Object entity,
			INameHandler nameHandler) {
		Class<?> clazz = entity.getClass();
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		String tableName = nameHandler.getTableName(clazz);
		String primaryName = nameHandler.getPrimaryName(clazz);
		// 获取属性信息
		BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" ");
		Object primaryValue = null;
		for (PropertyDescriptor pd : pds) {
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (value == null) {
				continue;
			}
			String columnName = nameHandler.getColumnName(clazz, pd.getName());
			if (primaryName.equalsIgnoreCase(columnName)) {
				primaryValue= value;
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" where ");
		sql.append(primaryName);
		sql.append(" = ?");
		params.add(primaryValue);
		return new SqlContext(sql, primaryName, params);
	}

	/**
	 * 构建查询条件
	 * 
	 * @param entity
	 * @param INameHandler
	 */
	public static SqlContext buildQueryCondition(Object entity,
			INameHandler nameHandler) {
		// 获取属性信息
		Class<?> cls = entity.getClass();
		BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(cls);
		// PropertyDescriptor[] pds =
		// BeanUtils.getPropertyDescriptors(entityClass);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		StringBuilder condition = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		int count = 0;
		for (PropertyDescriptor pd : pds) {
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (value == null) {
				continue;
			}
			if (count > 0) {
				condition.append(" and ");
			}
			condition.append(nameHandler.getColumnName(cls, pd.getName()));
			condition.append(" = ?");
			params.add(value);
			count++;
		}
		return new SqlContext(condition, null, params);
	}

	/**
	 * 获取属性值
	 * 
	 * @param readMethod
	 * @param entity
	 * @return
	 */
	private static Object getReadMethodValue(Method readMethod, Object entity) {
		if (readMethod == null) {
			return null;
		}
		try {
			if (!Modifier.isPublic(readMethod.getDeclaringClass()
					.getModifiers())) {
				readMethod.setAccessible(true);
			}
			return readMethod.invoke(entity);
		} catch (Exception e) {
			LOG.error("获取属性值失败", e);
			throw new RuntimeException(e);
		}
	}
}
