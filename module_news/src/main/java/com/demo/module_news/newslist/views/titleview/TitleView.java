package com.demo.module_news.newslist.views.titleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.demo.module_news.R;
import com.demo.module_news.databinding.NewsTitleViewBinding;
import com.demo.module_news.viewmodel.CustomView;


public class TitleView extends LinearLayout implements CustomView<TitleViewModel> {
    private NewsTitleViewBinding mBinding;
    private  TitleViewModel mViewModel;
    public TitleView(Context context) {
        super(context);
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.news_title_view, this, false);
        mBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebviewActivity.startCommonWeb(getContext(), "News", mViewModel.jumpUri);
            }
        });
        addView(mBinding.getRoot());
    }

    @Override
    public void setData(TitleViewModel data){
        mBinding.setViewModel(data);
        mBinding.executePendingBindings();
        this.mViewModel = data;
    }
}
