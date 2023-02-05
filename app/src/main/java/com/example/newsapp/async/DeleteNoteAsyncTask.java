package com.example.newsapp.async;

import android.os.AsyncTask;

import com.example.newsapp.data.local.ArticleDao;
import com.example.newsapp.models.NewsItem;

public class DeleteNoteAsyncTask extends AsyncTask<NewsItem, Void, Void> {

    private ArticleDao myAsyncTaskDao;

    public DeleteNoteAsyncTask(ArticleDao noteDao) {
        myAsyncTaskDao = noteDao;
    }

    @Override
    protected Void doInBackground(final NewsItem... newsItems) {
        myAsyncTaskDao.deleteArticle(newsItems[0]);
        return null;
    }
}