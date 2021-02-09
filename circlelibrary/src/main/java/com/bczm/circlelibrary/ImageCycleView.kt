package com.bczm.circlelibrary


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class ImageCycleView : LinearLayout {

    /**
     * 图片轮播视图
     */
    private var mAdvPager: ViewPager? = null

    /**
     * 滚动图片视图适配
     */
    private var mAdvAdapter: ImageCycleAdapter? = null
    /**
     * 上下文
     */
    private var mContext: Context? = null

    /**
     * 图片轮播指示器控件
     */
    private var mGroup: ViewGroup? = null

    /**
     * 图片轮播指示个图
     */
    private var mImageView: ImageView? = null

    /**
     * 滚动图片指示视图列表
     */
    private var mImageViews: Array<ImageView?>? = null

    /**
     * 图片滚动当前图片下标
     */
    private var mImageIndex = 0

    /**
     * 手机密度
     */
    private var mScale = 0f

    /**
     * 控制停止轮播
     */
    private var isStop = false
    var mH = Handler()

    /**
     * 图片描述
     */
    private var imageName:TextView? = null

    /**
     * 图片描述内容合集
     */
    private var mImageDescList:ArrayList<String>? = null
    /**
     * 图片自动轮播Task
     */
    private val mImageTimerTask: Runnable = object : Runnable {
        override fun run() {
            if (mImageViews != null) {
                mAdvPager!!.currentItem = mAdvPager!!.currentItem + 1
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mH.postDelayed(this, 3000)
                }
            }
        }
    }


    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    fun startImageCycle() {
        startImageTimerTask()
    }

    /**
     * 停止图片滚动任务
     */
    private fun stopImageTimerTask(){
        isStop = true
        mH.removeCallbacks(mImageTimerTask)
    }
    /**
     * 图片滚动任务
     */
    private fun startImageTimerTask() {
        stopImageTimerTask()

        // 图片滚动
        mH.postDelayed(mImageTimerTask, 3000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context?) {
        mContext = context;
        mScale = mContext!!.resources.displayMetrics.density
        LayoutInflater.from(context).inflate(R.layout.block_ad_cycle_view, this)
        mAdvPager = findViewById(R.id.adv_pager)
        mAdvPager!!.offscreenPageLimit = 3
        mAdvPager!!.addOnPageChangeListener(listener)
        mAdvPager!!.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_UP ->
                    startImageTimerTask()
                else ->
                    stopImageTimerTask()
            }
            false
        }
        // 滚动图片右下指示器视
        // 滚动图片右下指示器视
        mGroup = findViewById<View>(R.id.circles) as ViewGroup
        imageName = findViewById<View>(R.id.viewGroup2) as TextView
    }


    private var listener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                startImageTimerTask()
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }
        override fun onPageSelected(position: Int) {
           // 设置当前显示的图片
            mImageIndex  = position % mImageViews!! . size
            // 设置图片滚动指示器背
            mImageViews!![mImageIndex]!!.setBackgroundResource(R.mipmap.banner_dot_focus)
            imageName!!.text = mImageDescList!!.get(mImageIndex)
            for (i in mImageViews!!.indices) {
                if (mImageIndex != i) {
                    mImageViews!![i]!!.setBackgroundResource(R.mipmap.banner_dot_normal)
                }
            }
        }
    }

    /**
     * 装填图片数据
     */

    public fun setImageResources( imageDesList: ArrayList<String>,imageUrlList: ArrayList<String> ,imageCycleViewListener: ImageCycleViewListener ){
        mImageDescList = imageDesList
        if(mImageDescList!=null&& mImageDescList!!.size>0){
            this.visibility = View.VISIBLE
        }else{
            this.visibility = View.GONE
            return
        }
        mGroup!!.removeAllViews()

       var imageCount = imageUrlList.size
        mImageViews = arrayOfNulls(imageCount)

        for (i in 0 until imageCount){
            mImageView = ImageView(mContext)
            var imageParams = (mScale*10+0.5f).toInt()
            var imagePadding = (mScale * 5 + 0.5f).toInt()
            val params =  LayoutParams(imageParams, imageParams)
            params.leftMargin = 10

            mImageView!!.scaleType = ScaleType.CENTER_CROP
            mImageView!!.layoutParams = params
            mImageView!!.setPadding(imagePadding, imagePadding, imagePadding, imagePadding)

            mImageViews!![i] = mImageView
            if (i == 0) {
                mImageViews!![i]!!.setBackgroundResource(R.mipmap.banner_dot_focus)
            } else {
                mImageViews!![i]!!.setBackgroundResource(R.mipmap.banner_dot_normal)
            }
            mGroup!!.addView(mImageViews!![i])
        }

        imageName!!.text = imageDesList[0]
        imageName!!.setTextColor(resources.getColor(R.color.blue))
        mAdvAdapter = ImageCycleAdapter(
            mContext!!,
            imageUrlList,
            imageCycleViewListener
        )
        mAdvPager!!.adapter = mAdvAdapter
        startImageTimerTask()
    }


    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    interface ImageCycleViewListener {
        /**
         * 加载图片资源
         *
         * @param imageURL   :url
         * @param imageView: image
         */
        fun displayImage(
            imageURL: String?,
            imageView: ImageView?
        )

        /**
         * 单击图片事件
         *
         * @param position  :position
         * @param imageView :image
         */
        fun onImageClick(position: Int, imageView: View?)
    }


    private class ImageCycleAdapter(
        context: Context,
        adList: ArrayList<String>,
        imageCycleViewListener: ImageCycleViewListener
    ) : PagerAdapter() {
        /**
         * 图片视图缓存列表
         */
        private var mImageViewCacheList: java.util.ArrayList<View>? = null

        /**
         * 图片资源列表
         */
        private var mAdList = adList


        /**
         * 广告图片点击监听
         */
        private var mImageCycleViewListener: ImageCycleViewListener? = imageCycleViewListener

        private var mContext: Context = context

        init {
            mImageViewCacheList = ArrayList<View>()
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
           return view == `object`
        }

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var imageUrl = mAdList[position%mAdList.size]
            var view:View?
            var imageView:ClickableImageView
            if (mImageViewCacheList!!.isEmpty()) {
                view = View.inflate(mContext,R.layout.item_vp,null)
                imageView = view.findViewById(R.id.iv) as ClickableImageView

                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                imageView.scaleType =ScaleType.CENTER_CROP

            }else{
                view = mImageViewCacheList!!.removeAt(0)
                imageView = view.findViewById(R.id.iv) as ClickableImageView
            }

            //设置点击事件
            imageView.setOnClickListener { v->
                mImageCycleViewListener!!.onImageClick(position % mAdList.size, v )
            }
            view!!.tag = imageUrl
            container.addView(view)
            mImageCycleViewListener!!.displayImage(imageUrl, imageView)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
            mImageViewCacheList!!.add(view)
        }
    }


    /**
     * 是否隐藏底部
     *
     * @param hideBottom
     */
    fun hideBottom(hideBottom: Boolean) {
        if (hideBottom) {
            mGroup!!.visibility = View.GONE
            imageName!!.visibility = View.GONE
        } else {
            mGroup!!.visibility = View.VISIBLE
            imageName!!.visibility = View.VISIBLE
        }
    }


    /**
     * vp 效果
     */
    enum class CYCLE_T {
        /********
         * 普通的ViewPager
         */
        CYCLE_VIEW_NORMAL,

        /********
         * 放大进入
         */
        CYCLE_VIEW_ZOOM_IN,

        /********
         * 展示左右
         */
        CYCLE_VIEW_THREE_SCALE,

        /********
         * 右侧展示一部分
         */
        CYCLE_VIEW_RIGHT_IN
    }
    /**
     * 设置样式
     */
    fun setCycleType(T:CYCLE_T?) {
        when (T) {
            CYCLE_T.CYCLE_VIEW_NORMAL ->{

            }
            CYCLE_T.CYCLE_VIEW_THREE_SCALE -> {

                mAdvPager!!.setPageTransformer(false
                ) { page, position ->
                    val normalizedPosition =
                        Math.abs(Math.abs(position) - 1)
                    page.scaleX = normalizedPosition / 2 + 0.5f
                    page.scaleY = normalizedPosition / 2 + 0.5f
                }
            }
            CYCLE_T.CYCLE_VIEW_ZOOM_IN -> {
                mAdvPager!!.pageMargin = -DemiUitls.dip2px(mContext!!, 60f)
                mAdvPager!!.setPageTransformer(true, ZoomOutPageTransformer())

            }
            CYCLE_T.CYCLE_VIEW_RIGHT_IN ->{
                var params = mAdvPager!!.layoutParams;
                params.width = DemiUitls.dip2px(mContext!!, 300f)
                mAdvPager!!.layoutParams = params

                mAdvPager!!.pageMargin = DemiUitls.dip2px(mContext!!, 8f)
                mAdvPager!!.clipChildren = false
                var parent = mAdvPager!!.parent as RelativeLayout
                parent.clipChildren = false

            }
        }
    }
}




