package com.bczm.circleviewpager2

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import java.io.File

/**
 *  @author Jorge on 2021/2/4
 */
class BaseApplication : Application() {

    companion object {
        /** 主线程ID  */
        var mMainThreadId: Int = 0

        /** 获取主线程  */
        var mMainThread: Thread? = null

        /** 主线程Handler  */
        var mainThreadHandler: Handler? = null

        /** 主线程Looper  */
        var mMainLooper: Looper? = null

        /** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了  */
        var application: BaseApplication? = null

        /** imageLoader memoryCacheSize  */
        var memoryCacheSize: Int = 2 * 1024 * 1024

        lateinit var newInstance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        mMainThreadId = android.os.Process.myPid()
        mMainThread = Thread.currentThread()
        mainThreadHandler = Handler()
        mMainLooper = mainLooper
        application = this
        newInstance = this

        initImageLoader()


    }


    fun getMainThreadId(): Int = mMainThreadId
    fun getMainThread(): Thread? {
        return mMainThread
    }

    fun getMainThreadHandler(): Handler? {
        return mainThreadHandler
    }

    fun getMLooper(): Looper? {
        return mainLooper
    }

    fun getInstance(): BaseApplication {
        return newInstance
    }

    private fun initImageLoader() {
        var config = ImageLoaderConfiguration.Builder(application)
            .threadPriority(Thread.NORM_PRIORITY + 2)
            .denyCacheImageMultipleSizesInMemory()
            .threadPoolSize(10)
            .discCacheFileNameGenerator(Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .diskCache(UnlimitedDiskCache(File(FileUtils.getIconDir(this))))
            .memoryCache(LRULimitedMemoryCache(memoryCacheSize))
            .build()

        ImageLoader.getInstance().init(config)
    }

    fun exitApp() {
        System.gc()
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}