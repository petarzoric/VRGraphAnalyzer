package com.petarzoric.vr_feedback.Activities.EditorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.petarzoric.vr_feedback.Activities.MainActivity;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Activities.EditorActivities.Adapters.StudiesEditorAdapter;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;

/**
 * First Activity of the "editor area".
 * User is directed to this screen as soon as he clicks on the edit button on {@link MainActivity}
 * Shows all studies(if there are any) in a list. Uses {@link StudiesEditorAdapter} to organize them.
 *
 */
public class EditorOverviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper dbHelper;
    private StudiesEditorAdapter adapter;
    private String filter = "";
    private ImageButton addStudyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editor mode");
        setContentView(R.layout.activity_editor_bereich);
        addStudyButton = findViewById(R.id.button);
        addStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddStudyActivity();
            }
        });


        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_2);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populate recyclerview
        populaterecyclerView(filter);


    }

    private void populaterecyclerView(String filter){
        dbHelper = new DatabaseHelper(this);
        adapter = new StudiesEditorAdapter(dbHelper.getAllStudies(filter), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
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


    @Override
    public void onBackPressed() {
        goBack();
    }
    private void goBack(){
        Intent goBack = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goBack);
    }



    private void goToAddStudyActivity(){
        Intent intent = new Intent(EditorOverviewActivity.this, AddStudyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}