package com.creatordp.stockappadroid.viewModels;

public class FavoriteData {
    public String ticker;
    public String name;
    public boolean up;
    public boolean down;
    public double price;
    public double change;
    public FavoriteData(String ticker, String name, boolean up, boolean down, double price, double change ){
        this.ticker = ticker;
        this.name = name ;
        this.up = up;
        this.down = down;
        this.price = price;
        this.change = change;
    }
}
