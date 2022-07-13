package com.cubes.android.komentar.data.model.rv_item_model.rv_item_drawer;


import android.view.View;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.databinding.RvItemDrawerCategoryBinding;
import com.cubes.android.komentar.listeners.OnCategoryClickListener;
import com.cubes.android.komentar.ui.adapter.DrawerAdapter;


public class RvItemModelDrawerHome implements ItemModelDrawer {

    private OnCategoryClickListener listener;

    public RvItemModelDrawerHome(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void bind(DrawerAdapter.DrawerViewHolder holder) {

        RvItemDrawerCategoryBinding binding = (RvItemDrawerCategoryBinding) holder.binding;

        binding.textView.setText("Naslovna");
        binding.viewIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        binding.imageViewArrow.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(view -> listener.onCategoryClicked(0));

    }

}
