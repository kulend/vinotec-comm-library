package cn.vinotec.app.android.comm.tools;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import cn.vinotec.app.android.comm.VinoApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

//图片加载工具类
public class VinoImageLoadTool {

    public static void init(int EmptyPhotoResId)
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(EmptyPhotoResId)
                .showImageForEmptyUri(EmptyPhotoResId)
                .showImageOnFail(EmptyPhotoResId)
                .cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(VinoApplication.getInstance().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    public static void show(ImageView view, String url)
    {
        ImageLoader.getInstance().displayImage(url, view);
    }

    public static void show(ImageView view, String url, int defResId)
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defResId)
                .showImageForEmptyUri(defResId)
                .showImageOnFail(defResId)
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoader.getInstance().displayImage(url, view, defaultOptions);
    }

    public static void show(final ImageView imageView, final String url, final String thumbUrl, DisplayImageOptions options)
    {
        if(ImageLoader.getInstance().getMemoryCache().get(url) != null
                || ImageLoader.getInstance().getDiscCache().get(url) != null)
        {
            //有缓存图片
            ImageLoader.getInstance().displayImage(url, imageView, options);
        }else
        {
            //先显示缩略图
            ImageLoader.getInstance().displayImage(thumbUrl, imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            imageView.setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }

    public static void show(final ImageView imageView, final String url, final String thumbUrl, int defResId)
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defResId)
                .showImageForEmptyUri(defResId)
                .showImageOnFail(defResId)
                .cacheInMemory(true).cacheOnDisc(true).build();
        show(imageView, url, thumbUrl, defaultOptions);
    }

    public static void show(final ImageView imageView, final String url, final String thumbUrl)
    {
        show(imageView, url, thumbUrl, null);
    }
}
