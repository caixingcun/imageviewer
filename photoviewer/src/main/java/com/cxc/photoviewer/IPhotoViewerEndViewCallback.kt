package com.cxc.photoviewer

import android.view.View


/**
 * 有fragment中 九宫图 需要使用大图展示 切换后返回 转场动画的
 * fragment需要实现该接口 完成 通过 唯一码 uniqueId，和结束位置 返回一个结束view ，用来返回转场动画
 * 有需要的fragment 或者Activity 实现 （跳转图片集合的）
 *
 */
interface IPhotoViewerEndViewCallback {
    /**
     * 获取返回的view 用于 Images Viewer后返回转场动画
     * @param uniqueId 唯一码，用于区分列表中九宫图 不好定位获取view问题，
     *      一般用于跳转前转场动画view的 父id ，才好通过 endPos找到子view
     * @param endPos 结束预览时地址
     */
     fun getPhotoViewEndViewForTransaction(uniqueId: Int, endPos: Int): View?
}