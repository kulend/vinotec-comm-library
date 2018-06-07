package cn.vinotec.app.android.comm.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.vinotec.app.android.comm.view.XListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListViewAdapter<T> extends BaseAdapter {

	protected XListView listview;
	protected List<T> mList = new ArrayList<T>();
	
	public BaseListViewAdapter(XListView listView)
	{
		this.listview=listView;
		this.listview.setPullLoadEnable(false);
		this.listview.setPullRefreshEnable(false);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void loadData(List<T> list)
	{
		if (list == null)
		{
			listview.getFooter().nomore();
			return;
		}
		mList=list;
		notifyDataSetChanged();
		if (list.size() == 0)
		{
			listview.getFooter().nodata();
		}
		else
		{
			listview.getFooter().normal();
		}
	}

}
