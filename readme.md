
# Android 图片预览 
~~~
    支持多张图片语言，支持滑动预览，有底部指示器（类微信），超出9张 会显示 index/num 形式，单击返回
~~~

# dependencies
~~~
project 
    build.gradle 
    
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
--------------------------------
app build.gradle 

   implementation 'com.github.caixingcun:imageviewer:Tag'
~~~

# use

~~~
            PhotoViewer.getInstance().apply {
                imageEngine = object : ImageEngine {
                    override fun load(context: Context?, image: ImageView, path: String?) {
                        // set your imageEngine
                    }
                }
            }.start(
                this@MainActivity,
                arrayListOf(
                    "xxx.png",
                    "xxx.png",
                    )
            )
~~~




