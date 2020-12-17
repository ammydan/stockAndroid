package com.creatordp.stockappadroid.viewModels;

public class StockInfo {
    public Double price;
    public Double bidPrice;
    public Double openPrice;
    public Double low;
    public Double high;
    public Double mid;
    public Double volume;
    public boolean up;
    public boolean down;
    public Double change;
    public StockInfo(Double price, Double bidPrice, Double openPrice, Double low, Double mid, Double high, Double change, Double volume, boolean up, boolean down){
        this.price = price;
        this.bidPrice = bidPrice;
        this.openPrice = openPrice;
        this.low = low;
        this.high = high;
        this.mid = mid;
        this.volume = volume;
        this.up = up;
        this.down = down;
        this.change = change;
    }

}
