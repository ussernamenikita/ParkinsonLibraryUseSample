package com.bulygin.nikita.healthapp.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class HealthPagerAdapter(fragmentManager: FragmentManager,
                         private val fragments:Array<Fragment>,
                         private val titles:Array<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(index: Int): Fragment {
        return fragments[index]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int = fragments.size

}