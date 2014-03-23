package com.dvorakdev.dvquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dvorakdev.dvquiz.context.dvQuizContext;
import com.dvorakdev.dvquiz.model.Category;
import com.dvorakdev.dvquiz.model.Quiz;
import com.dvorakdev.lib.dvExpandableListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;
import android.widget.ExpandableListView;
 
public class MainActivity extends Activity {
 
    private static final int CONTEXT_MENU_EDIT = 2;
	private static final int CONTEXT_MENU_DELETE = 3;
	
	dvExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new dvExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);

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
			aHeaderTitle = (String) this.listAdapter.getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition));			
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			aHeaderTitle = (String) this.listAdapter.getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
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
			String aCategoryName = (String) this.listAdapter.getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition));
			
			Category selectedCategory = Category.oneByName(aCategoryName);
			
			switch (item.getItemId())
			{
			case CONTEXT_MENU_EDIT:
				dvQuizContext.getInstance().setValue("selectedCategory", selectedCategory);
				
				this.startActivity(new Intent(this, CategoryFormActivity.class));
				
				return true;
			case CONTEXT_MENU_DELETE:
				selectedCategory.delete();
				
				this.findViewById(R.layout.activity_main).invalidate();
				
				Toast.makeText(MainActivity.this, String.format(this.getString(R.string.info_object_successfully_delete), selectedCategory.toString()), Toast.LENGTH_SHORT).show();
				
				return true;
			}
			
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			String aQuizName = (String) this.listAdapter.getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
			
			switch (item.getItemId())
			{
			case CONTEXT_MENU_EDIT:
				Toast.makeText(MainActivity.this, Quiz.oneByName(aQuizName).toString(), Toast.LENGTH_SHORT).show();

				return true;
			case CONTEXT_MENU_DELETE:
				Toast.makeText(MainActivity.this, Quiz.oneByName(aQuizName).toString(), Toast.LENGTH_SHORT).show();

				return true;
			}
			break;
		default:
			return false;
		}
		
		this.prepareListData();
    	
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
	        	this.startActivity(new Intent(this, CategoryFormActivity.class));
	            return true;
	        case R.id.action_add_quiz:
	        	this.startActivity(new Intent(this, QuizFormActivity.class));
	            return true;
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void createListData()
	{        
        Category.truncate();
        
        for (int i = 0; i < 3; i++)
        {
        	Category aCategory = new Category();
        	
        	aCategory.setName("Category " + i);
        	
        	aCategory.save();
        	
        	for (int j = 0; j < 3; j++)
        	{
        		Quiz aQuiz = new Quiz();
        		
        		aQuiz.setCategory(aCategory);
        		aQuiz.setName(String.format("Quiz %d %d", i, j));
        		
        		aQuiz.save();
        	}
        }
	}
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();        
        
        for (Category aCategory : Category.allOrderBy("Name ASC"))
        {
            List<String> aQuizList = new ArrayList<String>();
            
            listDataHeader.add(aCategory.toString());
            
            for (Quiz aQuiz : aCategory.getQuizzesOrderBy("Name ASC"))
            {
            	aQuizList.add(aQuiz.toString());           	
            }
            
            listDataChild.put(aCategory.toString(), aQuizList);        	
        }
    }
}
