package com.demo.module_news.newslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.base.network.http.api.HttpApi
import com.demo.module_news.R
import com.demo.module_news.api.NewsApiInterface
import com.demo.module_news.databinding.NewsFragmentNewsBinding
import com.demo.module_news.getApi
import com.demo.module_news.newslist.views.picturetitleview.PictureTitleViewModel
import com.demo.module_news.newslist.views.titleview.TitleViewModel
import com.demo.module_news.viewmodel.CustomViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NewsListFragment : Fragment() {
    var mPage = 1
    lateinit var mAdapter: NewsListRecyclerViewAdapter
    lateinit var viewDataBinding: NewsFragmentNewsBinding

    companion object {
        val BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id"
        val BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
        fun newInstance(channelId: String, channelName: String): NewsListFragment {
            val fragment = NewsListFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId)
            bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.news_fragment_news, container, false)
        mAdapter = context?.let { NewsListRecyclerViewAdapter(it) }!!
        viewDataBinding.newsListview.setHasFixedSize(true)
        viewDataBinding.newsListview.layoutManager = LinearLayoutManager(context)
        viewDataBinding.newsListview.adapter = mAdapter
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("NewsListFragment","IO中启动网络数据加载 ${Thread.currentThread().name}")
                load()
            }
            Log.d("NewsListFragment","完成网络数据加载切回Main ${Thread.currentThread().name}")
            mAdapter.setData(viewModels)
        }
        return viewDataBinding.root
    }

    private val viewModels =
        ArrayList<CustomViewModel>()

    suspend fun load() {
        val api = getApi(this.requireContext())
        val newsListBean = api.createRequestApi(NewsApiInterface::class.java).getNewsList(
            arguments?.getString(BUNDLE_KEY_PARAM_CHANNEL_ID)!!,
            arguments?.getString(BUNDLE_KEY_PARAM_CHANNEL_NAME)!!, mPage
        )
        if (mPage == 0) {
            viewModels.clear()
        }
        Log.e("获取的数据", "  $newsListBean")
        for (contentlist in newsListBean.showapiResBody.pagebean.contentlist) {
            if (contentlist.imageurls != null && contentlist.imageurls.size > 0) {
                val pictureTitleViewModel =
                    PictureTitleViewModel(contentlist.link, contentlist.title)
                pictureTitleViewModel.pictureUrl = contentlist.imageurls.get(0).url
                viewModels.add(pictureTitleViewModel)
            } else {
                val titleViewModel = TitleViewModel(contentlist.link, contentlist.title)
                viewModels.add(titleViewModel)
            }
        }
        mPage++
    }
}