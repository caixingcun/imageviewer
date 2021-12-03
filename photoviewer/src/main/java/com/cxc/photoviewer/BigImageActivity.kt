package com.cxc.photoviewer

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.view.WindowManager
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
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
    var enterPos: Int = 0
    var currentPos: Int = 0

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
        uniqueIdValue = intent.getIntExtra(KEY_UNIQUE_ID, 0)
        paths = intent.getStringArrayListExtra(IMAGES)
        enterPos = intent.getIntExtra(ENTER_POS, 0)
        currentPos = enterPos
        initFragments()
        initIndicator()
        initViewPager()
    }

    private fun initIndicator() {

        tvPos = findViewById(R.id.tv_pos)
        indicator = findViewById<CustomIndicator>(R.id.indicator)

        if (fragments.size == 1) {
            tvPos.visibility = View.INVISIBLE
            indicator.visibility = View.INVISIBLE
        } else if (fragments.size > 9) {
            //文字指示器
            tvPos.visibility = View.VISIBLE
            tvPos.text = "$currentPos/${fragments.size}"
            indicator.visibility = View.INVISIBLE
        } else {
            tvPos.visibility = View.INVISIBLE
            //view指示器
            indicator.visibility = View.VISIBLE
            indicator.setIndicatorSize(fragments.size)
            indicator.setCurrent(currentPos)
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
                currentPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        viewPager.currentItem = currentPos

    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun supportFinishAfterTransition() {
        val intent = Intent().apply {
            if (enterPos == currentPos) {
                putExtra(IMAGE_CURRENT_POS, -1)
            } else {
                putExtra(IMAGE_CURRENT_POS, currentPos)
            }
        }
        setResult(Activity.RESULT_OK, intent)
        super.supportFinishAfterTransition()
    }

    companion object {
        private const val IMAGES = "images"
        private const val ENTER_POS = "enter_pos"
        const val IMAGE_CURRENT_POS = "image_current_pos"
        const val TRANS_NAME = "trans_name"
        const val KEY_UNIQUE_ID = "unique_id"
        var uniqueIdValue: Int = 0
        fun startIntent(
            activity: Activity,
            images: ArrayList<String>,
            pos: Int,
            view: View? = null,
            uniqueId: Int = 0
        ) {
            val intent = Intent(activity, BigImageActivity::class.java)
            intent.putStringArrayListExtra(IMAGES, images)
            intent.putExtra(ENTER_POS, pos)
            intent.putExtra(KEY_UNIQUE_ID, uniqueId)
            if (view != null) {
                val compat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANS_NAME)
                activity.startActivity(intent, compat.toBundle())
            } else {
                activity.startActivity(intent)
            }
        }

        /**
         * 供外部调用者处理 专场动画 需要在onActivityReenter来实现
         * @param context
         * @param resultCode
         * @param data
         * @param getExitViewBlock 需要使用者解析回传一个 endView 供转厂动画 endView 使用，
         *          根据 uniqueId 唯一标识，使用者自己传过来，单层列表可以不传，pos 结束时位置 确定一个endView
         */
        fun onActivityReenterHandle(
            activity: AppCompatActivity,
            resultCode: Int,
            data: Intent?,
            getExitViewBlock: (uniqueId: Int, pos: Int) -> View?
        ) {
            if (resultCode == RESULT_OK && data != null) {
                val exitPos = data.getIntExtra(IMAGE_CURRENT_POS, -1)
                var exitView = getExitViewBlock.invoke(uniqueIdValue, exitPos)
                if (exitView == null) {
                    Log.d("tag", "onActivityReenter exitView null")
                    return
                }
                ActivityCompat.setExitSharedElementCallback(
                    activity,
                    object : SharedElementCallback() {
                        override fun onMapSharedElements(
                            names: MutableList<String>?,
                            sharedElements: MutableMap<String, View>?
                        ) {
                            if (names == null || sharedElements == null) {
                                return
                            }
                            names.clear()
                            sharedElements.clear()
                            names.add(TRANS_NAME)
                            sharedElements[TRANS_NAME] = exitView
                            activity.setExitSharedElementCallback(object :
                                SharedElementCallback() {})
                        }
                    })
            }
        }
    }
}