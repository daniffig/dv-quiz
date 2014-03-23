package com.dvorakdev.dvquiz.reference;

public enum dvQuizReference {

	ADD_NEW_CATEGORY (2);
	
	private int referenceValue;
	
	public int getReferenceValue() {
		return referenceValue;
	}

	private void setReferenceValue(int referenceValue) {
		this.referenceValue = referenceValue;
	}

	dvQuizReference(int i)
	{
		this.setReferenceValue(i);
	}
}
