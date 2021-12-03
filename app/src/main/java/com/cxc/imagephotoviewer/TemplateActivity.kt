package com.cxc.imagephotoviewer

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.cxc.photoviewer.ImagesViewer

class TemplateActivity : AppCompatActivity() {

    lateinit var fl: FrameLayout
    var fragment = TempFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)
        fl = findViewById(R.id.fl)
        supportFragmentManager.beginTransaction().add(R.id.fl, fragment).show(fragment).commit()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        ImagesViewer.onActivityReenterHandle(this,resultCode,data)
    }
}