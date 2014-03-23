package com.dvorakdev.dvquiz;

import com.dvorakdev.dvquiz.context.dvQuizContext;
import com.dvorakdev.dvquiz.model.Category;
import com.dvorakdev.dvquiz.model.Quiz;
import com.dvorakdev.dvquiz.reference.dvQuizReference;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.content.Intent;

public class QuizFormActivity extends Activity {

	private Quiz quiz;
	
	private Spinner categorySpinner;
	private EditText quizNameEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_form);
		
		this.categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
		this.quizNameEditText = (EditText) this.findViewById(R.id.quizNameEditText);
		
		this.quiz = (Quiz) dvQuizContext.getInstance().getValue("selectedQuiz", new Quiz());
        
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
        android.R.layout.simple_spinner_item, Category.allOrderBy("Name ASC"));
        
        this.categorySpinner.setAdapter(adapter);
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
		if (this.isValid())
		{			
			this.quiz.setCategory((Category) this.categorySpinner.getAdapter().getItem(this.categorySpinner.getSelectedItemPosition()));
			this.quiz.setName(this.quizNameEditText.getText().toString());
			
			this.quiz.save();
			
			this.setResult(RESULT_OK);
			
			this.finish();
		}
		else
		{
			Toast.makeText(this, String.format(this.getString(R.string.error_object_already_exists), "Quiz"), Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean isValid()
	{		
		if (this.categorySpinner.getSelectedItemId() == -1)
		{
			return false;
		}
		
		if (this.categorySpinner.getSelectedItem() == null)
		{
			return false;
		}
		
		if (this.quizNameEditText.getText().toString() == "")
		{
			return false;
		}
		
		Category selectedCategory = (Category) this.categorySpinner.getAdapter().getItem(this.categorySpinner.getSelectedItemPosition());
		
		if (selectedCategory.getQuizByName(this.quizNameEditText.getText().toString()) != null)
		{
			return false;
		}
		
		return true;				
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue()) {
	    	
	        if (resultCode == RESULT_OK) {
	    		
	            Spinner s = (Spinner) findViewById(R.id.categorySpinner);
	            
	            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
	            android.R.layout.simple_spinner_item, Category.allOrderBy("Name ASC"));
	            
	            s.setAdapter(adapter);
	        }
	    }
	}

}
