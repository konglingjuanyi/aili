package org.hbhk.module.framework.shared.exception;

import org.hbhk.module.framework.server.exception.GeneralException;


/**
 * CacheId重复异常
 * 
 * <p style="display:none">modifyRecord</p>
 * <p style="display:none">version:V1.0,author:ningyu,date:2013-5-8 下午4:47:51,content:TODO </p>
 * @date 2013-5-8 下午4:47:51
 * @since
 * @version
 */
public class CacheDumplicateException extends GeneralException {

	private static final long serialVersionUID = -8867072528054610442L;
	
	public CacheDumplicateException(String msg) {
        super(msg);
    }
	
	public CacheDumplicateException(Throwable e) {
	    super(e);
	}

}
