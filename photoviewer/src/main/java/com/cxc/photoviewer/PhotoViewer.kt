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

    fun start(activity: Activity, images: ArrayList<String>, view: View? = null) {
        if (images == null || images.size == 0) {
            Log.d("PhotoViewer", "PhotoViewer.start param images size can not be zero")
            return
        }
        BigImageActivity.startIntent(activity, images, view)
    }
}