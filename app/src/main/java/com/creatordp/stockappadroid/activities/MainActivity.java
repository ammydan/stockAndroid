package com.creatordp.stockappadroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatordp.stockappadroid.R;
import com.creatordp.stockappadroid.viewModels.FavoriteSection;
import com.creatordp.stockappadroid.viewModels.HomeRecyclerViewAdapter;
import com.creatordp.stockappadroid.viewModels.ItemMoveCallback;
import com.creatordp.stockappadroid.viewModels.PortfolioSection;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

//network request

public class MainActivity extends AppCompatActivity {

    HomeRecyclerViewAdapter adapter;
    SectionedRecyclerViewAdapter homeAdapter;
    RecyclerView homeRecyclerView;
    PortfolioSection portfolioSection;
    FavoriteSection favoriteSection;
    ItemTouchHelper touchHelper;

    ConstraintLayout mainLayout;
    private String tiingoUrl = "https://www.tiingo.com";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1. splash screen
        setTheme(R.style.Theme_StockappAdroid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.homeView);
        //2. 网络请求
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://stockbackend.azurewebsites.net/api/search/aapl";
//        final TextView textView = (TextView) findViewById(R.id.testVolley);
        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
//            }
//        });



//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        textView.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//                        textView.setText("That didn't work!");
//
//                    }
//                });

        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest);

        /**3. recylerlayout**/
        //1)mock data
//        ArrayList<String>  tickers = new ArrayList<>();
//        tickers.add("IBM");
//        tickers.add("AAPL");
//
//        tickers.add("ALI");
//        tickers.add("JINDONG");
//        tickers.add("NAVID");
//        //2)set up the RecyclerView
//        RecyclerView recyclerView = findViewById(R.id.homelist);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new HomeRecyclerViewAdapter(this, tickers);
//        recyclerView.setAdapter(adapter);

        /**4. section layout**/
        // Create an instance of SectionedRecyclerViewAdapter
        homeAdapter = new SectionedRecyclerViewAdapter();

        // Add your Sections
        portfolioSection = new PortfolioSection();
        homeAdapter.addSection(portfolioSection);
        favoriteSection = new FavoriteSection();
        homeAdapter.addSection(favoriteSection);

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        homeRecyclerView = (RecyclerView) findViewById(R.id.homelist);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerView.setAdapter(homeAdapter);
        homeRecyclerView.addItemDecoration(new DividerItemDecoration(homeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        /**5. set swipe to delte**/
        enableSwipeToDeleteAndUndo();

        /**6. set drag to reorder**/
        ItemTouchHelper.Callback callback = new ItemMoveCallback(homeAdapter, portfolioSection, favoriteSection);
        touchHelper  = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(homeRecyclerView);

        /**8. set the autocomplete**/
        final AppCompatAutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_view);
        final TextView selectedText = findViewById(R.id.selectItem);

    }

    public void tiingoUrlClick(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(tiingoUrl));
        startActivity(i);
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                int portfolioNum = portfolioSection.getContentItemsTotal();
                int favoriteNum = favoriteSection.getContentItemsTotal();
                final int position = viewHolder.getAdapterPosition();
                String item;
                if(position>portfolioNum){
                    item = favoriteSection.getData().get(position-portfolioNum);
                    favoriteSection.removeItem(position-portfolioNum);
                }else{
                    item = portfolioSection.getData().get(position);
                    portfolioSection.removeItem(position);
                }
                Log.i("Home", "Hello!! you are doing something "+position);
                homeAdapter.notifyItemRemoved(position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(homeRecyclerView);
    }

    /**7. set the search toolbar**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setQueryHint("ticker");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Hello","Search Value change~~~");
                return true;
            }
        });


        return true;
    }
}