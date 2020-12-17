package com.creatordp.stockappadroid.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.creatordp.stockappadroid.R;
import com.creatordp.stockappadroid.services.AutoApiCall;
import com.creatordp.stockappadroid.services.RequestWorker;
import com.creatordp.stockappadroid.viewModels.AutoSuggestAdapter;
import com.creatordp.stockappadroid.viewModels.FavoriteSection;
import com.creatordp.stockappadroid.viewModels.HomeRecyclerViewAdapter;
import com.creatordp.stockappadroid.viewModels.ItemMoveCallback;
import com.creatordp.stockappadroid.viewModels.PortfolioSection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


//network request

public class MainActivity extends AppCompatActivity {

    HomeRecyclerViewAdapter adapter;
    SectionedRecyclerViewAdapter homeAdapter;
    RecyclerView homeRecyclerView;
    PortfolioSection portfolioSection;
    FavoriteSection favoriteSection;
    ItemTouchHelper touchHelper;
    String date;
    SimpleDateFormat ft = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);


    private AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    ConstraintLayout mainLayout;
    private String tiingoUrl = "https://www.tiingo.com";
    private String priceUrl = "https://stockbackend.azurewebsites.net/api/market/";
//    private String priceUrl = "http://localhost:3000/api/market/";

    SharedPreferences favoritePreference;
    SharedPreferences portfolioPreference;
    SharedPreferences pricePreference;
    SharedPreferences.Editor priceEditor;
    SharedPreferences.Editor favoriteEditor;
    SharedPreferences.Editor portfolioEditor;


    RequestQueue requestQueue;
    int progressNum;
    int entryNumber;
    Handler dataHandler = new Handler();
    Runnable runnable;
    int delay = 15000;

    public static final String EXTRA_MESSAGE = "com.creatordp.stockappadroid.MESSAGE";


    //improve function
    RequestWorker helperRequestWorker = new RequestWorker();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1. splash screen
        setTheme(R.style.Theme_StockappAdroid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.homeView);
        requestQueue = Volley.newRequestQueue(this);
        Date tdate = new Date();
        date = ft.format(tdate);
        TextView dateView=findViewById(R.id.thedate);
        dateView.setText(date);
        helperRequestWorker.start();

        

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


//        long startTime=System.currentTimeMillis();   //获取开始时间
//
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, priceUrl+"AAPL", null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        long endTime=System.currentTimeMillis(); //获取结束时间
//                        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//                        long endTime=System.currentTimeMillis(); //获取结束时间
//                        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//
//                    }
//                });
//
//        // Add the request to the RequestQueue.
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

        /**9. sharedPreference
         * ticker, shares(company in Favorite)
         * eg:
         * **/
        favoritePreference = getApplicationContext().getSharedPreferences("FavoriteList",0);
        portfolioPreference = getApplicationContext().getSharedPreferences("PortfolioList",0);
        favoriteEditor = favoritePreference.edit();
        portfolioEditor = portfolioPreference.edit();
        pricePreference = getApplicationContext().getSharedPreferences("priceList",0);
        priceEditor = pricePreference.edit();
//        initPreference();
        //clear
//        portfolioEditor.clear();
//        portfolioEditor.commit();
//        favoriteEditor.clear();
//        favoriteEditor.commit();
//        priceEditor.clear();
//        priceEditor.commit();
//        initPreference();

        /**4. section layout**/
        // Create an instance of SectionedRecyclerViewAdapter
        homeAdapter = new SectionedRecyclerViewAdapter();

        // Add your Sections
        portfolioSection = new PortfolioSection(portfolioPreference,portfolioEditor,pricePreference,priceEditor);
        homeAdapter.addSection(portfolioSection);
        favoriteSection = new FavoriteSection(favoritePreference,favoriteEditor,pricePreference,priceEditor);
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



    }
    @Override
    protected void onResume() {
        super.onResume();
        Map<String,?> entries = pricePreference.getAll();
        entryNumber = entries.size()-1;
        System.out.println("entryNumber:"+entryNumber);
        if(entries.size()==1){
            setHomepage();
            return ;
        }
        for(String t:entries.keySet()){
            if (t.equals("myNet")) continue;
            int whichSection = 0;
            if (portfolioPreference.getFloat(t, -1) > 0) whichSection = 1;
            if (!favoritePreference.getString(t, "_no").equals("_no")) {
                if (whichSection == 1) whichSection = 3;
                else whichSection = 2;
            }
            System.out.println("call asynLoading");
            asynLoading(t, whichSection);
        }
    }


    /**11. set progress**/
    public void asynLoading(String ticker, int whichSection){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, priceUrl+ticker, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double price = response.getDouble("last");
                            double change = response.getDouble("change");
                            boolean up = response.getBoolean("up");
                            boolean down = response.getBoolean("down");
                            updateItem(whichSection,price,change,up,down, ticker);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            progressNum++;
                            System.out.println("in lock"+progressNum+", "+entryNumber);
                            if(progressNum==entryNumber){
//                                findViewById(R.id.progressLayout).setVisibility(View.GONE);
////                                homeRecyclerView.setVisibility(View.VISIBLE);
//                                homeRecyclerView.setAdapter(homeAdapter);
////                                homeRecyclerView.addItemDecoration(new DividerItemDecoration(homeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
////
////                                /**5. set swipe to delte**/
////                                enableSwipeToDeleteAndUndo();
////
////                                /**6. set drag to reorder**/
////                                ItemTouchHelper.Callback callback = new ItemMoveCallback(homeAdapter, portfolioSection, favoriteSection);
////                                touchHelper  = new ItemTouchHelper(callback);
////                                touchHelper.attachToRecyclerView(homeRecyclerView);
//
//                                /**10.get the price every 15s**/
//                                helperRequestWorker.dataHandler.postDelayed(runnable = new Runnable() {
//                                    public void run() {
//                                        Map<String, ?> map = pricePreference.getAll();
//                                        for (String t : map.keySet()) {
//                                            if (t.equals("myNet")) continue;
//                                            int whichSection = 0;
//                                            if (portfolioPreference.getInt(t, -1) != -1) whichSection = 1;
//                                            if (!favoritePreference.getString(t, "_no").equals("_no")) {
//                                                if (whichSection == 1) whichSection = 3;
//                                                else whichSection = 2;
//                                            }
//                                            asynRquest(t, whichSection);
//                                        }
//                                        helperRequestWorker.dataHandler.postDelayed(runnable, delay);
//                                    }
//                                }, delay);
                                setHomepage();

                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        progressNum++;
                        if(progressNum==entryNumber){
//                            findViewById(R.id.progressLayout).setVisibility(View.GONE);
////                            homeRecyclerView.setVisibility(View.VISIBLE);
//                            homeRecyclerView.setAdapter(homeAdapter);
////                            homeRecyclerView.addItemDecoration(new DividerItemDecoration(homeRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
////
////                            /**5. set swipe to delte**/
////                            enableSwipeToDeleteAndUndo();
////
////                            /**6. set drag to reorder**/
////                            ItemTouchHelper.Callback callback = new ItemMoveCallback(homeAdapter, portfolioSection, favoriteSection);
////                            touchHelper  = new ItemTouchHelper(callback);
////                            touchHelper.attachToRecyclerView(homeRecyclerView);
//
//                            /**10.get the price every 15s**/
//                            helperRequestWorker.dataHandler.postDelayed(runnable = new Runnable() {
//                                public void run() {
//                                    Map<String, ?> map = pricePreference.getAll();
//                                    for (String t : map.keySet()) {
//                                        if (t.equals("myNet")) continue;
//                                        int whichSection = 0;
//                                        if (portfolioPreference.getInt(t, -1) != -1) whichSection = 1;
//                                        if (!favoritePreference.getString(t, "_no").equals("_no")) {
//                                            if (whichSection == 1) whichSection = 3;
//                                            else whichSection = 2;
//                                        }
//                                        asynRquest(t, whichSection);
//                                    }
//                                    helperRequestWorker.dataHandler.postDelayed(runnable, delay);
//                                }
//                            }, delay);
                            setHomepage();

                        }


                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void setHomepage(){
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
        homeRecyclerView.setAdapter(homeAdapter);

        /**10.get the price every 15s**/
        helperRequestWorker.dataHandler.postDelayed(runnable = new Runnable() {
            public void run() {
                Map<String, ?> map = pricePreference.getAll();
                for (String t : map.keySet()) {
                    if (t.equals("myNet")) continue;
                    int whichSection = 0;
                    if (portfolioPreference.getFloat(t, -1) > 0) whichSection = 1;
                    if (!favoritePreference.getString(t, "_no").equals("_no")) {
                        if (whichSection == 1) whichSection = 3;
                        else whichSection = 2;
                    }
                    asynRquest(t, whichSection);
                }
                helperRequestWorker.dataHandler.postDelayed(runnable, delay);
            }
        }, delay);
    }
    /**10.get the price every 15s**/
    public void asynRquest(String ticker, int whichSection) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, priceUrl+ticker, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    double price = 0;
                    try {
                        double change = response.getDouble("change");
                        price = response.getDouble("last");
                        boolean up = response.getBoolean("up");
                        boolean down = response.getBoolean("down");
                        updateItem(whichSection,price,change,up,down, ticker);
                        Log.i("data","success get data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error


                }
            });
        requestQueue.add(jsonObjectRequest);

    }
    public void updateItem(int whichSection, double price, double change, boolean up, boolean down, String ticker){
        System.out.println(whichSection);
        if(whichSection==1||whichSection==3){
            double thechange = portfolioSection.upChange(price,change,up, down, ticker);
//            double origin = pricePreference.getFloat("myNet", 0);
//            priceEditor.putFloat("myNet", (float) (origin+thechange));
            portfolioSection.updateNet(thechange);
        }
        if(whichSection==2||whichSection==3){
            favoriteSection.upChange(price,change, up, down ,ticker);
        }
        if(up) priceEditor.putString(ticker,price+"_"+change+"_u");
        else if(down)priceEditor.putString(ticker,price+"_"+change+"_d");
        else priceEditor.putString(ticker,price+"_"+change+"_n");
        priceEditor.commit();
        homeAdapter.notifyDataSetChanged();
    }

    /**9. init sharedPreference**/
    private void initPreference(){
//        portfolioEditor.putFloat("AAPL",5.0);
//        portfolioEditor.putFloat("IBM",7.0);
//        portfolioEditor.commit();
//
//        favoriteEditor.putString("AAPL","Apple Inc");
//        favoriteEditor.putString("IBM","International Business Machines Corp");
//        favoriteEditor.commit();
//
//        priceEditor.putString("AAPL","243_13.3_d");
//        priceEditor.putString("IBM","134_23.3_u");
        priceEditor.putFloat("myNet", 20000);
        priceEditor.commit();

    }

    /**8.set autocomplete**/
    private void makeApiCall(String text) {
        AutoApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("ticker")+"-"+row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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
                final int position = viewHolder.getAdapterPosition();
                System.out.println(position+" in the portofolio");
                if(position>portfolioNum&&position>0&&position-portfolioNum-2>=0){
                    String ticker = favoriteSection.getTicker(position-portfolioNum-2);
                    favoriteEditor.remove(ticker);
                    if(portfolioPreference.getFloat(ticker,-1)<0){
                        priceEditor.remove(ticker);
                    }
                    favoriteEditor.commit();
                    priceEditor.commit();
                    System.out.println(favoriteSection.getData().size());
                    favoriteSection.removeItem(position-portfolioNum-2);
                    homeAdapter.notifyItemRemoved(position);
                }else{
                    homeAdapter.notifyItemChanged(position);
                }
//                else{
//                    portfolioSection.removeItem(position-1);
//                }


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(homeRecyclerView);
    }

    /**7. set the search toolbar**/
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setQueryHint("ticker");
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);

        /**8. set autocomplete**/
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setThreshold(3);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setQuery(autoSuggestAdapter.getObject(position).split("-")[0],true);
            }
        });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchView.getQuery())) {
                        makeApiCall(searchView.getQuery().toString());
                    }
                }
                return false;
            }
        });
        MainActivity that = this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(that, DetailsActivity.class);
                intent.putExtra(EXTRA_MESSAGE, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause!!!");
        helperRequestWorker.dataHandler.removeCallbacks(runnable);
//        try {
//            helperRequestWorker.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helperRequestWorker.looper.quit();
    }
}