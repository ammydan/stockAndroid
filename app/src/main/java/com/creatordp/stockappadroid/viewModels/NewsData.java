package com.creatordp.stockappadroid.viewModels;

import java.util.Date;

public class NewsData {
    public Date publishDate;
    public String title;
    public String article;
    public String imageUrl;
    public String sourceName;
    public String url;
    public NewsData(Date publishDate, String title, String article, String imageUrl, String sourceName, String url){
        this.publishDate = publishDate;
        this.title = title;
        this.article = article;
        this.imageUrl = imageUrl;
        this.sourceName = sourceName;
        this.url = url;
    }
}
