package com.creatordp.stockappadroid.viewModels;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;

import java.util.List;

public class HomeRecyclerViewAdapter  extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener mClickListener;

    public HomeRecyclerViewAdapter(Context context, List<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ticker = mData.get(position);
        holder.ticker.setText(ticker);
    }

    @Override
    public int getItemCount() {
//        Log.i("Home","mysize"+mData.size());
        return mData.size();
    }
    String getItem(int id){
        return mData.get(id);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ticker;
//        TextView price;
//        TextView company;
//        ImageView detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticker = itemView.findViewById(R.id.ticker);
//            price = itemView.findViewById(R.id.price);
//            company = itemView.findViewById(R.id.company);
//            detail = itemView.findViewById(R.id.detail);
//            itemView.setOnClickListener(this);
        }
        public TextView getTicker(){
            return ticker;
        }

//        @Override
//        public void onClick(View v) {
//            Log.i("Home","click !!!");
//            if(mClickListener!=null) mClickListener.onItemClick(v, getAdapterPosition(),);
//        }
    }
}
