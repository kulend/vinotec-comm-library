package cn.vinotec.app.android.comm.entity;

public class AppVersionEntity
{
	public int update;
	private String version;
	private String update_log;
	private String download_url;

	public int getUpdate()
	{
		return update;
	}

	public void setUpdate(int update)
	{
		this.update = update;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getUpdate_log()
	{
		return update_log;
	}
	public void setUpdate_log(String update_log)
	{
		this.update_log = update_log;
	}

	public String getDownload_url()
	{
		return download_url;
	}

	public void setDownload_url(String download_url)
	{
		this.download_url = download_url;
	}


}
