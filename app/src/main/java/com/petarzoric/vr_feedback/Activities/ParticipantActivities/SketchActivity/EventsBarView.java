package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.petarzoric.vr_feedback.Model.StudyEvent;
import com.petarzoric.vr_feedback.R;

import java.util.List;

import static com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.DrawingArea.BRUSH_SIZE;
import static com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity.DrawingArea.DEFAULT_COLOR;


/**
 * A view that displays all the events of a study under the {@link DrawingArea}
 * Currenlty not due to space issues
 */
public class EventsBarView extends View {
    private Paint mPaint;
    private int width;
    private int height;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int currentColor;
    private int strokeWidth;
    private List<StudyEvent> events;
    private int studyDuration;

    public EventsBarView(Context context, AttributeSet attrs) {
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

        invalidate();

    }

    public EventsBarView(Context context) {
        this(context, null);
    }




    public void initScreen(int height, int width) {
        this.width = width;
        this.height = height;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        int canvasHeight = mCanvas.getHeight()/2;
        int canvasWidth = mCanvas.getWidth();
        Drawable d = getResources().getDrawable(R.drawable.drawing1);
        d.setBounds(0, 0, canvasWidth, canvasHeight);
        mBitmap.eraseColor(Color.RED);

        d.draw(mCanvas);
      //  mBitmap.eraseColor(Color.RED);

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(20);
        mCanvas.save();
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

    }

    public void setEvents(List<StudyEvent> events){
        this.events = events;
    }
    public void setStudyDuration(int duration){
        this.studyDuration = duration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        if(events.size() == 0){
            canvas.drawText("currently no events added", mCanvas.getWidth()/2 - 40, mCanvas.getHeight()/2, mPaint);
        }

        for(StudyEvent event: events){
            double factor = (double) event.getTime() /  (double) studyDuration;
            double x = canvas.getWidth() * factor;
            int x_position =  (int) Math.round(x);
            float xStart = (float) x_position;
            canvas.drawLine(xStart, 30f, xStart, 70f, paint);
            canvas.drawText(event.getName(), x_position, 100, paint);
        }
    }
}
