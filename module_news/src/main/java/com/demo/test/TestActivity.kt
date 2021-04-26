package com.demo.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.base.network.http.api.HttpApi
import com.demo.base.network.http.base.HttpApiHeadersProvider
import com.demo.base.network.http.base.HttpApiInterceptorProvider
import com.demo.module_news.R
import com.demo.module_news.RequestHeader
import com.demo.module_news.api.NewsApiInterface
import com.demo.module_news.databinding.NewsTestActivityBinding
import com.demo.module_news.headlinenews.HeadlineNewsFragment
import com.demo.module_news.headlinenews.HeadlineNewsFragmentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TestActivity : AppCompatActivity() {
    private val mHomeFragment: Fragment = HeadlineNewsFragment()
    lateinit var viewDataBinding: NewsTestActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.news_test_activity)

        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mHomeFragment, mHomeFragment.javaClass.simpleName)
        transaction.commit()
//        lifecycleScope.launch(Dispatchers.IO) {
//            load()
//        }
    }


    suspend fun load() {
        var api = HttpApi()
        api.initConfig(
            true,
            false,
            "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/",
            getCacheDir(),
            null,
            RequestHeader(),

            null
        )

        val newsChannelsBean = api.createRequestApi(NewsApiInterface::class.java).getNewsChannels()
        Log.e("获取的数据Head", "  ${newsChannelsBean.showapiResBody.channelList}")

        val channels = ArrayList<HeadlineNewsFragmentAdapter.Channel>()
        for (source in newsChannelsBean.showapiResBody.channelList) {
            val channel: HeadlineNewsFragmentAdapter.Channel = HeadlineNewsFragmentAdapter.Channel()
            channel.channelId = source.channelId.toString()
            channel.channelName = source.name.toString()
            channels.add(channel)
        }

    }

}