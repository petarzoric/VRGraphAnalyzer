package com.petarzoric.vr_feedback.Activities.ParticipantActivities.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Model.Participant;
import com.petarzoric.vr_feedback.R;

import java.util.List;

/**
 * Used by {@link com.petarzoric.vr_feedback.Activities.ParticipantActivities.StudyDetailViewActivity} in order
 * to display the current Participants of the specific study.
 * Could try to explain all the details, but you're better off reading this article:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {
    private List<Participant> participantList;
    private Context context;
    private RecyclerView recyclerView;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pseudonymTextView;
        public View layout;

        public ViewHolder(View v){
            super(v);
            layout = v;
            pseudonymTextView = v.findViewById(R.id.pseudonym);

        }
    }

    public void add(int position, Participant participant) {
        participantList.add(position, participant );
        notifyItemInserted(position);
    }

    //Not used right now
    public void remove(int position) {
        participantList.remove(position);
        notifyItemRemoved(position);
    }

    public ParticipantsAdapter(List<Participant> myDataSet, Context context, RecyclerView recyclerView){
        participantList = myDataSet;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_participant_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Participant participant = participantList.get(position);
        holder.pseudonymTextView.setText(participant.getPseudonym());

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }
}
