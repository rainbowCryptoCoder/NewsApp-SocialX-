package com.example.newsapp.viewModels;

import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsapp.models.NewsItem;
import com.example.newsapp.pagination.articles.ArticlesDataSource;
import com.example.newsapp.pagination.articles.ArticlesDataSourceFactory;
import com.example.newsapp.utils.DataStatus;

public class ArticlesViewModel extends ViewModel {

    public LiveData<PagedList<NewsItem>> itemPagedList;
    private MutableLiveData<ArticlesDataSource> liveDataSource;
    private ArticlesDataSourceFactory articlesDataSourceFactory;
    private LiveData dataStatus;

    public ArticlesViewModel() {

        articlesDataSourceFactory = new ArticlesDataSourceFactory();
        liveDataSource = articlesDataSourceFactory.getArticlesLiveDataSource();
        dataStatus = articlesDataSourceFactory.getDataStatusLiveData();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build();

        itemPagedList = (new LivePagedListBuilder(articlesDataSourceFactory, pagedListConfig)).build();
    }

    public void setKeyword(String query) {
        articlesDataSourceFactory.setQuery(query);
        refreshData();
    }

    void refreshData() {
        if (itemPagedList.getValue() != null) {
            itemPagedList.getValue().getDataSource().invalidate();
        }
    }

    public LiveData<DataStatus> getDataStatus() {
        return dataStatus;
    }
}
