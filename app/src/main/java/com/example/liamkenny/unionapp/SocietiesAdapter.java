package com.example.liamkenny.unionapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SocietiesAdapter extends RecyclerView.Adapter<SocietiesAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";

    private ArrayList<String> mSocietiesNames;
    private ArrayList<String> mSocietiesInfo;
    private Context mContext;
    private Fragment fragment;
    private Bundle mBundle;

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

    public SocietiesAdapter(ArrayList<String> societiesNames, ArrayList<String> societiesInfo) {
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
        viewHolder.getsocietiesName().setText(mSocietiesNames.get(position));
        viewHolder.getsocietiesInfo().setText(mSocietiesInfo.get(position));

        final int mItemSelected = viewHolder.getAdapterPosition();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Selected item " + position , Toast.LENGTH_SHORT).show();

               /* try {
                    fragment = SocietiesInfoFragment.class.newInstance();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainActivity mainActivity = (MainActivity)mContext;
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_layout, fragment);
                fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.commit();*/
/*

                fragment = new SocietiesInfoFragment();
                mBundle = new Bundle();
                mBundle.putParcelable("item_selected_key", mItemSelected);
                fragment.setArguments(mBundle);
                switchContent(R.id.frag1, fragment);
*/


            }
        });
    }


    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

    public int getItemCount() {
        return mSocietiesNames.size();
    }
}
