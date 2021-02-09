package com.bczm.circlelibrary

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class ClickableImageView :ImageView{
//    constructor(context:Context):super(context){
//
//    }
var touchDark: FloatArray = floatArrayOf(
    1f,
    0f,
    0f,
    0f,
    -50f,
    0f,
    1f,
    0f,
    0f,
    -50f,
    0f,
    0f,
    1f,
    0f,
    -50f,
    0f,
    0f,
    0f,
    1f,
    0f
)
    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init()
    }

    fun init(){
        setOnTouchListener(touchListener)
    }

    var touchListener =object :OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event!!.action == MotionEvent.ACTION_DOWN) {
                val iv = v as ImageView
                iv.setColorFilter(ColorMatrixColorFilter(touchDark))
            } else if (event.action == MotionEvent.ACTION_UP) {
                val iv = v as ImageView
                iv.clearColorFilter()
                performClick()
            } else if (event.action == MotionEvent.ACTION_CANCEL) {
                val iv = v as ImageView
                iv.clearColorFilter()
            }
            return true // 如为false，执行ACTION_DOWN后不再往下执行

        }


    }

}