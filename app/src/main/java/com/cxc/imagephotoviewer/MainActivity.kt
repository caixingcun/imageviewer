package com.cxc.imagephotoviewer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxc.photoviewer.BigImageActivity
import com.cxc.photoviewer.ImageEngine
import com.cxc.photoviewer.PhotoViewer
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        const val imgUrl =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic_source%2F53%2F0a%2Fda%2F530adad966630fce548cd408237ff200.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1641101385&t=f62bb49a35a61c4844cfcfcf5b56ccc5"
    }

    lateinit var recyclerView: RecyclerView
    val mAdapter = MyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initImage()
    }

    private fun initImage() {
        var iv = findViewById<ImageView>(R.id.iv)
        Glide.with(this).load(imgUrl).into(iv)
        iv.setOnClickListener {
            PhotoViewer.getInstance().apply {
                init(imageEngine = object : ImageEngine {
                    override fun load(context: Context?, image: ImageView, path: String?) {
                        context?.let {
                            Glide.with(it).load(path).into(image)
                        }
                    }

                })
            }.start(this, arrayListOf(imgUrl), 0, iv)
        }
    }

    private fun initRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        recyclerView.adapter = mAdapter
        mAdapter.setNewData(
            mutableListOf(
                imgUrl,
                imgUrl,
                imgUrl,
                imgUrl,
                imgUrl,
                imgUrl,
                imgUrl,
                imgUrl,
            )
        )
        mAdapter.bindToRecyclerView(recyclerView)

        mAdapter.setOnItemClickListener { adapter, view, position ->

            PhotoViewer.getInstance().apply {
                imageEngine = object : ImageEngine {
                    override fun load(context: Context?, image: ImageView, path: String?) {
                        context?.let {
                            Glide.with(it).load(path).into(image)
                        }
                    }
                }
            }.start(
                this@MainActivity,
                getArrayList(mAdapter.data),
                position,
                view,

                )

        }

    }

    private fun getArrayList(data: List<String>): ArrayList<String> {
        var result = ArrayList<String>()
        data.forEach {
            result.add(it)
        }
        return result
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        BigImageActivity.onActivityReenterHandle(this, resultCode, data) { uniqueId, pos ->
            getExitView(pos)
        }
    }

    private fun getExitView(exitPos: Int): View? {
        if (exitPos == -1) {
            return null
        }
        return mAdapter.getViewByPosition(exitPos, R.id.iv)
    }


}

class MyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item) {
    override fun convert(helper: BaseViewHolder, item: String) {
        Glide.with(mContext).load(item).into(helper.getView(R.id.iv))
    }
}

