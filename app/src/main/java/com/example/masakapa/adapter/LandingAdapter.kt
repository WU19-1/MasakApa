package com.example.masakapa.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.masakapa.landing.FirstLanding
import com.example.masakapa.landing.SecondLanding
import com.example.masakapa.landing.ThirdLanding

class LandingAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

    val pages = listOf<Fragment>(FirstLanding(),SecondLanding(),ThirdLanding())

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

}