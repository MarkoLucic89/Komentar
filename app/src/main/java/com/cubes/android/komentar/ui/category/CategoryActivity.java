package com.cubes.android.komentar.ui.category;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.data.source.remote.networking.NewsApi;
import com.cubes.android.komentar.data.source.remote.networking.response.CategoriesResponseModel;
import com.cubes.android.komentar.databinding.ActivityCategoryBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private Category mCategory;
    private Category mSubcategory;
    private ArrayList<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DataRepository.getInstance().getAllCategories(new DataRepository.CategoriesResponseListener() {
            @Override
            public void onResponse(ArrayList<Category> categories) {

                mCategories = categories;

                getCategoryAndSubcategory(categories);

                binding.textViewTitle.setText(mCategory.name);

                binding.imageViewBack.setOnClickListener(view -> finish());

                initViewPager();

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void getCategoryAndSubcategory(ArrayList<Category> categories) {

        int categoryId = getIntent().getIntExtra("category_id", -1);
        int subcategoryId = getIntent().getIntExtra("subcategory_id", -1);

        for (Category category : categories) {
            if (category.id == categoryId) {
                mCategory = category;
                break;
            }
        }

        for (Category category : mCategory.subcategories) {
            if (category.id == subcategoryId) {
                mSubcategory = category;
                break;
            }
        }
    }


    private void initViewPager() {

        NewsCategoriesViewPagerAdapter pagerAdapter = new NewsCategoriesViewPagerAdapter(this, mCategories,  mCategory.subcategories);
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> {
                        tab.setText(mCategory.subcategories.get(position).name);
                }
        ).attach();

        binding.viewPager.setCurrentItem(mCategory.subcategories.indexOf(mSubcategory));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}