package cn.vinotec.app.android.comm.entity;

import java.io.Serializable;

public class ShowImageEntity implements Serializable
{
	private static final long serialVersionUID = 8236743379918056261L;

	public static final int Type_URL = 1;
	public static final int Type_ASSETS = 2;
	public static final int Type_DRAWABLE = 3;
	public static final int Type_LOCAL = 4;

	private int type;
	private String path;
	private int resId;

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int getResId()
	{
		return resId;
	}

	public void setResId(int resId)
	{
		this.resId = resId;
	}

}
