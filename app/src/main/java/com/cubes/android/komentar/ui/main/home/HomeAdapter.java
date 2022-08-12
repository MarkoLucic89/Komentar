package com.cubes.android.komentar.ui.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryBoxBinding;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.ui.main.home.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.ui.main.home.rv_item_home.RvItemModelCategoryBox;
import com.cubes.android.komentar.ui.main.home.rv_item_home.RvItemModelHomeSlider;
import com.cubes.android.komentar.ui.main.home.rv_item_home.RvItemModelHomeSmallNews;
import com.cubes.android.komentar.ui.main.home.rv_item_home.RvItemModelHomeVideo;
import com.cubes.android.komentar.ui.main.home.rv_item_home.RvItemModelTabs;
import com.cubes.android.komentar.ui.main.latest.NewsListener;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<ItemModelHome> list = new ArrayList<>();

    private NewsListener listener;

    public HomeAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void updateList(HomePageResponseModel.HomePageDataResponseModel response) {

        //SLIDER
        addSlider("SLIDER", response);

        //TOP NEWS
        for (News news : response.top) {
            list.add(new RvItemModelHomeSmallNews(news, listener, response.top));
        }

        //TABS
        list.add(new RvItemModelTabs(response, this, list.size(), listener));

        //SPORT CATEGORY BOX
        addCategoryBox("SPORT", response);

        //EDITORS CHOICE
        addSlider("EDITORS CHOICE", response);

        //VIDEO
        if (!response.videos.isEmpty()) {
            list.add(new RvItemModelHomeVideo(response.videos, listener));
        }

        //SHOWBIZ
        addCategoryBox("SHOWBIZ", response);  //EMPTY RESPONSE

        //POLITIKA
        addCategoryBox("POLITIKA", response);

        //SVET
        addCategoryBox("SVET", response);

        //HRONIKA
        addCategoryBox("HRONIKA", response);  //EMPTY RESPONSE

        //DRUŠTVO
        addCategoryBox("DRUŠTVO", response);

        //BIZNIS
        addCategoryBox("BIZNIS", response);  //EMPTY RESPONSE

        //STIL ŽIVOTA
        addCategoryBox("STIL ŽIVOTA", response);  //EMPTY RESPONSE

        //KULTURA
        addCategoryBox("KULTURA", response);

        //SLOBODNO VREME
        addCategoryBox("SLOBODNO VREME", response);  //EMPTY RESPONSE

        //SRBIJA
        addCategoryBox("SRBIJA", response);  //EMPTY RESPONSE

        //BEOGRAD
        addCategoryBox("BEOGRAD", response);

        //REGION
        addCategoryBox("REGION", response);

        notifyDataSetChanged();
    }

    private void addSlider(String title, HomePageResponseModel.HomePageDataResponseModel response) {

        if (response == null) {
            return;
        }

        if (title.equalsIgnoreCase("SLIDER")) {

            if (response.slider != null && !response.slider.isEmpty()) {
                list.add(new RvItemModelHomeSlider(response.slider, false, listener));
            }

        } else if (title.equalsIgnoreCase("EDITORS CHOICE")) {

            if (response.editors_choice != null && !response.editors_choice.isEmpty()) {
                list.add(new RvItemModelHomeSlider(response.editors_choice, true, listener));
            }

        }

    }

    private void addCategoryBox(String title, HomePageResponseModel.HomePageDataResponseModel response) {

        HomePageResponseModel.CategoryBoxResponseModel categoryBoxResponseModel = getCategoryForTitle(title, response);

        if (categoryBoxResponseModel != null && !categoryBoxResponseModel.news.isEmpty()) {
            list.add(new RvItemModelCategoryBox(categoryBoxResponseModel, listener));
        }

    }

    private HomePageResponseModel.CategoryBoxResponseModel getCategoryForTitle(String title, HomePageResponseModel.HomePageDataResponseModel response) {

        for (HomePageResponseModel.CategoryBoxResponseModel categoryBox : response.category) {
            if (categoryBox.title.equalsIgnoreCase(title)) {
                return categoryBox;
            }
        }

        return null;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewBinding binding;

        switch (viewType) {
            case R.layout.rv_item_home_slider:
                binding = RvItemHomeSliderBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_home_tabs:
                binding = RvItemHomeTabsBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_home_category_box:
                binding = RvItemHomeCategoryBoxBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_home_video:
                binding = RvItemHomeVideoBinding.inflate(inflater, parent, false);
                break;

            default:
                binding = null;
        }

        return new HomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        list.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public HomeViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
