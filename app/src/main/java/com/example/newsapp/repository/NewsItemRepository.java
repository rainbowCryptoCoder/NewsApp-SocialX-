package com.example.newsapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.newsapp.async.DeleteAllNotesAsyncTask;
import com.example.newsapp.async.DeleteNoteAsyncTask;
import com.example.newsapp.async.InsertNoteAsyncTask;
import com.example.newsapp.data.local.ArticleDao;
import com.example.newsapp.data.local.NewsRoomDatabase;
import com.example.newsapp.data.remote.NewsApiClient;
import com.example.newsapp.models.NewsItem;

import java.util.List;

public class NewsItemRepository {

    private ArticleDao mArticleDao;
    private LiveData<List<NewsItem>> mAllSavedArticles;

    private static NewsItemRepository instance;
    private NewsApiClient mNewsApiClient;

    public static NewsItemRepository getInstance() {
        if (instance == null) {
            instance = new NewsItemRepository();
        }
        return instance;
    }

    public NewsItemRepository() {
        mNewsApiClient = NewsApiClient.getInstance();
    }

    public NewsItemRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
        mAllSavedArticles = mArticleDao.getAllSavedArticles();
    }

    public LiveData<List<NewsItem>> getAllSavedArticles() {
        return mAllSavedArticles;
    }

    public void insert(NewsItem newsItem) {
        new InsertNoteAsyncTask(mArticleDao).execute(newsItem);
    }

    public void delete(NewsItem newsItem) {
        new DeleteNoteAsyncTask(mArticleDao).execute(newsItem);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(mArticleDao).execute();
    }
}
