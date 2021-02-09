package com.bczm.circlelibrary

import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class ZoomOutPageTransformer:ViewPager.PageTransformer {
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f
    private val logTag = "ZoomOutPageTransformer";
    override fun transformPage(view: View, position: Float) {
        var pageWidth = view.width
        var pageHeight = view.height
        Log.e(logTag, "$view,$position")

        if(position<-1){
            view.alpha = 0f
        }else if(position <=1){
            var scaleFactor = Math.max(MIN_SCALE, 1 - abs(position))
            var verMargin = pageHeight*(1-scaleFactor)/2
            var horMargin = pageWidth*(1-scaleFactor)/2

            if(position < 0){
                view.translationX = horMargin - verMargin / 2
            }else{
                view.translationX = -horMargin + verMargin / 2
            }
            // Scale the page down (between MIN_SCALE and 1)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            // Fade the page relative to its size.
            // Fade the page relative to its size.
            view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)

        }else{
            view.alpha = 0f
        }
    }
}