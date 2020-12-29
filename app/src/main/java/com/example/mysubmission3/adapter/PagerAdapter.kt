package com.example.mysubmission3.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysubmission3.FollowerFragment
import com.example.mysubmission3.FollowingFragment
import com.example.mysubmission3.R

class PagerAdapter(private val username: String, private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitle = intArrayOf(
        R.string.tab_following,
        R.string.tab_folower
    )

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        val bundle = Bundle()
        when(position){
            0 -> {
                bundle.putString(FollowingFragment.USERNAME, username )
                fragment = FollowingFragment()
                fragment.arguments = bundle
            }
            1 -> {
                bundle.putString(FollowerFragment.USERNAME, username )
                fragment = FollowerFragment()
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int = 2

}