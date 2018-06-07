package cn.vinotec.app.android.comm.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cn.vinotec.app.android.comm.entity.PaginationResponse;

public abstract class BasePullToRefreshViewAdapter<T> extends BaseAdapter{
	
	protected LayoutInflater inflater;

	protected List<T> mList = new ArrayList<T>();
	protected Context context;
	
	protected int pageindex = 1;
	protected int lastcount = 0;
	protected int maxpageindex = 1;
	
	
	@Override
	public int getCount() {
		return mList.size();
	}

	public BasePullToRefreshViewAdapter(Context mContext){
		context = mContext;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return null;
	}

	public void appendData(PaginationResponse<T> data)
	{
		if (data == null)
		{
			return;
		}

		List<T> list2 = new ArrayList<T>();

		int index = data.getPagination().getPage_index();
		if (index == pageindex)
		{
			if (data.getItems().size() > lastcount)
			{
				for (int i = lastcount; i < data.getItems().size(); i++)
				{
					T t = data.getItems().get(i);
					list2.add(t);
				}
			}
		}
		else
		{
			list2 = data.getItems();
		}
		lastcount = data.getItems().size();
		pageindex = index;
		maxpageindex = data.getPagination().getMax_page_index();
		
		if (mList == null)
		{
			
		}
		
		mList.addAll(list2);
		notifyDataSetChanged();
	}
	
	public void clearData()
	{
		mList.clear();
		notifyDataSetChanged();
	}
	
	public void onRefresh()
	{
		clearData();
		pageindex = 1;
		lastcount = 0;
		LoadData(pageindex);
	}
	
	public void onLoadMore()
	{
		if (pageindex < maxpageindex)
		{
			LoadData(pageindex + 1);
		}
		else
		{
			LoadData(pageindex);
			Toast.makeText(context, "已经为您倾尽了所有!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public List<T> getAllDataList(){
		return mList;
	}
	
	public abstract void LoadData(int pageindex);
	
}
