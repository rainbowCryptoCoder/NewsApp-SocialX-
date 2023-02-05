package com.example.newsapp.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsItemAdapter;
import com.example.news.models.NewsItem;
import com.example.news.utils.DataStatus;
import com.example.news.viewmodels.NewsViewModel;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;

    private TextView emptyStateTextView;
    private TextView textViewTitle;

    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String keyword = "";

    private NewsViewModel mNewsViewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("News");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mContext = getActivity();
        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        textViewTitle = rootView.findViewById(R.id.text_view_top_headlines);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        adapter = new NewsItemAdapter(mContext);

        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString("keyword");
        }

        initEmptyRecyclerView();

        /**
         * Used to instantiate an instance of a ViewModel in an Android app.
         *
         * <p>ViewModelProviders is a class that provides access to ViewModel instances. ViewModel is a part of the
         * Android Jetpack architecture components and is used to store and manage UI-related data in a lifecycle-conscious way.
         * It allows data to survive configuration changes such as screen rotations, without the need to reload the data.
         *
         * <p>The code ViewModelProviders.of(this).get(NewsViewModel.class) gets a ViewModel of class NewsViewModel that is
         * associated with the current activity (indicated by this). If an instance of NewsViewModel already exists for this
         * activity, it is returned. Otherwise, a new instance is created.
         *
         * <p>The result is then stored in a class variable mNewsViewModel for further use in the activity. This way, the activity
         * can access and update the data stored in the NewsViewModel even if the activity is recreated due to a configuration
         * change.
         *
         */
        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        subscribeObservers();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mNewsViewModel.setKeyword(keyword);
        });

        setHasOptionsMenu(true);
        return rootView;
    }

    private void subscribeObservers() {
        mNewsViewModel.itemPagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<NewsItem>>() {
            @Override
            public void onChanged(PagedList<NewsItem> newsItems) {
                adapter.submitList(newsItems);
            }
        });
        mNewsViewModel.getDataStatus().observe(getViewLifecycleOwner(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                switch (dataStatus) {
                    case LOADED:
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.VISIBLE);
                        break;
                    case LOADING:
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case EMPTY:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_news_found);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        textViewTitle.setVisibility(View.INVISIBLE);
                        emptyStateTextView.setVisibility(View.VISIBLE);
                        emptyStateTextView.setText(R.string.no_internet_connection);
                        break;
                }
            }
        });
    }

    public void initEmptyRecyclerView() {

        /**This function is used to set up the RecyclerView with an empty adapter and a layout manager.
         The RecyclerView will be empty until data is added to the adapter.*/

        adapter = new NewsItemAdapter(mContext);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        searchKeywordFromSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchKeywordFromSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    keyword = query;
                    textViewTitle.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    mNewsViewModel.setKeyword(query);
                } else {
                    Toast.makeText(mContext, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                keyword = "";
                return true;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("keyword", keyword);
    }
}