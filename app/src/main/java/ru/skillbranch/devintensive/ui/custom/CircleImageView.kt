package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.toDp
import ru.skillbranch.devintensive.extensions.toPx
import kotlin.math.min
import kotlin.math.roundToInt


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2f
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private val SCALE_TYPE = ScaleType.CENTER_CROP
    }

    private var mBitmapShader: BitmapShader? = null
    private var mBitmap: Bitmap? = null
    private var mInitialized: Boolean
    private var mBorderBounds: RectF
    private var mBitmapDrawBounds: RectF
    private var mBorderPaint: Paint
    private var mBitmapPaint: Paint
    private var mShaderMatrix: Matrix
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)
            mBorderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            mBorderWidth = a.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                DEFAULT_BORDER_WIDTH.toDp()
            )
            a.recycle()
        }

        mShaderMatrix = Matrix()
        mBorderBounds = RectF()
        mBitmapDrawBounds = RectF()
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mInitialized = true

        setupBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        setupBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
    }

    override fun setImageBitmap(bitmap: Bitmap) {
        super.setImageBitmap(bitmap)
        setupBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateBorder()
        updateBitmap()
    }

    private fun updateBorder() {
        val halfBorderWidth = mBorderWidth / 2f
        updateCircleDrawBounds(mBitmapDrawBounds)
        mBorderBounds.set(mBitmapDrawBounds)
        mBorderBounds.inset(halfBorderWidth, halfBorderWidth)
    }

    override fun onDraw(canvas: Canvas) {
        drawBitmap(canvas)
        drawBorder(canvas)
    }

    @Dimension
    fun getBorderWidth(): Int = mBorderWidth.toDp().toInt()

    fun setBorderWidth(@Dimension dp: Int) {
        mBorderWidth = dp.toPx().toFloat()
        updateBorder()
        updateBitmap()
    }

    fun getBorderColor(): Int = mBorderColor

    fun setBorderColor(hex: String) {
        mBorderColor = Color.parseColor(hex)
        updateBitmap()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        mBorderColor = resources.getColor(colorId, context.theme)
    }

    private fun drawBorder(canvas: Canvas) {
        if (mBorderPaint.strokeWidth > 0f) {
            canvas.drawOval(mBorderBounds, mBorderPaint)
        }
    }

    private fun drawBitmap(canvas: Canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
    }

    private fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        super.setScaleType(SCALE_TYPE)

        if (!mInitialized) {
            return
        }
        if (drawable == null) {
            return
        }

        mBitmap = drawable.toBitmap()
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        updateBitmap()
    }

    private fun updateBitmap() {
        if (mBitmap == null) return

        val dx: Float
        val dy: Float
        val scale: Float

        mBorderPaint.color = mBorderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth

        if (mBitmap!!.width < mBitmap!!.height) {
            scale = mBitmapDrawBounds.width() / mBitmap!!.width
            dx = mBitmapDrawBounds.left
            dy =
                mBitmapDrawBounds.top - mBitmap!!.height * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = mBitmapDrawBounds.height() / mBitmap!!.height
            dx =
                mBitmapDrawBounds.left - mBitmap!!.width * scale / 2f + mBitmapDrawBounds.width() / 2f
            dy = mBitmapDrawBounds.top
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader?.setLocalMatrix(mShaderMatrix)
        if (mInitialized) invalidate()
    }


}
