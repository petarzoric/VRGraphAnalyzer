package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Model.GraphPoint;
import com.petarzoric.vr_feedback.R;

import java.util.ArrayList;
import java.util.List;

/**
 * NOT USED ANYMORE
 *
 * This was my first approach to implement the possibility to comment.
 * Was replaced by the possibility to just tap on the specific phase or point, but the pager itself
 * looks still pretty cool.
 * After the user submitted his graph it popped up under the PaintView.
 * The first page displays a table with all the extreme points and a column for the comment.
 * Second page showed extreme phases, third showed constant phases.
 * The plan was to highlight the specific point/phase on the paintview whenever
 * the focus was on the column for the specific point(not implemented).
 * Didn't work out so well as the keyboard overlapped it (that's why I decided to look for another solution).
 * But in case someone wants to reuse and/or improve it(maybe in another Activity?), I'll let it
 * in the files.
 * Credits: www.youtube.com/watch?v=fgaj-aai90I
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    Context context;
    List<PagerModel> pagesList;
    LayoutInflater inflater;
    RecyclerView recyclerViewExtremePoints;

    public PagerAdapter(Context context, List<PagerModel> pagesList) {
        this.context = context;
        this.pagesList = pagesList;

        inflater = ((Activity) context).getLayoutInflater();
    }


    @Override
    public int getCount() {
        return pagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        switch (pagesList.get(position).getType()){
            case "extremePoints": case "generalFeedback":
                View view = inflater.inflate(R.layout.pager_extremephases, container, false);

                TextView extremePagerText =  (TextView) view.findViewById(R.id.pagerExtremePhase);

                view.setTag(position);

                ((ViewPager) container).addView(view);
                PagerModel model = pagesList.get(position);
                extremePagerText.setText(model.getTitle());
                return view;

            case "extremePhases":
                View view2 = inflater.inflate(R.layout.pager_extremepointscomments, container, false);
                recyclerViewExtremePoints = view2.findViewById(R.id.extremePointsRecyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerViewExtremePoints.setLayoutManager(layoutManager);
                populateExtremePhasesRecycler(pagesList.get(position).getCreatedPoints());


                view2.setTag(position);

                ((ViewPager) container).addView(view2);


                return view2;

        }

        View view = inflater.inflate(R.layout.pager_extremepointscomments, container, false);

        TextView extremePagerText =  (TextView) view.findViewById(R.id.pagerExtreme);

        view.setTag(position);

        ((ViewPager) container).addView(view);
        PagerModel model = pagesList.get(position);
        extremePagerText.setText(model.getTitle());
        return view;





    }


    public void populateExtremePhasesRecycler(List<GraphPoint> generatedPoints){
        //get the List as input for the actual model
        List<GraphPoint> returnList = new ArrayList<GraphPoint>();
        for(GraphPoint p: generatedPoints){
            if(p.isHp() || p.isTp()){
                returnList.add(p);
            }
        }
        ExtremePointsPagerAdapter adapter = new ExtremePointsPagerAdapter(returnList, context, recyclerViewExtremePoints);
        recyclerViewExtremePoints.setAdapter(adapter);


    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }



}
