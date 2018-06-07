package cn.vinotec.app.android.comm.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.vinotec.app.android.comm.entity.PaginationResponse;
import cn.vinotec.app.android.comm.view.XListView;
import cn.vinotec.app.android.comm.view.XListView.IXListViewListener;

public abstract class BaseXListViewAdapter<T> extends BaseAdapter implements IXListViewListener
{
	private OnDataChangedListener onDataChangedListener;

	protected XListView listview;

	protected int pageindex = 1;
	protected int lastcount = 0;
	protected int maxpageindex = 1;

	protected List<T> mList = new ArrayList<T>();

	public BaseXListViewAdapter(XListView listview)
	{
		this.listview = listview;
		this.listview.setXListViewListener(this);
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	public List<T> getData()
	{
		return mList;
	}

	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void appendData(PaginationResponse<T> data)
	{
		if (data == null)
		{
			return;
		}

		List<T> list = new ArrayList<T>();

		int index = data.getPagination().getPage_index();
		if (index == pageindex)
		{
			if (data.getItems().size() > lastcount)
			{
				for (int i = lastcount; i < data.getItems().size(); i++)
				{
					T t = data.getItems().get(i);
					list.add(t);
				}
			}
		}
		else
		{
			list = data.getItems();
		}
		lastcount = data.getItems().size();
		pageindex = index;
		maxpageindex = data.getPagination().getMax_page_index();

		if (list == null)
		{
			listview.getFooter().nomore();
			return;
		}

		mList.addAll(list);
		notifyDataSetChanged();

		if (list.size() == 0 || pageindex == maxpageindex)
		{
			listview.getFooter().nomore();
		}
		else
		{
			listview.getFooter().normal();
		}

		if (getCount() == 0)
		{
			listview.getFooter().nodata();
		}

		if(onDataChangedListener != null)
		{
			onDataChangedListener.OnDataChanged(mList);
		}
	}

	public void clearData()
	{
		mList.clear();
		notifyDataSetChanged();
		listview.getFooter().nodata();
	}

	@Override
	public void onRefresh()
	{
		clearData();
		pageindex = 1;
		lastcount = 0;
        if(onDataChangedListener != null)
        {
            onDataChangedListener.onRefresh();
        }
		LoadData(pageindex);
	}

	public void setOnDataChangedListener(OnDataChangedListener listener)
	{
		this.onDataChangedListener = listener;
	}

	@Override
	public void onLoadMore()
	{
        if(onDataChangedListener != null)
        {
            onDataChangedListener.onRefresh();
        }
		if (pageindex < maxpageindex)
		{
			LoadData(pageindex + 1);
		}
		else
		{
			LoadData(pageindex);
		}
	}

	public abstract void LoadData(int pageindex);

	public void onLoad()
	{
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	public interface OnDataChangedListener{
        void onRefresh();
		void OnDataChanged(List data);
	}
}
