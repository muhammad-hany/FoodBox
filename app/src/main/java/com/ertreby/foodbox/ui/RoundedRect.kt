package com.ertreby.foodbox.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.ertreby.foodbox.R

class RoundedRect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.RoundedRect, defStyleAttr, 0)

    private val paintColor = typedArray.getColor(
        R.styleable.RoundedRect_color,
        ResourcesCompat.getColor(context.resources, R.color.color_primary, null)
    )
    val paint = Paint().apply {
        color = paintColor
    }


    var cornerRadius: Float=typedArray.getDimension(R.styleable.RoundedRect_bottomCornerRadius,0f)
        set(value) {
            field=value
            invalidate()
        }

    val path = Path()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            paint
        )
        canvas?.drawRect(0f,0f,width.toFloat(),height.toFloat()/2,paint)
    }

}