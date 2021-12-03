package com.cxc.imagephotoviewer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cxc.photoviewer.IPhotoViewerEndViewCallback
import com.cxc.photoviewer.ImageEngine
import com.cxc.photoviewer.ImagesViewer

/**
 * @author caixingcun
 * @date 2021/12/3
 * Description :
 */
class TempFragment : Fragment(), IPhotoViewerEndViewCallback {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_template, null)
    }

    lateinit var recyclerView: RecyclerView
    val mAdapter = MyAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        initRecyclerView()
    }

    val mUrls = arrayListOf(
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
        MainActivity.imgUrl,
    )

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        recyclerView.adapter = mAdapter
        mAdapter.setNewData(
            mUrls
        )
        mAdapter.bindToRecyclerView(recyclerView)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.let {
                ImagesViewer.getInstance().apply {
                    init(imageEngine = object : ImageEngine {
                        override fun load(context: Context?, image: ImageView, path: String?) {
                            context?.let {
                                Glide.with(it).load(path).into(image)
                            }
                        }

                    })
                    start(it, mUrls, position, view,R.id.recycler_view)
                }
            }
        }
    }

    override fun getPhotoViewEndViewForTransaction(uniqueId: Int, endPos: Int): View? {
        if (uniqueId == R.id.recycler_view) {
            return mAdapter.getViewByPosition(endPos, R.id.iv)
        }
        return null
    }
}