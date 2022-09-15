package com.cubes.android.komentar.ui.main.bookmarks;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.ui.main.bookmarks.rv_model_search.ItemModelBookmarks;
import com.cubes.android.komentar.ui.main.bookmarks.rv_model_search.RvItemModelBookmarks;
import com.cubes.android.komentar.ui.main.bookmarks.rv_model_search.RvItemModelBookmarksAd;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder> {

    private ArrayList<ItemModelBookmarks> itemModels = new ArrayList<>();
    private NewsListener listener;

    private int[] newsIdList;

//    private int adsCounter = 0;

    public BookmarksAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void initList(ArrayList<News> newsList) {

//        adsCounter = 0;

        itemModels.clear();

        newsIdList = MyMethodsClass.initNewsIdList(newsList);


        for (int i = 0; i < newsList.size(); i++) {

//            if (i > 0 && i % 5 == 0 && adsCounter < 5) {
//
//                itemModels.add(new RvItemModelBookmarksAd());
//                adsCounter++;
//            }

            itemModels.add(new RvItemModelBookmarks(newsList.get(i), listener, newsIdList));

        }

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewBinding binding;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_ad:
                binding = RvItemAdBinding.inflate(inflater, parent, false);
                AdRequest adRequest = new AdRequest.Builder().build();
                ((RvItemAdBinding) binding).adView.loadAd(adRequest);
                break;
            default:
                binding = null;
        }

        return new BookmarkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        itemModels.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemModels.get(position).getType();
    }


    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public BookmarkViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

