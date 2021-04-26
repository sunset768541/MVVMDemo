package com.demo.module_news.newslist

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.module_news.newslist.views.picturetitleview.PictureTitleView
import com.demo.module_news.newslist.views.picturetitleview.PictureTitleViewModel
import com.demo.module_news.newslist.views.titleview.TitleView
import com.demo.module_news.viewmodel.CustomViewModel
import com.demo.module_news.viewmodel.ViewHolder

class NewsListRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
    val mContext = context
    val VIEW_TYPE_PICTURE_TITLE = 1
    val VIEW_TYPE_TITLE = 2
    var mItems: List<CustomViewModel> = ArrayList<CustomViewModel>()


    fun setData(items: List<CustomViewModel>) {
        mItems = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_PICTURE_TITLE) {
            return ViewHolder(PictureTitleView(parent.getContext()))
        } else
            return ViewHolder(TitleView(parent.getContext()))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }


    override fun getItemViewType(position: Int): Int {
        if (mItems[position] is PictureTitleViewModel) {
            return VIEW_TYPE_PICTURE_TITLE
        } else {
            return VIEW_TYPE_TITLE
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItems.get(position))
    }

}