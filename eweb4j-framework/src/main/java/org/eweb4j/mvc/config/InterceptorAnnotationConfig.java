package org.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eweb4j.cache.InterConfigBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.ScanPackage;
import org.eweb4j.mvc.action.annotation.Singleton;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.mvc.interceptor.Interceptor;
import org.eweb4j.mvc.interceptor.Uri;

public class InterceptorAnnotationConfig extends ScanPackage{

	public final static Log log = LogFactory.getMVCLogger(InterceptorAnnotationConfig.class);

	public InterceptorAnnotationConfig() {
		super();
	}

	/**
	 * handle class
	 * 
	 * @param clsName
	 */
	public boolean handleClass(final String clsName) {

		Class<?> cls = null;
		try {
			cls = Thread.currentThread().getContextClassLoader().loadClass(clsName);

			if (cls == null)
				return false;

			Interceptor interAnn = cls.getAnnotation(Interceptor.class);
			if (interAnn == null)
				return false;
			Uri[] uris = interAnn.uri();
			if (uris == null || uris.length == 0)
				return false;
			
			InterConfigBean inter = new InterConfigBean();
			String name = "".equals(interAnn.name()) ? cls.getSimpleName() : interAnn.name();
			inter.setName(name);
			inter.setClazz(cls.getName());
			inter.setMethod(interAnn.method());
			String[] except = interAnn.except();
			if (except != null && except.length > 0){
				List<String> list = Arrays.asList(except);
				inter.setExcept(new ArrayList<String>(list));
			}
			
			inter.setPolicy(interAnn.policy());
			inter.setType(interAnn.type());
			inter.setPriority(String.valueOf(interAnn.priority()));
			Singleton sin = cls.getAnnotation(Singleton.class);
			if (sin != null)
				inter.setScope("singleton");
			else
				inter.setScope("prototype");
			
			List<org.eweb4j.mvc.config.bean.Uri> uriList = new ArrayList<org.eweb4j.mvc.config.bean.Uri>();
			for (Uri u : uris){
				org.eweb4j.mvc.config.bean.Uri uri = new org.eweb4j.mvc.config.bean.Uri();
				uri.setType(u.type());
				uri.setValue(u.value());
				uriList.add(uri);
			}
			inter.setUri(uriList);
			InterConfigBeanCache.add(inter);
		}  catch (Throwable e) {
			log.warn("the interceptor class new instance failued -> " + clsName + " | " + e.toString(), e);
			return false;
		} 
		
		return true;
	}

	@Override
	protected void onOk() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
