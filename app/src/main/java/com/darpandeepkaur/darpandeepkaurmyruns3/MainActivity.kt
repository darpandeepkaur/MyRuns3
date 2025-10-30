package com.darpandeepkaur.darpandeepkaurmyruns3

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentA: FragmentA
    private lateinit var fragmentB: FragmentB
    private lateinit var fragmentC: FragmentC
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var fragments: ArrayList<Fragment>
    private var tabTitle = arrayOf("START", "HISTORY", "SETTINGS")
    private lateinit var fragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentA = FragmentA()
        fragmentB = FragmentB()
        fragmentC = FragmentC()

        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabLayout)

        fragments = ArrayList()
        fragments.add(fragmentA)
        fragments.add(fragmentB)
        fragments.add(fragmentC)

        fragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = fragmentStateAdapter

        tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy {
                tab: TabLayout.Tab, pos: Int -> tab.text = tabTitle[pos]
        }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}