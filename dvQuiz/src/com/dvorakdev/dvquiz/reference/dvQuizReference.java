package com.dvorakdev.dvquiz.reference;

public enum dvQuizReference {

	ADD_NEW_CATEGORY (2),
	ADD_NEW_QUIZ (3),
	SELECT_QUIZ_FILE (5);
	
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
