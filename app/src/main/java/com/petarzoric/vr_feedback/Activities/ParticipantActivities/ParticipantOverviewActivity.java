package com.petarzoric.vr_feedback.Activities.ParticipantActivities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Activities.MainActivity;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Activities.ParticipantActivities.Adapters.StudiesParticipantAdapter;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;

import java.util.ArrayList;

/**
 * First activity of the "participant area".
 * It displays all the available studies(if there are any).
 * Uses {@link StudiesParticipantAdapter }
 */

public class ParticipantOverviewActivity extends AppCompatActivity {

    private static final String TAG = "MainActivitity";
    Dialog popopDialog;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private StudiesParticipantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bereich);
        popopDialog = new Dialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Aktuelle Studien");


        getImages();

        popopDialog.setContentView(R.layout.custompopup);
        TextView close = popopDialog.findViewById(R.id.closeTextView);
        EditText mtrNr = popopDialog.findViewById(R.id.questiona);
        Button submitButton = popopDialog.findViewById(R.id.submitButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popopDialog.dismiss();
            }
        });






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
        Intent goBack = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goBack);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");

        initRecyclerView("");



    }
    private void initRecyclerView(String filter){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerVIew);
        recyclerView.setLayoutManager(layoutManager);
        dbHelper = new DatabaseHelper(this);
        adapter = new StudiesParticipantAdapter(dbHelper.getAllStudies(filter), this, recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popopDialog.setContentView(R.layout.custompopup);
                TextView close = popopDialog.findViewById(R.id.closeTextView);
                EditText mtrNr = popopDialog.findViewById(R.id.questiona);
                Button submitButton = popopDialog.findViewById(R.id.submitButton);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popopDialog.dismiss();
                    }
                });
                popopDialog.show();
            }
        });


    }

}
