package com.example.liamkenny.unionapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";

    private String[] mEventNames;
    private String[] mEventInfo;
    private String[] mEventDate;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventName;
        private final TextView eventInfo;
        private final TextView eventDate;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            eventName = (TextView) v.findViewById(R.id.event_name);
            eventInfo = (TextView) v.findViewById(R.id.event_info);
            eventDate = (TextView) v.findViewById(R.id.event_date);

        }

        public TextView getEventName() {
            return eventName;
        }

        public TextView getEventInfo() {
            return eventInfo;
        }

        public TextView getEventDate() {
            return eventDate;
        }

    }

    public EventsAdapter(String[] eventNames) {
        mEventNames = eventNames;
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_event_item, viewGroup, false);

        return new ViewHolder(v);
    }



    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getEventName().setText(mEventNames[position]);
    }



    public int getItemCount() {
        return mEventNames.length;
    }
}
