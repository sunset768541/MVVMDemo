package com.demo.module_news.newslist.views.picturetitleview;


import com.demo.module_news.viewmodel.CustomViewModel;

import org.jetbrains.annotations.NotNull;

public class PictureTitleViewModel extends CustomViewModel {
    public String pictureUrl;

    public PictureTitleViewModel(@NotNull String jumpUri, @NotNull String title) {
        super(jumpUri, title);
    }
}
