package org.hbhk.aili.support.server.httpclient;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hbhk.aili.support.server.httpclient.exception.ClientException;
import org.hbhk.aili.support.server.httpclient.exception.ResponseException;

public abstract class Client {

	protected Log log = LogFactory.getLog(getClass());
	
	protected Map<String, String> params;
	
	protected CloseableHttpClient client;
	
	protected HttpUriRequest request;
	
	protected Client(CloseableHttpClient client) {
		this.client = client;
	}

	public static Client post(String url) {
		return new HttpClientUtil(HttpClients.createDefault(), url);
	}

	public Client param(String name, String value) {
		this.params.put(name, value);
		return this;
	}
	
	public Client param(Map<String, String> paramMap) {
		if (paramMap != null) {
			this.params.putAll(paramMap);
		}
		return this;
	}
	
	protected abstract void addParams() throws ClientException;
	
	public ResponseContent<String> send() throws ClientException {
		ResponseContent<String> result = null;
		try {
			// params
			this.addParams();
			 // Create a custom response handler
            ResponseHandler<ResponseContent<String>> responseHandler = new ResponseHandler<ResponseContent<String>>() {
                public ResponseContent<String> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                	ResponseContent<String> result = new ResponseContent<String>();
                	result.setStatus(response.getStatusLine().getStatusCode());
        			result.setResult(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
                	return result;
                }
            };
			
            if (log.isDebugEnabled()) {
            	if(this.request != null){
            		log.debug("url:" + this.request.getURI());
            	}
            }
            result = this.client.execute(request, responseHandler);
		} catch (Exception e) {
			throw new ClientException(e.getMessage(), e);
		} finally {
			closeClient(this.client);
		}
		
		return result;
	}
	
	public static void closeClient(CloseableHttpClient client) throws ClientException {
		try {
			client.close();
		} catch (IOException e) {
			throw new ClientException(e.getMessage(), e);
		}
	}
	
	public static void assertNotFoundException(int status, String url) throws ResponseException {
		if (status == HttpStatus.SC_NOT_FOUND) {
			throw new ResponseException("not found : " + url, HttpStatus.SC_NOT_FOUND);
		}
	}
	
}