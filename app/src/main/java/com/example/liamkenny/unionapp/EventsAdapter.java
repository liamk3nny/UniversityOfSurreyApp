package com.example.liamkenny.unionapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";

    private String[] mEventNames;
    private String[] mEventInfo;
    private String[] mEventDate;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView eventImage;
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
            eventName = v.findViewById(R.id.event_name);
            eventInfo = v.findViewById(R.id.event_info);
            eventDate = v.findViewById(R.id.event_date);
            eventImage = v.findViewById(R.id.event_photo);

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

        public ImageView getEventImage() { return  eventImage; }

    }

    public EventsAdapter(String[] eventNames, String[] eventInfo, String[] eventDates) {
        mEventNames = eventNames;
        mEventInfo = eventInfo;
        mEventDate = eventDates;
        //mEventImage = eventImage;
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_event_item, viewGroup, false);

        return new ViewHolder(v);
    }



    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getEventName().setText(mEventNames[position]);
        viewHolder.getEventInfo().setText(mEventInfo[position]);
        viewHolder.getEventDate().setText(mEventDate [position]);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Selected item " + position , Toast.LENGTH_SHORT).show();

            }
        });
    }



    public int getItemCount() {
        return mEventNames.length;
    }
}
