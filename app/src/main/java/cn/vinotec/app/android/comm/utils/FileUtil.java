package cn.vinotec.app.android.comm.utils;

import java.io.File;

public class FileUtil {
	
	public static boolean isFileIsExists(String path) {
		if (StringUtil.isBlank(path)) {
			return false;
		}
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
}
