package com.cubes.android.komentar.ui.category;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.domain.Category;
import com.cubes.android.komentar.databinding.ActivityCategoryBinding;
import com.cubes.android.komentar.ui.subcategories.SubcategoryPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


/*
NE KORISTI SE VISE
 */

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private Category mCategory;
    private Category mSubcategory;

    private int mCategoryId;
    private int mSubcategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getAllCategories();

        binding.imageViewBack.setOnClickListener(view -> finish());
        binding.imageViewRefresh.setOnClickListener(view -> getAllCategories());

    }

    private void getAllCategories() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.linearLayoutCategoryPager.setVisibility(View.GONE);
        binding.imageViewRefresh.setVisibility(View.GONE);

        mCategoryId = getIntent().getIntExtra("category_id", -1);
        mSubcategoryId = getIntent().getIntExtra("subcategory_id", -1);

        DataRepository.getInstance().getAllCategories(new DataRepository.CategoriesResponseListener() {
            @Override
            public void onResponse(ArrayList<Category> categories) {

                binding.progressBar.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.GONE);
                binding.linearLayoutCategoryPager.setVisibility(View.VISIBLE);

                getCategoryAndSubcategory(categories);

                binding.textViewTitle.setText(mCategory.name);

                binding.imageViewBack.setOnClickListener(view -> finish());

                initViewPager();

            }

            @Override
            public void onFailure(Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.linearLayoutCategoryPager.setVisibility(View.GONE);
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getCategoryAndSubcategory(ArrayList<Category> categories) {

//        int categoryId = getIntent().getIntExtra("category_id", -1);
//        int subcategoryId = getIntent().getIntExtra("subcategory_id", -1);

        for (Category category : categories) {
            if (category.id == mCategoryId) {
                mCategory = category;
                break;
            }
        }

        for (Category category : mCategory.subcategories) {
            if (category.id == mSubcategoryId) {
                mSubcategory = category;
                break;
            }
        }
    }

    private void initViewPager() {

        int[] idList = new int[mCategory.subcategories.size()];

        for (int i = 0; i < mCategory.subcategories.size(); i++) {
            idList[i] = mCategory.subcategories.get(i).id;
        }



        SubcategoryPagerAdapter adapter = new SubcategoryPagerAdapter(this, idList);
        binding.viewPager.setAdapter(adapter);


        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> {
                    tab.setText(mCategory.subcategories.get(position).name);
                }
        ).attach();

        binding.viewPager.setCurrentItem(mCategory.subcategories.indexOf(mSubcategory));

        Log.d("ADAPTER", "POSITION: " + mCategory.subcategories.indexOf(mSubcategory));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}