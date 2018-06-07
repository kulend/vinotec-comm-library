package cn.vinotec.app.android.comm.entity;

//列表过滤菜单导航Item
public class ListFilterNavItemEntity {
	private String text;
	private String tag;
	
	public ListFilterNavItemEntity()
	{}
	
	public ListFilterNavItemEntity(String text, String tag)
	{
		this.text = text;
		this.tag = tag;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
