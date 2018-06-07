package cn.vinotec.app.android.comm.https;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.vinotec.app.android.comm.utils.ToastUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.CommonLog;
import cn.vinotec.app.android.comm.utils.HttpUtils;
import cn.vinotec.app.android.comm.utils.LogFactory;


public class CustomHttpClient {
	private static String TAG = "CustomHttpClient";
	private static final CommonLog log = LogFactory.createLog();
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static final String CHARSET_GB2312 = "GB2312";
	private static HttpClient customerHttpClient;

	private static HashMap<String, String> mHttpClientHeaders = new HashMap<String, String>();

	public static void addHeader(String name, String value) {
		mHttpClientHeaders.put(name, value);
	}

	public static void removeHeader(String name) {
		if (mHttpClientHeaders.containsKey(name)) {
			mHttpClientHeaders.remove(name);
		}
	}

	private CustomHttpClient() {

	}

	/**
	 * HttpClient post方法
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String PostFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
					CHARSET_UTF8);
			HttpPost httpPost = new HttpPost(url);
			
			Iterator<Entry<String, String>> iter = mHttpClientHeaders.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				httpPost.addHeader(key, val);
			}
			httpPost.setEntity(urlEncoded);
			HttpClient client = getHttpClient(context);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException(context.getResources().getString(R.string.comm_httpError), e);
		} 
	}

	/**
	 * HttpClient post方法
	 * 
	 */
	public static String PostFromWebByHttpClient(Context context, String url, MultipartEntity entity)
	{
		try
		{
			HttpPost httpPost = new HttpPost(url);
			Iterator<Entry<String, String>> iter = mHttpClientHeaders.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				httpPost.addHeader(key, val);
			}
			httpPost.setEntity(entity);
			HttpClient client = getHttpClient(context);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET_UTF8);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.w(TAG, e.getMessage());
			return null;
		}
		catch (ClientProtocolException e)
		{
			Log.w(TAG, e.getMessage());
			return null;
		}
		catch (IOException e)
		{
			throw new RuntimeException(context.getResources().getString(R.string.comm_httpError), e);
		}
	}

	public static String getFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) throws Exception{
			log.d("getFromWebByHttpClient url = " + url);
		try {
			// http地址
			// String httpUrl =
			// "http://192.168.1.110:8080/httpget.jsp?par=HttpClient_android_Get";
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if (nameValuePairs != null && nameValuePairs.length > 0) {
				sb.append("?");
				for (int i = 0; i < nameValuePairs.length; i++) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}
			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(sb.toString());
			Iterator<Entry<String, String>> iter = mHttpClientHeaders.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				httpRequest.addHeader(key, val);
			}
			// 取得HttpClient对象
			HttpClient httpclient = getHttpClient(context);
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException(context.getResources().getString(
R.string.comm_httpError));
			}
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(context.getResources().getString(
R.string.comm_httpError), e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ToastUtil.debugToast(context, e.toString());
			log.e("IOException ");
			e.printStackTrace();
			throw new RuntimeException(context.getResources().getString(
R.string.comm_httpError), e);
		} 	
	}

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private static synchronized HttpClient getHttpClient(Context context) {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 1000);
			/* 连接超时 */
			int ConnectionTimeOut = 6000;
			if (!HttpUtils.isWifiDataEnable(context)) {
				ConnectionTimeOut = 15000;
			}
			HttpConnectionParams
					.setConnectionTimeout(params, ConnectionTimeOut);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 8000);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			KeyStore trustStore;
			try {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				// 允许所有主机的验证
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				schReg.register(new Scheme("https", sf, 443));
				//schReg.register(new Scheme("https", sf, 8091));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}
}
