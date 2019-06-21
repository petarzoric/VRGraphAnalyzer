package com.petarzoric.vr_feedback.Activities.EditorActivities.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Activities.EditorActivities.UpdateStudyActivity;
import com.petarzoric.vr_feedback.Model.Study;
import com.petarzoric.vr_feedback.Utility.DatabaseHelper;
import com.petarzoric.vr_feedback.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Shows/deletes/updates all the events in the {@link com.petarzoric.vr_feedback.Activities.EditorActivities.EditorOverviewActivity}
 * * Could try to explain all the details, but you're better off reading this article:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class StudiesEditorAdapter extends RecyclerView.Adapter<StudiesEditorAdapter.ViewHolder> {
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
            studienNameText = (TextView) v.findViewById(R.id.name);
            studienCreatorText = (TextView) v.findViewById(R.id.ersteller);
            studienDateText = (TextView) v.findViewById(R.id.date);
            studienImage = (ImageView) v.findViewById(R.id.image);




        }
    }

    public void add(int position, Study study) {
        studienList.add(position, study);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        studienList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public StudiesEditorAdapter(List<Study> myDataset, Context context, RecyclerView recyclerView) {
        studienList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudiesEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_row, parent, false);
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
        holder.studienNameText.setText("Name: " + study.getName());
        holder.studienCreatorText.setText("Creator: " + study.getCreator());
        holder.studienDateText.setText("Created on: " + study.getDate());
        Picasso.with(mContext).load(study.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.studienImage);

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete user?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(study.getId());

                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                        dbHelper.deleteStudy(study.getId(), mContext);

                        studienList.remove(position);
                        mRecyclerV.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, studienList.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }

    private void goToUpdateActivity(long personId){
        Intent goToUpdate = new Intent(mContext, UpdateStudyActivity.class);
        goToUpdate.putExtra("STUDIEN_ID", personId);
        mContext.startActivity(goToUpdate);
    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return studienList.size();
    }



}