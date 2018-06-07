package cn.vinotec.app.android.comm.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHorizontalScrollView extends HorizontalScrollView {
	/** 传入整体布局  */
	private View ll_content;
	/** 传入拖动栏布局 */
	private View rl_column;
	/** 左阴影图片 */
	private ImageView leftImage;
	/** 右阴影图片 */
	private ImageView rightImage;
	/** 屏幕宽度 */
	private int mScreenWitdh = 0;
	/** 父类的活动activity */
	private Activity activity;
	
	public ColumnHorizontalScrollView(Context context) {
		super(context);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	/** 
	 * 在拖动的时候执行
	 * */
	@Override
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		// TODO Auto-generated method stub
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		/*shade_ShowOrHide();
		if(!activity.isFinishing() && ll_content !=null && leftImage!=null && rightImage!=null && rl_column !=null){
			if(ll_content.getWidth() <= mScreenWitdh){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		}else{
			return;
		}
		if(paramInt1 ==0){
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		if(ll_content.getWidth() - paramInt1 + rl_column.getLeft() == mScreenWitdh){
			leftImage.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		leftImage.setVisibility(View.VISIBLE);
	   rightImage.setVisibility(View.VISIBLE);*/
	}
	/** 
	 * 传入父类布局中的资源文件
	 * */
	public void setParam(Activity activity, int mScreenWitdh,View paramView1,ImageView paramView2, ImageView paramView3, View paramView5){
		this.activity = activity;
		this.mScreenWitdh = mScreenWitdh;
		ll_content = paramView1;
		leftImage = paramView2;
		rightImage = paramView3;
		rl_column = paramView5;
	}
	/** 
	 * �ж�������Ӱ����ʾ����Ч��
	 * */
	public void shade_ShowOrHide() {
		if (!activity.isFinishing() && ll_content != null) {
			measure(0, 0);
			//���������С����Ļ��ȵĻ�����������Ӱ������
			if (mScreenWitdh >= getMeasuredWidth()) {
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		} else {
			return;
		}
		//��������������ʱ�������Ӱ���أ��ұ���ʾ
		if (getLeft() == 0) {
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		//������������ұ�ʱ�������Ӱ��ʾ���ұ�����
		if (getRight() == getMeasuredWidth() - mScreenWitdh) {
			leftImage.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		//����˵�����м�λ�ã�������Ӱ����ʾ
		leftImage.setVisibility(View.VISIBLE);
		rightImage.setVisibility(View.VISIBLE);
	}
}
