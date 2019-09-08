package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R


class VerticalItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private var leftSpace: Int = 0
    private var mDivider: Drawable? = null
    private val ATTRS = intArrayOf(android.R.attr.listDivider)

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        leftSpace = context.resources.getDimensionPixelSize(R.dimen.space_maximum_72)
    }



    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }



        val left = parent.paddingLeft + leftSpace
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            // hide the divider for the last child
            /*if (i == childCount - 1) {
                return
            }*/
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)

        }
    }

}