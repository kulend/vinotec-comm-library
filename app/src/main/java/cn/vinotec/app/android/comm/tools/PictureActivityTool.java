package cn.vinotec.app.android.comm.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.ToastUtil;

/**
 * 拍照工具类 调用该类须从在activity的onActivityResult获取返回数据
 * 
 * @auther:summer 时间： 2012-7-2 上午10:54:56
 */
public class PictureActivityTool
{
	public static int TaskId;

	private static final String TAG = "PictureActivityUtil";
	/** 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 168;
	/** 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
	/** 图片裁剪 */
	public static final int PHOTO_CROP = PHOTO_PICKED_WITH_DATA + 1;
	/** 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/vino/photos/");

	private static File mCurrentPhotoFile;// 照相机拍照得到的图片
	private static String mCurrentPhotoPath;

	private static int cut_w, cut_h;// 裁剪图片宽度,高度

    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 3000;
    public static final int STORAGE_PERMISSIONS_REQUEST_CODE = 3001;

	/**
	 * 得到本地图片路径
	 * 
	 * @return
	 */
	public static File getmCurrentPhotoFile()
	{
		if (mCurrentPhotoFile == null)
		{
			if (!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();// 创建照片的存储目录
			mCurrentPhotoPath = "photo-" + System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mCurrentPhotoPath);
			if (!mCurrentPhotoFile.exists())
			{
				try
				{
					mCurrentPhotoFile.createNewFile();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return mCurrentPhotoFile;
	}

	/**
	 * 设置裁剪图片尺寸
	 * 
	 * @param w
	 * @param h
	 */
	public static void InitCutSize(int w, int h)
	{
		cut_w = w;
		cut_h = h;
	}

	/**
	 * 开始启动照片选择框
	 * 
	 * @param context
	 * 
	 */
	public static void doPickPhotoAction(final Activity context, final Fragment fragment, int taskId)
	{
		PictureActivityTool.TaskId = taskId;

		View popwin = LayoutInflater.from(context).inflate(R.layout.comm_popwin_photo_select, null);
		final PopupWindow mPopWindow = new PopupWindow(popwin, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
				true);
		mPopWindow.setAnimationStyle(R.style.anim_pop_bottom);
		// 设置半透明灰色
		ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
		mPopWindow.setBackgroundDrawable(dw);

//		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//		lp.alpha = 0.7f; // 0.0-1.0
//		context.getWindow().setAttributes(lp);
//		mPopWindow.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//				lp.alpha = 1f; // 0.0-1.0
//				context.getWindow().setAttributes(lp);
//			}
//		});
		// mPopWindow.setOutsideTouchable(true);
		mPopWindow.setClippingEnabled(true);
		mPopWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

		TextView btn_take_photo = (TextView) popwin.findViewById(R.id.btn_take_photo);
		TextView btn_pick_photo = (TextView) popwin.findViewById(R.id.btn_pick_photo);
		TextView btn_cancel = (TextView) popwin.findViewById(R.id.btn_cancel);
		btn_take_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                // 用户点击了从照相机获取
                autoObtainCameraPermission(context, fragment);
				mPopWindow.dismiss();
			}
		});
		btn_pick_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                autoObtainStoragePermission(context, fragment);
				mPopWindow.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopWindow.dismiss();
			}
		});
	}

    /**
     * 获取相机权限
     */
    private static void autoObtainCameraPermission(Activity activity, Fragment fragment) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                ToastUtil.showShortToast(activity, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {
            //有权限直接调用系统相机拍照
            doTakePhoto(activity, fragment);
        }
    }

    /**
     * 自动获取sdk权限
     */
    private static void autoObtainStoragePermission(Activity context, Fragment fragment) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            // 从相册中去获取
            doPickPhotoFromGallery(context, fragment);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

	/**
	 * 拍照获取图片
	 * 
	 */
	public static void doTakePhoto(Activity context, Fragment fragment)
	{
        if (hasSdcard()) {
            try
            {
                if (!PHOTO_DIR.exists())
                    PHOTO_DIR.mkdirs();// 创建照片的存储目录
                mCurrentPhotoPath = "photo-" + System.currentTimeMillis() + ".jpg";
                mCurrentPhotoFile = new File(PHOTO_DIR, mCurrentPhotoPath);// 给新照的照片文件命名
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                Uri imageUri = Uri.fromFile(mCurrentPhotoFile);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    //通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(context, "com.zcptea.app.android.client.fileprovider", mCurrentPhotoFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 指定图片输出地址
                if (fragment != null)
                {
                    fragment.startActivityForResult(intent, CAMERA_WITH_DATA);
                }
                else
                {
                    context.startActivityForResult(intent, CAMERA_WITH_DATA);
                }
            }
            catch (Exception e)
            {
                Toast.makeText(context, "没有发现照相机!", Toast.LENGTH_LONG).show();
                ToastUtil.debugToast(context, e.toString());
            }
        } else {
            ToastUtil.showShortToast(context, "设备没有SD卡！");
        }
	}

	/**
	 * 请求Gallery相册程序
	 * 
	 * @param context
	 */
    public static void doPickPhotoFromGallery(Activity context, Fragment fragment)
	{
		try
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.putExtra("crop", "false");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 80);
			intent.putExtra("outputY", 80);
			//intent.putExtra("return-data", true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (fragment != null)
			{
				fragment.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			}
			else
			{
				context.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
			}
		}
		catch (Exception e)
		{
			Toast.makeText(context, "没有发现设备!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 所有图片裁剪回调 如不需要裁剪及不用调用该方法
	 * 
	 * @param context
	 *            图片资源地址
	 */
	public static void doCropPhoto(Activity context, Fragment fragment, Intent data)
	{
		try
		{

			Intent intent = new Intent("com.android.camera.action.CROP");
			Uri uri = null;
			if (data.getData() != null)
			{
				uri = data.getData();
			}
			else
			{
				uri = Uri.fromFile(getCameraPath(data)); // 拍照返回数据
			}
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", true);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", cut_w != 0 ? cut_w : 800);
			intent.putExtra("outputY", cut_h != 0 ? cut_h : 800);
			if (fragment != null)
			{
				fragment.startActivityForResult(intent, PHOTO_CROP);
			}
			else
			{
				context.startActivityForResult(intent, PHOTO_CROP);
			}
		}
		catch (Exception e)
		{
			Toast.makeText(context, "没有发现设备!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取不裁剪时图片返回的路径
	 * 
	 * @param activity
	 * @param data
	 * @return path
	 */
	public static String getNoCropPath(Activity activity, Intent data)
	{
		Bundle extras = data.getExtras();
		if (extras != null)
		{
			return getCameraPath(data).toString();// 获取拍照图片路径
		}
		String path = "";
		Uri imageuri = data.getData();
		if (imageuri != null)
		{
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(imageuri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.moveToFirst())
			{
				path = cursor.getString(column_index);
			}
		}
		return path;
	}

	public static String getFilePath(Activity activity, Intent data, int requestCode)
	{
		if (requestCode == PictureActivityTool.CAMERA_WITH_DATA)
		{
			// 照相机拍照返回
			return PictureActivityTool.getmCurrentPhotoFile().toString();
		}
		else
		{
            String path = "";
            Uri imageuri = data.getData();
            if(imageuri == null)
            {
                ToastUtil.showShortToast(activity, "有错误发生!");
                return "";
            }
            path = getPath(activity, imageuri);
			return path;
		}
	}

	/**
	 * 裁剪后的图片路径
	 * 
	 * @return
	 */
	public static String getCropPath(Activity activity, Intent data)
	{

		String path = "";
		Uri imageuri = data.getData();
		if (imageuri == null)
		{
			String str = data.getAction();
			imageuri = Uri.parse(str);
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(imageuri, proj, null, null, null);
		if (cursor != null)
		{
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.moveToFirst())
			{
				path = cursor.getString(column_index);
			}
		}
		if (path == "")
		{
			path = getCameraPath(data).toString();
		}
		return path;
	}

	/**
	 * 拍照图片路径
	 * 
	 * @return
	 */
	public static File getCameraPath(Intent data)
	{
		Bundle extras = data.getExtras();
		Bitmap myBitmap = (Bitmap) extras.get("data");
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(PictureActivityTool.getmCurrentPhotoFile());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
		return PictureActivityTool.getmCurrentPhotoFile();
	}

    @SuppressLint("NewApi")
    private static String getPath(final Context context, final Uri uri)
    {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
