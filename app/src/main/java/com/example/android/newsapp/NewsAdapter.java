package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sgomezp on 28/06/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private Context context;


    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        NewsViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_items, parent, false);
            viewHolder = new NewsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsViewHolder) convertView.getTag();
        }

        final News currentNews = getItem(position);
        viewHolder.vHeadline.setText(currentNews.getHeadline());
        viewHolder.vSection.setText(currentNews.getSection());
        viewHolder.vDate.setText(currentNews.getDate());
        viewHolder.vUrl.setText(currentNews.getUrl());

        // Start the intent for  News details
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNews.getUrl()));
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        protected TextView vHeadline;
        protected TextView vSection;
        protected TextView vDate;
        protected TextView vUrl;

        public NewsViewHolder(View view) {

            super(view);
            vHeadline = (TextView) view.findViewById(R.id.news_headline);
            vSection = (TextView) view.findViewById(R.id.news_section);
            vDate = (TextView) view.findViewById(R.id.news_date);
            vUrl = (TextView) view.findViewById(R.id.news_url);

        }
    }

}
