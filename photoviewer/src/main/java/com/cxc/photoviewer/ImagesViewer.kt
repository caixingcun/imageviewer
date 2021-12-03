package com.cxc.photoviewer

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import java.util.ArrayList

/**
 * @author caixingcun
 * @date 2021/12/1
 * Description : 多图查看工具 主要api提供类
 */
class ImagesViewer {
    companion object {
        /**
         * 图片切换后获取变换后View的回调方法名称
         * 与 IPhotoViewerEndViewCallback 中回调方法名称一致 getPhotoViewEndViewForTransaction
         */
        private const val GET_PHOTO_VIEW_END_VIEW_FOR_TRANSACTION =
            "getPhotoViewEndViewForTransaction"

        /**
         * 单例
         */
        private var photoViewerConfig: ImagesViewer? = null

        fun getInstance(): ImagesViewer {
            if (photoViewerConfig == null) {
                photoViewerConfig = ImagesViewer()
            }
            return photoViewerConfig!!
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
            data: Intent?
        ) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val exitPos = data.getIntExtra(BigImageActivity.IMAGE_CURRENT_POS, -1)
                if (exitPos == -1) {
                    return
                }
                var exitView: View? = null
                try {
                    exitView = getExitView(activity, BigImageActivity.uniqueIdValue, exitPos)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
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
                            names.add(BigImageActivity.TRANS_NAME)
                            sharedElements[BigImageActivity.TRANS_NAME] = exitView
                            activity.setExitSharedElementCallback(object :
                                SharedElementCallback() {})
                        }
                    })
            }
        }

        /**
         * 获取退出时原页面view
         * @param activity 活动
         * @param uniqueIdValue 唯一值 一般为某个view id
         * @param exitPos 退出时View位置
         */
        private fun getExitView(
            activity: AppCompatActivity,
            uniqueIdValue: Int,
            exitPos: Int
        ): View? {
            try {
                val activityMethod = activity.javaClass.getDeclaredMethod(
                    GET_PHOTO_VIEW_END_VIEW_FOR_TRANSACTION,
                    Int::class.java,
                    Int::class.java,
                )
                return activityMethod?.invoke(activity, uniqueIdValue, exitPos) as View?
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val fragments: MutableList<Fragment> = ArrayList()
            getAllFragments(fragments,activity = activity)
            for (fragment in fragments) {
                if (fragment.isVisible) {
                    try {
                        val fragmentMethod = fragment.javaClass.getDeclaredMethod(
                            GET_PHOTO_VIEW_END_VIEW_FOR_TRANSACTION,
                            Int::class.java,
                            Int::class.java,
                        )
                        return fragmentMethod.invoke(fragment, uniqueIdValue, exitPos) as View?
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
            return null
        }

        /**
         * 根据activity 获取activity承载的所有fragment
         * 递归函数
         * @param activity 传入fragment activity必须是null
         * @param fragment
         * @param result 最终所有fragments存储位置
         * @param isActivity 是否是活动
         */
        private fun getAllFragments(
            result: MutableList<Fragment>,
            activity: AppCompatActivity? = null,
            fragment: Fragment? = null,
        ) {

            if (activity == null) {
                if (fragment == null || !fragment.isVisible) {
                    return
                }
                result.add(fragment)
                val fragments = fragment.childFragmentManager.fragments
                for (childFragment in fragments) {
                    getAllFragments(result, fragment = childFragment)
                }
            } else {
                val fragments = activity.supportFragmentManager.fragments
                for (f in fragments) {
                    getAllFragments(result, fragment = f)
                }
            }
        }

    }

    var imageEngine: ImageEngine? = null
        private set

    fun init(imageEngine: ImageEngine) {
        this.imageEngine = imageEngine
    }

    /**
     * @param activity 上下文
     * @param images 图片
     * @param pos 位置 单张图可空，也可以设置为0
     * @param view 专场动画 图View
     * @param uniqueId 唯一id 可空 ,用来确定多图位置偏移后view查找
     */
    fun start(
        activity: Activity,
        images: ArrayList<String>,
        pos: Int = 0,
        view: View? = null,
        uniqueId: Int = 0
    ) {

        if (images == null || images.size == 0) {
            Log.d("PhotoViewer", "PhotoViewer.start param images size can not be zero")
            return
        }
        BigImageActivity.startIntent(activity, images, pos, view, uniqueId)
    }
}