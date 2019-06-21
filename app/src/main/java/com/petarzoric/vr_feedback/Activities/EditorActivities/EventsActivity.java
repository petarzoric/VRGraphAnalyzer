package com.petarzoric.vr_feedback.Activities.EditorActivities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.Utility.DurationConverter;
import com.petarzoric.vr_feedback.Activities.EditorActivities.Adapters.EventsAdapter;
import com.petarzoric.vr_feedback.Model.StudyEvent;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;
import com.petarzoric.vr_feedback.Activities.SwipeController;

import java.util.List;

/**
 * Shows all events of a specific study(if there are any)
 * Uses {@link EventsAdapter}.
 * Also allows to add events.
 * The added events will be shown in {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.EventsBarView}
 */

public class EventsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private long receivedStudyId;
    private Study queriedStudy;
    private EventsAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Dialog popupDialog;
    private Button addEventButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        try {
            //get intent to get person id
            receivedStudyId = getIntent().getLongExtra("STUDIEN_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbHelper = new DatabaseHelper(this);
        queriedStudy = dbHelper.getStudy(receivedStudyId);
        recyclerView= findViewById(R.id.eventsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        queriedStudy = dbHelper.getStudy(receivedStudyId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Managing events for " + queriedStudy.getName());
        /*
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Toast.makeText(EventsActivity.this, "Succesfully added.", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        */
        String titleBarName = queriedStudy.getName();
        addEventButton = findViewById(R.id.addEventButton);
        popupDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // shows dialog to add an event
                popupDialog.getWindow().setContentView(R.layout.customeventpopup);
                final Button addButton = popupDialog.findViewById(R.id.submitButton);
                addButton.setEnabled(false);
                TextView close = popupDialog.findViewById(R.id.closeEventButton);
                final EditText eventNameText = popupDialog.findViewById(R.id.eventNameText);
                addButton.setEnabled(false);
                addButton.setTextColor(Color.parseColor("#c7cdd6"));
                addButton.setAlpha(0.3f);
                eventNameText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length()>1){
                            addButton.setEnabled(true);
                            addButton.setTextColor(Color.WHITE);
                            addButton.setAlpha(1.0f);
                        } else {
                            addButton.setEnabled(false);
                            addButton.setTextColor(Color.parseColor("#c7cdd6"));
                            addButton.setAlpha(0.3f);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                //Setting limits for the the time pickers
                final NumberPicker eventMinutes = popupDialog.findViewById(R.id.minutesPickerEvent);
                eventMinutes.setMaxValue(queriedStudy.getDuration() / 60);

                final NumberPicker eventSeconds = popupDialog.findViewById(R.id.secondsPickerEvent);
                eventSeconds.setMaxValue(59);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupDialog.dismiss();
                    }

                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String eventName = eventNameText.getText().toString();

                        int eventTime = (eventMinutes.getValue() * 60) +  eventSeconds.getValue();
                        if(eventTime > queriedStudy.getDuration()){
                            Toast.makeText(EventsActivity.this, "The specified time isn't in the time frame of the study itself." +
                                    "Duration of the study:" + DurationConverter.getDurationInMMss(queriedStudy.getDuration()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StudyEvent studyEvent = new StudyEvent(eventName, eventTime);
                        dbHelper.addEventToStudy(queriedStudy, studyEvent);
                        Toast.makeText(EventsActivity.this, "Succesfully added.", Toast.LENGTH_SHORT).show();
                        adapter.refreshEvents(dbHelper.getEventsForStudy(receivedStudyId));
                        adapter.notifyDataSetChanged();
                        popupDialog.dismiss();




                    }
                });

                popupDialog.show();
            }
        });


        populateRecyclerView();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void goBack(){
        finish();
        Intent goBack = new Intent(getApplicationContext(), UpdateStudyActivity.class);
        goBack.putExtra("STUDIEN_ID", queriedStudy.getId());
        startActivity(goBack);

    }

    @Override
    public void onBackPressed() {
       goBack();
    }

    private void populateRecyclerView(){
        dbHelper = new DatabaseHelper(this);
        List<StudyEvent> events = dbHelper.getEventsForStudy(receivedStudyId);
        adapter = new EventsAdapter(events, this, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
