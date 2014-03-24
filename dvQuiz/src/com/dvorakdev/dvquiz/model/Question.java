package com.dvorakdev.dvquiz.model;

import java.util.Collections;
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
	
	public Boolean isNew()
	{
		return this.getId() == null;
	}
	
	private List<Answer> answers;
	private List<Answer> shuffledAnswers;
	
	@Column(name = "Quiz", onDelete = ForeignKeyAction.CASCADE)
	private Quiz quiz;
	
	@Column(name = "Question")
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
	
	public Boolean isFirst()
	{
		return this.getQuiz().getQuestions().get(0).equals(this);
	}
	
	public Boolean isLast()
	{
		return this.getQuiz().getQuestions().get(this.getQuiz().getQuestions().size() - 1).equals(this);
	}
	
	public Boolean hasPrevious()
	{
		return !this.isFirst();
	}
	
	public Boolean hasNext()
	{
		return !this.isLast();
	}

    public List<Answer> getAnswers()
    {
    	if (this.answers == null)
    	{
    		this.answers = this.getMany(Answer.class, "Question");
    	}
    	
        return this.answers; 
    }
    
    public List<Answer> getShuffledAnswers()
    {
    	if (this.shuffledAnswers == null)
    	{
    		this.shuffledAnswers = this.getAnswers();
    		
    		Collections.shuffle(this.shuffledAnswers);
    	}
    	
        return this.shuffledAnswers; 
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
