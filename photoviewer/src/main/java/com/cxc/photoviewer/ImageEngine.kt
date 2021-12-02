package com.cxc.photoviewer

import android.content.Context
import android.widget.ImageView

/**
 * @author caixingcun
 * @date 2021/12/1
 * Description :
 */
interface ImageEngine {
    fun load(context: Context?, image: ImageView, path: String?)
}