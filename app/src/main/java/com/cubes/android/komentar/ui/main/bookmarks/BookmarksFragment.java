package com.cubes.android.komentar.ui.main.bookmarks;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.source.local.database.dao.NewsBookmarksDao;
import com.cubes.android.komentar.databinding.FragmentBookmarksBinding;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.ui.comments.CommentsActivity;
import com.cubes.android.komentar.ui.detail.DetailsActivity;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
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

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        binding.textViewClearAll.setOnClickListener(view1 -> {

//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//            builder.setTitle(getContext().getResources().getString(R.string.bookmarks_title));
//            builder.setMessage(getContext().getResources().getString(R.string.bookmarks_message));
//            builder.setPositiveButton(getContext().getResources().getString(R.string.clear_all), (dialogInterface, i) -> {
//                deleteAllFavorites();
//            });
//            builder.setNegativeButton(getContext().getResources().getString(R.string.bookmarks_cancel), (dialogInterface, i) -> {
//
//            });
//            builder.create().show();

            BookmarkDialogFragment bookmarkDialogFragment = new BookmarkDialogFragment();
            bookmarkDialogFragment.show(getActivity().getSupportFragmentManager(), null);

        });

//        MyMethodsClass.animationSwipe(binding.swipeToast, 200, 200);

    }

    private void getBookmarks() {

        binding.swipeRefreshLayout.setRefreshing(true);

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            bookmarkNews = (ArrayList<News>) bookmarksDao.getBookmarkNews();

            //onPostExecute
            handler.post(() -> {

                updateUiIsEmptyList(bookmarkNews);

                adapter.initList(bookmarkNews);

                binding.swipeRefreshLayout.setRefreshing(false);

            });

        });

        service.shutdown();

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.closeAllMenus();
    }

    private void updateUiIsEmptyList(ArrayList<News> bookmarkNews) {
        if (bookmarkNews.isEmpty()) {
            binding.textViewListIsEmpty.setVisibility(View.VISIBLE);
            binding.textViewClearAll.setEnabled(false);
            binding.textViewClearAll.setTextColor(getContext().getResources().getColor(R.color.grey_text));
        } else {
            binding.textViewListIsEmpty.setVisibility(View.GONE);
            binding.textViewClearAll.setEnabled(true);
            binding.textViewClearAll.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBookmarks();
    }

    private News deletedNews = null;

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT
    ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            Collections.swap(bookmarkNews, viewHolder.getAdapterPosition(), target.getAdapterPosition());

            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            if (direction == ItemTouchHelper.RIGHT) {
                removeNewsFromFavorites(viewHolder);
            }

        }

    };


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
                updateUiIsEmptyList(bookmarkNews);
            });

        });

        service.shutdown();
    }

    private void removeNewsFromFavorites(RecyclerView.ViewHolder viewHolder) {

        int position = viewHolder.getAdapterPosition();
        News news = bookmarkNews.get(position);

        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            bookmarksDao.delete(news);

            //onPostExecute
            handler.post(() -> {
                bookmarkNews.remove(position);
                adapter.initList(bookmarkNews);
//                Toast.makeText(getContext(), "Vest je uspešno uklonjena", Toast.LENGTH_SHORT).show();
                updateUiIsEmptyList(bookmarkNews);


                Snackbar.make(binding.recyclerView, news.title, Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> {

                            ExecutorService service2 = Executors.newSingleThreadExecutor();
                            Handler handler2 = new Handler(Looper.getMainLooper());
                            service2.execute(() -> {

                                //doInBackgroundThread
                                bookmarksDao.insert(news);

                                handler2.post(() -> {


                                    bookmarkNews.add(position, news);
                                    adapter.initList(bookmarkNews);

                                    Toast.makeText(getContext(), "Vest je uspešno vracena", Toast.LENGTH_SHORT).show();

                                });

                            });

                            service2.shutdown();

                        }).show();

            });

        });

        service.shutdown();
    }

    public void deleteAllFavorites() {


        //Room
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(() -> {

            //doInBackgroundThread
            bookmarksDao.clearBookmarkList();

            //onPostExecute
            handler.post(() -> {

                adapter.clearList();
                bookmarkNews.clear();
                updateUiIsEmptyList(bookmarkNews);
            });

        });

        service.shutdown();
    }

    @Override
    public void closeOtherMenus() {
        adapter.closeAllMenus();
    }
}