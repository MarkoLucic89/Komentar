package com.cubes.android.komentar.ui.main.bookmarks;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.databinding.FragmentBookmarksBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.CategoryAdapter;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookmarksFragment extends Fragment implements NewsListener {

    private FragmentBookmarksBinding binding;

    private NewsBookmarksDao bookmarksDao;

    private BookmarksAdapter adapter;

    private ArrayList<News> bookmarkNews;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;

        bookmarksDao = appContainer.room.bookmarksDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BookmarksAdapter(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.swipeRefreshLayout.setRefreshing(true);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
             bookmarkNews = (ArrayList<News>) bookmarksDao.getBookmarkNews();

            //onPostExecute
            handler.post(() -> {

                adapter.initList(bookmarkNews);

                binding.swipeRefreshLayout.setRefreshing(false);

            });


        });

        service.shutdown();

    }

    @Override
    public void onResume() {
        super.onResume();

        binding.swipeRefreshLayout.setRefreshing(true);

        adapter.notifyDataSetChanged();

        binding.swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onNewsClicked(int newsId, int[] newsIdList) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("news_id", newsId);
        intent.putExtra("news_id_list", newsIdList);
        getContext().startActivity(intent);
    }

    @Override
    public void onNewsMenuCommentsClicked(int newsId) {
        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        intent.putExtra("news_id", newsId);
        getContext().startActivity(intent);
    }

    @Override
    public void onNewsMenuShareClicked(String url) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onNewsMenuFavoritesClicked(News news) {

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            bookmarksDao.delete(news);

            //onPostExecute
            handler.post(() -> {
                bookmarkNews.remove(news);
                adapter.initList(bookmarkNews);
                Toast.makeText(getContext(), "Vest je uspešno uklonjena", Toast.LENGTH_SHORT).show();
            });

        });

        service.shutdown();
    }

}