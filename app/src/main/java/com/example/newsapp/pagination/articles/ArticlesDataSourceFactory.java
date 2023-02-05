package com.example.newsapp.pagination.articles;

import android.arch.paging.DataSource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.newsapp.models.NewsItem;
import com.example.newsapp.utils.DataStatus;

public class ArticlesDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<ArticlesDataSource> itemLiveDataSource = new MutableLiveData<>();
    private String mQuery;
    private final LiveData<DataStatus> dataStatusLiveData = Transformations
            .switchMap(itemLiveDataSource, ArticlesDataSource::getDataStatusMutableLiveData);

    public ArticlesDataSourceFactory() {
        mQuery = "news";
    }

    @Override
    public DataSource<Integer, NewsItem> create() {
        ArticlesDataSource itemDataSource = new ArticlesDataSource(mQuery);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<ArticlesDataSource> getArticlesLiveDataSource() {
        return itemLiveDataSource;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public LiveData<DataStatus> getDataStatusLiveData() {
        return dataStatusLiveData;
    }
}