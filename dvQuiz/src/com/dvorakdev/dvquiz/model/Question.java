package com.dvorakdev.dvquiz.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

@Table(name = "Question")
public class Question extends Model {
	
	@Override
	public String toString()
	{
		return this.getQuestion();
	}
	
	@Column(name = "Quiz")
	private Quiz quiz;
	
	@Column(name = "Question", onDelete = ForeignKeyAction.CASCADE)
	private String question;

	@Column(name = "CorrectAnswer")	
	private Answer correctAnswer;
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Answer getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(Answer correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

    public List<Answer> getAnswers()
    {
        return this.getMany(Answer.class, "Question");
    }
    
    public static List<Question> all()
    {
    	return Model.all(Question.class);
    }
    
    public static void truncate()
    {
    	Model.delete(Question.class);
    }

}
