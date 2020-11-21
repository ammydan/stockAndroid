package com.creatordp.stockappadroid.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback{
    private SectionedRecyclerViewAdapter sectionedAdapter;
    private PortfolioSection portfolioSection;
    private FavoriteSection favoriteSection;

    public ItemMoveCallback(SectionedRecyclerViewAdapter adapter, PortfolioSection portfolioSection, FavoriteSection favoriteSection) {
        sectionedAdapter = adapter;
        this.portfolioSection = portfolioSection;
        this.favoriteSection = favoriteSection;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }



    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        if(target.getClass() != viewHolder.getClass())return false;
        int fromPostion = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        Log.i("Home", "from:"+fromPostion+", to:"+toPosition);
        if(viewHolder instanceof PortfolioSection.PortfolioViewHolder){
            portfolioSection.onRowMoved(fromPostion, toPosition);
        }else if(viewHolder instanceof  FavoriteSection.FavoriteViewHolder){
            favoriteSection.onRowMoved(fromPostion, toPosition, portfolioSection.getContentItemsTotal());
        }
        sectionedAdapter.notifyItemMoved(fromPostion,toPosition);
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof PortfolioSection.PortfolioViewHolder) {
                PortfolioSection.PortfolioViewHolder myViewHolder=
                        (PortfolioSection.PortfolioViewHolder) viewHolder;
                portfolioSection.onRowSelected(myViewHolder);

            }else if(viewHolder instanceof FavoriteSection.FavoriteViewHolder){
                FavoriteSection.FavoriteViewHolder myViwHolder = (FavoriteSection.FavoriteViewHolder) viewHolder;
                favoriteSection.onRowSelected(myViwHolder);
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof PortfolioSection.PortfolioViewHolder) {
            PortfolioSection.PortfolioViewHolder myViewHolder=
                    (PortfolioSection.PortfolioViewHolder) viewHolder;
            portfolioSection.onRowClear(myViewHolder);
        }else if(viewHolder instanceof FavoriteSection.FavoriteViewHolder){
            FavoriteSection.FavoriteViewHolder myViewHolder = (FavoriteSection.FavoriteViewHolder) viewHolder;
            favoriteSection.onRowClear(myViewHolder);
        }
    }

}
