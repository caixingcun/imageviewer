
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

   implementation 'com.github.caixingcun:imageviewer:v1.3'
~~~

# use

~~~
    /**
     * @param activity 上下文
     * @param images 图片
     * @param pos 位置 单张图可空，也可以设置为0
     * @param view 专场动画 图View
     * @param uniqueId 唯一id 可空 ,用来确定多图位置偏移后view查找
     * /
     
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
            
            
        九宫图需要额外配置 适配专场动画，重写 onActivityReenter
        调用 BigImageActivity.onActivityReenterHandle 传入 activity，resulrCode，data 
            及一个block，block 接收一个uniqueID start时传递的标识，pos为返回时图片位置，返回根据pos获取的位置
        如果非九宫可以不配置
        
        override fun onActivityReenter(resultCode: Int, data: Intent?) {
        BigImageActivity.onActivityReenterHandle(this, resultCode, data) { uniqueId, pos ->
            getExitView(pos)
        }
    }
        
            
~~~




