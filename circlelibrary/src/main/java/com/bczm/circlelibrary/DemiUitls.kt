package com.bczm.circlelibrary

import android.app.Activity
import android.content.Context

object  DemiUitls {

    public fun dip2px(context: Context, dpValue:Float):Int{
        var scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context,pxValue:Float):Int{
        var scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getScreenWidth(act: Activity):Int{
        return act.windowManager.defaultDisplay.width
    }

    fun getScreenHeight(act: Activity):Int{
        return act.windowManager.defaultDisplay.height
    }
}