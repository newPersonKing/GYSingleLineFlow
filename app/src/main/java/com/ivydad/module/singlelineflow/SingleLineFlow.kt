package com.ivydad.module.singlelineflow

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import java.util.*

class SingleLineFlow : LinearLayout{

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){

        postDelay()
    }


    private fun postDelay() {
        post {
            val childCount = childCount
            if (childCount == 1) {
                val child = getChildAt(0)
                if (child is LinearLayout) {
                    val lastIndex =  resolveOverWidth(child)
                    if(lastIndex == -1) child.layoutParams.width = width
                } else {
                    Log.i("singleLineFlow", "child必须是LinearLayout")
                }
            }
        }
    }

    private fun resolveOverWidth(parent: LinearLayout) :Int{
        val childCout = parent.childCount
        val containerWidth = width
        var accumulationWidth = 0
        var lastIndex = 0
        var lastChildWidth = 0
        val childParams: MutableList<LayoutParams> = ArrayList()
        for (i in 0 until childCout) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as LayoutParams
            val childWidth = child.measuredWidth + params.leftMargin + params.rightMargin
            val preTotalWidth = accumulationWidth
            accumulationWidth += childWidth
            childParams.add(params)
            if (accumulationWidth >= containerWidth) {
                lastIndex = i
                lastChildWidth = containerWidth - preTotalWidth
                break
            }
            if (i == childCout - 1) {
                /*证明最后一个还没有超出容器宽度 虽有params 设置widget 为1*/
                lastIndex = -1;
            }
        }

        if(lastIndex == -1){
            /*这里直接修改 layoutParams 的宽高 不可以*/
            parent.layoutParams = LayoutParams(width,parent.layoutParams.height)
            parent.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent))
            childParams.forEach {
                it.weight = 1F
            }
        }else{
            for (i in lastIndex until childCout) {
                val child = parent.getChildAt(i)
                if (lastChildWidth > 0 && i == lastIndex) {
                    child.layoutParams.width = lastChildWidth
                } else {
                    child.visibility = GONE
                }
            }
        }

        return  lastIndex

    }
}