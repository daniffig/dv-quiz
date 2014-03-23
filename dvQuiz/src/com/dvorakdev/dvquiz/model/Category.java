package com.dvorakdev.dvquiz.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

public class Category extends Model {
	
	@Override
	public String toString()
	{
		return this.getName();
	}
	
	@Column(name = "Name")
	private String name;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Quiz> getQuizzes()
    {
        return this.getMany(Quiz.class, "Category");
    }
	
	public List<Quiz> getQuizzesOrderBy(String anOrderCriteria)
	{
	    return new Select().from(Quiz.class).where("Category = ?", this.getId()).orderBy("Name ASC").execute();		
	}
	
	public Quiz getQuizByName(String aQuizName)
	{
	    return new Select().from(Quiz.class).where("Category = ? AND Name = ?", this.getId(), aQuizName).executeSingle();		
	}
	
	public static Category oneByName(String aCategoryName)
	{
		return new Select().from(Category.class).where("Name = ?", aCategoryName).executeSingle();
	}
    
    public static List<Category> all()
    {
    	return Model.all(Category.class);
    }
    
    public static List<Category> allOrderBy(String anOrderCriteria)
    {
    	return new Select().from(Category.class).orderBy(anOrderCriteria).execute();
    }
    
    public static void truncate()
    {
    	Model.delete(Category.class);
    }

}
