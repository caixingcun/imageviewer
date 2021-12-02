package com.cxc.photoviewer

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentPagerAdapter
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

/**
 * 大图
 */
class BigImageActivity : AppCompatActivity() {
    private var paths: List<String>? = null

    lateinit var viewPager: ViewPager
    lateinit var indicator: CustomIndicator
    lateinit var tvPos: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = resources.getColor(R.color.black)
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.setBackgroundDrawableResource(R.color.transparent)

        setContentView(R.layout.layout_viewpager)
        if (!intent.hasExtra(IMAGES)) {
            Log.d("photoViewer", "please use PhotoView.show to start BigImageActivity")
            finish()
            return
        }
        if (intent.getStringArrayListExtra(IMAGES) == null ||
            intent.getStringArrayListExtra(IMAGES)!!.size == 0
        ) {
            Log.d("photoViewer", "please use PhotoView.show to start BigImageActivity")
            finish()
            return
        }
        paths = intent.getStringArrayListExtra(IMAGES)
        initFragments()
        initIndicator()
        initViewPager()
    }

    private fun initIndicator() {

        tvPos = findViewById(R.id.tv_pos)
        indicator = findViewById<CustomIndicator>(R.id.indicator)
        if (fragments.size > 9) {
            //文字指示器
            tvPos.visibility = View.VISIBLE
            tvPos.text = "1/${fragments.size}"
            indicator.visibility = View.INVISIBLE
        } else {
            tvPos.visibility = View.INVISIBLE
            //view指示器
            indicator.visibility = View.VISIBLE
            indicator.setIndicatorSize(fragments.size)
            indicator.setCurrent(0)
            indicator.isEnabled = false
        }
    }

    var fragments: MutableList<Fragment> = ArrayList()

    private fun initFragments() {
        fragments.clear()
        paths?.forEach {
            fragments.add(ImageFragment.getInstance(it))
        }
    }

    private fun initViewPager() {
        viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (fragments.size > 9) {
                    tvPos.text = "${position + 1}/${fragments.size}"
                } else {
                    indicator.setCurrent(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })


    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    companion object {
        private const val IMAGES = "images"

        fun startIntent(activity: Activity, images: ArrayList<String>, view: View? = null) {
            val intent = Intent(activity, BigImageActivity::class.java)
            intent.putStringArrayListExtra(IMAGES, images)
            if (view != null) {
                val compat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "trans_name")
                activity.startActivity(intent, compat.toBundle())
            } else {
                activity.startActivity(intent)
            }
        }
    }
}