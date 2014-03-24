package com.dvorakdev.dvquiz.context;

import android.util.SparseArray;

public class dvQuizContext {

	private static dvQuizContext instance;
	
	private SparseArray<Object> values;
	
	public static dvQuizContext getInstance()
	{
		if (instance == null)
		{
			instance = new dvQuizContext();
			
			instance.setValues(new SparseArray<Object>());
		}
		
		return instance;
	}
	
	public void setValue(String aKey, Object aValue)
	{
		this.getValues().put(aKey.hashCode(), aValue);
	}
	
	public Object getValue(String aKey)
	{
		if (!this.hasValue(aKey))
		{
			return null;
		}
		
		return this.getValues().get(aKey.hashCode());
	}
	
	public Object getValue(String aKey, Object aDefaultValue)
	{
		return this.getValues().get(aKey.hashCode(), aDefaultValue);
	}
	
	public boolean hasValue(String aKey)
	{
		return !(this.getValues().indexOfKey(aKey.hashCode()) < 0);
	}
	
	public SparseArray<Object> getValues() {
		return values;
	}

	public void setValues(SparseArray<Object> values) {
		this.values = values;
	}
}
