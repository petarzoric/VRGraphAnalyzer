package com.petarzoric.vr_feedback.Activities.ParticipantActivities.SketchActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.petarzoric.vr_feedback.Model.GraphPoint;
import com.petarzoric.vr_feedback.R;

import java.util.List;


public class ExtremePointsPagerAdapter extends RecyclerView.Adapter<ExtremePointsPagerAdapter.ViewHolder>  {
    private List<GraphPoint> extremePointsList;
    private Context context;
    private RecyclerView recyclerView;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_extremepoints_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GraphPoint graphPoint = extremePointsList.get(position);
        holder.commentEditText.setHint(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return extremePointsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public EditText commentEditText;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout=v;
            commentEditText=v.findViewById(R.id.extremePointsEditTextFeedback);
        }

    }

    public ExtremePointsPagerAdapter(List<GraphPoint> myDataSet, Context context, RecyclerView recyclerView){
        extremePointsList=myDataSet;
        this.context=context;
        this.recyclerView=recyclerView;

    }



}
