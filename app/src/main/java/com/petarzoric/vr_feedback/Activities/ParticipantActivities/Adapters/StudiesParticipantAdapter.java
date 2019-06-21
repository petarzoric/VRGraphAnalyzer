package com.petarzoric.vr_feedback.Activities.ParticipantActivities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Activities.EditorActivities.UpdateStudyActivity;
import com.petarzoric.vr_feedback.Activities.ParticipantActivities.StudyDetailViewActivity;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Used by {@link StudiesParticipantAdapter} to display/delete/update the study records.
 * * Could try to explain all the details, but you're better off reading this article:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */


public class StudiesParticipantAdapter extends RecyclerView.Adapter<StudiesParticipantAdapter.ViewHolder> {
    private List<Study> studienList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView studienNameText;
        public TextView studienCreatorText;
        public TextView studienDateText;
        public ImageView studienImage;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            studienNameText = (TextView) v.findViewById(R.id.studyNameUser);
            studienImage = v.findViewById(R.id.studyImageUser);





        }
    }





    // Provide a suitable constructor (depends on the kind of dataset)
    public StudiesParticipantAdapter(List<Study> myDataset, Context context, RecyclerView recyclerView) {
        studienList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudiesParticipantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.layout_listitem, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Study study = studienList.get(position);
        holder.studienNameText.setText(study.getName());
        // holder.studienDateText.setText("Created on: " + study.getDate());
        Picasso.with(mContext).load(study.getImage()).placeholder(R.drawable.placeholder).into(holder.studienImage);
         holder.layout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 goToDetailView(study.getId());
             }
         });



    }

    private void goToUpdateActivity(long personId){
        Intent goToUpdate = new Intent(mContext, UpdateStudyActivity.class);
        goToUpdate.putExtra("STUDIEN_ID", personId);
        mContext.startActivity(goToUpdate);
    }

    public void goToDetailView(long id){
       Intent goToDetail = new Intent(mContext, StudyDetailViewActivity.class);
       goToDetail.putExtra("STUDIEN_ID", id);
       mContext.startActivity(goToDetail);
    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return studienList.size();
    }



}