package com.bczm.circleviewpager2

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.RandomAccessFile

/**
 * 操作文件 的工具类
 */
object FileUtils {
    const val ROOT_DIR = "CircleViewPager"
    const val DOWNLOAD_DIR = "download"
    const val CACHE_DIR = "cache"
    const val ICON_DIR = "pics"

    /** 判断SD卡是否挂载  */
    private val isSDCardAvailable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()


    /** 获取下载目录  */
    fun getDownloadDir(context: Context): String?{
        return getDir(
           DOWNLOAD_DIR,
            context
        )
    }

    /** 获取缓存目录  */
    fun getCacheDir(context: Context): String? {
        return getDir(
            CACHE_DIR,
            context
        )
    }

    /** 获取icon目录  */
    fun getIconDir(context: Context): String? {
        return getDir(
            ICON_DIR,
            context
        )
    }

    /** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录  */
    fun getDir(name: String, context: Context): String? {
        val sb = StringBuilder()
        if (isSDCardAvailable) {
            sb.append(externalStoragePath)
        } else {
            sb.append(getCachePath(context))
        }
        sb.append(name)
        sb.append(File.separator)
        val path = sb.toString()
        return if (createDirs(path)) {
            path
        } else {
            null
        }
    }

    /** 获取SD下的应用目录  */
    private val externalStoragePath: String
        get() {
            val sb = StringBuilder()
            sb.append(Environment.getExternalStorageDirectory().absolutePath)
            sb.append(File.separator)
            sb.append(ROOT_DIR)
            sb.append(File.separator)
            return sb.toString()
        }

    /** 获取应用的cache目录  */
    fun getCachePath(context: Context): String? {
        val f = context.cacheDir
        return if (null == f) {
            null
        } else {
            f.absolutePath + "/"
        }
    }

    /** 创建文件夹  */
    fun createDirs(dirPath: String?): Boolean {
//        val file = File(dirPath)
//        return if (!file.exists() || !file.isDirectory) {
//            file.mkdirs()
//        } else true
///storage/emulated/0/CircleViewPager/pics/
        var file =  File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs()
        }
        return true
    }

    /**
     * 把字符串数据写入文件
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    fun writeFile(
        content: ByteArray?,
        path: String?,
        append: Boolean
    ): Boolean {
        var res = false
        val f = File(path)
        var raf: RandomAccessFile? = null
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete()
                    f.createNewFile()
                }
            } else {
                f.createNewFile()
            }
            if (f.canWrite()) {
                raf = RandomAccessFile(f, "rw")
                raf.seek(raf.length())
                raf.write(content)
                res = true
            }
        } catch (e: Exception) {
//			LogUtils.e(e);
        } finally {
//			IOUtils.close(raf);
        }
        return res
    }
}