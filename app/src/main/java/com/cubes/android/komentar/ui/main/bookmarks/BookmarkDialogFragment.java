package com.cubes.android.komentar.ui.main.bookmarks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cubes.android.komentar.databinding.DialogFragmentBookmarksBinding;

public class BookmarkDialogFragment extends DialogFragment {

    private DialogFragmentBookmarksBinding binding;

    private DeleteAllBookmarksListener listener;

    public interface DeleteAllBookmarksListener {
        void onDeleteALlBookmarks();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DeleteAllBookmarksListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentBookmarksBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(null);


        binding.imageViewClose.setOnClickListener(view1 -> dismiss());

        binding.textViewCancel.setOnClickListener(view1 -> dismiss());

        binding.textViewDeleteAll.setOnClickListener(view1 -> {
            listener.onDeleteALlBookmarks();
            dismiss();
        });

    }
}
