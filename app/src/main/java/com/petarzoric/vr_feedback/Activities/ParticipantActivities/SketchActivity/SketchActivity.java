package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.opencsv.CSVWriter;
import com.petarzoric.vr_feedback.Activities.ParticipantActivities.StudyDetailViewActivity;
import com.petarzoric.vr_feedback.Model.Configuration;
import com.petarzoric.vr_feedback.Model.GraphPoint;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.Model.Participant;
import com.petarzoric.vr_feedback.R;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SketchActivity extends AppCompatActivity {

    private DrawingArea drawingArea;
    private Button backButton;
    private Button submitButton;

    private DatabaseHelper myDbHelper;

    private long receivedParticipantId;
    private long receivedStudienId;
    private String receivedPseudonym;
    private Participant participant;
    private Study study;
    private Configuration configuration;
    private Dialog commentInfoDialog;
    private Dialog feedbackDialog;
    private Dialog introDialog;
    private TextView startTimeText;
    private TextView endTimeText;
    boolean analyzed = false;
    boolean commentedPoints = false;
    private ImageView background;

   // private EventsBarView eventsBarView;
    private ArrayList<GraphPoint> createdPoints;

    private int STORAGE_PERMISSION_CODE = 1;

    int viewWidth;
    int viewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rate your experience!");
        drawingArea = (DrawingArea) findViewById(R.id.paintView);
       // eventsBarView  = findViewById(R.id.eventsView);
        //startTimeText = findViewById(R.id.startTimeTextView);
        //endTimeText = findViewById(R.id.endTimeTextView);
        background = findViewById(R.id.analysisBackgroudImage);




        myDbHelper = new DatabaseHelper(this);
        receivedStudienId = getIntent().getLongExtra("STUDIEN_ID", 1);
        receivedPseudonym = getIntent().getStringExtra("PSEUDONYM");
        participant = new Participant(receivedPseudonym);
        study = myDbHelper.getStudy(receivedStudienId);
        //startTimeText.setText("0:00");
        //endTimeText.setText(DurationConverter.getDurationInMMss(study.getDuration()));
        configuration = myDbHelper.getConfigForStudy(receivedStudienId);
        participant.setStudy_id(receivedStudienId);
        drawingArea.setConfiguration(configuration);
        introDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        introDialog.getWindow().setContentView(R.layout.sketchinfopopup);
        showIntro();
        commentInfoDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        commentInfoDialog.getWindow().setContentView(R.layout.popup_rate_extremepoints);
        feedbackDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        feedbackDialog.getWindow().setContentView(R.layout.feedbackdialog);




        /*
        ViewTreeObserver viewTreeObserver2 = eventsBarView.getViewTreeObserver();
        if (viewTreeObserver2.isAlive()) {
            viewTreeObserver2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    eventsBarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = eventsBarView.getWidth();
                    viewHeight = eventsBarView.getHeight();
                    eventsBarView.initScreen(viewHeight, viewWidth);
                }
            });
        }
    */

        ViewTreeObserver viewTreeObserver = drawingArea.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawingArea.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = drawingArea.getWidth();
                    viewHeight = drawingArea.getHeight();
                    drawingArea.initScreen(viewHeight, viewWidth);

                }
            });
        }




        if(ContextCompat.checkSelfPermission(SketchActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
           // Toast.makeText(this, "already granted", Toast.LENGTH_LONG).show();
        } else {
            requestStoragePermission();
        }

      //  eventsBarView.setEvents(myDbHelper.getEventsForStudy(receivedStudienId));
       // eventsBarView.setStudyDuration(study.getDuration());






        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        backButton = (Button) findViewById(R.id.backButton);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setText("Analyze sketch");
        backButton.setText("Clear Canvas");






        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GraphPoint[] test = paintView.testFunc();
                //System.out.print(test);
                if(analyzed){
                    askForGeneralFeedback();
                } else {
                    showAlert();
                }

    //


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingArea.clearCanvas();
                //drawingArea.undo();

            }
        });

        background.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.goBack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override public void onBackPressed(){
        this.goBack();
    }

    public void goBack(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SketchActivity.this);
        builder.setTitle("Are you sure you want to go back? You'll lose your sketch.");

        builder.setMessage("Submit or cancel.");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent goBack = new Intent(getApplicationContext(), StudyDetailViewActivity.class);
                goBack.putExtra("STUDIEN_ID", receivedStudienId);
                startActivity(goBack);


            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void analyzeAndAskForComments(){
        //analyze Graph and highlight it
        ArrayList<GraphPoint> list = drawingArea.analyzeGraph(study.getDuration());
        createdPoints = list;
        final TextView close = commentInfoDialog.findViewById(R.id.closeTextViewCommentPopup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentInfoDialog.dismiss();
            }
        });

        commentInfoDialog.show();
        analyzed=true;
        backButton.setVisibility(View.INVISIBLE);
        submitButton.setText("Submit comments");

        //show popup

    }

    public void startAnalysis(){
        myDbHelper.saveNewParticipant(participant);
        Participant currentParticipant = myDbHelper.getParticipant(receivedStudienId, participant.getPseudonym());
        myDbHelper.savePoints(drawingArea.analyzeGraph(study.getDuration()), currentParticipant.getId(), receivedStudienId);
        ArrayList<GraphPoint> testList = myDbHelper.getPointsForParticipant(receivedStudienId, currentParticipant.getId());
        System.out.println(testList);
        ArrayList<GraphPoint> list = drawingArea.analyzeGraph(study.getDuration());
        createdPoints = list;
        myDbHelper.insertPoints(list, currentParticipant.getId(), study.getId());
        writeCSV(currentParticipant.getId());
    }


    public void saveData(){
        myDbHelper.saveNewParticipant(participant);
        Participant currentParticipant = myDbHelper.getParticipant(receivedStudienId, participant.getPseudonym());
        myDbHelper.savePoints(createdPoints, currentParticipant.getId(), receivedStudienId);
        ArrayList<GraphPoint> testList = myDbHelper.getPointsForParticipant(receivedStudienId, currentParticipant.getId());
        System.out.println(testList);
        myDbHelper.insertPoints(createdPoints, currentParticipant.getId(), study.getId());
        myDbHelper.saveAnalysis(receivedStudienId, currentParticipant.getId(), drawingArea.getGraphAnalysisResult());
        writeCSV(currentParticipant.getId());
    }

    public void askForGeneralFeedback(){
        EditText comment = feedbackDialog.findViewById(R.id.feedBackInput);
        TextView close = feedbackDialog.findViewById(R.id.closeTextViewCommentPopup);
        Button submitButton = feedbackDialog.findViewById(R.id.submit);
        Button cancelButton = feedbackDialog.findViewById(R.id.cancel);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackDialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Table anlegen für GeneralFeedback und reinschreiben
                Toast.makeText(SketchActivity.this, "Thanks for participating", Toast.LENGTH_LONG).show();
                saveData();
                feedbackDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), StudyDetailViewActivity.class);
                intent.putExtra("STUDIEN_ID", study.getId());
                startActivity(intent);


            }
        });

        feedbackDialog.show();

    }

    public void showIntro(){
        TextView close = introDialog.findViewById(R.id.closeIntroTextView);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                introDialog.dismiss();
            }
        });
        introDialog.show();
    }




    public void writeCSV(long participant_id){

        int graphAnalysisID = myDbHelper.getAnalysisID((int)participant_id);
        String appFolderName = "VR_FEEDBACK";
        String studienFolderName = study.getName();
        File vrfeedbackFolder = new File(Environment.getExternalStorageDirectory(), appFolderName);
        if(!vrfeedbackFolder.exists()){
            vrfeedbackFolder.mkdirs();
        }

        File studienFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + appFolderName+"/", studienFolderName);
        if(!studienFolder.exists()){
            studienFolder.mkdirs();
        }

        File csvFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
        "/"+appFolderName+"/"+studienFolderName+"/", getFileName()
        );

        try{

            csvFile.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));
            SQLiteDatabase db = myDbHelper.getReadableDatabase();
            String query="select * from GraphPoints where participant_id ="+ participant_id  +
                    " AND study_id = " + study.getId();
            Cursor cursor = db.rawQuery(query, null);
            csvWrite.writeNext(cursor.getColumnNames());
            while(cursor.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11),
                        cursor.getString(12), cursor.getString(13)
                };
                csvWrite.writeNext(arrStr);
            }
            cursor.close();


            String query2="select * from Graph_analysis where participant_id ="+ participant_id  +
                    " AND study_id = " + study.getId();
            Cursor cursor2 = db.rawQuery(query2, null);
            csvWrite.writeNext(cursor2.getColumnNames());
            while(cursor2.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={cursor2.getString(0),cursor2.getString(1), cursor2.getString(2), cursor2.getString(3),
                        cursor2.getString(4), cursor2.getString(5), cursor2.getString(6), cursor2.getString(7),
                        cursor2.getString(8), cursor2.getString(9), cursor2.getString(10), cursor2.getString(11),
                        cursor2.getString(12), cursor2.getString(13), cursor2.getString(14), cursor2.getString(15),
                        cursor2.getString(16), cursor2.getString(17), cursor2.getString(18), cursor2.getString(19),
                        cursor2.getString(20), cursor2.getString(21), cursor2.getString(22),
                };
                csvWrite.writeNext(arrStr);
            }

            cursor2.close();

            String query3="select * from Recovery_phases where analysis_id ="+ graphAnalysisID;
            Cursor cursor3 = db.rawQuery(query3, null);
            csvWrite.writeNext(cursor3.getColumnNames());
            while(cursor3.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={cursor3.getString(0),cursor3.getString(1), cursor3.getString(2), cursor3.getString(3),
                        cursor3.getString(4), cursor3.getString(5), cursor3.getString(6), cursor3.getString(7),
                        cursor3.getString(8)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            cursor3.close();
            Toast.makeText(SketchActivity.this, "successfully saved", Toast.LENGTH_LONG).show();
        } catch(Exception sqlEx){
            Log.e("SketchActivity", sqlEx.getMessage(), sqlEx);
        }


    }


    public String getFileName(){
        String currentTime = new SimpleDateFormat("ddMMyyyy").format(new Date());
        return currentTime+"_"+participant.getPseudonym()+"_"+ study.getName()+".csv";
    }

    public void showAlert(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SketchActivity.this);
        builder.setTitle("Are you finished with your sketch?");

        builder.setMessage("Submit or cancel.");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              //  showEvaluationDialog(getWindow().getDecorView().getRootView());

                //startAnalysis();
                analyzeAndAskForComments();

                //go to update activity


            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        builder.create().show();
    }



    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("needed bz of dis and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SketchActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

    }



    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /*
    public void showEvaluationsLayout(){
        RelativeLayout rl = findViewById(R.id.evaluationLayout);

        List<PagerModel> pages = new ArrayList<>();
        PagerModel extremePointsPage = new PagerModel("2", "Pager 2", "extremePhases");
        extremePointsPage.setCreatedPoints(createdPoints);
        pages.add(new PagerModel("1", "Pager 1", "extremePoints"));
        pages.add(extremePointsPage);
        pages.add(new PagerModel("3", "Pager 3", "generalFeedback"));
        PagerAdapter adapter = new PagerAdapter(this, pages);
        AutoScrollViewPager pager = (AutoScrollViewPager) rl.findViewById(R.id.pagerLayout);
        pager.setAdapter(adapter);

        CirclePageIndicator pageIndicator = (CirclePageIndicator) rl.findViewById(R.id.indicatorLayout);
        pageIndicator.setViewPager(pager);
        pageIndicator.setCurrentItem(0);
        rl.setVisibility(View.VISIBLE);
    }


    public void showEvaluationDialog(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pager_layout);
        Toast.makeText(SketchActivity.this, "successfully saved", Toast.LENGTH_LONG).show();

        List<PagerModel> pages = new ArrayList<>();
        pages.add(new PagerModel("1", "Pager 1", "extremePoints"));
        pages.add(new PagerModel("2", "Pager 2", "extremePhases"));
        pages.add(new PagerModel("3", "Pager 3", "generalFeedback"));

        PagerAdapter adapter = new PagerAdapter(this, pages);

        AutoScrollViewPager pager = (AutoScrollViewPager) dialog.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        CirclePageIndicator pageIndicator = (CirclePageIndicator) dialog.findViewById(R.id.indicator);
        pageIndicator.setViewPager(pager);
        pageIndicator.setCurrentItem(0);

        dialog.show();
    }
    */
}


