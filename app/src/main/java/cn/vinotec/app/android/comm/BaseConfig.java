package cn.vinotec.app.android.comm;

public class BaseConfig {
	// 分钟
	public static int Content_ListCacheTime = 5;
	public static int Content_ContentCacheTime = 60 * 24 * 3;
	public static int ImageCacheTime = 60 * 24 * 15;
	public static int Content_DefaultCacheTime = 60 * 24 * 3;

	public static final String[] BASESUBCLASSES = new String[] {
 "cn.vinotec.app.android.comm.sqlite.ImageCacheColumn",
			"cn.vinotec.app.android.comm.sqlite.RequestCacheColumn" };

	protected static String[] EXTRASUBCLASSES = new String[] {};

	public static String[] getSubClasses() {
		String[] c = new String[BASESUBCLASSES.length + EXTRASUBCLASSES.length];
		System.arraycopy(BASESUBCLASSES, 0, c, 0, BASESUBCLASSES.length);
		System.arraycopy(EXTRASUBCLASSES, 0, c, BASESUBCLASSES.length, EXTRASUBCLASSES.length);
		return c;
	}
}
