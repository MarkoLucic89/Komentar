package com.cubes.android.komentar.ui.post_comment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.di.AppContainer;
import com.cubes.android.komentar.di.MyApplication;
import com.cubes.android.komentar.data.model.NewsCommentInsertApi;
import com.cubes.android.komentar.databinding.ActivityPostCommentBinding;

public class PostCommentActivity extends AppCompatActivity {

    private ActivityPostCommentBinding binding;

    private int newsId;
    private int replyId;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        dataRepository = appContainer.dataRepository;

        newsId = getIntent().getIntExtra("news", 0);
        replyId = getIntent().getIntExtra("reply_id", 0);

        binding.buttonPostComment.setOnClickListener(view -> postComment());

    }

    private void postComment() {

        String name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String content = binding.editTextContent.getText().toString().trim();

        if (name.length() < 1) {
            Toast.makeText(this, "Molim vas, unesite vaše ime", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.length() < 1) {
            Toast.makeText(this, "Molim vas, unesite vašu E-mail adresu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(this, "E-mail adresa je neispravna, pokušajte ponovo", Toast.LENGTH_SHORT).show();
            binding.editTextEmail.getText().clear();
            return;
        }

        if (content.length() < 1) {
            Toast.makeText(this, "Molim vas, unesite komentar", Toast.LENGTH_SHORT).show();
            return;
        }

        NewsCommentInsertApi newsCommentInsert = new NewsCommentInsertApi(
                newsId,
                replyId,
                name,
                email,
                content
        );

        dataRepository.postComment(newsCommentInsert, new DataRepository.PostCommentResponseListener() {
            @Override
            public void onResponse(NewsCommentInsertApi response) {
                Toast.makeText(PostCommentActivity.this, "Komentar je uspešno unet.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PostCommentActivity.this, "Komentar nije unet, došlo je do greške.", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        DRUGI NACIN:
         */

//        NewsApi
//                .getInstance()
//                .getNewsService()
//                .postComment(
//                        news,
//                        reply_id,
//                        name,
//                        email,
//                        content
//                )
//                .enqueue(new Callback<NewsCommentInsert>() {
//                    @Override
//                    public void onResponse(Call<NewsCommentInsert> call, Response<NewsCommentInsert> response) {
//
//                        if (response.isSuccessful()) {
//                            Toast.makeText(PostCommentActivity.this, "CODE: " + response.code(),
//                                    Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewsCommentInsert> call, Throwable t) {
//                        Toast.makeText(PostCommentActivity.this,
//                                "Komentar nije uspešno unet, došlo je do greške", Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        dataRepository = null;

    }
}