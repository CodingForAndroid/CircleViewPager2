package com.bczm.circleviewpager2

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bczm.circlelibrary.ImageCycleView

class MainActivity : AppCompatActivity() {
    private var cycleView:ImageCycleView? =null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         cycleView = findViewById(R.id.cycleView);
//        cycleView!!.setCycleType(ImageCycleView.CYCLE_T.CYCLE_VIEW_NORMAL)
        /**装在数据的集合  文字描述*/
        var imageDescList = arrayListOf<String>()

        /**装在数据的集合  图片地址*/
        var urlList= arrayListOf<String>(
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3769441834,4238032241&fm=26&gp=0.jpg"
            , "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=315050413,1010281204&fm=26&gp=0.jpg"
            , "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3633693073,3344238293&fm=26&gp=0.jpg"
            , "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1103786609,18779662&fm=26&gp=0.jpg"
            , "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1647075209,1787805644&fm=26&gp=0.jpg"
        )

        imageDescList.add("小仓柚子")
        imageDescList.add("抚媚妖娆性感美女")
        imageDescList.add("热血沸腾 比基尼")
        imageDescList.add(" 台球美女")
        imageDescList.add("身材妙曼")

        initCarsuelView(imageDescList, urlList)

    }

    private fun  initCarsuelView(imageDescList:ArrayList<String>, urlList: ArrayList<String>){
        var layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, getScreenHeight(this) * 3 / 10)
        cycleView!!.layoutParams = layoutParams;
        var listener =object : ImageCycleView.ImageCycleViewListener {
            override fun displayImage(imageURL: String?, imageView: ImageView?) {
                /**在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader） */
                ImageLoaderHelper.getInstance().loadImage(imageURL!!, imageView!!)
            }

            override fun onImageClick(position: Int, imageView: View?) {
                Toast.makeText(this@MainActivity, "position=$position", Toast.LENGTH_SHORT).show()
            }

        }

        /**设置数据*/
        cycleView!!.setImageResources(imageDescList,urlList, listener)
        // 是否隐藏底部
        cycleView!!.hideBottom(false)
        cycleView!!.setCycleType(ImageCycleView.CYCLE_T.CYCLE_VIEW_RIGHT_IN)
        cycleView!!.startImageCycle()
    }

    private fun getScreenHeight(context: Context):Int{
        if (null == context) {
            return 0
        }
        var dm =  DisplayMetrics();
        dm = context.applicationContext.resources.displayMetrics;
        return dm.heightPixels;
    }
}