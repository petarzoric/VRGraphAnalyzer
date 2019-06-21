package com.petarzoric.vr_feedback.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.petarzoric.vr_feedback.Activities.EditorActivities.EditorOverviewActivity;
import com.petarzoric.vr_feedback.Activities.ParticipantActivities.ParticipantOverviewActivity;
import com.petarzoric.vr_feedback.R;

/**
 * Devides the app into editor and participant area by showing two image buttons next to each other.
 */

public class MainActivity extends AppCompatActivity {

    private ImageButton userBereich;
    private ImageButton editorBereich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialisiere Elemente
        userBereich = findViewById(R.id.buttonUserBereich);
        editorBereich = findViewById(R.id.buttonEditorBereich);

        userBereich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUserBereich = new Intent(MainActivity.this, ParticipantOverviewActivity.class);
                startActivity(goToUserBereich);
            }
        });

        editorBereich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEditorBereich = new Intent(MainActivity.this, EditorOverviewActivity.class);
                startActivity(goToEditorBereich);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You can't go back from here.", Toast.LENGTH_SHORT).show();
    }
}
