package com.example.newsapp.async;

import android.os.AsyncTask;

import com.example.newsapp.data.local.ArticleDao;
import com.example.newsapp.models.NewsItem;

public class InsertNoteAsyncTask extends AsyncTask<NewsItem, Void, Void> {

    private ArticleDao mAsyncTaskDao;

    public InsertNoteAsyncTask(ArticleDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(NewsItem... newsItems) {
        mAsyncTaskDao.insert(newsItems[0]);
        return null;
    }
}
