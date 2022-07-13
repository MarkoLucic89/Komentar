package com.cubes.android.komentar.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.cubes.android.komentar.data.DataContainer;
import com.cubes.android.komentar.data.model.Category;
import com.cubes.android.komentar.databinding.ActivityCategoryBinding;
import com.cubes.android.komentar.ui.adapter.NewsCategoriesViewPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

public class CategoryActivity extends AppCompatActivity {

    private ActivityCategoryBinding binding;
    private Category mCategory;
    private Category mSubcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getCategoryAndSubcategory();

        binding.textViewTitle.setText(mCategory.name);

        binding.imageViewBack.setOnClickListener(view -> finish());

        initViewPager();
    }

    private void getCategoryAndSubcategory() {

        int categoryId = getIntent().getIntExtra("category_id", -1);
        int subcategoryId = getIntent().getIntExtra("subcategory_id", -1);

        for (Category category : DataContainer.categories) {
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

        NewsCategoriesViewPagerAdapter pagerAdapter = new NewsCategoriesViewPagerAdapter(this, mCategory.subcategories);
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
}