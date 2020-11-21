package com.creatordp.stockappadroid.viewModels;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class PortfolioSection extends Section {
    List<String> itemList = new ArrayList<String>();
    public PortfolioSection(){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.listrow)
                .headerResourceId(R.layout.portfolio_header)
                .build());

        itemList.add("item1");
        itemList.add("item2");
        itemList.add("item3");
        itemList.add("item4");
        itemList.add("item5");
        itemList.add("item6");
    }

    @Override
    public int getContentItemsTotal() {
        return itemList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new PortfolioViewHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new EmptyViewHolder(view);
    }

    public void removeItem(int position) {
        itemList.remove(position);
//        notifyItemRemoved(position);
    }


    public List<String> getData() {
        return itemList;
    }


    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition-1; i < toPosition-1; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition-1; i > toPosition-1; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }

    }

    public void onRowSelected(PortfolioViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    public void onRowClear(PortfolioViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        PortfolioViewHolder portfolioViewHolder = (PortfolioViewHolder) holder;

        //bind view
        portfolioViewHolder.portfolioItems.setText(itemList.get(position));
    }

    public class PortfolioViewHolder extends RecyclerView.ViewHolder {
        private TextView portfolioItems;
        View rowView;

        public PortfolioViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            portfolioItems = (TextView) itemView.findViewById(R.id.ticker);
        }

    }
}
