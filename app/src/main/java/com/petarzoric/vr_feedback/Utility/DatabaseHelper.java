package com.petarzoric.vr_feedback.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.petarzoric.vr_feedback.Model.Configuration;
import com.petarzoric.vr_feedback.Model.GraphAnalysisResult;
import com.petarzoric.vr_feedback.Model.GraphPoint;
import com.petarzoric.vr_feedback.Model.Participant;
import com.petarzoric.vr_feedback.Model.RecoveryPhase;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.Model.StudyEvent;

/**
 * Takes care of all the database transactions and offers save/edit/delete functionalities for
 * several tables.
 * Contains the following tables:
 *      - Studies
 *      - Participants
 *      - Points
 *      - Configurations
 *      - Study Events
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    //studies
    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1 ;
    public static final String TABLE_NAME_STUDIES = "Studies";
    public static final String COLUMN_STUDY_CREATED = "created";
    public static final String COLUMN_ID_STUDY = "_id";
    public static final String COLUMN_STUDY_NAME = "name";
    public static final String COLUMN_STUDY_DESCRIPTION = "description";
    public static final String COLUMN_STUDY_CREATOR = "creator";
    public static final String COLUMN_STUDY_DURATION = "duration";

    //participants
    public static final String TABLE_NAME_PARTICIPANTS = "Participants";
    public static final String COLUMN_PARTICIPANT_ID = "_id";
    public static final String COLUMN_PARTICIPANT_PSEUDONYM = "Pseudonym";
    public static final String COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY = "study_id";

    //points
    public static final String TABLE_NAME_POINTS = "GraphPoints";
    public static final String COLUMN_POINTS_DETAIL_ID = "_id";
    public static final String COLUMN_POINTS_DETAIL_INDEX = "_index";
    public static final String COLUMN_POINTS_DETAIL_X = "x";
    public static final String COLUMN_POINTS_DETAIL_Y = "y";
    public static final String COLUMN_POINTS_DETAIL_SCREENX = "screen_x";
    public static final String COLUMN_POINTS_DETAIL_SCREENY = "screen_y";
    public static final String COLUMN_POINTS_DETAIL_FIRSTDERIVATE = "first_derivate";
    public static final String COLUMN_POINTS_DETAIL_SECONDDERIVATE = "second_derivate";
    public static final String COLUMN_POINTS_DETAIL_THIRDDERIVATE = "third_derivate";
    public static final String COLUMN_POINTS_DETAIL_EXTREMEPOINT = "extreme_point";
    public static final String COLUMN_POINTS_DETAIL_HIGH = "high";
    public static final String COLUMN_POINTS_DETAIL_LOW = "low";
    public static final String COLUMN_POINTS_DETAIL_EXTREMEPHASE = "extreme_phase";
    public static final String COLUMN_POINTS_DETAIL_CONSTANTPHASE = "constant_phase";
    public static final String COLUMN_POINTS_DETAIL_STUDYID = "study_id";
    public static final String COLUMN_POINTS_DETAIL_PARTICIPANTID = "participant_id";
    public static final String COLUMN_POINTS_DETAIL_COMMENT = "comment";
    public static final String COLUMN_POINTS_DETAIL_PHASE ="phase";
    public static final String COLUMN_POINTS_DETAIL_RECOVERYPHASE ="recovery_phase";

    //Punkte v2
    public static final String TABLE_NAME_POINTS2 = "points2";
    public static final String COLUMN_POINTS_PARTICIPANT_ID = "participant_id";
    public static final String COLUMN_POINTS_STUDYID = "study_id";
    public static final String COLUMN_POINTS_POINTS = "points";
    public static final String COLUMN_POINTS_ID = "_id";

    //configuration
    public static final String TABLE_NAME_CONFIG = "Configurations";
    public static final String COLUMN_CONFIG_ID = "_id";
    public static final String COLUMN_CONFIG_TOTALPOINTS = "total_points";
    public static final String COLUMN_CONFIG_EXTREMEPAHSE_MINLENGTH = "extPhase_minlength";
    public static final String COLUMN_CONFIG_EXTREMEPAHSE_DELTA = "extPhase_delta";
    public static final String COLUMN_CONFIG_EXTREMEPAHSE_TOLERANCE = "extPhase_grdTolerance";
    public static final String COLUMN_CONFIG_CONSTANTPHASE_MINLENGTH = "constPhase_minlength";
    public static final String COLUMN_CONFIG_CONSTANTPHASE_DELTA = "constPhase_delta";
    public static final String COLUMN_CONFIG_CONSTANTPHASE_TOLERANCE = "constPhase_grdTolerance";
    public static final String COLUMN_CONFIG_CONSTANTPHASE_DEVIATION = "constPhase_deviation";
    public static final String COLUMN_CONFIG_STUDYID = "study_id";
    public static final String COLUMN_CONFIG_EXTREMEPOINTSFILTER ="extremePoints_filter";

    //study events
    public static final String TABLE_NAME_EVENTS = "Study_events";
    public static final String COLUMN_EVENT_ID = "_id";
    public static final String COLUMN_EVENT_NAME = "name";
    public static final String COLUMN_EVENT_TIME = "time";
    public static final String COLUMN_EVENT_STUDY_ID = "study_id";

    //analysis
    public static final String TABLE_NAME_ANALYSIS = "Graph_analysis";
    public static final String COLUMN_ANALYSIS_ID = "_id";
    public static final String COLUMN_ANALYSIS_AVERAGEPRESENCE = "average_presence";
    public static final String COLUMN_ANALYSIS_VIRTUALPRESENCESHARE = "virtual_share";
    public static final String COLUMN_ANALYSIS_REALPRESENCESHARE = "real_share";
    public static final String COLUMN_ANALYSIS_PHASE1SHARE = "phase1_share";
    public static final String COLUMN_ANALYSIS_PHASE2SHARE="phase2_share";
    public static final String COLUMN_ANALYSIS_PHASE3SHARE="phase3_share";
    public static final String COLUMN_ANALYSIS_PHASE1GRADIENT="phase1_gradient";
    public static final String COLUMN_ANALYSIS_PHASE2GRADIENT="phase2_gradient";
    public static final String COLUMN_ANALYSIS_PHASE3GRADIENT="phase3_gradient";
    public static final String COLUMN_ANALYSIS_PHASE1PRESENCE="phase1_presence";
    public static final String COLUMN_ANALYSIS_PHASE2PRESENCE="phase2_presence";
    public static final String COLUMN_ANALYSIS_PHASE3PRESENCE="phase3_presence";
    public static final String COLUMN_ANALYSIS_PHASE1ANGLE="phase1_angle";
    public static final String COLUMN_ANALYSIS_PHASE2ANGLE="phase2_angle";
    public static final String COLUMN_ANALYSIS_PHASE3ANGLE="phase3_angle";
    public static final String COLUMN_ANALYSIS_PHASE1DURATION="phase1_duration";
    public static final String COLUMN_ANALYSIS_PHASE2DURATION="phase2_duration";
    public static final String COLUMN_ANALYSIS_PHASE3DURATION="phase3_duration";
    public static final String COLUMN_ANALYSIS_MAX_PRESENCE="max_presence";
    public static final String COLUMN_ANALYSIS_BREAKS_IN_PRESENCE ="break_in_presence_amount";
    public static final String COLUMN_ANALYSIS_STUDY_ID ="study_id";
    public static final String COLUMN_ANALYSIS_PARTICIPANT_ID="participant_id";

    //recovery Phases
    public static final String TABLE_NAME_RECOVERY_PHASES = "Recovery_phases";
    public static final String COLUMN_RECOVERYPHASES_ID = "_id";
    public static final String COLUMN_RECOVERYPHASES_START_INDEX = "start_index";
    public static final String COLUMN_RECOVERYPHASES_END_INDEX = "end_index";
    public static final String COLUMN_RECOVERYPHASES_START_TIME = "start_time";
    public static final String COLUMN_RECOVERYPHASES_END_TIME = "end_time";
    public static final String COLUMN_RECOVERYPHASES_DURATION = "duration";
    public static final String COLUMN_RECOVERYPHASES_SHARE = "share";
    public static final String COLUMN_RECOVERYPHASES_MIN = "min_presence";
    public static final String COLUMN_RECOVERYPHASES_ANALYSIS_ID = "analysis_id";







    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME_STUDIES + " (" +
                COLUMN_ID_STUDY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_STUDY_NAME + " TEXT, " +
                COLUMN_STUDY_CREATED + " TEXT, " +
                COLUMN_STUDY_CREATOR + " TEXT, " +
                COLUMN_STUDY_DESCRIPTION + " TEXT, " +
                COLUMN_STUDY_DURATION + " INTEGER);"
        );



        db.execSQL(" CREATE TABLE " +  TABLE_NAME_PARTICIPANTS + " (" +
                COLUMN_PARTICIPANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY + " INTEGER, " +
                COLUMN_PARTICIPANT_PSEUDONYM + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY + ") REFERENCES " +
                TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY +")"+"ON DELETE CASCADE"+ ");"
        );



        db.execSQL(" CREATE TABLE " + TABLE_NAME_POINTS +
                " (" + COLUMN_POINTS_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POINTS_DETAIL_INDEX + " INTEGER, " +
                COLUMN_POINTS_DETAIL_X + " REAL, " +
                COLUMN_POINTS_DETAIL_Y + " REAL, " +
                COLUMN_POINTS_DETAIL_SCREENX + " REAL, " +
                COLUMN_POINTS_DETAIL_SCREENY + " REAL, " +
                COLUMN_POINTS_DETAIL_FIRSTDERIVATE + " REAL, " +
                COLUMN_POINTS_DETAIL_SECONDDERIVATE + " REAL, " +
                COLUMN_POINTS_DETAIL_THIRDDERIVATE + " REAL, " +
                COLUMN_POINTS_DETAIL_EXTREMEPOINT + " INTEGER, " +
                COLUMN_POINTS_DETAIL_HIGH + " INTEGER, " +
                COLUMN_POINTS_DETAIL_LOW + " INTEGER, " +
                COLUMN_POINTS_DETAIL_PHASE + " INTEGER, " +
                COLUMN_POINTS_DETAIL_EXTREMEPHASE + " INTEGER, " +
                COLUMN_POINTS_DETAIL_CONSTANTPHASE + " INTEGER, " +
                COLUMN_POINTS_DETAIL_RECOVERYPHASE + " INTEGER, " +
                COLUMN_POINTS_DETAIL_COMMENT + " TEXT, " +
                COLUMN_POINTS_DETAIL_STUDYID + " INTEGER, " +
                COLUMN_POINTS_DETAIL_PARTICIPANTID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_POINTS_DETAIL_STUDYID + ") REFERENCES " +
                TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY + " )"+" ON DELETE CASCADE"+ "," +
                "FOREIGN KEY(" + COLUMN_POINTS_DETAIL_PARTICIPANTID + ") REFERENCES " +
                TABLE_NAME_PARTICIPANTS + "(" + COLUMN_PARTICIPANT_ID +")"+"ON DELETE CASCADE"+ ");"
        );


        db.execSQL("CREATE TABLE " + TABLE_NAME_POINTS2
        + " (" +COLUMN_POINTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POINTS_STUDYID + " INTEGER, " +
                COLUMN_POINTS_PARTICIPANT_ID + " INTEGER, " +
                COLUMN_POINTS_POINTS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_POINTS_STUDYID + ") REFERENCES " +
                        TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY + " )"+" ON DELETE CASCADE"+ "," +
                "FOREIGN KEY(" + COLUMN_POINTS_PARTICIPANT_ID + ") REFERENCES " +
                TABLE_NAME_PARTICIPANTS + "(" + COLUMN_PARTICIPANT_ID +")"+"ON DELETE CASCADE"+ ");"
        );

        db.execSQL(" CREATE TABLE " + TABLE_NAME_CONFIG + " (" +
                COLUMN_CONFIG_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONFIG_TOTALPOINTS + " INTEGER, " +
                        COLUMN_CONFIG_EXTREMEPOINTSFILTER + " REAL, " +
                COLUMN_CONFIG_EXTREMEPAHSE_MINLENGTH + " REAL, " +
                COLUMN_CONFIG_EXTREMEPAHSE_DELTA + " REAL, " +
                COLUMN_CONFIG_EXTREMEPAHSE_TOLERANCE + " REAL, " +
                COLUMN_CONFIG_CONSTANTPHASE_MINLENGTH + " REAL, " +
                        COLUMN_CONFIG_CONSTANTPHASE_DELTA + " REAL, " +
                        COLUMN_CONFIG_CONSTANTPHASE_TOLERANCE + " REAL, " +
                        COLUMN_CONFIG_CONSTANTPHASE_DEVIATION + " REAL, " +
                COLUMN_CONFIG_STUDYID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CONFIG_STUDYID + ") REFERENCES " +
                        TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY +")"+"ON DELETE CASCADE"+ ");"
        );

        db.execSQL(" CREATE TABLE " + TABLE_NAME_RECOVERY_PHASES + " (" +
                COLUMN_RECOVERYPHASES_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECOVERYPHASES_START_INDEX + " INTEGER, " +
                COLUMN_RECOVERYPHASES_END_INDEX + " INTEGER, " +
                COLUMN_RECOVERYPHASES_START_TIME + " INTEGER, " +
                COLUMN_RECOVERYPHASES_END_TIME + " INTEGER, " +
                COLUMN_RECOVERYPHASES_DURATION + " INTEGER, " +
                COLUMN_RECOVERYPHASES_MIN + " REAL, " +
                COLUMN_RECOVERYPHASES_SHARE + " REAL, " +
                COLUMN_RECOVERYPHASES_ANALYSIS_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_RECOVERYPHASES_ANALYSIS_ID + ") REFERENCES " +
                TABLE_NAME_ANALYSIS + "(" + COLUMN_ANALYSIS_ID +")"+"ON DELETE CASCADE"+ ");"
        );

        db.execSQL(" CREATE TABLE " + TABLE_NAME_EVENTS + " (" +
                COLUMN_PARTICIPANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_NAME + " TEXT, " +
                COLUMN_EVENT_TIME + " INTEGER, " +
                COLUMN_EVENT_STUDY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_EVENT_STUDY_ID + ") REFERENCES " +
                TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY +")"+"ON DELETE CASCADE"+ ");"
        );

        db.execSQL(" CREATE TABLE " + TABLE_NAME_ANALYSIS +
                " (" + COLUMN_ANALYSIS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ANALYSIS_AVERAGEPRESENCE + " REAL, " +
                COLUMN_ANALYSIS_VIRTUALPRESENCESHARE + " REAL, " +
                COLUMN_ANALYSIS_REALPRESENCESHARE + " REAL, " +
                COLUMN_ANALYSIS_PHASE1SHARE + " REAL, " +
                COLUMN_ANALYSIS_PHASE2SHARE + " REAL, " +
                COLUMN_ANALYSIS_PHASE3SHARE + " REAL, " +
                COLUMN_ANALYSIS_PHASE1GRADIENT + " REAL, " +
                COLUMN_ANALYSIS_PHASE2GRADIENT + " REAL, " +
                COLUMN_ANALYSIS_PHASE3GRADIENT + " REAL, " +
                COLUMN_ANALYSIS_PHASE1PRESENCE + " REAL, " +
                COLUMN_ANALYSIS_PHASE2PRESENCE + " REAL, " +
                COLUMN_ANALYSIS_PHASE3PRESENCE + " REAL, " +
                COLUMN_ANALYSIS_PHASE1ANGLE + " REAL, " +
                COLUMN_ANALYSIS_PHASE2ANGLE + " REAL, " +
                COLUMN_ANALYSIS_PHASE3ANGLE + " REAL, " +
                COLUMN_ANALYSIS_PHASE1DURATION + " INTEGER, " +
                COLUMN_ANALYSIS_PHASE2DURATION + " INTEGER, " +
                COLUMN_ANALYSIS_PHASE3DURATION + " INTEGER, " +
                COLUMN_ANALYSIS_MAX_PRESENCE + " REAL, " +
                COLUMN_ANALYSIS_BREAKS_IN_PRESENCE + " INTEGER, " +
                COLUMN_ANALYSIS_STUDY_ID + " INTEGER, " +
                COLUMN_ANALYSIS_PARTICIPANT_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ANALYSIS_STUDY_ID + ") REFERENCES " +
                TABLE_NAME_STUDIES + "(" + COLUMN_ID_STUDY + " )"+" ON DELETE CASCADE"+ "," +
                "FOREIGN KEY(" + COLUMN_ANALYSIS_PARTICIPANT_ID + ") REFERENCES " +
                TABLE_NAME_PARTICIPANTS + "(" + COLUMN_PARTICIPANT_ID +")"+"ON DELETE CASCADE"+ ");"
        );
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POINTS2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ANALYSIS);
        this.onCreate(db);
    }




    /**------------------------  STUDIES TABLE ACTIONS  ------------------------**/

    public void addStudy(Study study) {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDY_NAME, study.getName());
        values.put(COLUMN_STUDY_DESCRIPTION, study.getDescription());
        values.put(COLUMN_STUDY_CREATOR, study.getCreator());
        values.put(COLUMN_STUDY_CREATED, date);
        values.put(COLUMN_STUDY_DURATION, study.getDuration());
        //  values.put(COLUMN_STUDIE_DATE, System.currentTimeMillis());
        // values.put( COLUMN_STUDIE_IMAGE, study.getImage());
        study.setId(db.insert(TABLE_NAME_STUDIES,null, values));
        this.addConfigToStudy(study.getConfiguration(), study);
        db.close();

    }

    /**update record**/
    public void updateStudy(long personId, Context context, Study study) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+ TABLE_NAME_STUDIES +" SET name ='"+ study.getName()
                + "', creator ='" + study.getCreator()+  "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }

    /**delete record**/
    public void deleteStudy(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="DELETE FROM "+ TABLE_NAME_STUDIES +
                " WHERE " + COLUMN_ID_STUDY + " = " + id;
        db.execSQL(query);
        db.close();
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    public List<Study> getAllStudies(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME_STUDIES;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME_STUDIES + " ORDER BY "+ filter;
        }

        List<Study> studies = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Study study;

        if (cursor.moveToFirst()) {
            do {
                study = new Study();

                study.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID_STUDY)));
                study.setName(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_NAME)));
                study.setCreator(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_CREATOR)));
                study.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_CREATED)));
                //  study.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_STUDIE_DATE)));
                // study.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_STUDIE_IMAGE)));
                studies.add(study);
            } while (cursor.moveToNext());
        }


        return studies;

    }

    /**Query only 1 record**/
    public Study getStudy(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME_STUDIES + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Study receivedStudy = new Study();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedStudy.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID_STUDY)));
            receivedStudy.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_STUDY_CREATED)));
            receivedStudy.setName(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_NAME)));
            receivedStudy.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_DESCRIPTION)));
            receivedStudy.setCreator(cursor.getString(cursor.getColumnIndex(COLUMN_STUDY_CREATOR)));
            receivedStudy.setDuration(cursor.getInt(cursor.getColumnIndex(COLUMN_STUDY_DURATION)));
            //  receivedStudy.setDate(cursor.getLong(cursor.getColumnIndex(COLUMN_STUDIE_DATE)));
            //receivedStudy.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_STUDIE_IMAGE)));
            receivedStudy.setConfiguration(this.getConfigForStudy(id));
        }
        return receivedStudy;

    }



    /**------------------------  PARTICIPANTS TABLE ACTIONS  ------------------------**/

    public List<Participant> getParticipantsForStudy(long study_id){
        String query = "SELECT * FROM " + TABLE_NAME_PARTICIPANTS +
                " WHERE " + COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY + " = " + study_id;
        List<Participant> participantsLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Participant participant;

        if(cursor.moveToFirst()){
            do {
                participant = new Participant();
                participant.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_PARTICIPANT_ID)));
                participant.setStudy_id(cursor.getLong(cursor.getColumnIndex(COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY)));
                participant.setPseudonym(cursor.getString(cursor.getColumnIndex(COLUMN_PARTICIPANT_PSEUDONYM)));
                participantsLinkedList.add(participant);
            } while (cursor.moveToNext());
        }
        return participantsLinkedList;
    }

    public void saveNewParticipant(Participant participant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PARTICIPANT_PSEUDONYM, participant.getPseudonym());
        values.put(COLUMN_PARTICIPANT_STUDY_ID_FOREIGKEY, participant.getStudy_id());

        db.insert(TABLE_NAME_PARTICIPANTS, null, values);

        db.close();

    }

    public Participant getParticipant(long study_id, String pseudonym){
        Participant returnParticipant = new Participant(88, "failed");
        List<Participant> idk = this.getParticipantsForStudy(study_id);
        for(Participant p: idk){
            if(p.getPseudonym().equals(pseudonym)){
                returnParticipant = p;
                return returnParticipant;

            }
        }
        return returnParticipant;

    }




    /**------------------------  POINTS TABLE ACTIONS  ------------------------**/

    public void insertPoints(ArrayList<GraphPoint> points, long participant_id, long study_id){
        SQLiteDatabase db = this.getWritableDatabase();

        for(GraphPoint point: points){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_POINTS_DETAIL_INDEX, point.getIndex());
            contentValues.put(COLUMN_POINTS_DETAIL_X, convertToDouble(point.getX()));
            contentValues.put(COLUMN_POINTS_DETAIL_Y, convertToDouble(point.getY()));
            contentValues.put(COLUMN_POINTS_DETAIL_SCREENX, convertToDouble(point.getScreenX()));
            contentValues.put(COLUMN_POINTS_DETAIL_SCREENY, convertToDouble(point.getScreenY()));
            contentValues.put(COLUMN_POINTS_DETAIL_FIRSTDERIVATE, convertToDouble(point.getFirstDerivate()));
            contentValues.put(COLUMN_POINTS_DETAIL_SECONDDERIVATE, convertToDouble(point.getSecondDerivate()));
            contentValues.put(COLUMN_POINTS_DETAIL_THIRDDERIVATE, convertToDouble(point.getThirdDerivate()));
            contentValues.put(COLUMN_POINTS_DETAIL_EXTREMEPOINT, point.isExtremePoint() == true? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_HIGH, point.isHp() == true ? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_LOW, point.isTp() == true ? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_EXTREMEPHASE, point.isPartOfConstantPhase() == true ? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_CONSTANTPHASE, point.isPartOfExtremePhase() == true ? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_RECOVERYPHASE, point.isPartOfRecoveryPhase() == true ? 1 : 0);
            contentValues.put(COLUMN_POINTS_DETAIL_STUDYID, study_id);
            contentValues.put(COLUMN_POINTS_DETAIL_PARTICIPANTID, participant_id);
            contentValues.put(COLUMN_POINTS_DETAIL_COMMENT, point.getComment());
            contentValues.put(COLUMN_POINTS_DETAIL_PHASE, point.getPhase());
            db.insert(TABLE_NAME_POINTS,null, contentValues);
        }


        db.close();
    }




    /**------------------------  CONFIGURATIONS TABLE ACTIONS  ------------------------**/

    public Configuration getConfigForStudy(long study_id){
        Configuration config = new Configuration();
        String query = "SELECT * FROM " + TABLE_NAME_CONFIG +
                " WHERE " + COLUMN_CONFIG_STUDYID +
                " = " + study_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                config.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_CONFIG_ID)));
                config.setStudy_id(cursor.getLong(cursor.getColumnIndex(COLUMN_CONFIG_STUDYID)));
                config.setPointsQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_CONFIG_TOTALPOINTS)));
                config.setExtremePointsFilter(cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_EXTREMEPOINTSFILTER)));

                //set constantAlgo values
                float extremePhaseMinLength = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_EXTREMEPAHSE_MINLENGTH));
                float extremePhaseDelta = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_EXTREMEPAHSE_DELTA));
                float extremePhaseTolerance = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_EXTREMEPAHSE_TOLERANCE));
                config.configureExtremePhaseParams(extremePhaseMinLength, extremePhaseDelta, extremePhaseTolerance);

                //set nullphasesAlgo values
                float constantPhaseMinLength = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_CONSTANTPHASE_MINLENGTH));
                float constantPhaseDelta = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_CONSTANTPHASE_DELTA));
                float constantPhaseTolerance = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_CONSTANTPHASE_TOLERANCE));
                float constantPhaseDeviation = cursor.getFloat(cursor.getColumnIndex(COLUMN_CONFIG_CONSTANTPHASE_DEVIATION));
                config.configureConstantPhaseParams(constantPhaseMinLength, constantPhaseDelta, constantPhaseTolerance, constantPhaseDeviation);

            } while (cursor.moveToNext());
        }

        return config;
    }

    public void addConfigToStudy(Configuration configuration, Study study){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONFIG_TOTALPOINTS, configuration.getPointsQuantity());
        values.put(COLUMN_CONFIG_EXTREMEPOINTSFILTER, configuration.getExtremePointsFilter());
        values.put(COLUMN_CONFIG_EXTREMEPAHSE_MINLENGTH, configuration.getExtremePhaseMinLength());
        values.put(COLUMN_CONFIG_EXTREMEPAHSE_DELTA, configuration.getExtremePhaseDelta());
        values.put(COLUMN_CONFIG_EXTREMEPAHSE_TOLERANCE, configuration.getExtremePhaseGradientTolerance());
        values.put(COLUMN_CONFIG_CONSTANTPHASE_MINLENGTH, configuration.getConstantPhaseMinLength());
        values.put(COLUMN_CONFIG_CONSTANTPHASE_DELTA, configuration.getConstantPhaseDelta());
        values.put(COLUMN_CONFIG_CONSTANTPHASE_TOLERANCE, configuration.getConstantPhaseGradientTolerance());
        values.put(COLUMN_CONFIG_CONSTANTPHASE_DEVIATION, configuration.getConstantPhaseDeviation());
        values.put(COLUMN_CONFIG_STUDYID, study.getId());
        db.insert(TABLE_NAME_CONFIG, null, values);

        db.close();

    }

    public void updateConfigForStudy(Configuration config, Study study){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_CONFIG +
                " SET " + COLUMN_CONFIG_TOTALPOINTS + " = " + config.getPointsQuantity() +", " +
                COLUMN_CONFIG_EXTREMEPAHSE_MINLENGTH + " = " + config.getExtremePhaseMinLength() +", " +
                COLUMN_CONFIG_EXTREMEPOINTSFILTER + " = " + config.getExtremePointsFilter() +", " +
                COLUMN_CONFIG_EXTREMEPAHSE_DELTA + " = " + config.getExtremePhaseDelta() +", " +
                COLUMN_CONFIG_EXTREMEPAHSE_TOLERANCE + " = " + config.getExtremePhaseGradientTolerance() +", " +
                COLUMN_CONFIG_CONSTANTPHASE_MINLENGTH + " = " + config.getConstantPhaseMinLength() +", " +
                COLUMN_CONFIG_CONSTANTPHASE_DELTA + " = " + config.getConstantPhaseDelta() +", " +
                COLUMN_CONFIG_CONSTANTPHASE_TOLERANCE + " = " + config.getConstantPhaseGradientTolerance() +", " +
                COLUMN_CONFIG_CONSTANTPHASE_DEVIATION + " = " + config.getConstantPhaseDeviation() +
                " WHERE " + COLUMN_CONFIG_STUDYID + " = " + study.getId();


        db.execSQL(query);

    }




    /**------------------------  STUDY EVENTS TABLE ACTIONS  ------------------------**/

    public void addEventToStudy(Study study, StudyEvent event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_TIME, event.getTime());
        values.put(COLUMN_EVENT_STUDY_ID, study.getId());
        event.setId(db.insert(TABLE_NAME_EVENTS, null, values));
        study.addEvent(event);
        db.close();
    }


    public void deleteEventForStudy(long event_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query="DELETE FROM "+ TABLE_NAME_EVENTS +
                " WHERE " + COLUMN_EVENT_ID + " = " + event_id;
        db.execSQL(query);
        db.close();
    }

    public List<StudyEvent> getEventsForStudy(long study_id) {
        String query = "SELECT * FROM " + TABLE_NAME_EVENTS +
                " WHERE " + COLUMN_EVENT_STUDY_ID + " = " +
                study_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<StudyEvent> events = new ArrayList<StudyEvent>();
        StudyEvent studyEvent;
        if (cursor.moveToFirst()) {
            do {
                studyEvent = new StudyEvent();
                studyEvent.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_EVENT_ID)));
                studyEvent.setTime(cursor.getInt(cursor.getColumnIndex(COLUMN_EVENT_TIME)));
                studyEvent.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_NAME)));
                studyEvent.setStudy_id(cursor.getLong(cursor.getColumnIndex(COLUMN_EVENT_STUDY_ID)));
                events.add(studyEvent);
            } while (cursor.moveToNext());
        }
        return events;

    }


    /**------------------------  ANALYSIS TABLE ACTIONS  ------------------------**/
    public void saveAnalysis(long study_id, long participant_id, GraphAnalysisResult graphAnalysisResult){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ANALYSIS_AVERAGEPRESENCE, convertToDouble(graphAnalysisResult.getAveragePresence()));
        values.put(COLUMN_ANALYSIS_VIRTUALPRESENCESHARE, convertToDouble(graphAnalysisResult.getVirtualPresenceShare()));
        values.put(COLUMN_ANALYSIS_REALPRESENCESHARE, convertToDouble(graphAnalysisResult.getRealPresenceShare()));
        values.put(COLUMN_ANALYSIS_PHASE1SHARE, convertToDouble(graphAnalysisResult.getP1Share()));
        values.put(COLUMN_ANALYSIS_PHASE2SHARE, convertToDouble(graphAnalysisResult.getP2Share()));
        values.put(COLUMN_ANALYSIS_PHASE3SHARE, convertToDouble(graphAnalysisResult.getP3Share()));
        values.put(COLUMN_ANALYSIS_PHASE1GRADIENT, convertToDouble(graphAnalysisResult.getP1Gradient()));
        values.put(COLUMN_ANALYSIS_PHASE2GRADIENT, convertToDouble(graphAnalysisResult.getP2Gradient()));
        values.put(COLUMN_ANALYSIS_PHASE3GRADIENT, convertToDouble(graphAnalysisResult.getP3Gradient()));
        values.put(COLUMN_ANALYSIS_PHASE1PRESENCE, convertToDouble(graphAnalysisResult.getP1Presence()));
        values.put(COLUMN_ANALYSIS_PHASE2PRESENCE, convertToDouble(graphAnalysisResult.getP2Presence()));
        values.put(COLUMN_ANALYSIS_PHASE3PRESENCE, convertToDouble(graphAnalysisResult.getP3Presence()));
        values.put(COLUMN_ANALYSIS_PHASE1ANGLE, convertToDouble(graphAnalysisResult.getP1Angle()));
        values.put(COLUMN_ANALYSIS_PHASE2ANGLE, convertToDouble(graphAnalysisResult.getP2Angle()));
        values.put(COLUMN_ANALYSIS_PHASE3ANGLE, convertToDouble(graphAnalysisResult.getP3Angle()));
        values.put(COLUMN_ANALYSIS_PHASE1DURATION, graphAnalysisResult.getP1duration());
        values.put(COLUMN_ANALYSIS_PHASE2DURATION, graphAnalysisResult.getP2duration());
        values.put(COLUMN_ANALYSIS_PHASE3DURATION, graphAnalysisResult.getP3duration());
        values.put(COLUMN_ANALYSIS_MAX_PRESENCE, convertToDouble(graphAnalysisResult.getMax_presence()));
        values.put(COLUMN_ANALYSIS_BREAKS_IN_PRESENCE, graphAnalysisResult.getBreaksInPresence());
        values.put(COLUMN_ANALYSIS_STUDY_ID, study_id);
        values.put(COLUMN_ANALYSIS_PARTICIPANT_ID, participant_id);
        long analysis_id = db.insert(TABLE_NAME_ANALYSIS, null, values);

        for(RecoveryPhase phase: graphAnalysisResult.getRecoveryPhases()){
            ContentValues phaseValues = new ContentValues();
            phaseValues.put(COLUMN_RECOVERYPHASES_START_INDEX, phase.getStartIndex());
            phaseValues.put(COLUMN_RECOVERYPHASES_END_INDEX, phase.getEndIndex());
            phaseValues.put(COLUMN_RECOVERYPHASES_START_TIME, phase.getStartTime());
            phaseValues.put(COLUMN_RECOVERYPHASES_END_TIME, phase.getEndTime());
            phaseValues.put(COLUMN_RECOVERYPHASES_DURATION, phase.getRecoveryTime());
            phaseValues.put(COLUMN_RECOVERYPHASES_MIN, phase.getMinPresence());
            phaseValues.put(COLUMN_RECOVERYPHASES_SHARE, phase.getRecoveryShare());
            phaseValues.put(COLUMN_RECOVERYPHASES_ANALYSIS_ID, analysis_id);
            db.insert(TABLE_NAME_RECOVERY_PHASES,null, phaseValues);
        }


        db.close();
    }

    public int getAnalysisID(int participant_id){
        int graphAnalysisID = 0;

        String query = "SELECT * FROM " + TABLE_NAME_ANALYSIS +
                " WHERE " + COLUMN_ANALYSIS_PARTICIPANT_ID +
                " = " + participant_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do {
                graphAnalysisID = cursor.getInt(cursor.getColumnIndex(COLUMN_ANALYSIS_ID));
            } while (cursor.moveToNext());
        }

        return graphAnalysisID;
    }





    /**------------------------ POINTS V2 TABLE ACTIONS  ------------------------**/

    public ArrayList<GraphPoint> getPointsForParticipantDetail(long participant_id, long study_id){
        ArrayList<GraphPoint> returnList = new ArrayList<GraphPoint>();
        String query = "SELECT * FROM " + TABLE_NAME_POINTS +
                " WHERE " + COLUMN_POINTS_DETAIL_PARTICIPANTID
                + " = " + participant_id + " AND " + COLUMN_POINTS_DETAIL_STUDYID
                + " = " + study_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        GraphPoint graphPoint;

        if(cursor.moveToFirst()){
            do {
                graphPoint = new GraphPoint();
                graphPoint.setIndex(cursor.getInt(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_INDEX)));
                graphPoint.setX(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_X)));
                graphPoint.setY(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_Y)));
                graphPoint.setScreenX(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_SCREENX)));
                graphPoint.setScreenY(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_SCREENY)));
                graphPoint.setFirstDerivate(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_FIRSTDERIVATE)));
                graphPoint.setSecondDerivate(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_SECONDDERIVATE)));
                graphPoint.setThirdDerivate(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_THIRDDERIVATE)));
                graphPoint.setExtremePoint(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_EXTREMEPOINT)) == 1 ? true : false);
                graphPoint.setHp(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_HIGH)) == 1 ? true : false);
                graphPoint.setTp(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_LOW)) == 1 ? true : false);
                graphPoint.setPartOfConstantPhase(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_EXTREMEPHASE)) == 1 ? true : false);
                graphPoint.setPartOfExtremePhase(cursor.getLong(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_CONSTANTPHASE)) == 1 ? true : false);
                graphPoint.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_COMMENT)));
                graphPoint.setPhase(cursor.getInt(cursor.getColumnIndex(COLUMN_POINTS_DETAIL_PHASE)));
                returnList.add(graphPoint);

            } while (cursor.moveToNext());



        }
        return returnList;
    }

    public ArrayList<GraphPoint> getPointsForParticipant(long study_id, long participant_id){

        String query = "SELECT * FROM " + TABLE_NAME_POINTS2 +
                " WHERE " + COLUMN_POINTS_STUDYID + " = " + study_id +
                " AND " + COLUMN_POINTS_PARTICIPANT_ID + " = " + participant_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String array = new String();

        if (cursor.moveToFirst()) {
            do {
                array = cursor.getString(cursor.getColumnIndex(COLUMN_POINTS_POINTS));
            } while (cursor.moveToNext());
        }


        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<GraphPoint>>() {}.getType();
        ArrayList<GraphPoint> outputString = gson.fromJson(array, type);
        return outputString;
    }

    public void savePoints(ArrayList<GraphPoint> points, long participant_id, long study_id){
        Gson gson = new Gson();
        String inputString = gson.toJson(points);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POINTS_PARTICIPANT_ID, participant_id);
        values.put(COLUMN_POINTS_STUDYID, study_id);
        values.put(COLUMN_POINTS_POINTS, inputString);

        db.insert(TABLE_NAME_POINTS2, null, values);
        db.close();
    }

    double convertToDouble(Number value)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.#######");
        return Double.valueOf(twoDForm.format(value));
    }



}
