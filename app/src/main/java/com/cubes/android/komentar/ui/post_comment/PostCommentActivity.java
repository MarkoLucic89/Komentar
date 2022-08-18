package com.cubes.android.komentar.ui.post_comment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cubes.android.komentar.data.DataRepository;
import com.cubes.android.komentar.data.model.NewsCommentInsert;
import com.cubes.android.komentar.databinding.ActivityPostCommentBinding;

public class PostCommentActivity extends AppCompatActivity {

    private ActivityPostCommentBinding binding;

    private int newsId;
    private int replyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        if (content.length() < 1) {
            Toast.makeText(this, "Molim vas, unesite komentar", Toast.LENGTH_SHORT).show();
            return;
        }

        NewsCommentInsert newsCommentInsert = new NewsCommentInsert(
                newsId,
                replyId,
                name,
                email,
                content
        );

        DataRepository.getInstance().postComment(newsCommentInsert, new DataRepository.PostCommentResponseListener() {
            @Override
            public void onResponse(NewsCommentInsert response) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}