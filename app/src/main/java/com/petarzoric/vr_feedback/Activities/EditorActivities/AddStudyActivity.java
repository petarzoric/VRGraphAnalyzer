package com.petarzoric.vr_feedback.Activities.EditorActivities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity which is shown when the user taps on the add button in the {@link EditorOverviewActivity}
 * The Activity asks for several inputs and creates a study object.
 */

public class AddStudyActivity extends AppCompatActivity {

    private EditText studienNameText;
    private EditText mstudienCreatorText;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private EditText descriptionEditText;
    private boolean changedImage = false;
    private int minutes;
    private int seconds;

    private Button mAddBtn;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add new study");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_studie);
        minutesPicker = findViewById(R.id.minutesPicker);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        secondsPicker = findViewById(R.id.secondsPicker);
        initPickers();

        //init
        studienNameText = (EditText)findViewById(R.id.studienName);
        studienNameText.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mstudienCreatorText = (EditText)findViewById(R.id.studienErsteller);
        mstudienCreatorText.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mAddBtn = (Button)findViewById(R.id.addNewStudieButton);
        mAddBtn.setEnabled(false);
        mAddBtn.setTextColor(Color.parseColor("#c7cdd6"));
        mAddBtn.setAlpha(0.3f);

        List<EditText> inputs = new ArrayList<EditText>();
        inputs.add(descriptionEditText);
        inputs.add(studienNameText);
        inputs.add(mstudienCreatorText);
        for(EditText e: inputs){
            //Checks if the entered values are valid. If yes, the add button active, otherwise it isn't
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(studienNameText.getText().toString().trim().length() > 1
                            && mstudienCreatorText.getText().toString().trim().length() > 1
                            ){
                        mAddBtn.setEnabled(true);
                        mAddBtn.setTextColor(Color.WHITE);
                        mAddBtn.setAlpha(1.0f);
                    } else {
                        mAddBtn.setEnabled(false);
                        mAddBtn.setTextColor(Color.parseColor("#c7cdd6"));
                        mAddBtn.setAlpha(0.3f);

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                saveStudie();
            }
        });

    }

    @Override
    public void onBackPressed() {
        goBackHome();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                goBackHome();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void initPickers(){
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(60);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
         minutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
             @Override
             public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                 minutes = numberPicker.getValue();
             }
         });

       secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
           @Override
           public void onValueChange(NumberPicker numberPicker, int i, int i1) {
               seconds = numberPicker.getValue();
           }
       });
    }


    //takes all the inputs and creates a study object.
    //Saves it in the DB
    private void saveStudie(){
        String name = studienNameText.getText().toString().trim();
        String creator =  mstudienCreatorText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        int duration = (minutes*60) + seconds;

        dbHelper = new DatabaseHelper(this);

        if(name.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(creator.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a creator name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(seconds == 0 && minutes == 0){
            Toast.makeText(this, "You must enter a valid duration (minimum 1 second)", Toast.LENGTH_SHORT).show();
            return;
        }



        //create new study
        Study study = new Study(name, creator, duration, description);

        dbHelper.addStudy(study);

        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(AddStudyActivity.this, EditorOverviewActivity.class));
    }
}