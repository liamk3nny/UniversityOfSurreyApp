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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";

    private ArrayList<String> mNewsNames;
    private ArrayList<String> mNewsInfo;
    private ArrayList<String> mNewsArticle;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView newsName;
        private final TextView newsInfo;
        private final TextView newsArticle;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            newsName = v.findViewById(R.id.news_name);
            newsInfo = v.findViewById(R.id.news_info);
            newsArticle = v.findViewById(R.id.news_article);


        }

        public TextView getnewsName() {
            return newsName;
        }

        public TextView getnewsInfo() {
            return newsInfo;
        }

        public TextView getNewsArticle() {
            return newsArticle;
        }
    }

    public NewsAdapter(ArrayList<String> newsNames, ArrayList<String> newsInfo, ArrayList<String> newsArticle) {
        mNewsNames = newsNames;
        mNewsInfo = newsInfo;
        mNewsArticle = newsArticle;

    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_news_item, viewGroup, false);

        return new ViewHolder(v);
    }



    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getnewsName().setText(mNewsNames.get(position));
        viewHolder.getnewsInfo().setText(mNewsInfo.get(position));
        viewHolder.getNewsArticle().setText(mNewsArticle.get(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Selected item " + position , Toast.LENGTH_SHORT).show();

            }
        });
    }



    public int getItemCount() {
        return mNewsNames.size();
    }
}
