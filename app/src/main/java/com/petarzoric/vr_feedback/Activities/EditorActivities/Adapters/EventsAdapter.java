package com.petarzoric.vr_feedback.Activities.EditorActivities.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petarzoric.vr_feedback.Utility.DatabaseHelper;
import com.petarzoric.vr_feedback.Utility.DurationConverter;
import com.petarzoric.vr_feedback.Model.StudyEvent;
import com.petarzoric.vr_feedback.R;

import java.util.List;

/**
 * Shows/udates/deletes all the events in the {@link com.petarzoric.vr_feedback.Activities.EditorActivities.EventsActivity}
 * * Could try to explain all the details, but you're better off reading this article:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<StudyEvent> eventList;
    private Context context;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView deleteEventText;

        public View layout;

        public ViewHolder(View v){
            super(v);
            layout=v;
            nameTextView = v.findViewById(R.id.eventNameTextVIew);
            timeTextView = v.findViewById(R.id.eventTimeTextView);
            deleteEventText = v.findViewById(R.id.deleteEventButton);

        }
    }

    public void add(int pos, StudyEvent studyEvent){
        eventList.add(pos, studyEvent);
        notifyItemInserted(pos);

    }

    public void remove(int pos){
        eventList.remove(pos);
        notifyItemRemoved(pos);
    }


    public void refreshEvents(List<StudyEvent> eventsUpdated){
        eventList.clear();
        eventList = eventsUpdated;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_event_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final StudyEvent studyEvent = eventList.get(position);
        holder.nameTextView.setText(studyEvent.getName());
        holder.timeTextView.setText(DurationConverter.getDurationInMMss(studyEvent.getTime()));
        holder.deleteEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete event?");
                builder.setMessage("Are you sure that you want to delete this event?");

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        long event_id = eventList.get(position).getId();
                        remove(position);
                        dbHelper.deleteEventForStudy(event_id);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.create().show();



            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }




    public EventsAdapter(List<StudyEvent> myDataSet, Context context, RecyclerView recyclerView){
        eventList=myDataSet;
        this.context = context;
        this.recyclerView= recyclerView;
        this.dbHelper = new DatabaseHelper(context);

    }
}
