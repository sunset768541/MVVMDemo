package com.demo.module_news.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsChannelsBean(
    @Expose
    @SerializedName("showapi_res_id")
    val showapi_res_id: String,
    @Expose
    @SerializedName("showapi_res_code")
    val showapi_res_code: Int,
    @Expose
    @SerializedName("showapi_res_error")
    val showapiResError: String,
    @Expose
    @SerializedName("showapi_res_body")
    val showapiResBody: ShowapiResBody
) {

}


data class ShowapiResBody(
    @Expose @SerializedName("totalNum") val totalNum: Int,
    @Expose @SerializedName("ret_code") val retCode: Int,
    @SerializedName("channelList") val channelList: List<ChannelList>
) {

}

class ChannelList {
    @SerializedName("channelId")
    @Expose
    var channelId: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}