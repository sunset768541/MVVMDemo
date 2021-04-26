package com.demo.module_news.newslist.views.picturetitleview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;


import com.bumptech.glide.Glide;
import com.demo.module_news.R;
import com.demo.module_news.databinding.NewsPictureTitleViewBinding;
import com.demo.module_news.viewmodel.CustomView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class PictureTitleView extends LinearLayout implements CustomView<PictureTitleViewModel> {
    private NewsPictureTitleViewBinding mBinding;
    private PictureTitleViewModel mViewModel;
    public PictureTitleView(Context context) {
        super(context);
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.news_picture_title_view, this, false);
        mBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebviewActivity.startCommonWeb(getContext(), "News", mViewModel.jumpUri);
            }
        });
        addView(mBinding.getRoot());
    }

    @Override
    public void setData(PictureTitleViewModel data){
        mBinding.setViewModel(data);
        mBinding.executePendingBindings();
        this.mViewModel = data;
    }

    @BindingAdapter("loadImageUrl")
    public static void loadUrl(ImageView imageView, String pitureUrl){
        if(!TextUtils.isEmpty(pitureUrl)) {
            Glide.with(imageView.getContext())
                    .load(pitureUrl)
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }
}
