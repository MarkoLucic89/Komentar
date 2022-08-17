package com.cubes.android.komentar.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.News;
import com.cubes.android.komentar.data.model.NewsComment;
import com.cubes.android.komentar.data.source.remote.networking.response.NewsDetailsResponseModel;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsAllCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsTitleBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.comments.rv_item_comments.ItemModelComments;
import com.cubes.android.komentar.ui.detail.news_detail_activity_with_viewpager.DetailsFragment;
import com.cubes.android.komentar.ui.detail.rv_item_details.ItemModelDetails;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemDetailsButtonAllComments;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailComment;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsComments;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsHeader;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsRelatedNews;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsSmallNews;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsTags;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsTitle;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsWebView;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsHeaderBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsRelatedNewsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsTagsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;

import java.util.ArrayList;

public class NewsDetailsAdapter extends RecyclerView.Adapter<NewsDetailsAdapter.NewsDetailsViewHolder> {

    private ArrayList<ItemModelDetails> list = new ArrayList<>();
    private CommentsAdapter.CommentsListener commentsListener;
    private NewsDetailsTagsAdapter.TagListener tagListener;
    private NewsListener newsListener;

    public NewsDetailsAdapter(Context context) {
        this.commentsListener = (CommentsAdapter.CommentsListener) context;
        this.tagListener = (NewsDetailsTagsAdapter.TagListener) context;
        this.newsListener = (NewsListener) context;
    }

    public NewsDetailsAdapter(CommentsAdapter.CommentsListener commentsListener, NewsDetailsTagsAdapter.TagListener tagListener, NewsListener newsListener) {
        this.commentsListener = commentsListener;
        this.tagListener = tagListener;
        this.newsListener = newsListener;
    }

    public void updateList(NewsDetailsResponseModel.NewsDetailsDataResponseModel newsDetails) {

        list.clear();

        //HEADER
        list.add(new RvItemModelDetailsHeader(newsDetails));

        //WEB VIEW
        list.add(new RvItemModelDetailsWebView(newsDetails.url));

        //TAGS
        list.add(new RvItemModelDetailsTags(tagListener, newsDetails.tags));

        //COMMENTS

//        list.add(new RvItemModelDetailsComments(commentsListener, newsDetails));

        list.add(new RvItemModelDetailsTitle(newsDetails.comments_count, newsDetails.id, commentsListener));

        for (NewsComment comment : newsDetails.comments_top_n) {

            list.add(new RvItemModelDetailComment(comment, commentsListener, false));

            for (NewsComment subComment : comment.children) {

                list.add(new RvItemModelDetailComment(subComment, commentsListener, true));

            }
        }
        if (newsDetails.comments_count > 0) {
            list.add(new RvItemDetailsButtonAllComments(newsDetails.id, newsDetails.comments_count, commentsListener));
        }

        //RELATED NEWS

//        list.add(new RvItemModelDetailsRelatedNews(newsListener, newsDetails.related_news));

        int[] relatedNewsIdList = MyMethodsClass.initNewsIdList(newsDetails.related_news);

        for (News news : newsDetails.related_news) {
            list.add(new RvItemModelDetailsSmallNews(news, false, newsListener, relatedNewsIdList));
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewBinding binding;

        switch (viewType) {
            case R.layout.rv_item_details_header:
                binding = RvItemDetailsHeaderBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_details_web_view:
                binding = RvItemDetailsWebViewBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_details_tags:
                binding = RvItemDetailsTagsBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_details_comments:
                binding = RvItemDetailsCommentsBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_details_title:
                binding = RvItemDetailsTitleBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_details_all_comments:
                binding = RvItemDetailsAllCommentsBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            default:
                binding = null;

        }
        return new NewsDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsViewHolder holder, int position) {
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

    public void commentLiked(int id, boolean vote) {

        for (ItemModelDetails itemModel : list) {

            if (itemModel.getCommentsId() == id) {

                itemModel.updateLikeUi();

                break;

            }
        }
    }

    public void commentDisliked(int id, boolean vote) {

        for (ItemModelDetails itemModel : list) {

            if (itemModel.getCommentsId() == id) {

                itemModel.updateDislikeUi();

                break;

            }

        }

    }

    public class NewsDetailsViewHolder extends RecyclerView.ViewHolder {

        public ViewBinding binding;

        public NewsDetailsViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
