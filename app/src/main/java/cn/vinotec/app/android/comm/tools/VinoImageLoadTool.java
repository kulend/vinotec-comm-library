package cn.vinotec.app.android.comm.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

//图片加载工具类
public class VinoImageLoadTool {

    private RequestManager requestManager;
    private RequestBuilder<?> builder;
    private static RequestOptions defaultOptions;

    public static void init(int emptyPhotoResId, int errorPhotoResId)
    {
        defaultOptions = new RequestOptions();
        defaultOptions.placeholder(emptyPhotoResId);
        defaultOptions.error(errorPhotoResId);
    }

    public static VinoImageLoadTool with(@NonNull Activity activity) {
        VinoImageLoadTool tool = new VinoImageLoadTool();
        tool.requestManager = Glide.with(activity);
        return tool;
    }

    public static VinoImageLoadTool with(@NonNull Fragment fragment) {
        VinoImageLoadTool tool = new VinoImageLoadTool();
        tool.requestManager = Glide.with(fragment);
        return tool;
    }

    public static VinoImageLoadTool with(@NonNull View view) {
        VinoImageLoadTool tool = new VinoImageLoadTool();
        tool.requestManager = Glide.with(view);
        return tool;
    }

    public VinoImageLoadTool load(@Nullable Bitmap bitmap) {
        builder = requestManager.load(bitmap);
        return this;
    }

    public VinoImageLoadTool load(@Nullable Drawable drawable) {
        builder = requestManager.load(drawable);
        return this;
    }

    public VinoImageLoadTool load(@Nullable String url) {
        builder =requestManager.load(url);
        return this;
    }

    public VinoImageLoadTool load(@Nullable Uri uri) {
        builder = requestManager.load(uri);
        return this;
    }

    public VinoImageLoadTool load(@Nullable File file) {
        requestManager.load(file);
        return this;
    }

    public VinoImageLoadTool load(@RawRes @DrawableRes @Nullable Integer resourceId) {
        requestManager.load(resourceId);
        return this;
    }

    public VinoImageLoadTool apply(@NonNull RequestOptions requestOptions) {
        builder.apply(requestOptions);
        return this;
    }

    public VinoImageLoadTool into(@NonNull ImageView view) {
        builder.into(view);
        return this;
    }
}
