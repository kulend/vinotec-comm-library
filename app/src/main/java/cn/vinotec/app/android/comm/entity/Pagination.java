package cn.vinotec.app.android.comm.entity;

public class Pagination 
{
	private int page_index;
	private int page_size;
	private int max_page_index;
	private int max_row_count;
	
	public int getPage_index() {
		return page_index;
	}
	public void setPage_index(int page_index) {
		this.page_index = page_index;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public int getMax_page_index() {
		return max_page_index;
	}
	public void setMax_page_index(int max_page_index) {
		this.max_page_index = max_page_index;
	}
	public int getMax_row_count() {
		return max_row_count;
	}
	public void setMax_row_count(int max_row_count) {
		this.max_row_count = max_row_count;
	}

}
