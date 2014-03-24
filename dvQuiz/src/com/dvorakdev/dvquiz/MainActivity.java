package com.dvorakdev.dvquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dvorakdev.dvquiz.context.dvQuizContext;
import com.dvorakdev.dvquiz.model.Category;
import com.dvorakdev.dvquiz.model.Quiz;
import com.dvorakdev.dvquiz.reference.dvQuizReference;
import com.dvorakdev.lib.dvExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.widget.ExpandableListView;
 
public class MainActivity extends Activity {
 
    private static final int CONTEXT_MENU_EDIT = 2;
	private static final int CONTEXT_MENU_DELETE = 3;
	
	dvExpandableListAdapter<Category, Quiz> listAdapter;
    ExpandableListView expListView;
    List<Category> listDataHeader;
    HashMap<Category, List<Quiz>> listDataChild;
    
    private Quiz selectedQuiz;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.loadListData();

        MainActivity.this.registerForContextMenu(expListView);
    }  

    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		
    	String aHeaderTitle = "";
    	
		switch (ExpandableListView.getPackedPositionType(info.packedPosition))
		{
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			aHeaderTitle = (String) this.listAdapter.getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition)).toString();			
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			aHeaderTitle = (String) this.listAdapter.getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition)).toString();
			break;
		default:
			break;
		}
    	
    	menu.setHeaderTitle(aHeaderTitle.toString());  
    	menu.add(0, CONTEXT_MENU_EDIT, 0, this.getString(R.string.edit));  
    	menu.add(0, CONTEXT_MENU_DELETE, 0, this.getString(R.string.delete));  
    }     
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) { 

    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		
		switch (ExpandableListView.getPackedPositionType(info.packedPosition))
		{
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			Category selectedCategory = (Category) this.listAdapter.getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition));
			
			switch (item.getItemId())
			{
			case CONTEXT_MENU_EDIT:
				dvQuizContext.getInstance().setValue("selectedCategory", selectedCategory);
				
				this.startActivityForResult(new Intent(this, CategoryFormActivity.class), dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue());
				
				return true;
			case CONTEXT_MENU_DELETE:
				selectedCategory.delete();
				
				this.redrawListData();
				
				Toast.makeText(MainActivity.this, String.format(this.getString(R.string.info_object_successfully_delete), selectedCategory.toString()), Toast.LENGTH_SHORT).show();
				
				return true;
			}
			
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:			
			Quiz selectedQuiz = (Quiz) this.listAdapter.getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
			
			switch (item.getItemId())
			{
			case CONTEXT_MENU_EDIT:
				dvQuizContext.getInstance().setValue("selectedQuiz", selectedQuiz);
				
				this.startActivity(new Intent(this, QuizFormActivity.class));
				return true;
			case CONTEXT_MENU_DELETE:
				selectedQuiz.delete();
				
				this.redrawListData();
				
				Toast.makeText(MainActivity.this, String.format(this.getString(R.string.info_object_successfully_delete), selectedQuiz.toString()), Toast.LENGTH_SHORT).show();

				return true;
			}
			break;
		default:
			return false;
		}
    	
    	return false;  
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
        	case R.id.action_add_category:		
	        	dvQuizContext.getInstance().setValue("selectedCategory", new Category());		
				this.startActivityForResult(new Intent(this, CategoryFormActivity.class), dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue());     	
	            return true;
	        case R.id.action_add_quiz:
	        	dvQuizContext.getInstance().setValue("selectedQuiz", new Quiz());
				this.startActivityForResult(new Intent(this, QuizFormActivity.class), dvQuizReference.ADD_NEW_QUIZ.getReferenceValue());  
	            return true;
	        case R.id.action_settings:
	            return true;
	        case R.id.action_about:
	        	this.startActivity(new Intent(this, AboutActivity.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
 
    /*
     * Preparing the list data
     */
    private void loadListData() {
        listDataHeader = new ArrayList<Category>();
        listDataChild = new HashMap<Category, List<Quiz>>();      
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);  
        
        for (Category aCategory : Category.allOrderBy("Name ASC"))
        {
            List<Quiz> aQuizList = new ArrayList<Quiz>();
            
            listDataHeader.add(aCategory);
            
            for (Quiz aQuiz : aCategory.getQuizzesOrderBy("Name ASC"))
            {
            	aQuizList.add(aQuiz);           	
            }
            
            listDataChild.put(aCategory, aQuizList);        	
        }
 
        listAdapter = new dvExpandableListAdapter<Category, Quiz>(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        expListView.setOnChildClickListener(new OnChildClickListener()
        {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
		        //Ask the user if they want to quit

    			selectedQuiz = (Quiz) MainActivity.this.listAdapter.getChild(groupPosition, childPosition);
    			
            	dvQuizContext.getInstance().setValue("selectedQuiz", selectedQuiz);
				
				// TODO i18n
				AlertDialog.Builder confirmDialog = new AlertDialog.Builder(MainActivity.this);
				
				confirmDialog.setIcon(android.R.drawable.ic_dialog_info);
				confirmDialog.setTitle("Start Quiz");
				confirmDialog.setMessage(String.format("Do you want to start the Quiz: %s?", selectedQuiz.toString()));
				confirmDialog.setPositiveButton("Yes", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
		            	
		            	MainActivity.this.startActivity(new Intent(MainActivity.this, QuizActivity.class));
					}
					
				});				
				confirmDialog.setNegativeButton("No", null);
				
				confirmDialog.show();
		        
		        return true;				
			}
        	
        });
    }
    
    private void redrawListData()
    {
    	this.loadListData();
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == dvQuizReference.ADD_NEW_CATEGORY.getReferenceValue()) {
	    	
	        if (resultCode == RESULT_OK) {
	        	this.redrawListData();
	        }
	    }
		
	    if (requestCode == dvQuizReference.ADD_NEW_QUIZ.getReferenceValue()) {
	    	
	        if (resultCode == RESULT_OK) {
	        	this.redrawListData();
	        }
	    }
	}
}
