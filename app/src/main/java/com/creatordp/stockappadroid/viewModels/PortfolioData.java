package com.creatordp.stockappadroid.viewModels;

public class PortfolioData {
    public String ticker;
    public boolean up;
    public boolean down;
    public float shares;
    public double price;
    public double change;
    public PortfolioData(String ticker, float shares, boolean up, boolean down, double price, double change){
        this.ticker = ticker;
        this.shares = shares;
        this.up = up;
        this.down = down;
        this.price = price;
        this.change = change;
    }
}
