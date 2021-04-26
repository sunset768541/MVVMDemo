package com.demo.module_news.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsListBean(
    @Expose
    @SerializedName("showapi_res_code")
    val showapi_res_code: Int,
    @Expose
    @SerializedName("showapi_res_error")
    val showapiResError: String,
    @SerializedName("showapi_res_body")
    @Expose
    val showapiResBody: ShowNewsApiResBody
) {}

data class Pagebean(
    @SerializedName("allPages")
    @Expose
    var allPages: Int,

    @SerializedName("contentlist")
    @Expose
    var contentlist: List<Contentlist>,
    @SerializedName("currentPage")
    @Expose
    var currentPage: Int,

    @SerializedName("allNum")
    @Expose
    var allNum: Int,

    @SerializedName("maxResult")
    @Expose
    var maxResult: Int,
) {}

data class ShowNewsApiResBody(
    @Expose @SerializedName("ret_code") val retCode: Int,
    @Expose @SerializedName("pagebean") val pagebean: Pagebean
) {

}

data class Contentlist(
    @SerializedName("allList")
    @Expose
    var allList: List<String>,

    @SerializedName("pubDate")
    @Expose
    var pubDate: String,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("channelName")
    @Expose
    var channelName: String,

    @SerializedName("imageurls")
    @Expose
    var imageurls: List<ImageUrl>,

    @SerializedName("desc")
    @Expose
    var desc: String,

    @SerializedName("source")
    @Expose
    var source: String,

    @SerializedName("channelId")
    @Expose
    var channelId: String,

    @SerializedName("nid")
    @Expose
    var nid: String,

    @SerializedName("link")
    @Expose
    var link: String
) {

}

data class ImageUrl(
    @SerializedName("height")
    @Expose
    var height: String,

    @SerializedName("width")
    @Expose
    var width: String,

    @SerializedName("url")
    @Expose
    var url: String
) {}