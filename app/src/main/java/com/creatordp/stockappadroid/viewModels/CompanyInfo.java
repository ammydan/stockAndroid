package com.creatordp.stockappadroid.viewModels;

import java.util.Date;

public class CompanyInfo {
    public String ticker;
    public String name;
    public String des;
    public String exchangeCode;
    public CompanyInfo(String ticker, String name, String des, String exchangeCode){
        this.ticker = ticker;
        this.name = name;
        this.des = des;
        this.exchangeCode = exchangeCode;
    }
}
