package info.cemu.cemu.emulation

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import info.cemu.cemu.nativeinterface.NativeInput

class CanvasOnTouchListener(
    private val isTV: Boolean,
    private val rotateLeft: Boolean = false,
) : View.OnTouchListener {
    private var currentPointerId: Int = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)
        if (currentPointerId != -1 && pointerId != currentPointerId) {
            return false
        }

        val xi = event.getX(pointerIndex).toInt()
        val yi = event.getY(pointerIndex).toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                NativeInput.onTouchDown(xi, yi, isTV)
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                currentPointerId = -1
                NativeInput.onTouchUp(xi, yi, isTV)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                NativeInput.onTouchMove(xi, yi, isTV)
                return true
            }
        }
        return false
    }
}