package com.demo.module_news.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private CustomView itemView;
    public ViewHolder(@NonNull CustomView itemView) {
        super((View) itemView);
        this.itemView = itemView;
    }

    public void bind(CustomViewModel viewModel){
        this.itemView.setData(viewModel);
    }
}
