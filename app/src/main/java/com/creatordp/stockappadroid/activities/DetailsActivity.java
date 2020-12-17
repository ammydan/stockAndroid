package com.creatordp.stockappadroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.creatordp.stockappadroid.R;
import com.creatordp.stockappadroid.viewModels.CompanyInfo;
import com.creatordp.stockappadroid.viewModels.NewsAdapter;
import com.creatordp.stockappadroid.viewModels.NewsData;
import com.creatordp.stockappadroid.viewModels.StockInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    SharedPreferences favoritePreference;
    SharedPreferences.Editor favoriteEditor;
    SharedPreferences portfolioPreference;
    SharedPreferences.Editor portfolioEditor;
    SharedPreferences pricePreference;
    SharedPreferences.Editor priceEditor;
    String ticker;
    CompanyInfo companyInfo;
    StockInfo stockInfo;
    ArrayList<NewsData> newsData;
    MenuItem myfavorite ;
    MenuItem mynonfavorite;
    RequestQueue requestQueue;
    WebView myWebView;
    TextView desView;
    TextView sharesTitleView ;
    TextView valueTitleView ;
    TextView sharesView ;
    TextView valueView ;
    TextView sumView;
    TextView shareView;
    TextView volumeView;
    RecyclerView newslistView;
    float shares = 0;
    float zero = (float) 0.000001;
    String stockUrl = "https://stockbackend.azurewebsites.net/api/market/";
    String companyUrl = "https://stockbackend.azurewebsites.net/api/des/";
    String newsUrl = "https://stockbackend.azurewebsites.net/api/news/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_StockappAdroid);
        setContentView(R.layout.activity_details);
        favoritePreference = getApplicationContext().getSharedPreferences("FavoriteList",0);
        portfolioPreference = getApplicationContext().getSharedPreferences("PortfolioList",0);
        favoriteEditor = favoritePreference.edit();
        portfolioEditor = portfolioPreference.edit();
        pricePreference = getApplicationContext().getSharedPreferences("priceList",0);
        priceEditor = pricePreference.edit();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        ticker = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.detailtickers);
        textView.setText(ticker);

        newsData = new ArrayList<>();
        newslistView = findViewById(R.id.detailNewsList);
        newslistView.setLayoutManager(new LinearLayoutManager(this));
        newslistView.setNestedScrollingEnabled(false);
        NewsAdapter adapter = new NewsAdapter(this, newsData);
        newslistView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(this);
        getCompanyInfo();




    }

    public void tradeDialog(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.trade_dialog);
        TextView titleView = dialog.findViewById(R.id.tradetitle);
        EditText inputView= dialog.findViewById(R.id.tradeeditTextNumber);
        sumView= dialog.findViewById(R.id.tradesum);
        TextView priceView= dialog.findViewById(R.id.tradeprice);
        TextView tickerView = dialog.findViewById(R.id.tradeticker);
        tickerView.setText(ticker);
        shareView= dialog.findViewById(R.id.tradeshares);
        volumeView = dialog.findViewById(R.id.tradeavolume);
        Button buy = dialog.findViewById(R.id.tradebuybutton);
        Button sell = dialog.findViewById(R.id.tradesellbutton);
        titleView.setText("Trade "+companyInfo.name+" shares");
        sumView.setText("$0.00");
        priceView.setText(String.format("%.2f",stockInfo.price)+"/share");
        shares = 0;
        shareView.setText("0 ");
        volumeView.setText("$"+String.format("%.2f",pricePreference.getFloat("myNet",0))+" ");
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    System.out.println("in the zero");
                    shareView.setText("0");
                    shares = 0;
                    sumView.setText("$0.00");
                }else{
                    try {
                        BigDecimal b = new BigDecimal(s.toString());
                        shares = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                        if(Math.abs(shares)<zero){
                            System.out.println("in the zero");
                            shareView.setText("0");
                            shares = 0;
                            sumView.setText("$0.00");
                        }else{
                            shareView.setText(String.format("$%.2f ",shares));
                            sumView.setText(String.format("$%.2f",shares*stockInfo.price));
                        }
                    }catch (Exception e){
                        System.out.println("in the zero");
                        shareView.setText("0");
                        shares = 0;
                        sumView.setText("$0.00");
                        Toast.makeText(getApplicationContext(), "Please input a valid number",  Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Context that = this;
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double origin =  pricePreference.getFloat("myNet",0);
                double sum = shares*stockInfo.price;
                System.out.println(shares);
                if(shares<0|| Math.abs(shares)<zero){
                    Toast.makeText(getApplicationContext(), "Can not buy less than 0 shares",  Toast.LENGTH_SHORT).show();
                }else if(sum>origin){
                    Toast.makeText(getApplicationContext(), "Not enough money to buy",  Toast.LENGTH_SHORT).show();
                }else{
                    double left = origin-sum;
                    priceEditor.putFloat("myNet", (float) left);
                    String c = "n";
                    if(stockInfo.up)c="u";
                    else if(stockInfo.down)c="d";
                    priceEditor.putString(ticker,stockInfo.price+"_"+stockInfo.change+"_"+c);
                    priceEditor.commit();
                    portfolioEditor.putFloat(ticker,shares+portfolioPreference.getFloat(ticker,0));
                    portfolioEditor.commit();
                    dialog.dismiss();
                    final Dialog successdialog = new Dialog(that);
                    successdialog.setContentView(R.layout.successful_dialog);
                    TextView successView = successdialog.findViewById(R.id.successText);
                    successView.setText("You have successfully bought "+shares+" of "+ticker);
                    Button button = successdialog.findViewById(R.id.done);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successdialog.dismiss();
                        }
                    });
                    successdialog.show();
                    portfoliopart();

                }
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double origin =  pricePreference.getFloat("myNet",0);
                double sum = shares*stockInfo.price;
                if(shares<0|| Math.abs(shares)<zero){
                    Toast.makeText(getApplicationContext(), "Can not sell less than 0 shares",  Toast.LENGTH_SHORT).show();
                }else if(shares>portfolioPreference.getFloat(ticker,0)){
                    Toast.makeText(getApplicationContext(), "Not enough shares to sell",  Toast.LENGTH_SHORT).show();
                }else{
                    double more = origin+sum;
                    priceEditor.putFloat("myNet", (float) more);
                    if(Math.abs(shares-portfolioPreference.getFloat(ticker,0))<zero){
                        portfolioEditor.remove(ticker);
                        if(favoritePreference.getString(ticker,"_no").equals("_no")){
                            priceEditor.remove(ticker);
                        }

                    }else{
                        portfolioEditor.putFloat(ticker,portfolioPreference.getFloat(ticker,0)-shares);
                    }
                    portfolioEditor.commit();
                    priceEditor.commit();
                    dialog.dismiss();
                    final Dialog successdialog = new Dialog(that);
                    successdialog.setContentView(R.layout.successful_dialog);
                    TextView successView = successdialog.findViewById(R.id.successText);
                    successView.setText("You have successfully sold "+shares+" of "+ticker);
                    Button button = successdialog.findViewById(R.id.done);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successdialog.dismiss();
                        }
                    });
                    successdialog.show();
                    portfoliopart();
                }
            }
        });
        dialog.show();

    }

    public void readMoreLess(View view){
        TextView read = (TextView) view;
        String content = (String) read.getText();

        if(content.equals("Show more...")){
//            desView.setText(companyInfo.des);
            desView.setMaxLines(Integer.MAX_VALUE);
            read.setText("Show less");
        }else if(content.equals("Show less")){
//            desView.setText(companyInfo.des.substring(0,105)+"...");
            desView.setMaxLines(2);
            read.setText("Show more...");
        }

    }
    public void setMarket(){
        ArrayList<String> market = new ArrayList<>();
        market.add("Current Price: "+(stockInfo.price==null?"0.0":stockInfo.price));
        market.add("Low: "+(stockInfo.low==null?"0.0":stockInfo.low));
        market.add("Bid Price: "+(stockInfo.bidPrice==null?"0.0":stockInfo.bidPrice));
        market.add("Open Price: "+(stockInfo.openPrice==null?"0.0":stockInfo.openPrice));
        market.add("Mid: "+(stockInfo.mid==null?"0.0":stockInfo.mid));
        market.add("High: "+(stockInfo.high==null?"0.0":stockInfo.high));
        DecimalFormat df = new DecimalFormat("#,###.00");
        market.add("Volume: "+(stockInfo.volume==null?"0.0":df.format(stockInfo.volume)));

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.market_item_grid,market);
        GridView grid = findViewById(R.id.detailMarket);
//        grid.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return event.getAction() == MotionEvent.ACTION_MOVE;
//            }
//
//        });
        grid.setAdapter(adapter);
    }
    public void portfoliopart(){
        float share = portfolioPreference.getFloat(ticker,0);
        if(Math.abs(share)<zero){
            sharesTitleView.setText("You have 0 shares of "+ticker+".");
            valueTitleView.setText("Start trading!");
            sharesView.setVisibility(View.GONE);
            valueView.setVisibility(View.GONE);
        }else{
            sharesView.setText(share+"");
            valueView.setText(String.format("%.2f",share*stockInfo.price));
            sharesView.setVisibility(View.VISIBLE);
            valueView.setVisibility(View.VISIBLE);
            sharesTitleView.setText("Shares owned: ");
            valueTitleView.setText("Market Value: ");

        }
    }
    public void setTickerMyselinfo(){
        sharesTitleView = findViewById(R.id.detailSharest);
        valueTitleView = findViewById(R.id.detailValuet);
        sharesView = findViewById(R.id.detailShares);
        valueView = findViewById(R.id.detailValue);
        TextView priceView = findViewById(R.id.detailprice);
        TextView nameView = findViewById(R.id.detailname);
        TextView changeView = findViewById(R.id.detailchange);
        desView = findViewById(R.id.detailDes);
        float share = portfolioPreference.getFloat(ticker,0);
        System.out.println("in the set you have shares:"+share+" "+ticker);
        if( Math.abs(share)<zero){
            sharesTitleView.setText("You have 0 shares of "+ticker+".");
            valueTitleView.setText("Start trading!");
            sharesView.setVisibility(View.GONE);
            valueView.setVisibility(View.GONE);
        }else{
            sharesView.setText(String.format("%.2f",share)+"");
            valueView.setText(String.format("%.2f",share*stockInfo.price));
        }
        priceView.setText("$"+String.format("%.2f",stockInfo.price));
        nameView.setText(companyInfo.name);
        changeView.setText("$"+String.format("%.2f",stockInfo.change));
        if(stockInfo.up)changeView.setTextColor(Color.GREEN);
        else if(stockInfo.down)changeView.setTextColor(Color.RED);
        else changeView.setTextColor(Color.BLACK);
        desView.setText(companyInfo.des);
    }

    public void setNewsData(){
        NewsAdapter adapter = new NewsAdapter(this, newsData);
        newslistView.setAdapter(adapter);
    }

    /***1. get the company info**/
    public void getCompanyInfo(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, companyUrl+ticker, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String  name, des, exchangeCode;
                    if(!response.isNull("error")){
                        setContentView(R.layout.error_ticker);
                        System.out.println("this is a wrong ticker");
                    }
                    else{
                        name = response.getString("name");
                        des = response.getString("description");
                        exchangeCode = response.getString("exchangeCode");
                        companyInfo = new CompanyInfo(ticker,name, des,exchangeCode);
                        Log.i("news","company !!!!");
                        getStockInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("hell0!");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void getStockInfo(){
        Context that = this;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, stockUrl+ticker, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Double price, change, low, high, mid, volume,bidPrice, openPrice;
                    boolean up, down;
                    price = response.getDouble("last");
                    change = response.getDouble("change");
                    up = response.getBoolean("up");
                    down = response.getBoolean("down");
                    if(response.isNull("volume"))volume = null;
                    else volume = response.getDouble("volume");
                    if(response.isNull("bidPrice"))bidPrice = null;
                    else bidPrice=response.getDouble("bidPrice");
                    if(response.isNull("open"))openPrice=null;
                    else openPrice = response.getDouble("open");
                    if(response.isNull("high"))high = null;
                    else high = response.getDouble("high");
                    if(response.isNull("mid"))mid = null;
                    else mid = response.getDouble("mid");
                    if(response.isNull("low"))low = null;
                    else low = response.getDouble("low");

                    stockInfo = new StockInfo(price,bidPrice,openPrice,low,mid,high,change,volume,up,down);
                    Log.i("news","stock");
                    getNews();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("hell0!");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getNews(){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, newsUrl+ticker, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String  title, article, imageUrl, sourceName, publishDate,url;
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            for(int i =0;i<Math.min(10,response.length());i++) {
                                JSONObject res = (JSONObject) response.get(i);
                                title = res.getString("title");
                                article = res.getString("description");
                                publishDate = res.getString("publishedAt");
                                JSONObject source = res.getJSONObject("source");
                                sourceName = source.getString("name");
                                imageUrl = res.getString("urlToImage");
                                url = res.getString("url");
                                NewsData newsInfo = new NewsData(input.parse(publishDate),title,article,imageUrl,sourceName,url);
                                newsData.add(newsInfo);

                            }
                            Log.i("news","finish news !!!!");
                            myfavorite.setEnabled(true);
                            mynonfavorite.setEnabled(true);
                            setTickerMyselinfo();
                            setMarket();
                            setNewsData();
                            findViewById(R.id.progressDetailLayout).setVisibility(View.GONE);
                            myWebView =  findViewById(R.id.highchart);
                            WebSettings webSettings = myWebView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            myWebView.setWebViewClient(new WebViewClient() {

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
                                    if (url.equals("file:///android_asset/highcharts.html")) {
                                        myWebView.loadUrl("javascript:getHighChart('"+ticker+"');");
                                    }
                                }
                            });
                            myWebView.loadUrl("file:///android_asset/highcharts.html");
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("hell0!");
                        Log.i("news","finish news !!!!error");
                        myfavorite.setEnabled(true);
                        mynonfavorite.setEnabled(true);
                        setTickerMyselinfo();
                        setMarket();
                        setNewsData();
                        findViewById(R.id.progressDetailLayout).setVisibility(View.GONE);
                        myWebView =  findViewById(R.id.highchart);
                        WebSettings webSettings = myWebView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        myWebView.setWebViewClient(new WebViewClient() {

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                if (url.equals("file:///android_asset/highcharts.html")) {
                                    myWebView.loadUrl("javascript:getHighChart('"+ticker+"');");
                                }
                            }
                        });
                        myWebView.loadUrl("file:///android_asset/highcharts.html");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.like, menu);
        myfavorite = menu.findItem(R.id.action_favorite);
        mynonfavorite = menu.findItem(R.id.action_disfavorite);
        if(favoritePreference.getString(ticker,"_no").equals("_no")){
            myfavorite.setVisible(false);
            mynonfavorite.setVisible(true);
        }else{
            myfavorite.setVisible(true);
            mynonfavorite.setVisible(false);
        }
        myfavorite.setEnabled(false);
        mynonfavorite.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                favoriteEditor.remove(ticker);
                favoriteEditor.commit();
                if(portfolioPreference.getFloat(ticker,-1)<0){
                    priceEditor.remove(ticker);
                    priceEditor.commit();
                }
                myfavorite.setVisible(false);
                mynonfavorite.setVisible(true);
                return true;
            case R.id.action_disfavorite:
                favoriteEditor.putString(ticker,companyInfo.name);
                if(pricePreference.getString(ticker,"_no").equals("_no")){
                    String str = "n";
                    if(stockInfo.up)str = "u";
                    else if(stockInfo.down) str="d";
                    priceEditor.putString(ticker,stockInfo.price+""+stockInfo.change+"_"+str);
                };
                favoriteEditor.commit();
                priceEditor.commit();
                myfavorite.setVisible(true);
                mynonfavorite.setVisible(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}