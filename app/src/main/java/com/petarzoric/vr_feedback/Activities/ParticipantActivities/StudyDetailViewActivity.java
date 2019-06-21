package com.petarzoric.vr_feedback.Activities.ParticipantActivities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.SketchActivity;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.Model.Participant;
import com.petarzoric.vr_feedback.Activities.ParticipantActivities.Adapters.ParticipantsAdapter;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;
import com.petarzoric.vr_feedback.Utility.InputFilterMinMax;
import com.petarzoric.vr_feedback.Utility.MinMaxTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the recent participants of the study and the description.
 * Offers the opportunity to add a User by displaying an appropiate dialog.
 * In order to display the participants, it uses the {@link ParticipantsAdapter}.
 */

public class StudyDetailViewActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    ParticipantsAdapter adapter;
    private long receivedStudienId;
    private Study queriedStudy;
    TextView description;
    private Dialog popopDialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String participantPseudonym;
    private ImageButton addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studien_detail_view);

        addButton = findViewById(R.id.addUserButton);


        try {
            //get intent to get person id
            receivedStudienId = getIntent().getLongExtra("STUDIEN_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbHelper = new DatabaseHelper(this);
        queriedStudy = dbHelper.getStudy(receivedStudienId);
        recyclerView =  (RecyclerView) findViewById(R.id.recyclerView_detailStudy);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        description = findViewById(R.id.descriptionEditText);
        description.setText(queriedStudy.getDescription().toString());

         queriedStudy = dbHelper.getStudy(receivedStudienId);

        //set field to this user data
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Study: "+ queriedStudy.getName());
        populaterecyclerView("");







        popopDialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popopDialog.getWindow().setContentView(R.layout.custompopup);
                popopDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

                final Button submitButton = popopDialog.findViewById(R.id.popupSubmit);
                submitButton.setEnabled(false);
                submitButton.setVisibility(View.INVISIBLE);
                TextView close = popopDialog.findViewById(R.id.closeTextView);
                final EditText questiona = popopDialog.findViewById(R.id.questiona);
                final int min = 1990;
                final int max = 2018;
                final EditText questionb = popopDialog.findViewById(R.id.questionb);
                final EditText questionc = popopDialog.findViewById(R.id.questionc);
                List<EditText> editTexts = new ArrayList<EditText>();
                editTexts.add(questiona);
                editTexts.add(questionb);
                editTexts.add(questionc);

                for(EditText editText: editTexts){
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(questiona.getText().toString().trim().length() > 1 && questionb.getText().toString().trim().length() > 3
                                    && questionc.getText().toString().trim().length() > 0
                                    ){
                                submitButton.setEnabled(true);
                                submitButton.setVisibility(View.VISIBLE);
                            } else {
                                submitButton.setEnabled(false);
                                submitButton.setVisibility(View.INVISIBLE);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popopDialog.dismiss();

                    }

                });


                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int birthyear = Integer.parseInt(questionb.getText().toString());
                        if( birthyear > 2018 || birthyear < 1900){
                            Toast.makeText(getApplicationContext(), "Please enter a value between 1900 and 2018 as your birthyear", Toast.LENGTH_SHORT).show();
                        } else {
                            EditText questiona = popopDialog.findViewById(R.id.questiona);
                            EditText questionb = popopDialog.findViewById(R.id.questionb);
                            EditText questionc = popopDialog.findViewById(R.id.questionc);
                            participantPseudonym = new StringBuilder(questiona.getText().toString()+questionb.getText().toString()+questionc.getText().toString()).reverse().toString();
                            participantPseudonym = participantPseudonym.charAt(participantPseudonym.length() - 1) + participantPseudonym.substring(1, participantPseudonym.length()- 1) + participantPseudonym.charAt(0);
                            Intent goToSketchActivity = new Intent(getApplicationContext(), SketchActivity.class);
                            goToSketchActivity.putExtra("STUDIEN_ID", receivedStudienId);
                            goToSketchActivity.putExtra("PSEUDONYM", participantPseudonym);
                            goToSketchActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(goToSketchActivity);
                        }



                    }
                });

                popopDialog.show();

            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                goBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
       goBack();
    }

    public void goBack(){
        finish();
        Intent goBack = new Intent(getApplicationContext(), ParticipantOverviewActivity.class);
        startActivity(goBack);
    }

    private void populaterecyclerView(String filter){
        dbHelper = new DatabaseHelper(this);
        List<Participant> participants = dbHelper.getParticipantsForStudy(receivedStudienId);
        adapter = new ParticipantsAdapter(participants, this,
                recyclerView);
        recyclerView.setAdapter(adapter);

    }



    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
