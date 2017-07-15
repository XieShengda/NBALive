package com.sender.nbalive;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XieShengda on 2017/3/13.
 */

public class Reptile {
    private static final String URL_STRING = "http://www.imaijia.com/";
    private static Document document;
    private static Map<String, String> tabMap;

    public static List<List<NewsBean>> getNews() {
        List<List<NewsBean>> lists = new ArrayList<>();
        tabMap = new HashMap<>();
        tabMap.put("最新文章", "article-new");
        tabMap.put("零售电商", "article-ds");
        tabMap.put("服饰美妆", "article-fsmz");
        tabMap.put("生鲜食品", "article-sxsp");
        tabMap.put("数码家电", "article-smjd");
        tabMap.put("跨境电商", "article-kj");
        try {
            document = Jsoup.connect(URL_STRING).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lists.add(getNewsList("最新文章"));

        return lists;

    }

    private static List<NewsBean> getNewsList(String tabName){
        List<NewsBean> newsBeanList = new ArrayList<>();
        String divId = tabMap.get(tabName);
        Log.d("divId", "getNewsList: " + divId);
        Element newsList = document.getElementById(divId);
        Elements newsItems = newsList.getElementsByClass("article-list-detail clearfix");
        for(Element element : newsItems){
            Elements img = element.getElementsByClass("article-list-detail-pic");
            Log.d("getNewsList", "getNewsList: " + img.getClass().getSimpleName());
            String imgUrl = "http://www.imaijia.com" + img.get(0).getElementsByClass("article-list-detail-pic-img").get(0).attr("src");
            String newsUrl = "http://www.imaijia.com" + img.get(0).attr("href");
            String title = element.getElementsByClass("detail-cont-tit").get(0).text();
            NewsBean newsBean = new NewsBean();
            newsBean.setImgUrl(imgUrl);
            newsBean.setNewsUrl(newsUrl);
            newsBean.setTitle(title);
            newsBeanList.add(newsBean);
        }

        return newsBeanList;

    }





}
