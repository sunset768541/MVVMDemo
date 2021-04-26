package com.demo.module_news.api

import com.demo.module_news.bean.NewsChannelsBean
import com.demo.module_news.bean.NewsListBean
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiInterface {
    @GET("release/news")
    suspend fun getNewsList(
        @Query("channelId") channelId: String,
        @Query("channelName") channelName: String,
        @Query("page") page: Int
    ): NewsListBean

    @GET("release/channel")
    suspend fun getNewsChannels(): NewsChannelsBean
}