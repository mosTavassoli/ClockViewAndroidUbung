package com.example.clockviewclass14apr

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation

class ClockView(context: Context, attrSet: AttributeSet?, defStyle: Int) :
    View(context, attrSet, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrSet: AttributeSet?) : this(context, attrSet, 0)


    var h: Int = 0
        set(value) {
            field = (value%12)
            invalidate()
        }
    var m: Int = 0
        set(value) {
            field = (value%60)
            invalidate()
        }
    private val paint1: Paint
    private val paint2: Paint
    private val path: Path // is a tool that we can define a set of connected points
                            // that make the form a kind og polygon
    private val d = context.resources.displayMetrics.density


    init {
        path = Path()
        paint1 = Paint()
        paint1.style = Paint.Style.STROKE
        paint1.color = Color.rgb(20, 20,20)
        paint1.strokeWidth = 3 * d

        paint2 = Paint()
        paint2.style = Paint.Style.FILL_AND_STROKE // have border and inner content
        paint2.color = Color.rgb(140, 140, 140)
        paint2.strokeWidth = 3 * d
        paint2.setShadowLayer(10*d,5*d,5*d, Color.argb(128,20,20,20))
    }


    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putInt("h", h)
            putInt("m", m)
            putParcelable("super", super.onSaveInstanceState())
        }
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var prevState = state
        if (prevState is Bundle) {
            h = prevState.getInt("h")
            m = prevState.getInt("m")
            prevState = prevState.getParcelable("super")
        }

        super.onRestoreInstanceState(state)
    }

    // access point to android, graphic android
    override fun onDraw(canvas: Canvas) {
        val size = Math.min(canvas.width, canvas.height) * 0.45f
        canvas.withTranslation(canvas.width * 0.5f, canvas.height * 0.5f) {
            //this for is for creating each minutes line, 12 lines
            for (i in 1..12) {
                drawCircle(0f, 0f, size, paint1)
                withRotation(i * 30f) {
                    drawLine(0f, -size * 0.9f, 0f, -size, paint1)
                }
            }

            // hour handler


            // hour handler
            // with this method we can rotate the handle of clock
            // by changing the m and h that we defined earlier
            withRotation (m*6f){
                path.rewind()
                path.moveTo(0f, size * 0.1f)
                path.lineTo(size * .075f, 0f)
                path.lineTo(0f, -size * .95f)
                path.lineTo(-size * .075f, 0f)
                path.close()
//            drawLine(0f, 0f, 0f, -size, paint1)
                drawPath(path, paint2)
                drawPath(path, paint1)
            }

            withRotation ((h+m/60f)*30f){
                path.rewind()
                path.moveTo(0f, size * 0.1f)
                path.lineTo(size * .075f, 0f)
                path.lineTo(0f, -size * .65f)
                path.lineTo(-size * .075f, 0f)
                path.close()
//            drawLine(0f, 0f, 0f, -size, paint1)
                drawPath(path, paint2)
                drawPath(path, paint1)
            }


        }

//        canvas.drawColor(Color.rgb(255,0,0))
        super.onDraw(canvas)
    }
}