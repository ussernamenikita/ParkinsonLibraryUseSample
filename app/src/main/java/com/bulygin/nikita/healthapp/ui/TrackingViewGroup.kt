package com.bulygin.nikita.healthapp.ui

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

const val TAG = "TrackingView"
const val DEFAULT_TIMEOUT = 5000L
const val DEFAULT_MAX_DISTANCE = 20.0

class TrackingViewGroup : FrameLayout, MaxDistanceProvider {
    override fun getMaxDistance(): Double = maxDistanceValue


    private lateinit var internalContextLink: Context
    var timeOut = DEFAULT_TIMEOUT
    var maxDistanceValue = DEFAULT_MAX_DISTANCE
    var drawRect = false
    private val boundsPaint = Paint()

    init {
        boundsPaint.color = Color.rgb(0, 0, 0)
        boundsPaint.strokeWidth = 5f
        boundsPaint.style = Paint.Style.STROKE
    }


    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        this.internalContextLink = context
    }


    private fun onTouch(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        for (tracker in views) {
            tracker.onMotionEvent(event)
        }
        return false
    }

    private val views: ArrayList<ViewTracker> = ArrayList()

    var consumer: MissClickEventsConsumer? = null
        set(newValue) {
            for (tracker in views) {
                tracker.missClickConsumer = newValue
            }
        }

    fun addTrackingView(view: View) {
        this.views.add(ViewTracker(view, timeOut, this, consumer))
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return this.onTouch(ev) || super.onInterceptTouchEvent(ev)
    }


    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (canvas != null && drawRect) {
            drawViewsBounds(canvas)
        }
    }

    private fun drawViewsBounds(canvas: Canvas) {
        val myRect = Rect()
        getGlobalVisibleRect(myRect)
        for (tracker in views) {
            val viewRect = Rect()
            tracker.view.getGlobalVisibleRect(viewRect)
            val width = viewRect.right - viewRect.left
            val height = viewRect.bottom - viewRect.top
            val distance = fromDpi(maxDistanceValue)
            viewRect.top = Math.max(0, viewRect.top - myRect.top - distance.toInt())
            viewRect.bottom = viewRect.top + height + distance.toInt() * 2
            viewRect.left = Math.max(0, viewRect.left - myRect.left - distance.toInt())
            viewRect.right = viewRect.left + width + distance.toInt() * 2
            canvas.drawRect(viewRect, boundsPaint)
        }
    }

    private fun fromDpi(value: Double): Double = densityDivider * value

    private val densityDivider: Float = context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()
}

class ViewTracker(val view: View,
                  private val trackingTimeout: Long,
                  private val maxDistanceProvider: MaxDistanceProvider,
                  var missClickConsumer: MissClickEventsConsumer?) {

    private val densityDivider: Float = view.context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()

    private var lastAction = MotionEvent.ACTION_CANCEL

    private var closeEvents = ArrayList<CloseTouchEvent>()

    private var lastAddedActionTimestamp: Long = 0L

    fun onMotionEvent(event: MotionEvent) {
        val isEventOnView = isOnView(view, event)
        val timeStamp = getCurrentTime()
        if (isEventOnView) {
            if (isClickOnView(event)) {
                if (isHasCloseEvents(timeStamp)) {
                    missClickConsumer?.onConsume(getCurrentTime(), getAverageDitance(), getMissClickCount(timeStamp))
                }
                closeEvents.clear()
            }
            lastAction = event.action
        } else {
            val currentDistance = getDistanceFromViewInDpi(event)
            val currentTime = getCurrentTime()
            println("CURRENT DISTANCE : $currentDistance")
            if (currentDistance <= maxDistanceProvider.getMaxDistance() && currentTime - lastAddedActionTimestamp >= 100L) {
                println("Added")
                this.lastAddedActionTimestamp = currentTime
                closeEvents.add(CloseTouchEvent(currentTime, currentDistance))
            }
        }


    }

    private fun getDistanceFromViewInDpi(event: MotionEvent): Double {
        val rect = Rect()
        this.getViewRect(rect, view)
        val rectF = RectF(rect)
        val eventPoint = PointF(getXFromEvent(event).toFloat(), getYFromEvent(event).toFloat())
        return inDpi(min(
                dist(eventPoint, PointF(rectF.left, rectF.top), PointF(rectF.left, rectF.bottom)),
                dist(eventPoint, PointF(rectF.right, rectF.top), PointF(rectF.right, rectF.bottom)),
                dist(eventPoint, PointF(rectF.left, rectF.top), PointF(rectF.right, rectF.top)),
                dist(eventPoint, PointF(rectF.left, rectF.bottom), PointF(rectF.right, rectF.bottom))))
    }

    private fun inDpi(value: Double): Double = value / densityDivider.toDouble()

    private fun min(vararg values: Double): Double {
        var minimum: Double? = null
        for (value in values) {
            minimum = if (minimum == null) {
                value
            } else {
                Math.min(minimum, value)
            }
        }
        return minimum ?: 0.0
    }


    private fun dist(event: PointF, lineP1: PointF, lineP2: PointF): Double {
        val xmin = Math.min(lineP1.x, lineP2.x)
        val ymin = Math.min(lineP1.y, lineP2.y)
        val xmax = Math.max(lineP1.x, lineP2.x)
        val ymax = Math.max(lineP1.y, lineP2.y)
        val closest = PointF()
        if (event.x in xmin..xmax) {
            closest.x = event.x
        } else {
            closest.x = if (event.x <= xmin) xmin else xmax
        }
        if (event.y in ymin..ymax) {
            closest.y = event.y
        } else {
            closest.y = if (event.y <= ymin) ymin else ymax
        }
        return dist(event, closest)
    }

    private fun dist(a: PointF, b: PointF): Double {
        return Math.sqrt(Math.pow((a.x - b.x).toDouble(), 2.0) + Math.pow((a.y - b.y).toDouble(), 2.0))
    }

    private fun getMissClickCount(timestamp: Long): Int {
        var count = 0
        for (event in closeEvents) {
            count += if (timestamp - event.timestamp < trackingTimeout) 1 else 0
        }
        return count
    }


    private fun getAverageDitance(): Double {
        var average = 0.0
        for (value in closeEvents) {
            average += value.distance / closeEvents.size
        }
        return average
    }

    private fun getCurrentTime(): Long = System.currentTimeMillis()


    private fun isHasCloseEvents(timeStamp: Long): Boolean = closeEvents.isNotEmpty() &&
            closeEvents.find { timeStamp - it.timestamp < trackingTimeout } != null

    private fun isClickOnView(event: MotionEvent): Boolean {
        return lastAction == MotionEvent.ACTION_DOWN && event.action == MotionEvent.ACTION_UP
    }

    private fun isOnView(view: View, event: MotionEvent): Boolean {
        val x = this.getXFromEvent(event)
        val y = this.getYFromEvent(event)
        val rect = Rect()
        this.getViewRect(rect, view)
        return rect.contains(x, y)
    }

    private fun getYFromEvent(event: MotionEvent): Int = event.rawY.toInt()

    private fun getXFromEvent(event: MotionEvent): Int = event.rawX.toInt()

    private fun getViewRect(rect: Rect, viewForRect: View) {
        viewForRect.getGlobalVisibleRect(rect)
    }
}

interface MaxDistanceProvider {
    fun getMaxDistance(): Double
}

interface MissClickEventsConsumer {
    fun onConsume(timestamp: Long, distance: Double, missClickCount: Int)
}

data class CloseTouchEvent(val timestamp: Long,
                           val distance: Double)

