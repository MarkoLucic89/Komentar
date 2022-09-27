package com.cubes.android.komentar.ui.detail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.cubes.android.komentar.R;
import com.cubes.android.komentar.data.model.domain.News;
import com.cubes.android.komentar.data.model.domain.NewsComment;
import com.cubes.android.komentar.data.model.domain.NewsDetails;
import com.cubes.android.komentar.databinding.RvItemAdBinding;
import com.cubes.android.komentar.databinding.RvItemCategorySmallBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsAllCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsCommentsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsHeaderBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsTagsBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsTitleBinding;
import com.cubes.android.komentar.databinding.RvItemDetailsWebViewBinding;
import com.cubes.android.komentar.databinding.RvItemHomeCategoryTitleBinding;
import com.cubes.android.komentar.ui.comments.CommentsAdapter;
import com.cubes.android.komentar.ui.detail.rv_item_details.ItemModelDetails;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemDetailsButtonAllComments;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailAd;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailComment;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsHeaderComments;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsHeaderRelatedNews;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsSmallNews;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsTags;
import com.cubes.android.komentar.ui.detail.rv_item_details.RvItemModelDetailsWebView;
import com.cubes.android.komentar.ui.main.latest.NewsListener;
import com.cubes.android.komentar.ui.main.latest.rv_model_category.ItemModelCategory;
import com.cubes.android.komentar.ui.tools.MyMethodsClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class NewsDetailsAdapter extends RecyclerView.Adapter<NewsDetailsAdapter.NewsDetailsViewHolder> {

    private ArrayList<ItemModelDetails> list = new ArrayList<>();
    private CommentsAdapter.CommentsListener commentsListener;
    private NewsDetailsTagsAdapter.TagListener tagListener;
    private NewsListener newsListener;
    private WebViewListener webViewListener;



    public interface WebViewListener {
        void onWebViewLoaded();
    }

    public NewsDetailsAdapter(Context context) {
        this.commentsListener = (CommentsAdapter.CommentsListener) context;
        this.tagListener = (NewsDetailsTagsAdapter.TagListener) context;
        this.newsListener = (NewsListener) context;
        this.webViewListener = (WebViewListener) context;
    }

    public NewsDetailsAdapter(CommentsAdapter.CommentsListener commentsListener, NewsDetailsTagsAdapter.TagListener tagListener, NewsListener newsListener, WebViewListener webViewListener) {
        this.commentsListener = commentsListener;
        this.tagListener = tagListener;
        this.newsListener = newsListener;
        this.webViewListener = webViewListener;
    }

    public void updateList(NewsDetails newsDetails) {

        list.clear();

        //HEADER
//        list.add(new RvItemModelDetailsHeader(newsDetails));

        //AD 1
        list.add(new RvItemModelDetailAd());

        //WEB VIEW
        list.add(new RvItemModelDetailsWebView(newsDetails.url, newsDetails.id, webViewListener));

        //AD 2
        list.add(new RvItemModelDetailAd());

        //TAGS
        list.add(new RvItemModelDetailsTags(tagListener, newsDetails.tags));

        //COMMENTS

//        list.add(new RvItemModelDetailsComments(commentsListener, newsDetails));

        list.add(new RvItemModelDetailsHeaderComments(newsDetails.commentsCount, newsDetails.id, commentsListener));

        for (NewsComment comment : newsDetails.commentsTop) {

            list.add(new RvItemModelDetailComment(comment, commentsListener, false));

            for (NewsComment subComment : comment.children) {

                list.add(new RvItemModelDetailComment(subComment, commentsListener, true));

            }
        }
        if (newsDetails.commentsCount > 0) {
            list.add(new RvItemDetailsButtonAllComments(newsDetails.id, newsDetails.commentsCount, commentsListener));
        }

        //AD 3
        list.add(new RvItemModelDetailAd());

        //RELATED NEWS

//        list.add(new RvItemModelDetailsRelatedNews(newsListener, newsDetails.related_news));

        list.add(new RvItemModelDetailsHeaderRelatedNews());

        for (News news : newsDetails.relatedNews) {
            list.add(new RvItemModelDetailsSmallNews(news, false, newsListener));
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
            case R.layout.rv_item_home_category_title:
                binding = RvItemHomeCategoryTitleBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_category_small:
                binding = RvItemCategorySmallBinding.inflate(inflater, parent, false);
                break;
            case R.layout.rv_item_ad:

                binding = RvItemAdBinding.inflate(inflater, parent, false);

                ((RvItemAdBinding) binding).shimmerLayout.setVisibility(View.VISIBLE);
                ((RvItemAdBinding) binding).shimmerLayout.startLayoutAnimation();
                ((RvItemAdBinding) binding).adView.setVisibility(View.GONE);

                AdRequest adRequest = new AdRequest.Builder().build();
                ((RvItemAdBinding) binding).adView.loadAd(adRequest);

                ((RvItemAdBinding) binding).adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                        ((RvItemAdBinding) binding).shimmerLayout.stopShimmer();
                        ((RvItemAdBinding) binding).shimmerLayout.setVisibility(View.GONE);
                        ((RvItemAdBinding) binding).adView.setVisibility(View.VISIBLE);

                    }
                });

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

    public void closeAllMenus() {
        for (ItemModelDetails itemModelDetails : list) {
            itemModelDetails.closeMenu();
        }
    }

    public void updateBookmarks(int mNewsId, News bookmark) {

        for (ItemModelDetails model : list) {

            if (model.getNews() != null && model.getNews().id == mNewsId) {

                if (bookmark == null) {
                    model.getNews().isInBookmarks = false;
                } else {
                    model.getNews().isInBookmarks = true;
                }

                notifyItemChanged(list.indexOf(model));

                Log.d("DETAIL", "updateBookmarks: " + model.getNews().isInBookmarks);

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
