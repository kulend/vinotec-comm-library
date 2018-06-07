/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package cn.vinotec.app.android.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.vinotec.app.android.comm.library.R;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_NODATA = 3;
	public final static int STATE_NOMOREDATA = 4;

	private Context mContext;

	private RelativeLayout mSubContentView;
	private String text_normal;
	private String text_nodata;
	private String text_nomoredata;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private int state;
	
	public int getState() {
		return state;
	}

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

    private Animation animation;

	private void setState(int state)
	{
		Log.d("XListViewFooter", "State：" + state);
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
        if(animation != null && !animation.hasEnded())
        {
            animation.cancel();
        }
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else if (state == STATE_NODATA) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(text_nodata);
			postDelayed(new Runnable() {
				@Override
				public void run() {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.app_xlistview_text_hide_alpha);
                    mHintView.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mHintView.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
				}
			}, 0);
		}
		else if (state == STATE_NOMOREDATA)
		{
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(text_nomoredata);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.app_xlistview_text_hide_alpha);
                    mHintView.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mHintView.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            }, 0);
		}
		else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(text_normal);
		}
		this.state = state;
	}
	
	public void setBottomMargin(int height) {
		if (height < 0) return ;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	/**
	 * normal status
	 */
	public void normal() {
		setState(STATE_NORMAL);
		// mHintView.setVisibility(View.VISIBLE);
		// mProgressBar.setVisibility(View.GONE);
		// this.state = STATE_NORMAL;
	}
	
	/**
	 * nodata status
	 */
	public void nodata()
	{
		setState(STATE_NODATA);
	}
	
	/**
	 * READY status
	 */
	public void ready()
	{
		setState(STATE_READY);
	}

	/**
	 * nodata status
	 */
	public void nomore()
	{
		setState(STATE_NOMOREDATA);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		this.state = STATE_LOADING;
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.widget_xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView)moreView.findViewById(R.id.xlistview_footer_hint_textview);
		mSubContentView = (RelativeLayout) moreView.findViewById(R.id.xlistview_footer_sub_content);
		mSubContentView.setVisibility(View.GONE);

		text_normal = context.getString(R.string.xlistview_footer_hint_normal);
		text_nodata = context.getString(R.string.xlistview_footer_hint_nodata);
		text_nomoredata = context.getString(R.string.xlistview_footer_hint_nomoredata);
	}
	
	public void addSubView(View v)
	{
		mSubContentView.addView(v);
		mSubContentView.setVisibility(View.VISIBLE);
	}

	public void removeSubView()
	{
		mSubContentView.removeAllViews();
		mSubContentView.setVisibility(View.GONE);
	}
	
	public void hideSubView()
	{
		mSubContentView.setVisibility(View.GONE);
	}

	public void showSubView()
	{
		mSubContentView.setVisibility(View.VISIBLE);
	}

}
