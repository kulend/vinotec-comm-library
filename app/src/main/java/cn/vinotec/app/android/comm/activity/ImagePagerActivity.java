package cn.vinotec.app.android.comm.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
import cn.vinotec.app.android.comm.entity.ShowImageEntity;
import cn.vinotec.app.android.comm.fragment.ImageDetailFragment;
import cn.vinotec.app.android.comm.library.R;
import cn.vinotec.app.android.comm.view.HackyViewPager;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_image_detail_pager);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		// String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		@SuppressWarnings("unchecked")
		List<ShowImageEntity> images = (List<ShowImageEntity>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), images);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.comm_image_show_pagination, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.comm_image_show_pagination,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<ShowImageEntity> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<ShowImageEntity> fileList)
		{
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			ShowImageEntity image = fileList.get(position);
			return ImageDetailFragment.newInstance(image);
		}

	}
}