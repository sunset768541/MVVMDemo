package com.demo.module_news.headlinenews

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.demo.module_news.newslist.NewsListFragment
import java.util.*
import kotlin.collections.ArrayList

class HeadlineNewsFragmentAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    class Channel {
        var channelId: String = "11"
        var channelName: String = "11"
    }

    private var mChannels: ArrayList<Channel> = ArrayList()
    private val fragmentHashMap = HashMap<String, Fragment>()
    init {
        mChannels.add(Channel())
    }

    fun setChannels(channels: ArrayList<Channel>) {
        mChannels = channels
        notifyDataSetChanged()
    }

    override fun getItem(pos: Int): Fragment {

        val key = mChannels[pos].channelId + ":" + mChannels[pos].channelName

        fragmentHashMap[key]?.let {
            return fragmentHashMap.getValue(key)
        }

        val fragment: Fragment = NewsListFragment.newInstance(
            mChannels[pos].channelId,
            mChannels[pos].channelName
        )
        fragmentHashMap[key] = fragment
        return fragment
    }

    override fun getCount(): Int {
        return if (mChannels.size > 0) {
            mChannels.size
        } else 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mChannels[position].channelName
    }

    override fun restoreState(
        parcelable: Parcelable?,
        classLoader: ClassLoader?
    ) {
    }


}