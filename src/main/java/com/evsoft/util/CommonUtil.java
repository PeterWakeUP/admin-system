package com.evsoft.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用方法工具类
 * 
 * @author liuzifeng
 */
public class CommonUtil {
	
	/**
	 * 日志工具
	 */
    public static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * 身份证格式简单校验
	 * @param idCard
	 * @return
	 */
	public static boolean isIDCard(String idCard){
		//String regex = "^(13|14|15|17|18)[0-9]{9}$";//手机号正则表达式
		String regex = "(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)";//格式15位，18位末尾为X或数字
		return idCard.matches(regex);
	}

	public static boolean isGuid(String guid)
	{
		String regex="^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
		return guid.toLowerCase().matches(regex);
	}
	/**
	 * 将数组转换为{@link List}，空数组返回空的{@link ArrayList}（非null）
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> List<T> toList(T... array) {
		return array == null || array.length == 0 ? new ArrayList<>() : new ArrayList<>(Arrays.asList(array));
	}
	
	/**
	 * 将集合{@link Collection}转换为{@link List}，空集合返回空的{@link ArrayList}（非null）
	 * @param <T>
	 * @param col
	 * @return
	 */
	public static <T> List<T> toList(Collection<T> col) {
		return isBlank(col) ? new ArrayList<>() : new ArrayList<>(col);
	}
	
	/**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * CommonUtil.isBlank(null)      = true
     * CommonUtil.isBlank("")        = true
     * CommonUtil.isBlank(" ")       = true
     * CommonUtil.isBlank("bob")     = false
     * CommonUtil.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * <p>Check if a Collection is null or empty (0 element)</p>
	 * 
	 * @param col
	 * @return
	 */
	public static boolean isBlank(Collection<?> col) {
		return col == null || col.isEmpty();
	}
	
	/**
	 * <p>Check if a value is int (Integer)</p>
	 * 
     * <pre>
     * CommonUtil.isInteger("1")     = true
     * CommonUtil.isInteger("-2")    = true
     * CommonUtil.isInteger("  3  ") = false
     * CommonUtil.isInteger("2.0")   = false
     * CommonUtil.isInteger("0.3")   = false
     * CommonUtil.isInteger("bob")   = false
     * CommonUtil.isInteger(null)    = false
     * </pre>
     * 
	 * @param value
	 * @return
	 */
	public static boolean isInteger(String value) {
		if(!isBlank(value)) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
			}
		}
		return false;
	}

	/**
	 * <p>Parse value to int, return default value if exception</p>
	 *
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int getInteger(String value, int defaultValue) {
		if(!isBlank(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
		}
		return defaultValue;
	}

	/**
	 * <p>Parse value to double, return default value if exception</p>
	 *
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static double getDouble(String value, double defaultValue) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 判断是否为合法的密码（合法密码：6-20位英文字母、数字或下划线的组合）
	 * @param pwd
	 * @return true 合法；false 不合法
	 */
	public static boolean isLegalPassword(String pwd) {
		return pwd != null && pwd.matches("[a-zA-Z0-9\\_]{6,20}");
	}
	
	/**
	 * 判断 类型为 &lt;T&gt; 的对象各属性值是否为null
	 * <p>默认过滤 "serialVersionUID" 的值
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static <T> boolean isNull(T obj) {
		if(obj == null) return true;
		boolean NULL = true;
		try {
			for(Field f : obj.getClass().getDeclaredFields()) {
				if(f.getName().equals("serialVersionUID")) continue;
				f.setAccessible(true);
				if(f.get(obj) != null) {
					NULL = false;
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return NULL;
	}
	
	/**
	 * 判断 类型为 &lt;T&gt; 的对象各属性值是否为空（不为空的标准：基础类型 !=0,!=true，其他对象 !=null）
	 * <p>默认过滤 "serialVersionUID" 的值
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static <T> boolean isEmpty(T obj) {
		if(obj == null) return true;
		boolean empty = true;
		try {
			for(Field f : obj.getClass().getDeclaredFields()) {
				if(f.getName().equals("serialVersionUID")) continue;
				String type = f.getGenericType().toString();
				f.setAccessible(true);
				
				if(type.equals(int.class.toString())) {
					empty = f.getInt(obj) == 0;
				} else if(type.equals(long.class.toString())) {
					empty = f.getLong(obj) == 0;
				} else if(type.equals(float.class.toString())) {
					empty = BigDecimal.valueOf(f.getFloat(obj)).compareTo(BigDecimal.ZERO) == 0;
				} else if(type.equals(double.class.toString())) {
					empty = BigDecimal.valueOf(f.getDouble(obj)).compareTo(BigDecimal.ZERO) == 0;
				} else if(type.equals(byte.class.toString())) {
					empty = f.getByte(obj) == 0;
				} else if(type.equals(short.class.toString())) {
					empty = f.getShort(obj) == 0;
				} else if(type.equals(boolean.class.toString())) {
					empty = !f.getBoolean(obj);
				} else {
					empty = f.get(obj) == null;
				}
				if(!empty) break;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return empty;
	}
	
	/**
	 * 取交集
	 * @param <T>
	 * @param a
	 * @param b
	 */
	public static <T> List<T> ins(List<T> a, List<T> b) {
		if(isBlank(a) || isBlank(b)) return new ArrayList<>();
		List<T> c = new ArrayList<>();
		for(T o : a) {
			if(b.contains(o)) c.add(o);
		}
		return c;
	}

	public static Double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 取并集
	 * @param <T>
	 * @param a
	 * @param b
	 */
	public static <T> List<T> uni(List<T> a, List<T> b) {
		if(isBlank(a)) return b;
		if(isBlank(b)) return a;
		List<T> c = new ArrayList<>();
		c.addAll(a);
		for(T o : b) {
			if(!c.contains(o)) c.add(o);
		}
		return c;
	}
	
	/**
	 * 将对象集合按照拼接规则拼接成字符串，类似于JavaScript中的join方法 （空集合，返回emptyValue，可指定空串或null）
	 * <p>e.g. : ["1", "2", "3"] -> "1,2,3" or "1;2;3", ...
	 * @param collection
	 * @param notation
	 * @param emptyValue
	 * @return
	 */
	public static <T> String join(Collection<T> collection, String notation, String emptyValue) {
		if(isBlank(collection)) return emptyValue;
		StringBuilder sb = new StringBuilder();
		for(T o : collection) {
			sb.append(notation).append(o);
		}
		return sb.substring(notation.length());
	}
	
	/**
	 * 将对象集合按照拼接规则拼接成字符串，类似于JavaScript中的join方法 （空集合，返回null）
	 * <p>e.g. : ["1", "2", "3"] -> "1,2,3" or "1;2;3", ...
	 * @param collection
	 * @param notation
	 * @return
	 */
	public static <T> String join(Collection<T> collection, String notation) {
		return join(collection, notation, null);
	}
	
	/**
	 * 将对象数组按照拼接规则拼接成字符串，类似于JavaScript中的join方法 （空数组，返回emptyValue，可指定空串或null）
	 * <p>e.g. : ["1", "2", "3"] -> "1,2,3" or "1;2;3", ...
	 * @param ary
	 * @param notation
	 * @param emptyValue
	 * @return
	 */
	public static <T> String join(T[] ary, String notation, String emptyValue) {
		return ary == null || ary.length == 0 ? emptyValue : join(toList(ary), notation, emptyValue);
	}
	
	/**
	 * 将对象数组按照拼接规则拼接成字符串，类似于JavaScript中的join方法 （空数组，返回null）
	 * <p>e.g. : ["1", "2", "3"] -> "1,2,3" or "1;2;3", ...
	 * @param ary
	 * @param notation
	 * @return
	 */
	public static <T> String join(T[] ary, String notation) {
		return join(ary, notation, null);
	}
	
	/**
	 * 获取对象的一个副本，默认包含父类（解除引用）
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static <T> T copy(T obj) {
		return copy(obj, true);
	}
	
	/**
	 * 获取对象的一个副本（解除引用）
	 * @param <T>
	 * @param obj
	 * @param withParent
	 * @return
	 */
	public static <T> T copy(T obj, boolean withParent) {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>)obj.getClass();
			T target = clazz.newInstance();
			List<Field> fs = toList(clazz.getDeclaredFields());
			if(withParent) {
				fs.addAll(toList(clazz.getSuperclass().getDeclaredFields()));
			}
			for(Field f : fs) {
				f.setAccessible(true);
				Object value = f.get(obj);
				f.set(target, value);
			}
			return target;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取Map的一个副本（解除引用）
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V> Map<K, V> copy(Map<K, V> map) {
		Map<K, V> mp = new HashMap<K, V>();
		for(Entry<K, V> en : map.entrySet()) {
			mp.put(en.getKey(), en.getValue());
		}
		return mp;
	}
	
	/**
	 * 获取列表的一个副本（解除引用）
	 * @param src
	 * @return
	 */
	public static <T> List<T> copy(List<T> src) {
		List<T> list = new ArrayList<T>();
		if(isBlank(src)) return list;
		for(T o : src) {
			list.add(o);
		}
		return list;
	}

	/**
	 * 生成标准的32位MD5值, null、空字符串、多个空格字符串均返回空字符
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		if (text == null || StringUtils.isEmpty(text.trim()))
			return "";
		try {
			StringBuilder sb = new StringBuilder();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes(StandardCharsets.UTF_8));
			for (byte b : md.digest()) {
				int n = b;
				if(n < 0) n += 256;
				if(n < 16) sb.append("0");
				sb.append(Integer.toHexString(n));
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error("{}",e);
		}
		return null;
	}
	
	/**
	 * 使用SHA-1加密
	 * @param text
	 * @return
	 */
	public static String sha1(String text) {
		Formatter ft = new Formatter();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
	        md.update(text.getBytes(StandardCharsets.UTF_8));
			for (byte b : md.digest()) {
				ft.format("%02x", b);
			}
	        String str = ft.toString();
	        return str;
		} catch (Exception e) {
            logger.error("{}",e);
		} finally {
			ft.close();
		}
		return null;
	}
	
	/**
	 * 获取 url 数据
	 * @param url
	 * @return
	 */
	public static String getUrlData(String url) {
		return getUrlData(url, null);
	}

	/**
	 * 获取指定中文字符编码的 url 数据（超时设置：10秒）
	 * @param url
	 * @param charset	-- 编码 e.g. "UTF-8", "GBK"
	 * @return
	 */
	public static String getUrlData(String url, String charset) {
		return getUrlData(url, charset, 10000, 10000);
	}
	
	/**
	 * 获取指定中文字符编码的 url 数据
	 * @param url
	 * @param charset	-- 编码 e.g. "UTF-8", "GBK"
	 * @param connectTimeout	-- 连接超时时间
	 * @param readTimeout		-- 读取超时时间
	 * @return
	 */
	public static String getUrlData(String url, String charset, int connectTimeout, int readTimeout) {
		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(connectTimeout); // 连接超时设置
			conn.setReadTimeout(readTimeout); // 读取超时设置
			return getStringFromInputStream(conn.getInputStream(), charset);
		} catch (Exception e) {
			logger.error(e.getMessage() + " [URL: " + url + "] ", e);
		}
		return null;
	}
	
//	public static String httpGet(String url) {
//		return httpGet(url, null);
//	}
	
//	public static String httpGet(String url, String params) {
//		HttpClient client = new HttpClient();
//		if(!isBlank(params)) url += "?" + params;
//		HttpMethod method = new GetMethod(url);
//		HttpMethodParams param = method.getParams();
//		param.setContentCharset("UTF-8");
//		try {
//			client.executeMethod(method);
//			return getStringFromInputStream(method.getResponseBodyAsStream(), null);
//		} catch (IOException e) {
//			logger.error(e.getMessage() + " [URL: " + url + "] ");
//		}
//		return null;
//	}
	
//	public static String httpPost(String url) {
//		return httpPost(url, null);
//	}
	
//	public static String httpPost(String url, String params) {
//		HttpClient client = new HttpClient();
//		PostMethod method = new PostMethod(url);
//		if(params != null && params.length() > 0) {
//			String[] ps = params.split("&");
//			NameValuePair[] nvp = new NameValuePair[ps.length];
//			for(int i = 0; i < ps.length; i++) {
//				String[] p = ps[i].split("=");
//				nvp[i] = new NameValuePair(p[0], p[1]);
//			}
//			method.setRequestBody(nvp);
//		}
//		HttpMethodParams param = method.getParams();
//		param.setContentCharset("UTF-8");
//		try {
//			client.executeMethod(method);
//			return getStringFromInputStream(method.getResponseBodyAsStream(), null);
//		} catch (IOException e) {
//			logger.error(e.getMessage() + " [URL: " + url + "] ");
//		}
//		return null;
//	}
	
	private static String getStringFromInputStream(InputStream is, String charset) throws IOException {
		InputStreamReader isr = charset == null || charset.length() == 0 ? new InputStreamReader(is) : new InputStreamReader(is, charset);
		BufferedReader in = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String str = null;
		boolean firstLine = true;
		while((str = in.readLine()) != null) {
			if(!firstLine) sb.append('\n');
			sb.append(str);
			firstLine = false;
		}
		in.close();
		isr.close();
		is.close();
		return sb.toString();
	}

	/**
	 * 以post请求
	 * @param url
	 * @param data		-- 数据
	 * @param dataType	-- 类型：xml, json
	 *
	 * @return
	 */
	public static String post(String url, String data, String dataType,int timeOut) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xml", "text/xml");
		map.put("json", "application/json");
		map.put("form", "application/x-www-form-urlencoded");
		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true); // 发送POST请求， 必须设置允许输出
			conn.setUseCaches(false);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", map.get(dataType));

			// 发送POST内容到服务器
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
			bw.write(data);
			bw.flush();
			bw.close();

			if(conn.getResponseCode() != 200) {
				throw new Exception("请求URL失败");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str).append('\n');
			}
			br.close();
			conn.disconnect();
			return sb.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 以post请求
	 * @param url
	 * @param data		-- 数据
	 * @param dataType	-- 类型：xml, json
	 *
	 * @return
	 */
	public static String post(String url, String data, String dataType) {
		return post(url, data, dataType, 5000);
	}

	/**
	 * 发送HttpPost请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, BasicNameValuePair... params) {
		return post(url, Arrays.asList(params));
	}

    /**
     * 发送HttpPost请求，参数为map
     * @param url
     * @param paramMap
     * @return
     */
    /*public static String post(String url, Map<String, String> paramMap) {
		List<BasicNameValuePair> params = Lists.newArrayList();
		for (Entry<String, String> entry : paramMap.entrySet()) {
            //给参数赋值
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
		return post(url, params);
	}*/

    /**
     * 发送HttpPost请求，参数为map
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, List<BasicNameValuePair> params) {
		CloseableHttpClient httpclient = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params , Consts.UTF_8);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			if(response != null){
				HttpEntity entity1 = response.getEntity();
				if(entity1 != null){
					result = EntityUtils.toString(entity1);
				}
			}
		} catch (Exception e) {
			logger.error("{}",e);
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (Exception e) {
                    logger.error("{}",e);
				}
			}
		}
		return result;
	}

    /**
     * 发送HttpPost请求，参数为map
     * @param clazz
     * @param url
     * @param map
     * @param <T>
     * @return
     */
    /*public static <T> T post(Class<T> clazz, String url, Map<String, String> map) {
        String res = post(url, map);
		return res == null ? null :  JSONObject.parseObject(res,clazz);
    }*/

    /**
     * 发送HttpGet请求
     * @param url
     * @return
     */
    public static String get(String url) {
		return get(url, 3000);
	}

	/**
	 * 发送HttpGet请求
	 * @param url
	 * @param timeOut
	 * @return
	 */
	public static String get(String url,int timeOut) {
        CloseableHttpClient httpclient = null;
        String result = null;
        try {
			httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			RequestConfig.Builder builder = RequestConfig.custom().setConnectTimeout(timeOut).setSocketTimeout(timeOut);
			httpget.setConfig(builder.build());
			CloseableHttpResponse response = httpclient.execute(httpget);
			if(response != null){
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
            logger.error(e.getMessage());
		} finally {
			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (Exception e) {
                    logger.error("{}",e);
				}
			}
		}
		return result;
    }

    /**
	 * 将字符串转为w3c的Document对象
	 * @param xml
	 * @return
	 */
	public static Document parseDocument(String xml) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xml)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 验证Email地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		return email != null && email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
	}
	
	/**
	 * 验证多个Email地址
	 * @param emails
	 * @param seperator
	 * @return
	 */
	public static boolean isEmail(String emails, String seperator) {
		if(isBlank(emails) || isBlank(seperator)) return false;
		for(String e : emails.split(seperator)) {
			if(!isEmail(e)) return false;
		}
		return true;
	}
	
	/**
	 * 向客户端输出一段字符串
	 * @param response
	 * @param str
	 * @throws IOException
	 */
	public static void writeString(HttpServletResponse response, String str) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(str);
	}

	/**
	 * 向客户端输出一段JavaScript
	 * @param response
	 * @param script
	 * @throws IOException
	 */
	public static void writeJavaScript(HttpServletResponse response, String script) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter rw = response.getWriter();
		rw.write("<script>" + script + "</script>");
		rw.flush();
	}
	
	/**
	 * 生成uuid
	 * @return uuid
	 */
	public static String createUUID() {
        return UUID.randomUUID().toString();
	}

	/**
	 * 生成uuid
	 * @return uuid
	 */
	public static String UUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 生成6位随机整数
	 * @return
	 */
	public static int randomNumber() {
		return new Random().nextInt(900000) + 100000;
	}
	
	public static String fuzzyCardID(String cardID) {
		return cardID == null || cardID.length() < 15 ? "*" : cardID.substring(0, 6) + "********" + cardID.substring(14);
	}
	
	public static String fuzzyNumber(String mobile) {
		if(mobile.length() != 11) return "***********";
		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}

	/**
	 * 返回用户的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String toIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取本机的IP地址
	 * @return
	 */
	public static List<String> getCurServerIps() {
	    List<String> ips = new ArrayList<String>();
	    try{
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement();
	            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
	            while (ienum.hasMoreElements()) {
	                InetAddress ia = ienum.nextElement();
	                String adress = ia.getHostAddress().toString();
	                if( adress.length() < 16) {
	                	if(!adress.startsWith("127") && adress.indexOf(":") <= 0){
	                		ips.add(adress);
	                	}
	                }
	            }
	        }
	    } catch(Exception e) {
	        logger.error("error:{}", e);
	    }
	    return ips;
	}
	
	public static boolean matchIp(String check, String targetIp) {
		if(check.indexOf("*") == -1) return check.equals(targetIp);
		Pattern p = Pattern.compile(check.replaceAll("\\*", "[0-9]+"));
		return p.matcher(targetIp).matches();
	}

	public static void main(String[] agrs){
	    System.out.println(isPhone("8F8BA4FB-36A4-4250-9351-569050C9518F"));
	}

	public static boolean isPhone(String phoneNum){
		String regex = "^[1]\\d{10}$";//手机号正则表达式
		return phoneNum.matches(regex);
	}

	/**
	 * 隐藏手机号
	 * @param phone 手机号码
	 * */
	public static String hidePhone(String phone){
		return StringUtils.isEmpty(phone) ? "" : phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
	}

	public static String hideEmail(String email){
		int len = email.split("@")[0].length();//邮箱长度
		if(len>=7){
			return StringUtils.isEmpty(email) ? "" : email.replace(email.substring(3, 7), "****");
		}
		return StringUtils.isEmpty(email) ? "" : email.replace(email.substring(2, len), len==3?"*":len==4?"**":len==5?"***":"****");
	}

	public static String hideEmail2(String email){
		if(!StringUtils.isEmpty(email)){
			String[] e = email.split("@");
			StringBuffer head = new StringBuffer();
			for(int i=0;i<e[0].length();i++){
				head.append("*");
			}
			return head.toString() + e[1];
		}
		return "";
	}

	/**
	 * 获取邮箱登录地址
	 * */
	public static String getEmailLoginUrl(String email){
		String url = "";
		if (StringUtils.isEmpty(email)) return url;
		if (!isEmail(email)) return url;

		String type = email.substring(email.lastIndexOf("@") + 1);
		switch (type){
			case "163.com": url = "http://mail.163.com"; break;
			case "126.com": url = "http://mail.126.com"; break;
			case "139.com": url = "http://mail.139.com"; break;
			case "188.com": url = "http://mail.188.com"; break;
			case "189.cn": url = "http://mail.189.cn"; break;
			case "263.net": url = "http://mail.263.net"; break;
			case "vip.163.com": url = "http://vip.163.com"; break;
			case "yahoo.com": url = "http://mail.yahoo.com"; break;
			case "yahoo.com.cn": url = "http://cn.mail.yahoo.com"; break;
			case "gmail.com": url = "http://mail.google.com/mail/"; break;
			case "sina.com": url = "http://mail.sina.com.cn"; break;
			case "vip.sina.com": url = "http://mail.sina.com.cn/cgi-bin/viplogin.php"; break;
			case "sohu.com": url = "http://mail.sohu.com"; break;
			case "qq.com": url = "http://mail.qq.com"; break;
			case "live.com": url = "http://login.live.com"; break;
			case "hotmail.com": url = "http://login.live.com"; break;
			case "tom.com": url = "http://mail.tom.com"; break;
			case "yeah.net": url = "http://www.yeah.net"; break;
			case "foxmail.com": url = "http://mail.foxmail.com"; break;
			case "21cn.com": url = "http://mail.21cn.com"; break;
			default: url = "http://mail." + type; break;
		}
		return url;
	}

	/**
	 * 生成随机字符串(数字)
	 * @param len 字符串的位数
	 * @return 可插入数据库的字符串
	 */
	public static String getRandomString(int len) {
		String rndStr = "";
		Random rnd = new Random();
		for (int i = 0; i < len; i++) {
			rndStr += rnd.nextInt(10);
		}
		return rndStr;
	}

	/**
	 * 替换字符为*
	 * @param target
	 * @param s 开始位置
	 * @return
	 */
	public static String encryptedStr(String target,int s){
		if(StringUtils.isBlank(target))return "";
		return  encryptedStr(target,s,0);
	}

	/**
	 * 替换字符为*
	 * @param target
	 * @param s 开始位置
	 * @return
	 */
	public static String encryptedStr(String target,int s,int e){
		if(StringUtils.isBlank(target))return "";
		return new StringBuilder(target).replace(s,target.length()-e,getStartStr(target.length()-s-e)).toString();
	}

	/**
	 * 生成*字符串
	 * @param count
	 * @return
	 */
	public static String getStartStr(int count){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append("*");
		}
		return  sb.toString();
	}

	/**
	 * 取消小数点的后面的o 如 0.0500000 = 0.05
	 * @param num
	 * @return
	 */
	public static String deleteNumZero(String num) {
		if (StringUtils.isBlank(num)) return "0";
		NumberFormat nf = NumberFormat.getInstance();
        return nf.format(Double.parseDouble(num));
	}

	/**
	 * 截取字符串长度，ASCII字符长度为1，中文字符长度为2
	 * @param str
	 * @param maxLength
	 * @return
	 */
	public static String substr(String str, int maxLength) {
		if(isBlank(str) || maxLength <= 0) {
			return "";
		}
		int length = 0;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			String s = String.valueOf(str.charAt(i));
			int len = s.getBytes(StandardCharsets.UTF_8).length == 1 ? 1 : 2;
			length += len;
			if(length > maxLength) {
				break;
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 截取字符串长度，ASCII字符长度为1，中文字符长度为2
	 * @param str
	 * @param maxLength
	 * @return
	 */
	public static String substr(String str, int maxLength, String suffix) {
		if(isBlank(str) || maxLength <= 0) {
			return "";
		}
		int length = 0;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			String s = String.valueOf(str.charAt(i));
			int len = s.getBytes(StandardCharsets.UTF_8).length == 1 ? 1 : 2;
			length += len;
			if(length > maxLength) {
				if(!isBlank(suffix)) {
					sb.append(suffix);
				}
				break;
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 替换四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）的解决方案 😁 Micorsoft sql
	 * server->t_user_basic_info_ext->university 有这种字符
	 */
	public static String removeFourChar(String content) {
		byte[] conbyte = content.getBytes();
		for (int i = 0; i < conbyte.length; i++) {
			if ((conbyte[i] & 0xF8) == 0xF0) {
				for (int j = 0; j < 4; j++) {
					conbyte[i + j] = 0x30;
				}
				i += 3;
			}
		}
		content = new String(conbyte);
		return content.replaceAll("0000", "");
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 *
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate) {
		if (strDate == null && StringUtils.isEmpty(strDate))
			return true;

		Pattern pattern = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null && StringUtils.isEmpty(str))
			return true;

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 根据身份证获取性别
	 *
	 * @param idCard 身份证号
	 * @return
	 */
	public static int getGenderFromIdCard(String idCard) {
		if (org.apache.commons.lang.StringUtils.isEmpty(idCard)) {
			return 0;
		}
		return (idCard.length() == 18) ? (Integer.valueOf(idCard.substring(idCard.length() - 2, idCard.length() - 1)) % 2 == 0 ? 2 : 1) : 0;
	}

	/**
	 * 替换https,http公用方法
	 *
	 * @param url
	 * @return
	 */
	public static String convertHttpOrHttpsUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			return "//js2.tdw.cn/images/users/avatar/bav_head.png";
		}
		return url.trim().replace("https:", "").replace("http:", "");
	}

}
