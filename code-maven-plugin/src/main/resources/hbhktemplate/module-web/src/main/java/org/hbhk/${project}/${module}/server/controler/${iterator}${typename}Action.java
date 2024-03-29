[#ftl]
[#assign service="${table.typeName?uncap_first}Service"]
package com.deppon.${project}.module.${module}.server.action;

import com.deppon.foss.framework.exception.BusinessException;
import com.deppon.foss.framework.server.web.action.AbstractAction;
import com.deppon.foss.framework.server.web.result.json.annotation.JSON;
import com.deppon.${project}.module.${module}.shared.domain.${table.typeName}Entity;
import com.deppon.${project}.module.${module}.server.service.I${table.typeName}Service;
import com.deppon.${project}.module.${module}.shared.vo.${table.typeName}Vo;
import java.util.Arrays;
import java.util.List;
/**
 * ${table.name}
 * 
 * @author DPAP
 * 
 */
public class ${table.typeName}Action extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private I${table.typeName}Service ${service};
	
	private ${table.typeName}Vo ${table.typeName?uncap_first}Vo;
	
	/** 新增  */
	@JSON
	public String insert${table.typeName}() {
		try {
			this.${service}.insert(${table.typeName?uncap_first}Vo.get${table.typeName}());
			return returnSuccess("dpap.code.gen.save.success");			
		} catch (BusinessException e) {
			return returnError(e);
		}
	}
	/**删除 */
	@JSON
	public String delete${table.typeName}() {
		try {
			String[] id = ${table.typeName?uncap_first}Vo.getIds().split(",");
			List<String> ids = Arrays.asList(id);
			this.${service}.delete(ids);
			return returnSuccess("dpap.code.gen.delete.success");			
		} catch (BusinessException e) {
			return returnError(e);
		}
	}
	
	/** 修改 */
	@JSON
	public String update${table.typeName}() {
		try {
			this.${service}.update(${table.typeName?uncap_first}Vo.get${table.typeName}());
			return returnSuccess("dpap.code.gen.update.success");			
		} catch (BusinessException e) {
			return returnError(e);
		}
	}
	
	/** 查询所有 */
	@JSON
	public String queryAll${table.typeName}() {
		try {
			List<${table.typeName}Entity> ${table.typeName?uncap_first}s = this.${service}.getAll();
			${table.typeName?uncap_first}Vo.set${table.typeName}s(${table.typeName?uncap_first}s);
			return returnSuccess();			
		} catch (BusinessException e) {
			return returnError(e);
		}
	}
	
	@JSON
	public String queryBy${table.typeName}() {
		try {
			${table.typeName}Entity entity=${table.typeName?uncap_first}Vo.get${table.typeName}();
			List<${table.typeName}Entity> ${table.typeName?uncap_first}s = this.${service}.queryByEntity(entity,start,
					limit);
			totalCount = this.${service}.queryByEntityCount(entity);
			${table.typeName?uncap_first}Vo.set${table.typeName}s(${table.typeName?uncap_first}s);
			return returnSuccess();			
		} catch (BusinessException e) {
			return returnError(e);
		}
	}
	
	public void set${service?cap_first}(I${table.typeName}Service ${service}) {
		this.${service} = ${service};
	}
	
	public void set${table.typeName}Vo(${table.typeName}Vo ${table.typeName?uncap_first}Vo) {
		this.${table.typeName?uncap_first}Vo = ${table.typeName?uncap_first}Vo;
	}
	
	public ${table.typeName}Vo get${table.typeName}Vo() {
		return this.${table.typeName?uncap_first}Vo;
	}
}
