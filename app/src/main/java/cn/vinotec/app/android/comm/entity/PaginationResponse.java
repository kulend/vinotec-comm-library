package cn.vinotec.app.android.comm.entity;

import java.util.List;

public class PaginationResponse<T> {

	private Pagination pagination;

	private List<T> items;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}


}
