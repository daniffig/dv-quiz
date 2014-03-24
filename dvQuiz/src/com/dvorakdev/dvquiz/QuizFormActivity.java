package com.dvorakdev.dvquiz;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Scanner;

import com.dvorakdev.dvquiz.context.dvQuizContext;
import com.dvorakdev.dvquiz.model.Answer;
import com.dvorakdev.dvquiz.model.Category;
import com.dvorakdev.dvquiz.model.Question;
import com.dvorakdev.dvquiz.model.Quiz;
import com.dvorakdev.dvquiz.reference.dvQuizReference;
import com.dvorakdev.lib.dvUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.content.Intent;

public class QuizFormActivity extends Activity {

	private Quiz quiz;
	
	private Spinner categorySpinner;
	private EditText quizNameEditText;
	private TextView quizFileNameTextView;
	private File quizFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_form);
		
		this.categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		this.quizNameEditText = (EditText) this.findViewById(R.id.quizNameEditText);
		this.quizFileNameTextView = (TextView) this.findViewById(R.id.quizFileNameTextView);
		
		this.quiz = (Quiz) dvQuizContext.getInstance().getValue("selectedQuiz", new Quiz());
        
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
        android.R.layout.simple_spinner_item, Category.allOrderBy("Name ASC"));
        
        this.categorySpinner.setAdapter(adapter);
        
        // TODO Falta armar toda la parte de la edición
        if (!this.quiz.isNew())
        {
        	this.findViewById(R.id.quizFileNameTextView).setVisibility(View.GONE);
        	this.findViewById(R.id.quizSelectFileButton).setVisibility(View.GONE);
        }
	}

	public void selectFile(View view) {
		
		/*
		 * Author: http://stackoverflow.com/users/377260/paul-burke
		 */
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		intent.setType("*/*"); 
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					dvQuizReference.SELECT_QUIZ_FILE.getReferenceValue());
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.", 
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_add_category:
			this.startActivityForResult(new Intent(this, CategoryFormActivity.class), dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue());
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void processForm(View view)
	{
		String error = this.isValid();
		
		if (error == null)
		{			
			this.quiz.setCategory((Category) this.categorySpinner.getAdapter().getItem(this.categorySpinner.getSelectedItemPosition()));
			this.quiz.setName(this.quizNameEditText.getText().toString());
			
			this.quiz.save();
			
			this.populateQuiz();
			
			this.setResult(RESULT_OK);
			
			this.finish();
		}
		else
		{
			Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
		}
	}
	
	public String isValid()
	{		
		// TODO i18n
		if ((this.categorySpinner.getSelectedItemId() == -1) || (this.categorySpinner.getSelectedItem() == null))
		{
			return String.format("You must select a Category.");
		}
		
		if ((this.quizNameEditText.getText().toString() == ""))
		{
			return String.format("You must set a Quiz name.");
		}
		
		Category selectedCategory = (Category) this.categorySpinner.getAdapter().getItem(this.categorySpinner.getSelectedItemPosition());
		
		if (selectedCategory.getQuizByName(this.quizNameEditText.getText().toString()) != null)
		{
			return String.format(this.getString(R.string.error_object_already_exists), "Quiz");
		}
		
		if (!this.isQuizFileValid(this.quizFile))
		{
			// TODO i18n
			return String.format("Invalid quiz file.");
		}
		
		return null;				
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue()) {
	    	
	        if (resultCode == RESULT_OK) {
	    		
	            Spinner s = (Spinner) findViewById(R.id.categorySpinner);
	            
	            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
	            android.R.layout.simple_spinner_item, Category.allOrderBy("Name ASC"));
	            
	            s.setAdapter(adapter);
	            
	            s.setSelection(adapter.getCount() - 1);
	        }
	    }
		
		/*
		 * Author: http://stackoverflow.com/users/377260/paul-burke
		 */
	    if (requestCode == dvQuizReference.SELECT_QUIZ_FILE.getReferenceValue()) {
	    	
	    	if (data == null)
	    	{
	    		return;
	    	}
            
            try
            {	            
            	this.quizFile = new File(dvUtils.getPath(this, data.getData()));
            	this.quizFileNameTextView.setText(this.quizFile.getName());
			}
            catch (URISyntaxException e)
            {
				e.printStackTrace();
			}
	    }
	}
	
	private void populateQuiz()
	{	
		try
		{			
			Scanner fileScanner = new Scanner(this.quizFile);
			
			fileScanner.nextLine();
			
			Integer quizQuestionsQty = Integer.valueOf(fileScanner.nextLine());
			
			for (int i = 0; i < quizQuestionsQty; i++)
			{
				Question aQuestion = new Question();
				
				String question = fileScanner.nextLine();
				
				aQuestion.setQuiz(this.quiz);
				aQuestion.setQuestion(question); //dvUtils.cleanString(question));
				
				aQuestion.save();				
				
				Integer questionAnswersQty = Integer.valueOf(fileScanner.nextLine());
				
				for (int j = 0; j < questionAnswersQty; j++)
				{
					Answer anAnswer = new Answer();
					
					String answer = fileScanner.nextLine();
					
					anAnswer.setQuestion(aQuestion);
					anAnswer.setAnswer(answer);
					
					anAnswer.save();
				}
				
				Integer correctAnswerPosition = Integer.valueOf(fileScanner.nextLine());
				
				aQuestion.setCorrectAnswer(aQuestion.getAnswers().get(correctAnswerPosition));
				
				aQuestion.save();		
			}
			
			fileScanner.close();
		}
		catch (Exception e)
		{
			
		}		
	}
	
	private Boolean isQuizFileValid(File aQuizFile)
	{
		Boolean isValid = false;
		
		try
		{			
			Scanner fileScanner = new Scanner(aQuizFile);
			
			fileScanner.nextLine();
			
			Integer quizQuestionsQty = Integer.valueOf(fileScanner.nextLine());
			
			for (int i = 0; i < quizQuestionsQty; i++)
			{
				fileScanner.nextLine();
				
				Integer questionAnswersQty = Integer.valueOf(fileScanner.nextLine());
				
				for (int j = 0; j < questionAnswersQty; j++)
				{
					fileScanner.nextLine();
				}
				
				Integer.valueOf(fileScanner.nextLine());
			}
			
			fileScanner.close();
			
			isValid = true;
		}
		catch (Exception e)
		{
			
		}
		
		return isValid;
	}

}
