package com.dvorakdev.dvquiz.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class Quiz extends Model {
	
	@Column(name = "Name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public List<Question> getQuestions()
    {
        return this.getMany(Question.class, "Quiz");
    }
    
    public static List<Quiz> all()
    {
    	return Model.all(Quiz.class);
    }

}
