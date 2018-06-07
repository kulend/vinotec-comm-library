package cn.vinotec.app.android.comm.entity;

public class BaseOptionEntity {
	private String text;
	private long value;
	private boolean isChecked;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public BaseOptionEntity() {
	}

	public BaseOptionEntity(String text, long value) {
		this.text = text;
		this.value = value;
		this.isChecked = false;
	}

	public BaseOptionEntity(String text, long value, boolean isChecked) {
		this.text = text;
		this.value = value;
		this.isChecked = isChecked;
	}
}
