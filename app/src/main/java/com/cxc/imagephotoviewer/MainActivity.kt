package com.cxc.imagephotoviewer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cxc.photoviewer.ImageEngine
import com.cxc.photoviewer.PhotoViewer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv).setOnClickListener { _ ->
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
                arrayListOf(
                    "https://cdnimg.chinagoods.com/jpg/2021/10/11/1723d75cf421240ff3af4c734e967fcc.jpg",
                    "https://cdnimg.chinagoods.com/jpg/2021/11/12/d8fa088bc6d5029c69cf22bcdbd31420.jpg",
                    "https://cdnimg.chinagoods.com/png/2021/11/19/c17e79683fc70e7e16875434252e4aad.png",
                    "https://cdnimg.chinagoods.com/png/2021/11/12/fdf5dc3e061fb364900b6ddc76687e90.png",
                    "https://cdnimg.chinagoods.com/jpg/2021/10/11/1723d75cf421240ff3af4c734e967fcc.jpg",
                    "https://cdnimg.chinagoods.com/jpg/2021/11/12/d8fa088bc6d5029c69cf22bcdbd31420.jpg",
                    "https://cdnimg.chinagoods.com/png/2021/11/19/c17e79683fc70e7e16875434252e4aad.png",
                    "https://cdnimg.chinagoods.com/png/2021/11/12/fdf5dc3e061fb364900b6ddc76687e90.png",
                    "https://cdnimg.chinagoods.com/jpg/2021/10/11/1723d75cf421240ff3af4c734e967fcc.jpg",
                    "https://cdnimg.chinagoods.com/jpg/2021/11/12/d8fa088bc6d5029c69cf22bcdbd31420.jpg",
                    "https://cdnimg.chinagoods.com/png/2021/11/19/c17e79683fc70e7e16875434252e4aad.png",
                    "https://cdnimg.chinagoods.com/png/2021/11/12/fdf5dc3e061fb364900b6ddc76687e90.png",
                    )
            )

        }
    }
}