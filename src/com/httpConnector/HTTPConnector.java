//$Id$
package com.httpConnector;

import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.methods.HttpDelete;
public class HTTPConnector {
	private int connectionTimeout;
	private int socketTimeout;
	private int statusCode;
	private String responseBody;
	private String requestBody;
	private String url;
	private HashMap<String,String> requestParams = new HashMap<String, String>();
	private HashMap<String,String> requestHeaders = new HashMap<String, String>();
//	public String toString()
//	{
//		return 	"url::"+url+"\n"
//				+"RequestParams::"+requestParams+"\n"
//				+"RequestHeaders::"+requestHeaders+"\n"
//				+"StatusCode::"+statusCode+"\n"
//				+"Response::"+responseBody+"\n"
//				+"RequestBody::"+requestBody+"\n";
//	}
	/*
	 * Constructors
	 */
	public HTTPConnector(){
		this.connectionTimeout = 30000;
		this.socketTimeout = 500000;
	}
	public HTTPConnector(int connectionTimeout,int socketTimeout){
		this.connectionTimeout = connectionTimeout;
		this.socketTimeout = socketTimeout;
	}
	/*
	 * getter setters
	 */
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void addParam(String key,String value) {
		requestParams.put(key, value);
	}
	public void addHeadder(String key,String value) {
		requestHeaders.put(key, value);
	}
	
	private HttpClient getHttpClient()
	{
		HttpClient hClient = new HttpClient();
		MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = cm.getParams();
		params.setConnectionTimeout(connectionTimeout);
		params.setSoTimeout(socketTimeout); //set socket timeout (how long it takes to retrieve data from remote host)
		hClient.setHttpConnectionManager(cm);
		return hClient;
	}
	
	private void validate(String method) throws Exception
	{
		if (url==null) {
			throw new Exception("Please specify url before perform "+method);
		}
	}
	/*
	 * post method
	 */
	public String post() throws Exception
	{
		validate("post");
		PostMethod post = new PostMethod(url);
		if(requestParams!=null&&!requestParams.isEmpty())
		{
			for(String key: requestParams.keySet())
			{
				String value=requestParams.get(key);
				if(value!=null)
				{
					post.addParameter(key, value);
				}
			}
		}
		if(requestHeaders!=null && !requestHeaders.isEmpty())
		{
			for(String key: requestHeaders.keySet())
			{
				String value=requestHeaders.get(key);
				if(value!=null)
				{
					post.setRequestHeader(key, value);
				}
			}
			
		}
		post.setRequestBody(getRequestBody());
		getHttpClient().executeMethod(post);
		responseBody = post.getResponseBodyAsString();
		statusCode = post.getStatusCode();
		return responseBody;
	}
	/*
	 * GET Methods
	 */
	public  String get() throws Exception
	{
		validate("get");
		GetMethod get = new GetMethod(url);
		if(requestParams!=null&&!requestParams.isEmpty())
		{					
			HttpMethodParams temp = new HttpMethodParams();
			for(String key: requestParams.keySet())
			{
				String value=requestParams.get(key);
				if(value!=null)
				{
					temp.setParameter(key, value);
				}
			}
			get.setParams(temp);
		}
		if(requestHeaders!=null && !requestHeaders.isEmpty())
		{
			for(String key: requestHeaders.keySet())
			{
				String value=requestHeaders.get(key);
				if(value!=null)
				{
					org.apache.commons.httpclient.Header header = new org.apache.commons.httpclient.Header();
					header.setName(key);				
					header.setValue(value);
					get.addRequestHeader(header);
				}
			}
		}
		getHttpClient().executeMethod(get);
		responseBody = get.getResponseBodyAsString();
		statusCode = get.getStatusCode();
		return responseBody;
	}
	
	public String put() throws Exception
	{
		validate("put");
		PutMethod put = new PutMethod(url);
		if(requestHeaders!=null && !requestHeaders.isEmpty())
		{
			for(String key: requestHeaders.keySet())
			{
				String value=requestHeaders.get(key);
				if(value!=null)
				{
					put.setRequestHeader(key, value);
				}
			}
			
		}
		put.setRequestBody(getRequestBody());
		getHttpClient().executeMethod(put);
		responseBody = put.getResponseBodyAsString();
		statusCode = put.getStatusCode();
		return responseBody;
	}
	public String delete() throws Exception{
		validate("delete");
		DeleteMethod delete = new DeleteMethod(url);
		if(requestParams!=null&&!requestParams.isEmpty())
		{					
			HttpMethodParams temp = new HttpMethodParams();
			for(String key: requestParams.keySet())
			{
				String value=requestParams.get(key);
				if(value!=null)
				{
					temp.setParameter(key, value);
				}
			}
			delete.setParams(temp);
		}
		if(requestHeaders!=null && !requestHeaders.isEmpty())
		{
			for(String key: requestHeaders.keySet())
			{
				String value=requestHeaders.get(key);
				if(value!=null)
				{
					delete.setRequestHeader(key, value);
				}
			}
			
		}
		getHttpClient().executeMethod(delete);
		responseBody = delete.getResponseBodyAsString();
		statusCode = delete.getStatusCode();
		return responseBody;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public int getResponseCode() {
		return statusCode;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
}
	
