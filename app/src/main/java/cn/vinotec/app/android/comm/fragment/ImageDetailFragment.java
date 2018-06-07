package cn.vinotec.app.android.comm.fragment;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.vinotec.app.android.comm.entity.ShowImageEntity;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.BitMapUtil;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageDetailFragment extends Fragment {
	private ShowImageEntity mImage;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(ShowImageEntity image)
	{
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putSerializable("image", image);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImage = getArguments() != null ? (ShowImageEntity) getArguments().getSerializable("image") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.comm_image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (mImage != null)
		{
			if (mImage.getType() == ShowImageEntity.Type_URL)
			{
				ImageLoader.getInstance().displayImage(mImage.getPath(), mImageView, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view)
					{
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason)
					{
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "下载错误";
							break;
						case DECODING_ERROR:
							message = "图片无法显示";
							break;
						case NETWORK_DENIED:
							message = "网络有问题，无法下载";
							break;
						case OUT_OF_MEMORY:
							message = "图片太大无法显示";
							break;
						case UNKNOWN:
							message = "未知的错误";
							break;
						}
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
					{
						progressBar.setVisibility(View.GONE);
						mAttacher.update();
					}
				});
			}
			else if (mImage.getType() == ShowImageEntity.Type_LOCAL)
			{
				Bitmap bmp = BitMapUtil.getBitmap(mImage.getPath(), 500, 500);
				mImageView.setImageBitmap(bmp);
			}
			else
			{
				// TODO:设置其他类型时的显示
				mImageView.setImageResource(R.drawable.comm_empty_photo);
			}
		}
		else
		{
			mImageView.setImageResource(R.drawable.comm_empty_photo);
		}
	}

}
