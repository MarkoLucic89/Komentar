package com.cubes.android.komentar.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.response.home_response.CategoryBoxResponseModel;
import com.cubes.android.komentar.data.model.response.home_response.HomePageResponseModel;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.ItemModelHome;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.RvItemModelCategoryBox;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.RvItemModelHomeSlider;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.RvItemModelHomeSmallNews;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.RvItemModelHomeVideo;
import com.cubes.android.komentar.data.model.rv_item_model.rv_item_home.RvItemModelTabs;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryBoxBinding;
import com.cubes.android.komentar.databinding.RvItemHomeSliderBinding;
import com.cubes.android.komentar.databinding.RvItemHomeTabsBinding;
import com.cubes.android.komentar.databinding.RvItemHomeVideoBinding;

import java.util.ArrayList;

import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private ArrayList<ItemModelHome> list;

    public HomeAdapter(Response<HomePageResponseModel> response) {

        list = new ArrayList<>();

        //SLIDER
        addSlider("SLIDER", response);

        //TOP NEWS
        for (News news : response.body().data.top) {
            list.add(new RvItemModelHomeSmallNews(news));
        }

        //TABS
        list.add(new RvItemModelTabs(response.body().data, this, list.size()));

        //SPORT CATEGORY BOX
        addCategoryBox("SPORT", response);

        //EDITORS CHOICE
        addSlider("EDITORS CHOICE", response);

        //VIDEO
        if (!response.body().data.videos.isEmpty()) {
            list.add(new RvItemModelHomeVideo(response.body().data.videos));
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


    }

    private void addSlider(String title, Response<HomePageResponseModel> response) {

        if (response.body() == null || response.body().data == null) {
            return;
        }

        if (title.equalsIgnoreCase("SLIDER")) {

            if (response.body().data.slider != null && !response.body().data.slider.isEmpty()) {
                list.add(new RvItemModelHomeSlider(response.body().data.slider));
            }

        } else if (title.equalsIgnoreCase("EDITORS CHOICE")) {


//            list.add(new RvItemModelHomeSlider(response.body().data.slider, true));   //BRISI OVO!!!

            if (response.body().data.editors_choice != null && !response.body().data.editors_choice.isEmpty()) {
                list.add(new RvItemModelHomeSlider(response.body().data.editors_choice, true));
            }

        } else {

        }

    }

    private void addCategoryBox(String title, Response<HomePageResponseModel> response) {

        CategoryBoxResponseModel categoryBoxResponseModel = getCategoryForTitle(title, response);

        if (categoryBoxResponseModel != null && !categoryBoxResponseModel.news.isEmpty()) {
            list.add(new RvItemModelCategoryBox(categoryBoxResponseModel));
        }

    }

    private CategoryBoxResponseModel getCategoryForTitle(String title, Response<HomePageResponseModel> response) {

        for (CategoryBoxResponseModel categoryBox : response.body().data.category) {
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
            case 0:
                binding = RvItemHomeSliderBinding.inflate(inflater, parent, false);
                break;
            case 1:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case 2:
                binding = RvItemHomeTabsBinding.inflate(inflater, parent, false);
                break;
            case 3:
                binding = RvItemHomeCategoryBoxBinding.inflate(inflater, parent, false);
                break;
            case 4:
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
