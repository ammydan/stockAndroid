package com.creatordp.stockappadroid.viewModels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;
import com.creatordp.stockappadroid.activities.DetailsActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

import static com.creatordp.stockappadroid.activities.MainActivity.EXTRA_MESSAGE;

public class FavoriteSection extends Section {
    List<FavoriteData> favoriteData = new ArrayList<>();
    HashMap<String, Integer> index = new HashMap<>();
    SharedPreferences favoritePreference;
    SharedPreferences.Editor favoriteEditor;
    SharedPreferences pricePreference;
    SharedPreferences.Editor priceEditor;
    public FavoriteSection(SharedPreferences favoritePreference, SharedPreferences.Editor favoriteEditor, SharedPreferences pricePreference, SharedPreferences.Editor priceEditor){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.listrow)
                .headerResourceId(R.layout.favorite_header)
                .build());
        this.favoritePreference = favoritePreference;
        this.favoriteEditor = favoriteEditor;
        this.pricePreference = pricePreference;
        this.priceEditor = priceEditor;
        init();
        System.out.println("init:"+favoriteData.size());
        Log.i("data", "in favoriteSection AAPL"+favoritePreference.getString("AAPL","NO"));
    }

    public void init(){
        Map<String,String> allData= (Map<String, String>) favoritePreference.getAll();
        int cnt = 0;
        for(String t: allData.keySet()){
            boolean up = false;
            boolean down = false;
            double price =0;
            double change =0;
            String[]str = pricePreference.getString(t,"false").split("_");
            if(str.length==3){
                price = Double.parseDouble(str[0]);
                change = Double.parseDouble(str[1]);
                if(str[2].equals("u")){
                    up = true;
                }else if(str[2].equals("d")){
                    down = true;
                }
            }
            index.put(t,cnt);
            cnt++;
            favoriteData.add(new FavoriteData(t,allData.get(t),up,down,price,change));
        }
        Log.i("portfolio","numbers"+favoriteData.size());
    }

    @Override
    public int getContentItemsTotal() {
        return favoriteData.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new FavoriteSection.FavoriteViewHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    public String getTicker(int position){
        return favoriteData.get(position).ticker;
    }


    public void removeItem(int position) {
        index.remove(favoriteData.get(position).ticker);
        favoriteData.remove(position);
        for(int i =0;i<favoriteData.size();i++){
            index.put(favoriteData.get(i).ticker,i);
        }
    }


    public List<FavoriteData> getData() {
        return favoriteData;
    }


    public void onRowMoved(int fromPosition, int toPosition, int offset) {
        fromPosition-=offset;
        toPosition-=offset;
        Log.i("favorite","from:"+fromPosition+", to:"+toPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition-2; i < toPosition-2; i++) {
                String a  =favoriteData.get(i).ticker;
                String b = favoriteData.get(i+1).ticker;
                index.put(a,i+1);
                index.put(b,i);
                Collections.swap(favoriteData, i, i+1 );
            }
        } else {
            for (int i = fromPosition-2; i > toPosition-2; i--) {
                String a  =favoriteData.get(i).ticker;
                String b = favoriteData.get(i-1).ticker;
                index.put(a,i-1);
                index.put(b,i);
                Collections.swap(favoriteData, i, i - 1);
            }
        }

    }

    public void onRowSelected(FavoriteViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    public void onRowClear(FavoriteViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.rgb(242, 242, 244));
    }

    public void upChange(double price, double change , boolean up, boolean down, String ticker){
        FavoriteData item = favoriteData.get(index.get(ticker));
        item.price = price;
        item.change = change;
        item.up = up;
        item.down = down;
//        myViewHolder.change.setText(change+"");
    }

    public void addNewItem(FavoriteData item){
        index.put(item.ticker, favoriteData.size());
        favoriteData.add(item);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavoriteSection.FavoriteViewHolder favoriteViewHolder = (FavoriteViewHolder) holder;

        //bind view
        favoriteViewHolder.ticker.setText(favoriteData.get(position).ticker);
        favoriteViewHolder.price.setText(String.format("%.2f",favoriteData.get(position).price));
        favoriteViewHolder.change.setText(String.format("%.2f",favoriteData.get(position).change));
        String name = favoriteData.get(position).name;
        if(name.length()>=40)name = name.substring(0, 35)+"...";
        favoriteViewHolder.company.setText(name);
        if(favoriteData.get(position).up) favoriteViewHolder.up.setVisibility(View.VISIBLE);
        else favoriteViewHolder.up.setVisibility(View.INVISIBLE);

        if(favoriteData.get(position).down)favoriteViewHolder.down.setVisibility(View.VISIBLE);
        else favoriteViewHolder.down.setVisibility(View.INVISIBLE);
        /**12. go to detailActivity**/
        favoriteViewHolder.details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = favoriteViewHolder.rowView.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);
                String message = favoriteData.get(position).ticker;
                intent.putExtra(EXTRA_MESSAGE, message);
                context.startActivity(intent);
            }
        });
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView ticker;
        private TextView company;
        private TextView price;
        private TextView change;
        private ImageView up;
        private ImageView down;
        private ImageView details;
        View rowView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            ticker = (TextView) itemView.findViewById(R.id.ticker);
            company = (TextView) itemView.findViewById(R.id.company);
            price = (TextView) itemView.findViewById(R.id.price);
            change = (TextView) itemView.findViewById(R.id.change);
            up = (ImageView) itemView.findViewById(R.id.thisup);
            down = (ImageView) itemView.findViewById(R.id.down);
            details = (ImageView) itemView.findViewById(R.id.detail);
        }

    }
}
