package cn.vinotec.app.android.comm.fragment;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cn.vinotec.app.android.comm.entity.ShowImageEntity;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.utils.BitMapUtil;

public class ImageDetailFragment extends Fragment {
	private ShowImageEntity mImage;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(ShowImageEntity image) {
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

		if (mImage != null) {
			if (mImage.getType() == ShowImageEntity.Type_URL) {
                progressBar.setVisibility(View.VISIBLE);
				Glide.with(ImageDetailFragment.this).load(mImage.getPath()).listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(getActivity(), "图片加载出错！", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
					    return false;
					}
					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        mAttacher.update();
					    return false;
					}
				}).into(mImageView);
			} else if (mImage.getType() == ShowImageEntity.Type_LOCAL) {
				Bitmap bmp = BitMapUtil.getBitmap(mImage.getPath(), 500, 500);
				mImageView.setImageBitmap(bmp);
			} else {
				// TODO:设置其他类型时的显示
				mImageView.setImageResource(R.drawable.comm_empty_photo);
			}
		} else {
			mImageView.setImageResource(R.drawable.comm_empty_photo);
		}
	}

}
