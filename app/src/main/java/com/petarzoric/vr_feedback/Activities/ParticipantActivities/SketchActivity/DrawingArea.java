package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.petarzoric.vr_feedback.Model.Configuration;
import com.petarzoric.vr_feedback.Model.GraphAnalysisResult;
import com.petarzoric.vr_feedback.Model.GraphPhase;
import com.petarzoric.vr_feedback.Model.GraphPoint;
import com.petarzoric.vr_feedback.Model.Location;
import com.petarzoric.vr_feedback.Model.RecoveryPhase;
import com.petarzoric.vr_feedback.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
    This class does basically everything involved in the quantification and analysis process.
    It waits for user input, takes the path of the graph and creates a specific amount of points,
    which represent the sketch.
    It analyzes the sketch, waits for user comments and saves the results in the database
 */


public class DrawingArea extends View {

    public static int BRUSH_SIZE = 5;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();

    private Paint pointerPaint;
    private CanvasPointer canvasPointer;
    private boolean dragged = false;
    private boolean sketchFinished = false;
    private boolean arrivedInVR = false;
    private Bitmap mBitmap;
    private int width;
    private int extentions = 0;
    private Dialog popupDialog;
    private int height;
    private List<RecoveryPhase> breaksInPresence;
    private Path currentPath;
    private List<GraphPoint> createdPoints;
    private List<GraphPoint> extremePointsList = new ArrayList<GraphPoint>();
    private List<GraphPhase> constPhases = new ArrayList<>();
    private List <GraphPhase> graphPhasesForComments = new ArrayList<>();
    private List <GraphPoint> phase2Points = new ArrayList<>();
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<GraphPoint> graphPoints = new ArrayList<GraphPoint>();
    private GraphAnalysisResult graphAnalysisResult;


    private Configuration configuration;
    private HashMap<GraphPoint, Dialog> dialogMap;



    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }


    public DrawingArea(Context context) {
        this(context, null);
    }


    public DrawingArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
        mPath = new Path();
        dialogMap = new HashMap<GraphPoint, Dialog>();
        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setColor(Color.BLUE);
        pointerPaint.setStyle(Paint.Style.FILL);
        popupDialog = new Dialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar);
        popupDialog.getWindow().setContentView(R.layout.comment_popup);

    }


    /**
     *  This method is called by {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.SketchActivity}
     *  As the height and width of this view are defined using relative values,
     *  the absolute values have to be calculated on runtime.
     *  {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.SketchActivity} calculates them
     *  and passes them.
     *  Otherwise this method creates a transparent bitmap to draw on.
     */
    public void initScreen(int height, int width) {
        this.width = width;
        this.height = height;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        int canvasHeight = mCanvas.getHeight();
        int canvasWidth = mCanvas.getWidth();
        mPaint.setColor(Color.BLUE);
        canvasPointer = new CanvasPointer(10, mCanvas.getHeight()-10, 15);
        canvasPointer.getLastLocations().add(new Location(10,mCanvas.getHeight()-10 ));
        this.setBackgroundColor(Color.TRANSPARENT);
        mCanvas.save();
    }


    /**
     * Draws the current path on the bitmap.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        mPaint.setMaskFilter(null);
        FingerPath currentPath = new FingerPath(new Path());
        if(paths.size() > 0) {
            mPath = paths.get(paths.size()-1).path;
        }

        mCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawCircle(canvasPointer.getX(), canvasPointer.getY(), canvasPointer.getRadius(), pointerPaint);
        canvas.restore();
    }


    /**
     * This method checks if the user has actually dragged the CanvasPointer.
     * If yes it returns true, otherwise false.
     */
    boolean checkDrag(float x, float y){

        boolean returnValue = false;
        if((canvasPointer.getX() - canvasPointer.getRadius()*2.5 < x && canvasPointer.getX() + canvasPointer.getRadius()*2.5 > x)
                && (canvasPointer.getY() - canvasPointer.getRadius()*2.5 < y && canvasPointer.getY() + canvasPointer.getRadius()*2.5 > y)
                ){
            returnValue = true;
            dragged = true;

        } else {
            returnValue = false;
        }
        return returnValue;

    }


    /**
     * Listens for touch event and calls specific method based on the type of the touch event.
     * It passes the coordinates to the called methods.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if(sketchFinished){
                    checkForCommentInteraction(x, y);
                } else {
                    touchStart(x, y);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }


    /**
     * Just a helper, that returns the context.
     */
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }


    /**
     * After graph has been analyzed (on math basis), the canvas needs to listen for comment interactions.
     * Called by {@link #onDraw(Canvas)} if the sketching process is already done.
     * Checks if the touch event hits any of the marked points or phases.
     * In case it does it opens a dialog which provides the opportunity to comment on the specific point or phase.
     */
    public void checkForCommentInteraction(float x, float y){
        //checks for each extrempoint if it was hit by the user
        for(final GraphPoint fp: extremePointsList){
            if((fp.getScreenX() - canvasPointer.getRadius() < x && fp.getScreenX() + canvasPointer.getRadius() > x)
                    && (fp.getScreenY() - canvasPointer.getRadius() < y && fp.getScreenY() + canvasPointer.getRadius() > y)
                    ){

                popupDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                TextView submitButton = popupDialog.findViewById(R.id.saveTextViewCommentPopup);
                TextView close = popupDialog.findViewById(R.id.closeTextViewCommentPopup);
                final EditText commentEditText = popupDialog.findViewById(R.id.commentInputEditText);
                commentEditText.setText(fp.getComment().toString());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         popupDialog.dismiss();
                         popupDialog.cancel();
                    }
                });

                submitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = commentEditText.getText().toString();
                        fp.setComment(comment);
                        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                        popupDialog.dismiss();
                        highlightPoint(fp);
                    }
                });
                popupDialog.show();
            }
        }

        //Checks for each phase if it was hit by the user
        for( final GraphPhase phase: this.graphPhasesForComments){
            if(this.checkIfClickedOnPhase(x,y, phase)){
                TextView submitButton = popupDialog.findViewById(R.id.saveTextViewCommentPopup);
                TextView close = popupDialog.findViewById(R.id.closeTextViewCommentPopup);
                final EditText commentEditText = popupDialog.findViewById(R.id.commentInputEditText);
                commentEditText.setText(phase.getComment().toString());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupDialog.dismiss();
                        popupDialog.cancel();
                    }
                });

                submitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = commentEditText.getText().toString();
                        phase.setComment(comment);
                        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                        highlightPhase(phase);
                        popupDialog.dismiss();
                    }
                });
                popupDialog.show();
            }
        }




        /**
         * Called by {@link #checkForCommentInteraction(float, float)} if the user decides to comment on a phase.
         * It highlights the phase on the canvas to show the user that this phase already has been commented.
         */
    }
    public void highlightPhase(GraphPhase phase){
        Paint checkedPaint = new Paint();
        checkedPaint.setStyle(Paint.Style.STROKE);
        checkedPaint.setColor(Color.GREEN);
        checkedPaint.setStrokeWidth(6);

        for(GraphPoint point: phase.getPoints()){

                mCanvas.drawCircle(point.getScreenX(), point.getScreenY(), convertDpToPixel(8, getContext()), checkedPaint);
        }
        invalidate();
    }


    /**
     * Called by {@link #checkForCommentInteraction(float, float)} if the user decides to comment on a point.
     * It highlights the point on the canvas to show the user that this point already has been commented.
     */

    public void highlightPoint(GraphPoint fp){
        Paint checkedPaint = new Paint();
        checkedPaint.setStyle(Paint.Style.STROKE);
        checkedPaint.setColor(Color.GREEN);
        checkedPaint.setStrokeWidth(6);
        mCanvas.drawCircle(fp.getScreenX(), fp.getScreenY(), convertDpToPixel(10, getContext()), checkedPaint);
        invalidate();
    }


    /**
     * Helper which is called by {@link #checkForCommentInteraction(float, float)}.
     * Gets the coordinates of the touch event and a phase.
     * Checks if the touch event has hit this specific phases and returns true or false.
     */
    public boolean checkIfClickedOnPhase(float x, float y, GraphPhase cf){
        boolean returnValue = false;
        for(GraphPoint point: cf.getPoints()){
            if((point.getScreenX() - canvasPointer.getRadius() < x && point.getScreenX() + canvasPointer.getRadius() > x)
                    && (point.getScreenY() - canvasPointer.getRadius() < y && point.getScreenY() + canvasPointer.getRadius() > y)
                    ){
                return true;
            }
        }
        return returnValue;
    }


    /**
     * Clears the canvas.
     */
    public void clearCanvas(){
        mPath.reset();
        mPath = new Path();
        paths.clear();
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvasPointer.setX(10);
        canvasPointer.setY(mCanvas.getHeight()-10);
        mPath.set(new Path());
        mPath.moveTo(0,mCanvas.getHeight());
        invalidate();
    }

    public void undo(){
        if(extentions > 0){extentions--;}

        if(paths.size() > 0){
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvasPointer.setLocation(canvasPointer.getLastLocations().get(canvasPointer.getLastLocations().size()  - extentions));
            invalidate();
        }
    }


    /**
     * Called by {@link #onTouchEvent(MotionEvent)} in case the event was a touch up event.
     * If CanvasPointer hasn't been hit it shows a toast and doesn't start drawing.
     */
    private void touchStart(float x, float y) {

        if(checkDrag(x, y)){
            if(!paths.isEmpty()){

                mPath.lineTo(x, y);
            } else {
                mPath.moveTo(0, mCanvas.getHeight());
                mPath.lineTo(x,y);

            }

            Path myPath = new Path(mPath);

            mX = x;
            mY = y;

        } else {
            Toast.makeText(getContext(), "Please use the blue pointer in order to draw.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Called by {@link #onTouchEvent(MotionEvent)} in case the event was a touch move event.
     * Draws the path and checks if there is an collision with the edges of the graph template.
     * Moves the CanvasPointer with the path.
     */
    private void touchMove(float x, float y) {
        if(dragged){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if(x<=0 || y<= 0 || x >= mCanvas.getWidth() || y >= mCanvas.getHeight()){
               // touchUp();
                return;
            }

            canvasPointer.setX(x);
            canvasPointer.setY(y);

            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
    }


    /**
     * Called by {@link #onTouchEvent(MotionEvent)} in case the event was a touch up event.
     * Saves the path.
     */
    private void touchUp() {
       if(dragged){
           mPath.lineTo(mX, mY);
           canvasPointer.getLastLocations().add(new Location(mX, mY));
           extentions++;
       }

        currentPath = new Path(mPath);
        paths.add(new FingerPath(currentPath));
        dragged=false;


    }


    /**
     * Called by {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.SketchActivity} if the user
     * submits his sketch.
     * It subdivides the path into a specific amount(determined by the configuration) of points.
     * Then it translates the screen coordinates of each point into relative coordinates using [0,6] for x Values
     * and [-1,1] for y values.
     * Analyses the graph on math basis.
     */
    public ArrayList<GraphPoint> analyzeGraph(int duration){

        Paint newPaint = new Paint();
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setColor(Color.BLACK);
        newPaint.setStrokeWidth(8);

        //Step1: subdivide graph into points
        ArrayList<GraphPoint> graphPoints = quantifyGraph();

        //Step 2: translation of the coordinates
        for(GraphPoint point: graphPoints){
            point.setIndex(graphPoints.indexOf(point));
            translateCoordinates(point);
            //mCanvas.drawCircle(point.screenX, point.screenY, 5, newPaint);
        }
        createdPoints=graphPoints;
        this.graphPoints = new ArrayList<GraphPoint>(graphPoints);

        //Step 3: analysis math based
        analyzeGraphOnMathBasis(graphPoints);

        //Step4: further analyis, vr context based
        analyzeGraphVRContext(graphPoints, duration);


        invalidate();
        return graphPoints;

    }


    /**
     * Takes the path of the graph and subdivides it into points.
     * The amount of the points is based on the configuration
     */
    public ArrayList<GraphPoint> quantifyGraph(){
        int amount = configuration.getPointsQuantity();
        ArrayList<GraphPoint> graphPoints = new ArrayList<GraphPoint>();
        Path myPath = new Path(mPath);

        PathMeasure pm = new PathMeasure(mPath, false);
        float length = pm.getLength();
        float distance = 0f;
        float speed = length / amount;
        int counter = 0;
        float[] aCoordinates = new float[2];

        while ((distance < length) && (counter < amount)) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null);
            graphPoints.add(new GraphPoint(aCoordinates[0],
                    aCoordinates[1], counter+1));

            counter++;
            distance = distance + speed;
        }
        return graphPoints;
    }


    /**
     * Takes a Graphpoint and translates the screen coordinates to relative coordinates.
     * X-Values: [0, 6]
     * Y-Values: [-1, 1]
     */
    public void translateCoordinates(GraphPoint point){
        point.setX(( point.getScreenX() / this.getWidth())*6 );
        point.setY(-2f*( point.getScreenY() / this.getHeight()) + 1f);
    }


    /**
     * Finds interesting parts of the graph by calculating extremepoints as well as
     * constant and extreme phases.
     */
    public void analyzeGraphOnMathBasis( ArrayList<GraphPoint> graphPoints){
        initDerivatves(graphPoints);
        //initGlobaleMaxUndMin(graphPoints);
        //initLokaleMaxUndMin(graphPoints);
        findExtremePoints(graphPoints);
        findConstantPhases(graphPoints);
        findExtremePhases(graphPoints);

        drawAnalysisResults(graphPoints);
        // drawIdk(phase2List, "#FFFFFF");
        sketchFinished=true;
        createdPoints=graphPoints;
        for(GraphPoint fp: createdPoints){
            if(fp.isTp() || fp.isHp()){
                // createDialog(fp);
                extremePointsList.add(fp);
            }
        }
    }

    /**
     * Uses the results of the previous steps to analyze the graph again,
     * but this time vr context based.
     */
    public void analyzeGraphVRContext(ArrayList<GraphPoint> graphPoints, int duration){
        graphAnalysisResult = new GraphAnalysisResult();
        initPhases(graphPoints, 0);
        calculatePresenceShares(graphPoints, duration);
        findRecoveryPhases(phase2Points, duration);

    }

    public void findRecoveryPhases(List<GraphPoint> phase2Points, int duration){
       boolean presenceBreak = false;
       GraphPoint start = new GraphPoint(-5,-5);
       GraphPoint end = new GraphPoint(-5,-5);

       ArrayList<RecoveryPhase> recoveryPhases = new ArrayList<>();

           for(GraphPoint point: phase2Points){
               if(!presenceBreak && point.getY() < 0){
                       start = point;
                       presenceBreak = true;
                   }
                   if(presenceBreak && point.getY() > 0){
                       end = point;
                       presenceBreak=false;
                       recoveryPhases.add(new RecoveryPhase(start, end));
                   }


           }

           for(RecoveryPhase phase: recoveryPhases){


               double min = 0;
               for(GraphPoint p:
                       createdPoints.subList(phase.getDiveIntoReal().getIndex(),
                               phase.getDiveIntoVirtual().getIndex())){
                   p.setPartOfRecoveryPhase(true);
                   if(p.getY() < min){

                       min = p.getY();
                   }
               }
               phase.setMinPresence(min);

               int length = phase.getEndIndex() -  phase.getStartIndex();
               double share = (double)length / (double)configuration.getPointsQuantity();

               int startTime = (int)(((double)phase.getStartIndex() / (double) configuration.getPointsQuantity())*(double)duration);
               int endTime = (int)(((double)phase.getEndIndex() / (double) configuration.getPointsQuantity())*(double)duration);
               int time = endTime - startTime;

               phase.setStartTime(startTime);
               phase.setEndTime(endTime);
               phase.setRecoveryTime(time);
               phase.setRecoveryShare(share);

           }
           breaksInPresence = recoveryPhases;
           graphAnalysisResult.setRecoveryPhases(recoveryPhases);
          // highlightRecoveryPhases(phase2Points);
    }

    public void calculatePresenceShares(ArrayList<GraphPoint> graphPoints, int duration){
        float presence_sum = 0f;
        double pointsInVR = 0.0;
        float max_presence = -1f;
        int p1PointsAmount = 0;
        int p2PointsAmount = 0;
        int p3PointsAmount = 0;

        double p1PresenceSum=0.0;
        double p2PresenceSum=0.0;
        double p3PresenceSum=0.0;

        double p1GradientSum=0.0;
        double p2GradientSum=0.0;
        double p3GradientSum=0.0;

        for(GraphPoint point: graphPoints){

            switch(point.getPhase()){
                case 1:
                    p1PointsAmount++;
                    p1PresenceSum = p1PresenceSum + point.getY();
                    p1GradientSum = p1GradientSum + point.getFirstDerivate();
                    break;

                case 2:
                    p2PointsAmount++;
                    p2PresenceSum = p2PresenceSum + point.getY();
                    p2GradientSum = p2GradientSum + point.getFirstDerivate();
                    break;

                case 3:
                    p3PointsAmount++;
                    p3PresenceSum = p3PresenceSum + point.getY();
                    p3GradientSum = p3GradientSum + point.getFirstDerivate();
                    break;

                default:
                    break;
            }

            presence_sum = presence_sum + point.getY();

            if(point.getY() > 0.0f){
                pointsInVR = pointsInVR + 1.0;
            }

            if(point.getY() > max_presence){
                max_presence = point.getY();
            }
        }

        graphAnalysisResult.setMax_presence(max_presence);

        double virtualPresenceShare = pointsInVR / (double)configuration.getPointsQuantity();
        double realPresenceShare = 1 - virtualPresenceShare;

        graphAnalysisResult.setVirtualPresenceShare(virtualPresenceShare);
        graphAnalysisResult.setRealPresenceShare(realPresenceShare);

        Double averagePresence = (double) presence_sum / (double) configuration.getPointsQuantity();
        graphAnalysisResult.setAveragePresence(averagePresence);

        if(!arrivedInVR){
            graphAnalysisResult.setP1Share(1.0);
            graphAnalysisResult.setP1duration(duration);

            graphAnalysisResult.setP2Share(0.0);
            graphAnalysisResult.setP2duration(0);

            graphAnalysisResult.setP3Share(0.0);
            graphAnalysisResult.setP3duration(0);
        } else {
            double p1Share = (double) p1PointsAmount / (double) configuration.getPointsQuantity();
            double p3Share = (double) p3PointsAmount / (double) configuration.getPointsQuantity();
            double p2Share = 1.0 - (p1Share + p3Share);

            int p1Duration = (int)(duration * p1Share);
            int p3Duration = (int)(duration * p3Share);
            int p2Duration = duration - (p1Duration + p3Duration);

            graphAnalysisResult.setP1duration(p1Duration);
            graphAnalysisResult.setP1Share(p1Share);

            graphAnalysisResult.setP2duration(p2Duration);
            graphAnalysisResult.setP2Share(p2Share);

            graphAnalysisResult.setP3duration(p3Duration);
            graphAnalysisResult.setP3Share(p3Share);
        }
        double p1Presence = p1PresenceSum / (double)p1PointsAmount;
        double p2Presence = p2PresenceSum / (double)p2PointsAmount;
        double p3Presence = p3PresenceSum / (double)p3PointsAmount;

        double p1Gradient = p1GradientSum / (double)p1PointsAmount;
        double p2Gradient = p2GradientSum / (double)p2PointsAmount;
        double p3Gradient = p3GradientSum / (double)p3PointsAmount;

        graphAnalysisResult.setP1Presence(p1Presence);
        graphAnalysisResult.setP2Presence(p2Presence);
        graphAnalysisResult.setP3Presence(p3Presence);

        graphAnalysisResult.setP1Gradient(p1Gradient);
        graphAnalysisResult.setP2Gradient(p2Gradient);
        graphAnalysisResult.setP3Gradient(p3Gradient);



    }

    public void highlightPhases(ArrayList<GraphPoint> list){
        Paint newPaint = new Paint();
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setColor(Color.BLACK);
        newPaint.setStrokeWidth(3);
        initPhases(graphPoints, 0);
        for(GraphPoint point: list){
            switch(point.getPhase()){
                case 1:
                    newPaint.setColor(Color.BLACK);
                    mCanvas.drawCircle(point.getScreenX(),point.getScreenY(), convertDpToPixel(5, getContext()), newPaint);
                    break;

                case 2:
                    newPaint.setColor(Color.GREEN);
                    mCanvas.drawCircle(point.getScreenX(),point.getScreenY(), convertDpToPixel(5, getContext()), newPaint);
                    break;

                case 3:
                    newPaint.setColor(Color.BLUE);
                    mCanvas.drawCircle(point.getScreenX(),point.getScreenY(), convertDpToPixel(5, getContext()), newPaint);
                    break;
            }
        }
    }

    public void highlightRecoveryPhases(List<GraphPoint> list){
        Paint newPaint = new Paint();
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setColor(Color.YELLOW);
        newPaint.setStrokeWidth(3);
        for(GraphPoint point: list){
            if(point.isPartOfRecoveryPhase()){
                mCanvas.drawCircle(point.getScreenX(),point.getScreenY(), convertDpToPixel(5, getContext()), newPaint);
            }
        }
    }


    /**
     * Called by {@link #analyzeGraph()} in order to display the extremePoints and phases.
     */
    public void drawAnalysisResults(ArrayList<GraphPoint> list){

        Paint newPaint = new Paint();
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setColor(Color.BLACK);
        newPaint.setStrokeWidth(3);

        for(GraphPoint point: list){
            // show extremePoints
            if(point.isHp() || point.isTp()){
                newPaint.setColor(Color.BLACK);
                mCanvas.drawCircle(point.getScreenX(),point.getScreenY(), convertDpToPixel(10, getContext()), newPaint);
            }

            // show constant and extreme phases
            mPaint.setColor(Color.parseColor("#b94abf"));
            if(point.isPartOfExtremePhase()){

                mCanvas.drawCircle(point.getScreenX(), point.getScreenY(), convertDpToPixel(8, getContext()), mPaint);
            }

            mPaint.setColor(Color.parseColor("#2f76eb"));
            if(point.isPartOfConstantPhase()){

                mCanvas.drawCircle(point.getScreenX(), point.getScreenY(), convertDpToPixel(8, getContext()), mPaint);
            }
        }


    }

    /**
     * Calculates first, second and third derivate for all the graphpoints.
     */
    private void initDerivatves(ArrayList<GraphPoint> graphPoints){
        // first derivate
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            graphPoints.get(i).setFirstDerivate(graphPoints.get(i+1).getY() - graphPoints.get(i).getY());
        }

        // second derivate
        for(int j = 0; j<graphPoints.size()-1; j++){
            graphPoints.get(j).setSecondDerivate(graphPoints.get(j+1).getFirstDerivate() - graphPoints.get(j).getFirstDerivate());
        }

        // third derivate
        for(int k = 0; k <graphPoints.size()-1; k++){
            graphPoints.get(k).setThirdDerivate(graphPoints.get(k+1).getSecondDerivate() - graphPoints.get(k).getSecondDerivate());
        }

    }

    /**
     * Helps to display all the canvas objects uniformally on different displays.
     * Credits: Stackoverflow
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    /**
     * Determines for each point if it is part of phase 1, 2 or 3 (VR-Context).
     *
     */
    public void initPhases(ArrayList<GraphPoint> list, float tolerance){
        ArrayList<List<GraphPoint>> phases = new ArrayList<List<GraphPoint>>();
        int phase1endIndex = -1;
        int phase2endIndex = -1;
        boolean phase1Finished = false;
        boolean phase3Finished = false;
        // Find all points which are part of phase 1

        for(int i = 0; i< list.size()-1 && !phase1Finished; i++){
            GraphPoint current = list.get(i);
            float yVal = current.getY();
            if(yVal > tolerance && (current.isPartOfConstantPhase() || current.isHp())){
                phase1endIndex = i;
                phase1Finished = true;
            }
        }

        if(phase1endIndex==-1){
            Toast.makeText(getContext(), "index 1 == -1", Toast.LENGTH_LONG).show();
            for(GraphPoint p: list){
                p.setPhase(1);

            }

        } else {
            for(int i = list.size()-1; i >= phase1endIndex && !phase3Finished; i--){
                GraphPoint current = list.get(i);
                float yVal = current.getY();
                if( yVal > tolerance && (current.isHp() || current.isPartOfConstantPhase() ||
                        (current.isPartOfExtremePhase() && list.get(i-1).isPartOfExtremePhase()==false))){
                    phase2endIndex = i;
                    phase3Finished = true;
                }
            }


            if(phase1endIndex > phase2endIndex){
                Toast.makeText(getContext(), "index 1 > index 2 ", Toast.LENGTH_LONG).show();
               for(GraphPoint p: list){
                   p.setPhase(1);

               }
            } else {
                if(phase1endIndex == phase2endIndex){
                    List<GraphPoint> subListOne = list.subList(0, phase1endIndex);
                    List<GraphPoint> subListTwo = list.subList(phase1endIndex, phase2endIndex+1);
                    List<GraphPoint> subListThree = list.subList(phase1endIndex+1, configuration.getPointsQuantity());
                    for(GraphPoint p: subListOne){
                        p.setPhase(1);
                    }
                    for(GraphPoint p: subListTwo){
                        p.setPhase(2);
                    }
                    for(GraphPoint p: subListThree){
                        p.setPhase(3);
                    }
                    this.phase2Points = subListTwo;
                    arrivedInVR=true;

                } else {
                    List<GraphPoint> subListOne = list.subList(0, phase1endIndex);
                    List<GraphPoint> subListTwo = list.subList(phase1endIndex, phase2endIndex);
                    List<GraphPoint> subListThree = list.subList(phase2endIndex, configuration.getPointsQuantity());
                    for(GraphPoint p: subListOne){
                        p.setPhase(1);
                    }
                    for(GraphPoint p: subListTwo){
                        p.setPhase(2);
                    }
                    for(GraphPoint p: subListThree){
                        p.setPhase(3);
                    }
                    this.phase2Points = subListTwo;
                    arrivedInVR = true;
                }
            }


        }

    }


    /**
     * Finds the Global Max and Min.
     */
    private void initGlobaleMaxUndMin(ArrayList<GraphPoint> list){

        //globales Max ausfindig machen
        Collections.max(list, new Comparator<GraphPoint>() {
            @Override
            public int compare(GraphPoint o1, GraphPoint o2) {
                return Float.compare(o1.getY(), o2.getY());
            }
        }).setHp(true);

        //globales Min ausfindig machen
        Collections.min(list, new Comparator<GraphPoint>() {
            @Override
            public int compare(GraphPoint o1, GraphPoint o2) {
                return Float.compare(o1.getY(), o2.getY());
            }
        }).setTp(true);
    }


    /**
     * Finds constant phases based on the configuration
     */
    public List<GraphPhase> findConstantPhases(ArrayList<GraphPoint> list ){

        //get relevant config stuff
        int minLength = Math.round(configuration.getPointsQuantity() * configuration.getConstantPhaseMinLength());
        float delta = (float) (configuration.getConstantPhaseDelta() *( 200.0 / configuration.getPointsQuantity()));
        float tolerance = (float) (configuration.getConstantPhaseGradientTolerance() *(200.0 / configuration.getPointsQuantity()));
        double deviation = configuration.getConstantPhaseDeviation();

        List<GraphPhase> constantPhases = new ArrayList<GraphPhase>();
        int index = 0;
        while(index < list.size()-minLength){
            boolean isOk = true;
            ArrayList<GraphPoint> currentList = new ArrayList<GraphPoint>();
            currentList.add(list.get(index));
            float initValue = currentList.get(0).getFirstDerivate();

            while(isOk && index < list.size()){
                // GraphPoint current = list.get(index);
                GraphPoint toCompare = list.get(index);
                float difference = Math.abs(initValue - toCompare.getFirstDerivate());
                if(difference > delta){
                    index++;
                    isOk = false;

                    if(currentList.size() >= minLength){
                        /*
                         At this point we have basically found all phases with a similar first derivate.
                         Now we need to make sure that we only get all the constant phases.
                         */
                       double listSize = currentList.size();
                       double nullPoints = 0.0;
                       for(GraphPoint point: currentList){
                           if(Math.abs(point.getFirstDerivate()) < tolerance){
                                nullPoints = nullPoints +1.0;
                           }
                       }

                       Double gradientAccuracy = (nullPoints / listSize);
                       if(gradientAccuracy >= deviation){
                           for(GraphPoint p: currentList){
                               p.setHp(false);
                               p.setTp(false);
                               p.setExtremePoint(false);
                           }
                           GraphPhase currentPhase = new GraphPhase(currentList, 0);
                           constantPhases.add(currentPhase);
                           this.graphPhasesForComments.add(currentPhase);
                       }
                    }
                    //add points as long as they check the delta criterium
                } else {
                    index++;
                    currentList.add(toCompare);
                }
            }
        }
        constPhases = constantPhases;
        return constantPhases;
    }


    /**
     * Finds extreme phases based on the configuration
     */
    public List<GraphPhase> findExtremePhases(ArrayList<GraphPoint> list){

        int minLength = Math.round(configuration.getPointsQuantity() * configuration.getExtremePhaseMinLength());
        float delta = (float) (configuration.getExtremePhaseDelta() *( 200.0 / configuration.getPointsQuantity()));
        float tolerance = (float) (configuration.getExtremePhaseGradientTolerance() *(200.0 / configuration.getPointsQuantity()));

        ArrayList<GraphPhase> extPhases = new ArrayList<GraphPhase>();
        int index = 0;
        while(index < list.size()-minLength){
            boolean isOk = true;
            ArrayList<GraphPoint> currentList = new ArrayList<GraphPoint>();
            currentList.add(list.get(index));
            float initValue = currentList.get(0).getFirstDerivate();
            while(isOk && index < list.size()){
                // GraphPoint current = list.get(index);
                GraphPoint toCompare = list.get(index);
                float difference = Math.abs(initValue - toCompare.getFirstDerivate());
                if(difference > delta || toCompare.isTp()==true || toCompare.isHp() == true || toCompare.isPartOfExtremePhase()){
                    index++;
                    isOk = false;

                        if(currentList.size() >= minLength && Math.abs(currentList.get(0).getFirstDerivate()) > tolerance){
                            for(GraphPoint p: currentList){
                                p.setPartOfConstantPhase(false);
                            }
                            GraphPhase currentPhase = new GraphPhase(currentList, 1);
                            this.graphPhasesForComments.add(currentPhase);
                            extPhases.add(currentPhase);
                        }
                } else {
                    index++;
                    currentList.add(toCompare);
                }
            }



        }
        //approach to merge extreme phases
        /*
        if(graphPhasesForComments.size() > 1){
            ArrayList<GraphPhase> newConstantGradients = new ArrayList<GraphPhase>();
            for(int i =0; i< graphPhasesForComments.size()-1;i++){
                GraphPhase phase1 = graphPhasesForComments.get(i);
                GraphPhase phase2 = graphPhasesForComments.get(i+1);
                int deltaDistance = phase2.getStartIndex() -  phase1.getEndIndex();
                float deltaGradient = Math.abs(Math.abs(phase2.getAverageGradient()) -Math.abs(phase1.getAverageGradient()));
                if(deltaDistance < 20 && deltaGradient < 0.0015f){
                    ArrayList<GraphPoint> newPoints = new ArrayList<GraphPoint>();
                    for(int j = phase1.getStartIndex(); j<= phase2.getEndIndex(); j++){
                        newPoints.add(list.get(i));
                    }
                    GraphPhase mergedPhase = new GraphPhase(newPoints, 1);
                    newConstantGradients.add(mergedPhase);
                    i++;


                } else {
                    newConstantGradients.add(phase1);
                   //
                    if(i==list.size()-1){
                        newConstantGradients.add(phase2);
                    }
                }
            }
            graphPhasesForComments = newConstantGradients;
        }
        */
        for(GraphPhase p: extPhases){
            System.out.println("startindex: " + p.getStartIndex());
            System.out.println("endindex: " + p.getEndIndex());
            System.out.println("Winkel: " + p.getAngle());


        }

        return extPhases;
    }

    /**
     * Finds extreme points of the graph based on the configuration.
     */
    public void findExtremePoints(ArrayList<GraphPoint> list){
        int direction = 1;
        float lastYVal = 0;
        float extPointsFilter = configuration.getExtremePointsFilter();

        for(int i = 0; i<list.size() -1; i++){

            GraphPoint current = list.get(i);
            int currentSign = getSign(current.getFirstDerivate());
            if(currentSign + direction == 0 && currentSign != direction){
                //init vz-Wechsel
                if(currentSign > 0){
                    direction = currentSign;
                    if(current.getY() > lastYVal +extPointsFilter || current.getY() < lastYVal-extPointsFilter){
                        current.setTp(true);

                        lastYVal = current.getY();
                    }

                } else {
                    if(currentSign < 0){
                        direction = currentSign;

                        if(current.getY() > lastYVal +extPointsFilter || current.getY() < lastYVal-extPointsFilter){
                            current.setHp(true);

                            lastYVal = current.getY();

                        }
                    }
                }
            }

        }

    }

    /**
     * Helper for {@link #findExtremePoints(ArrayList)}
     */
    public int getSign(float val){
        int ret = -2;
        if(val == 0f){
            ret = 0;
        } else {
            if(val > 0){
                ret = 1;
            } else {
                if(val < 0){
                    ret = -1;
                }

            }
        }
        return ret;
    }

    public GraphAnalysisResult getGraphAnalysisResult(){
        return this.graphAnalysisResult;
    }



    /*
    public void checkWendeEigenschaft(GraphPoint point){
        if((point.getSecondDerivate() > 0 && point.getNext().getSecondDerivate() < 0 ) || (point.getSecondDerivate() < 0 && point.getNext().getSecondDerivate() > 0)){
            if(point.getSecondDerivate() == 0){
                point.setWende();

            } else {
                if(point.getNext().getSecondDerivate() == 0){
                    point.getNext().setWende();
                } else {
                    //idk
                }
                if(){

                }
            }
        }
    }
    */

      /*
    public void previousPath(){
        paths.remove(paths.size()-1);
        invalidate();

        int idk=0;
        for(FingerPath path: paths){

            Matrix matrix = new Matrix();
            matrix.setTranslate(10*idk,10*idk);
            path.path.transform(matrix);
            idk++;
            mCanvas.drawPath(path.path, mPaint);
        }
    }
    */

    public List<ArrayList<GraphPoint>> analyzeConstantPhasesv2(ArrayList<GraphPoint> list){
        //Array an Tupeln, wobei der erste Eintrag für den Startindex und der letzte für den Endindex stehen
        int minLength = Math.round(configuration.getPointsQuantity() * configuration.getExtremePhaseMinLength());
        float tolerance = (float) (configuration.getExtremePhaseDelta() *( 200.0 / configuration.getPointsQuantity()));
        ArrayList<ArrayList<GraphPoint>> konstanteGradients = new ArrayList<ArrayList<GraphPoint>>();
        int index = 0;
        while(index < list.size()-minLength){
            boolean isOk = true;
            ArrayList<GraphPoint> currentList = new ArrayList<GraphPoint>();
            currentList.add(list.get(index));
            float initValue = currentList.get(0).getFirstDerivate();
            while(isOk && index < list.size()){
                // GraphPoint current = list.get(index);
                GraphPoint toCompare = list.get(index);
                float difference = Math.abs(initValue - toCompare.getFirstDerivate());
                if(difference > tolerance){
                    index++;
                    isOk = false;

                    if(currentList.size() >= minLength){
                        for(GraphPoint p: currentList){
                            p.setPartOfConstantPhase(true);
                        }
                        konstanteGradients.add(currentList);
                    }
                } else {
                    index++;
                    currentList.add(toCompare);
                }
            }



        }

        return konstanteGradients;
    }

    public List<ArrayList<GraphPoint>> analyzeConstantphases(ArrayList<GraphPoint> list, int minLength, float tolerance, float extremeTolerance){
        //Array an Tupeln, wobei der erste Eintrag für den Startindex und der letzte für den Endindex stehen
        ArrayList<ArrayList<GraphPoint>> konstanteGradients = new ArrayList<ArrayList<GraphPoint>>();
        int index = 0;
        while(index < list.size()-minLength){
            boolean isOk = true;
            ArrayList<GraphPoint> currentList = new ArrayList<GraphPoint>();
            currentList.add(list.get(index));
            float initValue = currentList.get(0).getFirstDerivate();
            while(isOk && index < list.size()){
                // GraphPoint current = list.get(index);
                GraphPoint toCompare = list.get(index);
                float difference = Math.abs(initValue - toCompare.getFirstDerivate());
                if(difference > tolerance){
                    index++;
                    isOk = false;

                    if(currentList.size() >= minLength){
                        if(Math.abs(currentList.get(0).getFirstDerivate()) < extremeTolerance){
                            for(GraphPoint p: currentList){
                                p.setPartOfExtremePhase(true);
                            }
                        }

                        konstanteGradients.add(currentList);
                    }
                } else {
                    index++;
                    currentList.add(toCompare);
                }
            }



        }
        return konstanteGradients;
    }

    public ArrayList<GraphPhase> mergeConstantGradientsIfPossible(List<GraphPhase> gradients){
        ArrayList<GraphPhase> newConstantGradients = new ArrayList<GraphPhase>();
        for(int i =0; i< gradients.size()-1;i++){
            GraphPhase phase1 = gradients.get(i);
            GraphPhase phase2 = gradients.get(i+1);
            int deltaDistance = phase2.getStartIndex() -  phase1.getEndIndex();
            float deltaGradient = Math.abs(Math.abs(phase2.getAverageGradient()) -Math.abs(phase1.getAverageGradient()));
            if(deltaDistance < 10 && deltaGradient < 0.001f){

            } else {

            }
        }
        return newConstantGradients;

    }

    public void createDialog(GraphPoint graphPoint){
        popupDialog = new  Dialog(getContext());
        popupDialog.setContentView(R.layout.comment_popup);
        Button submitButton = popupDialog.findViewById(R.id.saveTextViewCommentPopup);
        TextView close = popupDialog.findViewById(R.id.closeTextViewCommentPopup);
        EditText mtrNr = popupDialog.findViewById(R.id.questiona);
        mtrNr.setText(graphPoint.getComment());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popupDialog.dismiss();

            }

        });
        //  dialogMap.put(graphPoint, dialog);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void drawIdk(List<GraphPoint> list, String color){
        for(GraphPoint point: list){
            mPaint.setColor(Color.parseColor(color));
            mCanvas.drawCircle(point.getScreenX(), point.getScreenY(), 10, mPaint );
        }
    }

}
