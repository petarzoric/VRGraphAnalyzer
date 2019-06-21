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
import android.widget.Toast;

import com.petarzoric.vr_feedback.Model.Configuration;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the user to update study data, like name.
 * Also allows to change the algorithm parameters in order to customize the analysis.
 */

public class UpdateStudyActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText creatorEditText;

    private Button mUpdateBtn;
    private Button updateConfigButton;
    private DatabaseHelper dbHelper;
    private long receivedStudyId;
    private EditText extremePhaseMinLengthEditText;
    private EditText extremePhaseDeltaEditText;
    private EditText extremePhaseToleranceEditText;
    private EditText pointsNumberEditText;
    private EditText constantPhaseMinLengthEditText;
    private EditText constantPhaseDeltaEditText;
    private EditText constantPhaseToleranceEdittext;
    private EditText constantPhaseDeviationEdittext;
    private EditText extPointsFilter;
    private Button manageEventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_studie);

        //initializes all the elements of the view
        initViewElements();

        dbHelper = new DatabaseHelper(this);
        //
        try {
            //get intent to get person id
            receivedStudyId = getIntent().getLongExtra("STUDIEN_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        final Study queriedStudy = dbHelper.getStudy(receivedStudyId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editing " + queriedStudy.getName());
        //set field to this user data
        displayConfig(queriedStudy);
        nameEditText.setText(queriedStudy.getName());
        creatorEditText.setText(queriedStudy.getCreator());

        //listen to add button click to update
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                updateStudie();
            }
        });

        //takes the parameters and updates the config object
        updateConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConfig(queriedStudy);
            }
        });

        //leads to the EventsActivity
        manageEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEventsIntent = new Intent(getApplicationContext(), EventsActivity.class);
                goToEventsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                goToEventsIntent.putExtra("STUDIEN_ID", receivedStudyId);
                getApplicationContext().startActivity(goToEventsIntent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBackHome();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void initViewElements(){
        mUpdateBtn = (Button)findViewById(R.id.updateStudieButton);
        nameEditText = (EditText)findViewById(R.id.userNameUpdate);
        creatorEditText = (EditText)findViewById(R.id.creatorUpdate);
        
        pointsNumberEditText = findViewById(R.id.totalPoints_edittext);
        extPointsFilter = findViewById(R.id.extremePointsFilter);
        
        extremePhaseMinLengthEditText = findViewById(R.id.extPhase_minLength_editText);
        extremePhaseDeltaEditText = findViewById(R.id.extPhase_delta_editText);
        extremePhaseToleranceEditText = findViewById(R.id.extPhase_tolerance_editText);

        
        constantPhaseMinLengthEditText = findViewById(R.id.constPhase_minLength_editText);
        constantPhaseDeltaEditText = findViewById(R.id.constPhase_delta_editText);
        constantPhaseToleranceEdittext = findViewById(R.id.constPhase_tolerance_editText);
        constantPhaseDeviationEdittext = findViewById(R.id.constPhase_deviation_editText);
        
        updateConfigButton = findViewById(R.id.updateConfigButton);
        updateConfigButton.setEnabled(false);
        manageEventsButton = findViewById(R.id.manageEventsButton);
        


    }

    @Override
    public void onBackPressed() {
       goBackHome();
    }

    //displays all the current values of the study
    public void displayConfig(Study study){
        Configuration currentConfig = study.getConfiguration();
        pointsNumberEditText.setText(Integer.toString(currentConfig.getPointsQuantity()));
        extPointsFilter.setText(Float.toString(currentConfig.getExtremePointsFilter()));

        extremePhaseMinLengthEditText.setText(Float.toString(currentConfig.getExtremePhaseMinLength()));
        extremePhaseDeltaEditText.setText(Float.toString(currentConfig.getExtremePhaseDelta()));
        extremePhaseToleranceEditText.setText(Float.toString(currentConfig.getExtremePhaseGradientTolerance()));


        constantPhaseMinLengthEditText.setText(Float.toString(currentConfig.getConstantPhaseMinLength()));
        constantPhaseDeltaEditText.setText(Float.toString(currentConfig.getConstantPhaseDelta()));
        constantPhaseToleranceEdittext.setText(Float.toString(currentConfig.getConstantPhaseGradientTolerance()));
        constantPhaseDeviationEdittext.setText(Float.toString(currentConfig.getConstantPhaseDeviation()));

        List<EditText> editTexts = new ArrayList<EditText>();
        editTexts.add(extremePhaseMinLengthEditText);
        editTexts.add(extremePhaseDeltaEditText);
        editTexts.add(pointsNumberEditText);
        editTexts.add(extPointsFilter);
        editTexts.add(constantPhaseMinLengthEditText);
        editTexts.add(constantPhaseDeltaEditText);
        editTexts.add(extremePhaseToleranceEditText);
        editTexts.add(constantPhaseToleranceEdittext);
        editTexts.add(constantPhaseDeviationEdittext);
        for(EditText editText: editTexts){
            editText.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updateConfigButton.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    //updates configuration
    private void updateConfig(Study study){
        Configuration configuration = new Configuration();
        int totalPoints = Integer.parseInt(pointsNumberEditText.getText().toString());
        float extremePhaseMinLength = Float.parseFloat(extremePhaseMinLengthEditText.getText().toString());
        float extremePhaseDelta = Float.parseFloat(extremePhaseDeltaEditText.getText().toString());
        float extremePhaseGradientTolerance = Float.parseFloat(extremePhaseToleranceEditText.getText().toString());
        
        float constantPhaseMinLength = Float.parseFloat(constantPhaseMinLengthEditText.getText().toString());
        float constantPhaseDelta = Float.parseFloat(constantPhaseDeltaEditText.getText().toString());
        float constantPhaseTolerance = Float.parseFloat(constantPhaseToleranceEdittext.getText().toString());
        float constantPhaseDeviation = Float.parseFloat(constantPhaseDeviationEdittext.getText().toString());
        float extremePointsFilter = Float.parseFloat(extPointsFilter.getText().toString());

        configuration.configureExtremePhaseParams(extremePhaseMinLength, extremePhaseDelta, extremePhaseGradientTolerance);
        configuration.configureConstantPhaseParams(constantPhaseMinLength, constantPhaseDelta, constantPhaseTolerance, constantPhaseDeviation);
        configuration.setPointsQuantity(totalPoints);
        configuration.setExtremePointsFilter(extremePointsFilter);
        dbHelper.updateConfigForStudy(configuration, study);

        Toast.makeText(this, "Your configuration was succesfully updated.", Toast.LENGTH_SHORT).show();
        updateConfigButton.setEnabled(false);
    }

    //updates study
    private void updateStudie(){
        String name = nameEditText.getText().toString().trim();
        String creator = creatorEditText.getText().toString().trim();



        if(name.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(creator.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an age", Toast.LENGTH_SHORT).show();
            return;
        }
        //create updated person
        Study updatedStudy = new Study(name, creator);

        //call dbhelper update
        dbHelper.updateStudy(receivedStudyId, this, updatedStudy);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        finish();
        startActivity(new Intent(this, EditorOverviewActivity.class));
    }
}
