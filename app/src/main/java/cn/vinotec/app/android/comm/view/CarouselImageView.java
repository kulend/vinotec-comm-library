package cn.vinotec.app.android.comm.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.vinotec.app.android.comm.entity.ImageAdEntity;
import cn.vinotec.app.android.comm.library.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 广告图片自动轮播控件
 */
public class CarouselImageView extends LinearLayout {

	/**
	 * 上下文
	 */
	private Context mContext;

	/**
	 * 图片轮播视图
	 */
	private CycleViewPager mBannerPager = null;

	/**
	 * 滚动图片视图适配器
	 */
	private ImageCycleAdapter mAdvAdapter;

	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示器-个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示器-视图列表
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 图片滚动当前图片下标
	 */
	private int mImageIndex = 1;

	/**
	 * 手机密度
	 */
	private float mScale;

    private int mPointResIdNor = R.drawable.comm_icon_point_nor;
    private int mPonitResIdSel = R.drawable.comm_icon_point_sel;
    private int mInterval = 3000;
    private int mGravity;
    private int mPonitMargin = 3;

    /**
     * 广告图片点击监听器
     */
    private CarouselImageViewListener mCarouselImageViewListener;

    /**
	 * @param context
	 */
	public CarouselImageView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CarouselImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.view_banner_content, this);
		mBannerPager = (CycleViewPager) findViewById(R.id.pager_banner);
		mBannerPager.setOnPageChangeListener(new GuidePageChangeListener());
		mBannerPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						// 开始图片滚动
						startImageTimerTask();
						break;
					default:
						// 停止图片滚动
						stopImageTimerTask();
						break;
				}
				return false;
			}
		});
		// 滚动图片右下指示器视图
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);

	}

    public void setPonitStyle(int gravity, int resNor, int resSel, int margin)
    {
        this.mGravity = gravity;
        ((LinearLayout)mGroup).setGravity(gravity);

        mPointResIdNor = resNor;
        mPonitResIdSel = resSel;
        this.mPonitMargin = margin;
    }

    public void setInterval(int interval)
    {
        this.mInterval = interval;
    }

    public void setCarouselImageViewListener(CarouselImageViewListener listener)
    {
        this.mCarouselImageViewListener = listener;
    }

	/**
	 * 装填图片数据
	 */
	public void setImageResources(ArrayList<ImageAdEntity> infoList) {
		// 清除所有子视图
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = infoList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
//			int imageParams = (int) (mScale * 20 + 0.5f);// XP与DP转换，适应不同分辨率
//			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.setMargins(mPonitMargin, 0, mPonitMargin, 0);
			mImageView.setLayoutParams(layout);
			//mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(mPonitResIdSel);
			} else {
				mImageViews[i].setBackgroundResource(mPointResIdNor);
			}
			mGroup.addView(mImageViews[i]);
		}
		mAdvAdapter = new ImageCycleAdapter(mContext, infoList);
		mBannerPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}

	/**
	 * 开始轮播(手动控制自动轮播与否，便于资源控制)
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播——用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 开始图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 图片每3秒滚动一次
		mHandler.postDelayed(mImageTimerTask, mInterval);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {

		@Override
		public void run() {
			if (mImageViews != null) {
				// 下标等于图片列表长度说明已滚动到最后一张图片,重置下标
				if ((++mImageIndex) == mImageViews.length + 1) {
					mImageIndex = 1;
				}
				mBannerPager.setCurrentItem(mImageIndex);
			}
		}
	};

	/**
	 * 轮播图片状态监听器
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask(); // 开始下次计时
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			
			if (index == 0 || index == mImageViews.length + 1) {
				return;
			}
			// 设置图片滚动指示器背景
			mImageIndex = index;
			index -= 1;
			mImageViews[index].setBackgroundResource(mPonitResIdSel);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(mPointResIdNor);
				}
			}

		}
	}

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<ImageAdEntity> mAdList = new ArrayList<ImageAdEntity>();

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<ImageAdEntity> adList) {
            mContext = context;
            mAdList = adList;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mAdList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if(mAdList.size() == 0 || position < 0)
            {
                return null;
            }
            String imageUrl = mAdList.get(position).getUrl();
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCarouselImageViewListener != null)
                    {
                        mCarouselImageViewListener.onImageClick(mAdList.get(position),position, v);
                    }
                }
            });
            imageView.setTag(imageUrl);
            container.addView(imageView);
            ImageLoader.getInstance().displayImage(imageUrl, imageView);// 使用ImageLoader对图片进行加装！
            //mImageCycleViewListener.displayImage(imageUrl, imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }
    }

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface CarouselImageViewListener {

		/**
		 * 单击图片事件
		 *
		 * @param imageView
		 */
		public void onImageClick(ImageAdEntity info, int postion, View imageView);
	}

}
