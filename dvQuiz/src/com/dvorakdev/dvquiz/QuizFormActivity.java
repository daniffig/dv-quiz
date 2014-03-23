package com.dvorakdev.dvquiz;

import com.dvorakdev.dvquiz.model.Category;
import com.dvorakdev.dvquiz.reference.dvQuizReference;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.content.Intent;

public class QuizFormActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_form);
		
        Spinner s = (Spinner) findViewById(R.id.categorySpinner);
        
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
        android.R.layout.simple_spinner_item, Category.allOrderBy("Name ASC"));
        
        s.setAdapter(adapter);
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
