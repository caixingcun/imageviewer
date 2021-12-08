package com.cxc.imagephotoviewer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cxc.photoviewer.IPhotoViewerEndViewCallback
import com.cxc.photoviewer.ImageEngine
import com.cxc.photoviewer.ImagesViewer
import java.util.ArrayList

class MainActivity : AppCompatActivity(), IPhotoViewerEndViewCallback {
    companion object {
        const val imgUrl =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic_source%2F53%2F0a%2Fda%2F530adad966630fce548cd408237ff200.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1641101385&t=f62bb49a35a61c4844cfcfcf5b56ccc5"
        "https://t7.baidu.com/it/u=3093477943,472212470&fm=193&f=GIF"
    }

    lateinit var recyclerView: RecyclerView
    val mAdapter = MyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initImage()
        initButton()

    }

    private fun initButton() {
        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, TemplateActivity::class.java))
        }
    }

    private fun initImage() {
        var iv = findViewById<ImageView>(R.id.iv)
        Glide.with(this).load(imgUrl).into(iv)
        iv.setOnClickListener {
            ImagesViewer.getInstance().apply {
                init(imageEngine = object : ImageEngine {
                    override fun load(context: Context?, image: ImageView, path: String?) {
                        context?.let {
                            Glide.with(it).load(path).into(image)
                        }
                    }

                })
                start(this@MainActivity, arrayListOf(imgUrl), 0, iv)
            }
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

            ImagesViewer.getInstance().apply {
                init(object : ImageEngine {
                    override fun load(context: Context?, image: ImageView, path: String?) {
                        context?.let {
                            Glide.with(it).load(path).into(image)
                        }
                    }
                })
                start(
                    this@MainActivity,
                    getArrayList(mAdapter.data),
                    position,
                    view,
                    recyclerView.id
                )
            }
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
        ImagesViewer.onActivityReenterHandle(this, resultCode, data)
    }

    override fun getPhotoViewEndViewForTransaction(uniqueId: Int, endPos: Int):View? {
        if (uniqueId == R.id.recycler_view) {
            return mAdapter.getViewByPosition(endPos, R.id.iv)
        }
        return null
    }

}

class MyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item) {
    override fun convert(helper: BaseViewHolder, item: String) {
        Glide.with(mContext).load(item).into(helper.getView(R.id.iv))
    }
}

