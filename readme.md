
# Android 图片预览 
~~~
    支持多张图片语言，
    支持滑动预览，
    有底部指示器（类微信），超出9张 会显示 index/num 形式，单击返回 
    包含转场动画
~~~
# 样例
~~~
 样例主要包含 单图，多图，以及Fragment中多图预览
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

   implementation 'com.github.caixingcun:imageviewer:v1.5'
~~~

# use
~~~
   
   1.==============================================================================
    /**
     * start 
     * @param activity 上下文
     * @param images 图片集
     * @param pos 位置 单张图可空，也可以设置为0
     * @param view 转场动画 点击View 
     * @param uniqueId 唯一id 可空  如果是recyclerView 可以设置为其id，如果是多层嵌套 请确定唯一id ，如果是单独view 可以不填，不填意味着不修改转场
     * /
     
    ImagesViewer
        .getInstance()  //单例
        .apply {        //init 设置图片加载引擎   
                    init(imageEngine = object : ImageEngine {
                        override fun load(context: Context?, image: ImageView, path: String?) {
                            context?.let {
                                Glide.with(it).load(path).into(image)
                            }
                        }

                    })  
                          //开启跳转 
                    start(it, mUrls, position, view,R.id.recycler_view)
                }
            
            
    2.==================================================================================        
    对于Activity 或者Fragment /多层嵌套fragment 中 ，多图预览，切换后，需要修改返回转场的 ，额外添加
    
    在Activity中（Fragment则在承载其Activity上重写 onActivityReenter）
    // 固定写法
    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        ImagesViewer.onActivityReenterHandle(this, resultCode, data)
    }
    
    
    
    3.==================================================================================      
    //使用Activity/Fragment （有需要转场调整的，一般内含图片列表的），实现  IPhotoViewerEndViewCallback 接口 重写其 getPhotoViewEndViewForTransaction 方法
    
    /**
     * 获取返回的view 用于 Images Viewer后返回转场动画
     * @param uniqueId 唯一码，用于区分列表中九宫图 不好定位获取view问题，
     *      一般用于跳转前转场动画view的 父id ，才好通过 endPos找到子view
     * @param endPos 结束预览时地址
     */
  
    override fun getPhotoViewEndViewForTransaction(uniqueId: Int, endPos: Int):View? {
        if (uniqueId == R.id.recycler_view) {
            return mAdapter.getViewByPosition(endPos, R.id.iv)
        }
        return null
    }
    
    注意：
    如果recyclerView使用 BaseQuickAdapter ，初始化时需要绑定 ，否则会找不到 getViewByPosition 会找不到 view   
       
    mAdapter.bindToRecyclerView(recyclerView)
~~~




