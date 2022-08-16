package com.cubes.android.komentar.ui.main.home.home_pager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.source.remote.networking.response.HomePageResponseModel;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelCategoryTitle;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelHomeCategoryBig;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelHomeSlider;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelHomeSmallNews;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelHomeVideos;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelTabs;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class HomePagerAdapter extends RecyclerView.Adapter<HomePagerAdapter.HomeViewHolder> {

    private ArrayList<ItemModelHome> list = new ArrayList<>();

    private NewsListener listener;

    public HomePagerAdapter(NewsListener listener) {
        this.listener = listener;
    }

    public void updateList(HomePageResponseModel.HomePageDataResponseModel response) {

        //SLIDER
        addSlider("SLIDER", response);

        //TOP NEWS
        int[] newsIdList = MyMethodsClass.initNewsIdList(response.top);

        for (News news : response.top) {
            list.add(new RvItemModelHomeSmallNews(news, listener, newsIdList));
        }

        //TABS
        list.add(new RvItemModelTabs(response, this, list.size(), listener));

        //SPORT CATEGORY BOX
        addCategoryBox("SPORT", response);



        //EDITORS CHOICE
        addSlider("EDITORS CHOICE", response);

        //VIDEO
        if (!response.videos.isEmpty()) {

//            list.add(new RvItemModelHomeVideo(response.videos, listener));

            int[] videosIdList = MyMethodsClass.initNewsIdList(response.videos);

            list.add(new RvItemModelCategoryTitle("Video", "#FE0000"));

            for (News news : response.videos) {
                list.add(new RvItemModelHomeVideos(news, listener, videosIdList));
            }
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

//            list.add(new RvItemModelCategoryBox(categoryBoxResponseModel, listener));

            list.add(new RvItemModelCategoryTitle(categoryBoxResponseModel.title, categoryBoxResponseModel.color));

            int[] newsIdList = MyMethodsClass.initNewsIdList(categoryBoxResponseModel.news);

            list.add(new RvItemModelHomeCategoryBig(categoryBoxResponseModel.news.get(0), true, listener, newsIdList));


            //servisi za svaki CategoryBox vracaju preko 20 vesti, zato je ovo trenutno zakomentarisano

//            for (int i = 1; i < categoryBoxResponseModel.news.size(); i++) {
//                list.add(new RvItemModelHomeSmallNews(categoryBoxResponseModel.news.get(i), listener, newsIdList));
//            }

            //trenutno je size 5 zbog testiranja
            for (int i = 1; i < 5; i++) {
                list.add(new RvItemModelHomeSmallNews(categoryBoxResponseModel.news.get(i), listener, newsIdList));
            }
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
            case R.layout.rv_item_category_big:
                binding = RvItemCategoryBigBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_home_tabs:
                binding = RvItemHomeTabsBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_videos:
                binding = RvItemVideosBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_home_category_title:
                binding = RvItemHomeCategoryTitleBinding.inflate(inflater, parent, false);
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
