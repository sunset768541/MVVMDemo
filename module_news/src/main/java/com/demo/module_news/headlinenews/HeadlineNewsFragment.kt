package com.demo.module_news.headlinenews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.base.network.http.api.HttpApi
import com.demo.module_news.R
import com.demo.module_news.api.NewsApiInterface
import com.demo.module_news.databinding.NewsFragmentHomeBinding
import com.demo.module_news.getApi
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

class HeadlineNewsFragment : Fragment() {
    lateinit var viewDataBinding: NewsFragmentHomeBinding
    lateinit var adapter: HeadlineNewsFragmentAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            DataBindingUtil.inflate(inflater, R.layout.news_fragment_home, container, false)
        viewDataBinding.newsTablelayout.setTabMode(TabLayout.MODE_SCROLLABLE)
        adapter = HeadlineNewsFragmentAdapter(childFragmentManager)
        viewDataBinding.newsViewpager.setAdapter(adapter)
        viewDataBinding.newsTablelayout.setupWithViewPager(viewDataBinding.newsViewpager)
        viewDataBinding.newsViewpager.setOffscreenPageLimit(1)
        viewLifecycleOwner.lifecycleScope.launch() {
            load()
        }
        return viewDataBinding.root
    }

    suspend fun load() {
        val api = getApi(this.requireContext())
        val newsChannelsBean = api.createRequestApi(NewsApiInterface::class.java).getNewsChannels()
        Log.e("获取的数据Head", "  $newsChannelsBean")

        val channels = ArrayList<HeadlineNewsFragmentAdapter.Channel>()
        for (source in newsChannelsBean.showapiResBody.channelList) {
            val channel: HeadlineNewsFragmentAdapter.Channel = HeadlineNewsFragmentAdapter.Channel()
            channel.channelId = source.channelId.toString()
            channel.channelName = source.name.toString()
            channels.add(channel)
        }
        adapter.setChannels(channels)
    }
}