package com.example.newsapp.async;

import android.os.AsyncTask;

import com.example.newsapp.data.local.ArticleDao;
import com.example.newsapp.data.local.NewsRoomDatabase;
import com.example.newsapp.models.NewsItem;
import com.example.newsapp.models.Source;

public class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

    private final ArticleDao mDao;

    public PopulateDbAsyncTask(NewsRoomDatabase db) {
        mDao = db.articleDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        NewsItem newsItem = new NewsItem();
        newsItem.setAuthor("AuthorName");
        newsItem.setTitle("NewsTitle");
        newsItem.setUrl("imageUrl");
        newsItem.setSource(new Source("BBC"));
        newsItem.setDescription("Description");
        newsItem.setPublishedAt("2 Jan 2020");

        mDao.insert(newsItem);
        return null;
    }
}