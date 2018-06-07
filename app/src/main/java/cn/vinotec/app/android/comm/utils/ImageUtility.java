package cn.vinotec.app.android.comm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;

public class ImageUtility 
{
	
	public static Bitmap getbitmap(String path)
	{
		Bitmap pic = null;
		try {
            URL url;
            url = new URL(path);
            HttpURLConnection conn;
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            pic = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return pic;
	}

	/**
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName)
	{
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}

	public static Bitmap compressImage(Bitmap image)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 120)
 { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap getImageSetMaxPx(String srcPath)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww)
 {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		}
		else if (w < h && h > hh)
 {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compress(Bitmap image, float hh, float ww)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 2048)
		{
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 25, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		else if (baos.toByteArray().length / 1024 > 1024)
 {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		if (hh == 0)
		{
			hh = 800f;// 这里设置高度为800f
		}
		if (ww == 0)
		{
			ww = 480f;// 这里设置宽度为480f
		}
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww)
 {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		}
		else if (w < h && h > hh)
 {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/** 保存方法 */
	public static void saveBitmap(String imagePath, Bitmap bm, Bitmap.CompressFormat format)
	{
		if (bm == null || imagePath == null || "".equals(imagePath))
		{
			return;
		}

		File f = new File(imagePath);
		try
		{
			if (f.exists())
			{
				f.delete();
			}

			File parentFile = f.getParentFile();
			if (!parentFile.exists())
			{
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos;
			fos = new FileOutputStream(f);
			bm.compress(format, 100, fos);
			fos.close();
		} catch (FileNotFoundException e)
		{
			f.delete();
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
			f.delete();
		}
	}

	public static String drawTextAndCompress(Context gContext, String imagepath)
	{
		try
		{
			Bitmap bitmap = null;
			// 处理图片方向
			ExifInterface exifInterface = new ExifInterface(imagepath);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int rotate = 0;
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}
			// 读取图片
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagepath, options);
			int w = options.outWidth;
			int h = options.outHeight;
			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 800f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > ww)
 {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (options.outWidth / ww);
			}
			else if (w < h && h > hh)
 {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (options.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;// 设置缩放比例
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(imagepath, options);
			if (rotate > 0)
			{
				Matrix matrix = new Matrix();
				matrix.setRotate(rotate);
				Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
				if (rotateBitmap != null)
				{
					bitmap.recycle();
					bitmap = rotateBitmap;
				}
			}
			if (bitmap == null)
			{
				return imagepath;
			}
			bitmap = ImageUtility.compressImage(bitmap);

			// 添加水印
			// bitmap = ImageUtility.drawTextToBitmap(gContext, bitmap, "智慧安全");

			File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/apl/photos/");
			if (!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();// 创建照片的存储目录

			String path = "photo-" + System.currentTimeMillis() + "-final.jpg";
			File file = new File(PHOTO_DIR, path);
			if (!file.exists())
			{
				try
				{
					file.createNewFile();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			imagepath = file.toString();
			fos.flush();
			fos.close();
		} catch (OutOfMemoryError oom)
		{
			oom.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return imagepath;
	}

	@SuppressLint("SimpleDateFormat")
	public static Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap, String gText)
	{
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		// set default bitmap config if none
		if (bitmapConfig == null)
		{
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		// paint.setColor(Color.rgb(61,61,61));
		paint.setColor(Color.WHITE);
		// text size in pixels
		paint.setTextSize((int) (2 * scale * 3.5));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// draw text to the Canvas center
		Rect bounds = new Rect();
		// 添加时间戳
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(new Date());
		gText = gText + " " + str;
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		// int x = (bitmap.getWidth() - bounds.width()) / 2;
		// int y = (bitmap.getHeight() + bounds.height()) / 2;
		// draw text to the bottom
		int x = (bitmap.getWidth() - bounds.width()) / 10 * 9;
		int y = (bitmap.getHeight() - bounds.height());
		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}
}
