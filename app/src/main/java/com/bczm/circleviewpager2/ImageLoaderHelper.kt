package com.bczm.circleviewpager2

import android.graphics.Bitmap
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader

class ImageLoaderHelper private constructor(){

     var imageLoader: ImageLoader

    private var options:DisplayImageOptions? = null
    private var IMG_LOAD_DELAY:Int = 200
    companion object {
        private var imageLoaderHelper:ImageLoaderHelper? =null
        get() {
            return field?:ImageLoaderHelper()
        }
        @JvmStatic
        @Synchronized
        fun getInstance():ImageLoaderHelper{
            return requireNotNull(imageLoaderHelper)
        }
    }

    init {
        options = DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.ic_launcher_round)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .delayBeforeLoading(IMG_LOAD_DELAY)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
            .build()
        imageLoader = ImageLoader.getInstance()
    }

    fun loadImage(url: String, imageView: ImageView) {
        imageLoader.displayImage(url.trim(), imageView, options)

    }
}