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

public class SocietiesAdapter extends RecyclerView.Adapter<SocietiesAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";

    private String[] mSocietiesNames;
    private String[] mSocietiesInfo;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView societiesImage;
        private final TextView societiesName;
        private final TextView societiesInfo;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            societiesName = v.findViewById(R.id.societies_name);
            societiesInfo = v.findViewById(R.id.societies_info);
            societiesImage = v.findViewById(R.id.societies_photo);

        }

        public TextView getsocietiesName() {
            return societiesName;
        }

        public TextView getsocietiesInfo() {
            return societiesInfo;
        }

        public ImageView getsocietiesImage() { return  societiesImage; }

    }

    public SocietiesAdapter(String[] societiesNames, String[] societiesInfo) {
        mSocietiesNames = societiesNames;
        mSocietiesInfo = societiesInfo;
        //msocietiesImage = societiesImage;
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_societies_item, viewGroup, false);

        return new ViewHolder(v);
    }



    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getsocietiesName().setText(mSocietiesNames[position]);
        viewHolder.getsocietiesInfo().setText(mSocietiesInfo[position]);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Selected item " + position , Toast.LENGTH_SHORT).show();

            }
        });
    }



    public int getItemCount() {
        return mSocietiesNames.length;
    }
}
