package cn.chitanda.weather.widget.weather.controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.View
import androidx.annotation.ColorInt
import cn.chitanda.weather.R
import cn.chitanda.weather.utils.cos
import cn.chitanda.weather.utils.sin
import cn.chitanda.weather.widget.weather.DynamicWeatherView

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:04
 *@description:
 **/
abstract class BaseController(protected val context: Context) : IController {
    protected val TAG = "WeatherController"
    protected var width = 0
    protected var height = 0
    protected var xAngle = 0f
    protected var yAngle = 0f
    protected val originPoint = PointF()
    protected lateinit var view: View
    protected var isInited = false
    protected val polygonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    protected open fun setOriginPoint() {
        originPoint.x = width / 2f
        originPoint.y = 0f
    }


    override fun init(view: DynamicWeatherView, width: Int, height: Int) {
        this.view = view
        this.width = width
        this.height = height
        setOriginPoint()
        isInited = true
    }

    override fun setOrientationAngles(xAngle: Float, yAngle: Float) {
        if (!isInited) return
        this.xAngle = xAngle
        this.yAngle = yAngle
    }

    /*
     * @Author  chen
     * @Description  绘制多边形
     * @Date 14:51 2021/1/2
     * @Param count 多边形的边数 radius 多边形外接园的半径 rotation 多边形旋转角度
     * @return
     **/
    protected fun drawPolygon(
        canvas: Canvas,
        count: Int,
        radius: Float,
        rotation: Float = 0F,
        x: Float = 0f,
        y: Float = 0f
    ) {
        if (count < 5) return
        canvas.save()
        canvas.translate(x + originPoint.x, y + originPoint.y)
        canvas.rotate(rotation)
        val path = Path()
        repeat(count) {
            if (it == 0) {
                //绘制起点
                path.moveTo(radius * cos(360f / count * it), radius * sin(360f / count * it))
            } else {
                path.lineTo(radius * cos(360f / count * it), radius * sin(360f / count * it))
            }
        }
        path.close()
        canvas.drawPath(path, polygonPaint)
        canvas.restore()
    }

    protected fun drawBackground(canvas: Canvas, @ColorInt color: Int=context.getColor(R.color.theme_color)) {
        canvas.drawColor(color)
    }


}