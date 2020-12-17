package com.creatordp.stockappadroid.viewModels;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;
import com.creatordp.stockappadroid.activities.DetailsActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

import static com.creatordp.stockappadroid.activities.MainActivity.EXTRA_MESSAGE;

public class PortfolioSection extends Section {
    List<PortfolioData> portfolioData= new ArrayList<>();
    double netWorth;

    HashMap<String, Integer> index;
    SharedPreferences portfolioPreference;
    SharedPreferences.Editor portfolioEditor;
    SharedPreferences pricePreference;
    SharedPreferences.Editor priceEditor;
    public PortfolioSection(SharedPreferences portfolioPreference, SharedPreferences.Editor portfolioEditor, SharedPreferences pricePreference, SharedPreferences.Editor priceEditor){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.listrow)
                .headerResourceId(R.layout.portfolio_header)
                .build());
        this.portfolioPreference = portfolioPreference;
        this.portfolioEditor = portfolioEditor;
        this.pricePreference = pricePreference;
        this.priceEditor = priceEditor;
        index = new HashMap<>();
        init();
    }

    public void init(){
        Map<String,Float> allData= (Map<String, Float>) portfolioPreference.getAll();
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
            netWorth+=(price*allData.get(t));
            System.out.println("in the portfolio init"+ t+" "+allData.get(t));
            portfolioData.add(new PortfolioData(t,allData.get(t),up,down,price,change));
            index.put(t,cnt);
            cnt++;
        }

        netWorth += pricePreference.getFloat("myNet",0);

        Log.i("portfolio","numbers"+portfolioData.size());
    }

    @Override
    public int getContentItemsTotal() {
        return portfolioData.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new PortfolioViewHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new PortfolioHeaderViewHolder(view);
    }
    public String getTicker(int position){
        return portfolioData.get(position).ticker;
    }

    public void removeItem(int position) {
        index.remove(portfolioData.get(position).ticker);
        portfolioData.remove(position);
        for(int i =0;i<portfolioData.size();i++){
            index.put(portfolioData.get(i).ticker,i);
        }
    }


    public List<PortfolioData> getData() {
        return portfolioData;
    }



    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition-1; i < toPosition-1; i++) {
                String a  =portfolioData.get(i).ticker;
                String b = portfolioData.get(i+1).ticker;
                index.put(a,i+1);
                index.put(b,i);
                Collections.swap(portfolioData, i, i + 1);
            }
        } else {
            for (int i = fromPosition-1; i > toPosition-1; i--) {
                String a  =portfolioData.get(i).ticker;
                String b = portfolioData.get(i-1).ticker;
                index.put(a,i-1);
                index.put(b,i);
                Collections.swap(portfolioData, i, i - 1);
            }
        }

    }

    public void onRowSelected(PortfolioViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    public void onRowClear(PortfolioViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.rgb(242, 242, 244));

    }

    public double upChange(double price, double change , boolean up, boolean down, String ticker){

        PortfolioData item = portfolioData.get(index.get(ticker));
        double thechange = (price-item.price)*item.shares;
        item.price = price;
        item.change = change;
        item.up = up;
        item.down = down;
        return thechange;
//        myViewHolder.change.setText(change+"");
    }

    public void addNewItem(PortfolioData item){
        index.put(item.ticker, portfolioData.size());
        portfolioData.add(item);
    }

    public void updateNet(double theChange){
        netWorth = netWorth+theChange;
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        PortfolioViewHolder portfolioViewHolder = (PortfolioViewHolder) holder;

        //bind view
        portfolioViewHolder.ticker.setText(portfolioData.get(position).ticker);
        portfolioViewHolder.price.setText(String.format("%.2f",portfolioData.get(position).price));
        portfolioViewHolder.change.setText(String.format("%.2f",portfolioData.get(position).change));
        portfolioViewHolder.company.setText(String.format("%.2f",portfolioData.get(position).shares)+" shares");
        if(portfolioData.get(position).up) portfolioViewHolder.up.setVisibility(View.VISIBLE);
        else portfolioViewHolder.up.setVisibility(View.INVISIBLE);

        if(portfolioData.get(position).down)portfolioViewHolder.down.setVisibility(View.VISIBLE);
        else portfolioViewHolder.down.setVisibility(View.INVISIBLE);
        portfolioViewHolder.details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = portfolioViewHolder.rowView.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);
                String message = portfolioData.get(position).ticker;
                intent.putExtra(EXTRA_MESSAGE, message);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        PortfolioHeaderViewHolder portfolioHeaderViewHolder = (PortfolioHeaderViewHolder) holder;
        portfolioHeaderViewHolder.netWorth.setText(String.format("%.2f",netWorth));
    }


    public class PortfolioHeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView netWorth;

        public PortfolioHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            netWorth = (TextView) itemView.findViewById(R.id.networth);

        }
    }

    public class PortfolioViewHolder extends RecyclerView.ViewHolder {
        private TextView ticker;
        private TextView company;
        private TextView price;
        private TextView change;
        private ImageView up;
        private ImageView down;
        private ImageView details;
        View rowView;

        public PortfolioViewHolder(View itemView) {
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
