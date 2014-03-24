package com.dvorakdev.dvquiz;

import java.util.ArrayList;
import java.util.List;

import com.dvorakdev.dvquiz.context.dvQuizContext;
import com.dvorakdev.dvquiz.model.Answer;
import com.dvorakdev.dvquiz.model.Question;
import com.dvorakdev.dvquiz.model.Quiz;
import com.dvorakdev.lib.dvObjectRadioButton;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class QuizActivity extends Activity {
	
	private Quiz selectedQuiz;
	private SparseArray<Answer> selectedAnswers = new SparseArray<Answer>();
	
	private Spinner questionSpinner;
	
	private TextView quizQuestionTextView;
	private RadioGroup quizQuestionAnswerRadioGroup;	
	private Button previousButton;
	private Button nextButton;
	
	private Question currentQuestion;
	private Integer currentQuestionPosition = 0;
	
	private Boolean isQuizStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		// Show the Up button in the action bar.
		setupActionBar();		
		
		if (!this.isQuizStarted)
		{
			this.isQuizStarted = true;
			
			this.selectedQuiz = (Quiz) dvQuizContext.getInstance().getValue("selectedQuiz");
			
			for (Question aQuestion : this.selectedQuiz.getShuffledQuestions())
			{
				this.selectedAnswers.put(aQuestion.hashCode(), null);
			}			
			
			this.quizQuestionTextView = (TextView) this.findViewById(R.id.quizQuestionTextView);
			this.questionSpinner = (Spinner) this.findViewById(R.id.quizQuestionSpinner);
			this.quizQuestionAnswerRadioGroup = (RadioGroup) this.findViewById(R.id.quizQuestionAnswerRadioGroup);
			this.previousButton = (Button) this.findViewById(R.id.quizActionPreviousQuestion);
			this.nextButton = (Button) this.findViewById(R.id.quizActionNextQuestion);
			
			List<String> questionList = new ArrayList<String>();
			
			for (int i = 1; i <= this.selectedQuiz.getQuestions().size(); i++)
			{
				questionList.add(String.format("Question %d", i));
			}			
	        
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        R.layout.question_spinner_item, questionList);
	        
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        this.questionSpinner.setAdapter(adapter);
	        
	        this.questionSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
	        {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {					
					QuizActivity.this.currentQuestionPosition = arg2;
					QuizActivity.this.loadQuestion();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {					
				}
	        });
		}
	}
	
	private void loadQuestion()
	{
		this.currentQuestion = this.selectedQuiz.getQuestions().get(this.currentQuestionPosition);
		
		this.quizQuestionTextView.setText(String.format(this.currentQuestion.getQuestion(), "ISO-8859-15"));		
		
		this.quizQuestionAnswerRadioGroup.removeAllViews();		
		
		for (Answer anAnswer : this.currentQuestion.getShuffledAnswers())
		{
			final dvObjectRadioButton<Answer> anAnswerRadioButton = new dvObjectRadioButton<Answer>(this);
			
			anAnswerRadioButton.setObject(anAnswer);
			anAnswerRadioButton.setId(anAnswer.hashCode());
			anAnswerRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getResources().getDimensionPixelSize(R.dimen.question_answer));			
			
			if (!(this.selectedAnswers.get(this.currentQuestion.hashCode()) == null) && anAnswer.equals(this.selectedAnswers.get(this.currentQuestion.hashCode())))
			{
				this.quizQuestionAnswerRadioGroup.check(anAnswerRadioButton.getId());
				
				anAnswerRadioButton.setChecked(true);
			}
			
			anAnswerRadioButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0) {
					QuizActivity.this.selectedAnswers.put(QuizActivity.this.currentQuestion.hashCode(), anAnswerRadioButton.getObject());
				}
				
			});
			
			this.quizQuestionAnswerRadioGroup.addView(anAnswerRadioButton);
		}
		
		if (this.currentQuestion.isFirst())
		{
			this.previousButton.setEnabled(false);
		}
		
		if (this.currentQuestion.isLast())
		{
			this.nextButton.setEnabled(false);
		}
		
		if (this.currentQuestion.hasPrevious())
		{
			this.previousButton.setEnabled(true);
		}
		
		if (this.currentQuestion.hasNext())
		{
			this.nextButton.setEnabled(true);
		}
		
		this.questionSpinner.setSelection(this.currentQuestionPosition);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_submit_answers) {
			
			dvQuizContext.getInstance().setValue("selectedAnswers", this.selectedAnswers);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void previousQuestion(View view)
	{
		if (this.currentQuestion.hasPrevious())
		{
			this.currentQuestionPosition--;
		}
		
		this.loadQuestion();
	}
	
	public void nextQuestion(View view)
	{
		if (this.currentQuestion.hasNext())
		{
			this.currentQuestionPosition++;
		}
		
		this.loadQuestion();
		
	}
	
	public void checkAnswer(View view)
	{
		// TODO i18n
		if (this.selectedAnswers.get(this.currentQuestion.hashCode()) == null)
		{
			Toast.makeText(this, "You must select an Answer first", Toast.LENGTH_SHORT).show();
			
			return;
		}

		if (this.selectedAnswers.get(this.currentQuestion.hashCode()).isCorrect())
		{
			Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
		}
	}

}
