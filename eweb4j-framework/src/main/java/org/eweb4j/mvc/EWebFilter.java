package org.eweb4j.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.config.EWeb4JListener;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.ListenerBean;
import org.eweb4j.config.bean.Listeners;
import org.eweb4j.i18n.Lang;
import org.eweb4j.mvc.action.ActionExecution;
import org.eweb4j.mvc.config.ActionConfig;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.mvc.interceptor.InterExecution;
import org.eweb4j.mvc.upload.UploadFile;
import org.eweb4j.mvc.upload.UploadUtil;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.FileUtil;

/**
 * eweb4j.MVC filter
 * 
 * @author weiwei
 * @since 1.b.8
 * 
 */
public class EWebFilter implements Filter, Servlet {
	private static ServletContext servletContext;

	/**
	 * 初始化Filter
	 */
	public void init(FilterConfig config) throws ServletException {
		servletContext = config.getServletContext();

		EWeb4JConfig.setROOT_PATH(config.getInitParameter("RootPath"));
		
		EWeb4JConfig.setCONFIG_BASE_PATH(config.getInitParameter(MVCCons.CONFIG_BASE_PATH));
		
		EWeb4JConfig.setCHECK_START_FILE_EXIST(config.getInitParameter(MVCCons.CHECK_START_FILE_EXIST));

		EWeb4JConfig.setSTART_FILE_NAME(config.getInitParameter(MVCCons.START_FILE_NAME));

		ActionConfig.setFORWARD_BASE_PATH(config.getInitParameter(MVCCons.FORWARD_BASE_PATH));
		
		ActionConfig.setFORWARD_BASE_PATH(config.getInitParameter(MVCCons.VIEW_BASE_PATH));
		
		ActionConfig.setLAYOUT_SCREEN_CONTENT_KEY(config.getInitParameter(MVCCons.LAYOUT_SCREEN_CONTENT_KEY));
		
		ActionConfig.setBASE_URL_PARSE_TYPE(config.getInitParameter(MVCCons.BASE_URL_PARSE_TYPE));
		
		ActionConfig.setBASE_URL_KEY(config.getInitParameter(MVCCons.BASE_URL_KEY));

		ActionConfig.setREQ_PARAM_SCOPE_KEY(config.getInitParameter(MVCCons.REQ_PARAM_MAP_KEY));
		
		ActionConfig.setHTTP_HEADER_ACCEPT_PARAM(config.getInitParameter(MVCCons.HTTP_METHOD_PARAM));
		
		ActionConfig.setHTTP_HEADER_ACCEPT_PARAM(config.getInitParameter(MVCCons.HTTP_HEADER_ACCEPT_PARAM));

		StringBuilder info = new StringBuilder("eweb4j filter init \n");

//		try {
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//	        Class<?> cla = classLoader.getClass();
//	        while (cla != ClassLoader.class)
//	            cla = cla.getSuperclass();
//	        Field field = cla.getDeclaredField("classes");
//	        field.setAccessible(true);
//	        Vector v = (Vector) field.get(classLoader);
//	        for (int i = 0; i < v.size(); i++) {
//	            System.out.println("fuck->>>>>>"+((Class)v.get(i)).getName());
//	        }
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
        
		info.append("ClassPath --> ").append(Thread.currentThread().getContextClassLoader().getResource("/").getPath()).append("\n");
		
		info.append("RootPath --> ").append(ConfigConstant.ROOT_PATH).append("\n");
		info.append("ConfigBasePath --> ").append(ConfigConstant.CONFIG_BASE_PATH).append("\n");
		info.append("StartFileName --> ").append(ConfigConstant.START_FILE_NAME).append("\n");

		info.append("BaseURLKey --> ").append(MVCConfigConstant.BASE_URL_KEY).append("\n");
		
		info.append("LayoutScreenContentKey --> ").append(MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY).append("\n");

		info.append("ReqParamMapKey --> ").append(MVCConfigConstant.REQ_PARAM_SCOPE_KEY).append("\n");

		System.out.println(info.toString());
	}

	public static final boolean isMultipartContent(HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith("multipart/")) {
            return true;
        }
        return false;
    }

	/**
	 * 获取客户端IP
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	
	/**
	 * 初始化
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	private Context initContext(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		Context context = new Context(servletContext, request, response, null, null, null, null);
		String ip = getIpAddr(request);
		context.setIp(ip);
		// 将request的请求参数转到另外一个map中去
		Map<String, String[]> qpMap = new HashMap<String, String[]>();
		qpMap.putAll(ParamUtil.copyReqParams(context.getRequest()));
		context.setQueryParamMap(qpMap);
		
		//将上传的表单元素注入到context中
		if (isMultipartContent(context.getRequest())) 
			UploadUtil.handleUpload(context);
		
		return context;
	}

	/**
	 * 执行Filter
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Context context = null;
		try {
			String err = EWeb4JConfig.start(ConfigConstant.START_FILE_NAME);// 2
			// 启动eweb4j
			if (err != null) {
				this.printHtml(err, res.getWriter());
				return;
			}

			context = this.initContext(request, response);// 1 初始化环境
			MVC.getThreadLocal().set(context);// 最主要的还是提供给 org.eweb4j.i18n.Lang.java 类使用
			
			Lang.change(request.getLocale());// 设置国际化语言
			
			String uri = this.parseURL(request);// 3.URI解析
			context.setUri(uri);

			// 拿到BaseURL
			parseBaseUrl(context);

			String reqMethod = this.parseMethod(request);// HTTP Method 解析
			context.setHttpMethod(reqMethod);
			
			InterExecution before_interExe = new InterExecution("before", context);// 4.外部前置拦截器
			if (before_interExe.findAndExecuteInter()) {
				before_interExe.showErr();
				return;
			}

			// method + uri,用来判断是否有Action与之绑定
			ActionExecution actionExe = new ActionExecution(uri, reqMethod, context);
			if (actionExe.findAction()) {
				actionExe.execute();// 5.execute the action
				return;
			}
			
			this.normalReqLog(uri);// log
			chain.doFilter(req, res);// chain
		} catch (Exception e) {
			e.printStackTrace();
			String info = CommonUtil.getExceptionString(e);
			LogFactory.getMVCLogger(EWebFilter.class).error(info);
			this.printHtml(info, res.getWriter());
		}finally{
			// 清空临时文件
			if (context != null && !context.getUploadMap().isEmpty())
				for (Iterator<Entry<String, List<UploadFile>>> it = context.getUploadMap().entrySet().iterator(); it.hasNext(); ){
					Entry<String, List<UploadFile>> en = it.next();
					if (en.getValue() == null)
						continue;
					
					for (UploadFile f : en.getValue()){
						FileUtil.deleteFile(f.getTmpFile());
				}
			}
		}
	}

	/**
	 * 将错误信息打印，HTML格式
	 * 
	 * @param err
	 * @throws IOException
	 */
	private void printHtml(String err, PrintWriter writer) {
		writer.print(HtmlCreator.create(err));
		writer.flush();
		writer = null;
	}

	/**
	 * 解析URL，得到后部分的URI
	 * 
	 * @return
	 * @throws Exception
	 */
	private String parseURL(HttpServletRequest request) throws Exception {
		String uri = URLDecoder.decode(request.getRequestURI(), "utf-8");
		String contextPath = URLDecoder.decode(request.getContextPath(),"utf-8");

		if (contextPath != null && contextPath.trim().length() > 0)
			return uri.replace(contextPath + "/", "");

		return uri.substring(1);
	}

	private void parseBaseUrl(Context context) throws Exception {
		ServletContext servletContext = context.getServletContext();
		HttpServletRequest request = context.getRequest();
		String uri = context.getUri();

		String parseType = MVCConfigConstant.BASE_URL_PARSE_TYPE;
		//只要不是固定的，都是用动态策略
		if (!"fixed".equalsIgnoreCase(parseType)){
			MVCConfigConstant.BASE_URL = URLDecoder.decode(request.getRequestURL().toString(),"utf-8").replace(uri, "");
			servletContext.setAttribute(MVCConfigConstant.BASE_URL_KEY, MVCConfigConstant.BASE_URL);
		} else {
			if (servletContext.getAttribute(MVCConfigConstant.BASE_URL_KEY) == null) {
				MVCConfigConstant.BASE_URL = URLDecoder.decode(request.getRequestURL().toString(),"utf-8").replace(uri, "");
				servletContext.setAttribute(MVCConfigConstant.BASE_URL_KEY, MVCConfigConstant.BASE_URL);
			}
		}
		
		LogFactory.getMVCLogger(EWebFilter.class).debug("${" + MVCConfigConstant.BASE_URL_KEY + "} -> " + MVCConfigConstant.BASE_URL);
	}

	/**
	 * 解析HTTP Method，得到请求方法的类型（POST | GET | PUT | DELETE）
	 * 
	 * @return
	 */
	private String parseMethod(HttpServletRequest request) {
		String reqMethod = request.getMethod();

		if (!Http.Method.POST.equalsIgnoreCase(reqMethod))
			return reqMethod;

		String _method = request.getParameter(MVCConfigConstant.HTTP_METHOD_PARAM);
		// POST
		if (_method == null)
			return reqMethod;

		if (Http.Method.PUT.equalsIgnoreCase(_method.trim()))
			reqMethod = Http.Method.PUT;
		else if (Http.Method.DELETE.equalsIgnoreCase(_method.trim()))
			reqMethod = Http.Method.DELETE;

		return reqMethod;
	}

	/**
	 * 退出Filter
	 */
	public void destroy() {
		String info = "eweb4j filter destroy invoke...\n";
		LogFactory.getMVCLogger(EWebFilter.class).debug(info);
		//CallBack after destroy
		ConfigBean cb = (ConfigBean)SingleBeanCache.get(ConfigBean.class.getName());
		Listeners listeners = cb.getListeners();
		if (listeners != null && listeners.getListener() != null && !listeners.getListener().isEmpty()) {
			for (ListenerBean lb : listeners.getListener()){
				String clazz = lb.getClazz();
				try {
					EWeb4JListener listener = (EWeb4JListener)CommonUtil.loadClass(clazz).newInstance();
					listener.onDestroy();
					LogFactory.getMVCLogger(EWebFilter.class).debug("listener->"+listener+".onDestroy execute...");
				} catch (Throwable e) {
					e.printStackTrace();
				} 
			}
		}
	}

	private void normalReqLog(String uri) {
		StringBuilder sb = new StringBuilder();
		sb.append("normal uri -> ").append(uri);
		LogFactory.getMVCLogger(EWebFilter.class).debug(sb.toString());
	}

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
