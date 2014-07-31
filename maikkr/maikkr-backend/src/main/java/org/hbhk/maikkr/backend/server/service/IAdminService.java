package org.hbhk.maikkr.backend.server.service;

import org.hbhk.maikkr.backend.shared.pojo.AdminInfo;

/**
 * Service接口开发规范 1.必须继承com.deppon.foss.framework.service.IService接口
 * 2.类名必须以I开头、以Service结尾
 */
public interface IAdminService {

	AdminInfo get(AdminInfo admin);

	AdminInfo login(AdminInfo admin);
}