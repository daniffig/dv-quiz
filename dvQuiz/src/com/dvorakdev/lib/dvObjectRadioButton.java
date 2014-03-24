package com.dvorakdev.lib;

import android.content.Context;
import android.widget.RadioButton;

public class dvObjectRadioButton<T> extends RadioButton {

	private T object;
	
	public dvObjectRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
		this.setText(object.toString());
	}	

}
