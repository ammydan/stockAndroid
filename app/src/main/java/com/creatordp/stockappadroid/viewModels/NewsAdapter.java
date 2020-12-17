package com.creatordp.stockappadroid.viewModels;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter{
    private ArrayList<NewsData> newsData;
    private LayoutInflater mInflater;
    private Date now;
    private Context context;

    public NewsAdapter(Context context, ArrayList<NewsData>newsData){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.newsData= newsData;
        this.now = new Date();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View view1 = mInflater.inflate(R.layout.news_firstitem, parent, false);
                return new FirstViewHolder(view1);
            default:
                View view2 = mInflater.inflate(R.layout.newslistrow, parent, false);
                return new ItemViewHolder(view2);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return 0;
        else return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            FirstViewHolder firstViewHolder = (FirstViewHolder)holder;
            firstViewHolder.newsSource.setText(newsData.get(position).sourceName);
            firstViewHolder.newsTitle.setText(newsData.get(position).title);

            long diff = now.getTime() - newsData.get(position).publishDate.getTime();
            if (diff >= 24 * 60 * 60000) {
                long cnt = diff / (24 * 60 * 60 * 1000);
                if (cnt > 1) {
                    firstViewHolder.newsDate.setText(cnt + " days ago");
                } else {
                    firstViewHolder.newsDate.setText(cnt + " day ago");
                }
            } else if (diff >= 60 * 60 * 1000) {
                long cnt = diff / (60 * 60 * 1000);
                if (cnt > 1) {
                    firstViewHolder.newsDate.setText(cnt + "hours ago");
                } else {
                    firstViewHolder.newsDate.setText(cnt + " hour ago");
                }
            } else {
                long cnt = diff / (60 * 1000);
                if (cnt <= 1) {
                    firstViewHolder.newsDate.setText(1 + " minute ago");
                } else {
                    firstViewHolder.newsDate.setText(cnt + " minutes ago");
                }
            }
            Picasso.with(context).load(newsData.get(position).imageUrl).into(firstViewHolder.imageView);
        }else {
            ItemViewHolder itemholder = (ItemViewHolder)holder;
            itemholder.newsSource.setText(newsData.get(position).sourceName);
            itemholder.newsTitle.setText(newsData.get(position).title);

            long diff = now.getTime() - newsData.get(position).publishDate.getTime();
            if (diff >= 24 * 60 * 60000) {
                long cnt = diff / (24 * 60 * 60 * 1000);
                if (cnt > 1) {
                    itemholder.newsDate.setText(cnt + " days ago");
                } else {
                    itemholder.newsDate.setText(cnt + " day ago");
                }
            } else if (diff >= 60 * 60 * 1000) {
                long cnt = diff / (60 * 60 * 1000);
                if (cnt > 1) {
                    itemholder.newsDate.setText(cnt + "hours ago");
                } else {
                    itemholder.newsDate.setText(cnt + " hour ago");
                }
            } else {
                long cnt = diff / (60 * 1000);
                if (cnt <= 1) {
                    itemholder.newsDate.setText(1 + " minute ago");
                } else {
                    itemholder.newsDate.setText(cnt + " minutes ago");
                }
            }
            Picasso.with(context).load(newsData.get(position).imageUrl).into(itemholder.imageView);
        }
    }


    @Override
    public int getItemCount() {
        return newsData.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView newsDate;
        public TextView newsSource;
        public TextView newsTitle;
        public ImageView imageView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsDate = itemView.findViewById(R.id.newsDate);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            imageView = itemView.findViewById(R.id.newsImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(newsData.get(position).url));
            context.startActivity(i);
            System.out.println("news :"+position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            System.out.println("long news :"+position);
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.news_dialog);

            // set the custom dialog components - text, image and button
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.newsdialogtitle);
            dialogTitle.setText(newsData.get(position).title);
            ImageView image =  dialog.findViewById(R.id.newsdialogimage);
            ImageView twitter = dialog.findViewById(R.id.newsdialogtiwtter);
            ImageView chrome = dialog.findViewById(R.id.newsdialogchrome);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/intent/tweet?text="+newsData.get(position).title+"&url="+newsData.get(position).url));
                    context.startActivity(i);
                }
            });
            chrome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(newsData.get(position).url));
                    context.startActivity(i);

                }
            });
            Picasso.with(context).load(newsData.get(position).imageUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_launcher_background).into(image);
            dialog.show();
            return true;
        }
    }
    public class FirstViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView newsDate;
        public TextView newsSource;
        public TextView newsTitle;
        public ImageView imageView;

        public FirstViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            newsSource = itemView.findViewById(R.id.newsFirstSource);
            newsDate = itemView.findViewById(R.id.newsFirstDate);
            newsTitle = itemView.findViewById(R.id.newsFirstTitle);
            imageView = itemView.findViewById(R.id.newsFirstImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(newsData.get(position).url));
            context.startActivity(i);
            System.out.println("news :"+position);
        }

        @Override
        public boolean onLongClick(View v) {
            final int position = getAdapterPosition();
            System.out.println("long news :"+position);
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.news_dialog);

            // set the custom dialog components - text, image and button
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.newsdialogtitle);
            dialogTitle.setText(newsData.get(position).title);
            ImageView image =  dialog.findViewById(R.id.newsdialogimage);
            ImageView twitter = dialog.findViewById(R.id.newsdialogtiwtter);
            ImageView chrome = dialog.findViewById(R.id.newsdialogchrome);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/intent/tweet?text="+newsData.get(position).title+"&url="+newsData.get(position).url));
                    context.startActivity(i);
                }
            });
            chrome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(newsData.get(position).url));
                    context.startActivity(i);

                }
            });
            Picasso.with(context).load(newsData.get(position).imageUrl).into(image);
            dialog.show();
            return true;
        }
    }
}
