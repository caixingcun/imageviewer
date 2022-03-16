package com.cxc.photoviewer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
/**
 * @author caixingcun
 * @date 2022/3/16
 * Description : 规避 java.lang.IllegalArgumentException: pointerIndex out of range
 */


class ViewPagerFixed : androidx.viewpager.widget.ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }
}