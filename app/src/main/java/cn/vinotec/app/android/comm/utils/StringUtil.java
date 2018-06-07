package cn.vinotec.app.android.comm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";
	public static final String EMAIL_REG_EXPRESSION = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(URL_REG_EXPRESSION, s);
	}

	public static boolean isEmail(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches(EMAIL_REG_EXPRESSION, s);
	}

	/**
	 * 手机号验证
	 *
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	/**
	 * 电话号码验证
	 *
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
			b = m.matches();
		}else{
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}

	public static String join(String join, Object[] strAry)
	{
		if (strAry == null || strAry.length == 0)
		{
			return "";
		}
		if (join == null)
		{
			join = ",";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strAry.length; i++)
		{
			if (i == (strAry.length - 1))
			{
				sb.append(strAry[i]);
			} else
			{
				sb.append(strAry[i]).append(join);
			}
		}

		return new String(sb);
	}

	public static String join(String join, long[] strAry) {
		if (strAry == null || strAry.length == 0) {
			return "";
		}
		if (join == null) {
			join = ",";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strAry.length; i++) {
			if (i == (strAry.length - 1)) {
				sb.append(strAry[i]);
			} else {
				sb.append(strAry[i]).append(join);
			}
		}

		return new String(sb);
	}

	public static String fromFile(File f) throws IOException {
		InputStream is = new FileInputStream(f);
		byte[] bs = new byte[is.available()];
		is.read(bs);
		is.close();
		return new String(bs);
	}

	public static void toFile(File f, String s) throws IOException {
		// 只有手机rom有足够的空间才写入本地缓存
		if (CommonUtil.enoughSpaceOnPhone(s.getBytes().length)) {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(s.getBytes());
			fos.close();
		}
	}

	public static String toMaxIntValue(int value, int max)
	{
		if (value > max)
		{
			return max + "+";
		}
		else
		{
			return String.valueOf(value);
		}
	}

	public static String NullToEmpty(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public static HashMap<String, String> parseCookieStr(String cookieValue) {
		if (cookieValue == null) {
			return new HashMap<String, String>();
		}
		HashMap<String, String> maps = new HashMap<String, String>();
		String[] datas = cookieValue.split(";");
		for (String data : datas) {
			String[] pars = data.split("=");
			if (pars.length == 2) {
				maps.put(pars[0].trim(), pars[1].trim());
			}
		}
		return maps;
	}

	public static String parseAndGetCookieValue(String cookieValue, String key) {
		HashMap<String, String> maps = parseCookieStr(cookieValue);
		if (maps.containsKey(key)) {
			return maps.get(key);
		} else {
			return null;
		}
	}
}
