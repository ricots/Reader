package com.bean.simplenews.module.news.model;

import com.bean.simplenews.bean.NewsBean;
import com.bean.simplenews.bean.NewsDetailBean;
import com.bean.simplenews.api.Urls;
import com.bean.simplenews.common.Constants;
import com.bean.simplenews.module.news.NewsJsonUtils;
import com.bean.simplenews.util.OkHttpUtils;

import java.util.List;

public class NewsModel implements INewsModel {

    /**
     * 加载新闻列表
     * @param url
     * @param listener
     */
    @Override
    public void loadNews(String url, final int type, final OnLoadNewsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    /**
     * 加载新闻详情
     * @param docid
     * @param listener
     */
    @Override
    public void loadNewsDetail(final String docid, final OnLoadNewsDetailListener listener) {
        String url = getDetailUrl(docid);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
                listener.onSuccess(newsDetailBean);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news detail info failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    /**
     * 获取ID
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case Constants.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case Constants.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case Constants.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case Constants.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    public interface OnLoadNewsListListener {
        void onSuccess(List<NewsBean> list);
        void onFailure(String msg, Exception e);
    }

    public interface OnLoadNewsDetailListener {
        void onSuccess(NewsDetailBean newsDetailBean);
        void onFailure(String msg, Exception e);
    }

}
