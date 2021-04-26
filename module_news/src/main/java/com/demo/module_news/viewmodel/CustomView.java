package com.demo.module_news.viewmodel;

public interface CustomView<D extends CustomViewModel> {
    void setData(D data);
}
