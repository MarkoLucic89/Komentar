package com.cubes.android.komentar.ui.subcategories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.ActivitySubcategoriesBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class SubcategoriesActivity extends AppCompatActivity {

    private ActivitySubcategoriesBinding binding;

    private Category mCategory;

    private int mCategoryId;
    private int mSubcategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubcategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCategoryId = getIntent().getIntExtra("category_id", -1);
        mSubcategoryId = getIntent().getIntExtra("subcategory_id", -1);

        getAllCategories(mCategoryId, mSubcategoryId);

        binding.imageViewBack.setOnClickListener(view -> finish());
        binding.imageViewRefresh.setOnClickListener(view -> getAllCategories(mCategoryId, mSubcategoryId));

    }

    private void getAllCategories(int mCategoryId, int mSubcategoryId) {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.imageViewRefresh.setVisibility(View.GONE);

        DataRepository.getInstance().getAllCategories(new DataRepository.CategoriesResponseListener() {
            @Override
            public void onResponse(ArrayList<Category> categories) {

                binding.progressBar.setVisibility(View.GONE);
                binding.linearLayoutCategoryPager.setVisibility(View.VISIBLE);

                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).id == mCategoryId) {
                        mCategory = categories.get(i);
                        break;
                    }
                }

                int currentSubcategoryPosition = -1;

                for (int i = 0; i < mCategory.subcategories.size(); i++) {
                    if (mSubcategoryId == mCategory.subcategories.get(i).id) {
                        currentSubcategoryPosition = i;
                        break;
                    }
                }

                int[] subcategotyIdList = new int[mCategory.subcategories.size()];

                for (int i = 0; i < mCategory.subcategories.size(); i++) {
                    subcategotyIdList[i] = mCategory.subcategories.get(i).id;
                }

                SubcategoryPagerAdapter adapter = new SubcategoryPagerAdapter(SubcategoriesActivity.this, subcategotyIdList);
                binding.viewPager.setAdapter(adapter);

                new TabLayoutMediator(
                        binding.tabLayout,
                        binding.viewPager,
                        (tab, position) -> {
                            tab.setText(mCategory.subcategories.get(position).name);
                        }
                ).attach();

                binding.viewPager.setCurrentItem(currentSubcategoryPosition);
            }

            @Override
            public void onFailure(Throwable t) {
                binding.imageViewRefresh.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.linearLayoutCategoryPager.setVisibility(View.GONE);
            }
        });

    }
}