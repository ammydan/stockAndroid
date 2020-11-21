package com.creatordp.stockappadroid.viewModels;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

public class FavoriteSection extends Section {
    List<String> itemList = new ArrayList<>();
    public FavoriteSection(){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.listrow)
                .headerResourceId(R.layout.favorite_header)
                .footerResourceId(R.layout.footer)
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


    public void removeItem(int position) {
        itemList.remove(position);
//        notifyItemRemoved(position);
    }


    public List<String> getData() {
        return itemList;
    }


    public void onRowMoved(int fromPosition, int toPosition, int offset) {
        fromPosition-=offset;
        toPosition-=offset;
        Log.i("favorite","from:"+fromPosition+", to:"+toPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition-2; i < toPosition-2; i++) {
                Collections.swap(itemList, i, i+1 );
            }
        } else {
            for (int i = fromPosition-2; i > toPosition-2; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }

    }

    public void onRowSelected(FavoriteViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    public void onRowClear(FavoriteViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavoriteSection.FavoriteViewHolder favoriteViewHolder = (FavoriteViewHolder) holder;

        //bind view
        favoriteViewHolder.favoriteItems.setText(itemList.get(position));
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView favoriteItems;
        View rowView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            favoriteItems = (TextView) itemView.findViewById(R.id.ticker);
        }

    }
}
