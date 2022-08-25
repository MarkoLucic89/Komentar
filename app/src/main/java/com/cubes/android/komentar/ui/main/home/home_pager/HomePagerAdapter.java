package com.cubes.android.komentar.ui.main.home.home_pager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.HomePageData;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategoryBigBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;
import com.cubes.android.komentar.databinding.RvItemVideosBinding;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelCategoryTitle;
import com.cubes.android.komentar.ui.main.home.home_pager.rv_item_home.RvItemModelHomeAd;
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

    public void updateList(HomePageData data) {

        list.clear();

        //SLIDER
        addSlider("SLIDER", data);

        //AD 1
        list.add(new RvItemModelHomeAd());

        //TOP NEWS
        int[] newsIdList = MyMethodsClass.initNewsIdList(data.top);

        for (News news : data.top) {
            list.add(new RvItemModelHomeSmallNews(news, listener, newsIdList));
        }

        //TABS
        list.add(new RvItemModelTabs(data, this, list.size(), listener));

        //AD 2
        list.add(new RvItemModelHomeAd());

        //SPORT CATEGORY BOX
        addCategoryBox("SPORT", data);


        //AD 3
        list.add(new RvItemModelHomeAd());

        //EDITORS CHOICE
        addSlider("EDITORS CHOICE", data);

        //AD 4
        list.add(new RvItemModelHomeAd());

        //VIDEO
        if (!data.videos.isEmpty()) {

//            list.add(new RvItemModelHomeVideo(response.videos, listener));

            int[] videosIdList = MyMethodsClass.initNewsIdList(data.videos);

            list.add(new RvItemModelCategoryTitle("Video", "#FE0000"));

            for (News news : data.videos) {
                list.add(new RvItemModelHomeVideos(news, listener, videosIdList));
            }
        }

        //AD 5
        list.add(new RvItemModelHomeAd());

        //SHOWBIZ
        addCategoryBox("SHOWBIZ", data);  //EMPTY RESPONSE

        //POLITIKA
        addCategoryBox("POLITIKA", data);

        //SVET
        addCategoryBox("SVET", data);

        //HRONIKA
        addCategoryBox("HRONIKA", data);  //EMPTY RESPONSE

        //DRUŠTVO
        addCategoryBox("DRUŠTVO", data);

        //BIZNIS
        addCategoryBox("BIZNIS", data);  //EMPTY RESPONSE

        //STIL ŽIVOTA
        addCategoryBox("STIL ŽIVOTA", data);  //EMPTY RESPONSE

        //KULTURA
        addCategoryBox("KULTURA", data);

        //SLOBODNO VREME
        addCategoryBox("SLOBODNO VREME", data);  //EMPTY RESPONSE

        //SRBIJA
        addCategoryBox("SRBIJA", data);  //EMPTY RESPONSE

        //BEOGRAD
        addCategoryBox("BEOGRAD", data);

        //REGION
        addCategoryBox("REGION", data);

        notifyDataSetChanged();
    }

    private void addSlider(String title, HomePageData data) {

        if (title.equalsIgnoreCase("SLIDER")) {

            if (data.slider != null && !data.slider.isEmpty()) {
                list.add(new RvItemModelHomeSlider(data.slider, false, listener));
            }

        } else if (title.equalsIgnoreCase("EDITORS CHOICE")) {

            if (data.editorsChoice != null && !data.editorsChoice.isEmpty()) {
                list.add(new RvItemModelHomeSlider(data.editorsChoice, true, listener));
            }

        }

    }

    private void addCategoryBox(String title, HomePageData data) {

        HomePageData.CategoryBox categoryBox = getCategoryForTitle(title, data);

        if (categoryBox != null && !categoryBox.news.isEmpty()) {

//            list.add(new RvItemModelCategoryBox(categoryBoxResponseModel, listener));

            list.add(new RvItemModelCategoryTitle(categoryBox.title, categoryBox.color));

            int[] newsIdList = MyMethodsClass.initNewsIdList(categoryBox.news);

            list.add(new RvItemModelHomeCategoryBig(categoryBox.news.get(0), true, listener, newsIdList));


            //servisi za svaki CategoryBox vracaju preko 20 vesti, zato je ovo trenutno zakomentarisano

//            for (int i = 1; i < categoryBoxResponseModel.news.size(); i++) {
//                list.add(new RvItemModelHomeSmallNews(categoryBoxResponseModel.news.get(i), listener, newsIdList));
//            }

            //trenutno je size 5 zbog testiranja
            for (int i = 1; i < 5; i++) {
                list.add(new RvItemModelHomeSmallNews(categoryBox.news.get(i), listener, newsIdList));
            }
        }

    }

    private HomePageData.CategoryBox getCategoryForTitle(String title, HomePageData data) {

        for (HomePageData.CategoryBox categoryBox : data.category) {
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
            case R.layout.rv_item_ad:
                binding = RvItemAdBinding.inflate(inflater, parent, false);
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
