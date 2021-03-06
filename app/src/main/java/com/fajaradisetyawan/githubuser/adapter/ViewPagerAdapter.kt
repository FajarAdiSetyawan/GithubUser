package com.fajaradisetyawan.githubuser.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fajaradisetyawan.githubuser.view.fragment.FollowerFragment
import com.fajaradisetyawan.githubuser.view.fragment.FollowingFragment

class ViewPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = FollowingFragment.getUsername(username.toString())
            1 -> fragment = FollowerFragment.getUsername(username.toString())
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Following"
            else -> "Follower"
        }
    }

    override fun getCount(): Int {
        return 2
    }
}