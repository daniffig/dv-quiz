package com.dvorakdev.dvquiz.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column.ForeignKeyAction;

@Table(name = "Answer")
public class Answer extends Model {
	
	@Override
	public String toString()
	{
		return this.getAnswer();
	}
	
	@Column(name = "Question", onDelete = ForeignKeyAction.CASCADE)
	private Question question;
	
	@Column(name = "Answer")
	private String answer;

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
