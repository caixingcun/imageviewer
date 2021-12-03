package com.cxc.photoviewer

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import java.util.ArrayList

/**
 * @author caixingcun
 * @date 2021/12/1
 * Description :
 */
class PhotoViewer {
    companion object {
        private var photoViewerConfig: PhotoViewer? = null
        fun getInstance(): PhotoViewer {
            if (photoViewerConfig == null) {
                photoViewerConfig = PhotoViewer()
            }
            return photoViewerConfig!!
        }
    }

    var imageEngine: ImageEngine? = null

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
        BigImageActivity.startIntent(activity, images, pos, view,uniqueId)
    }
}