package info.cemu.cemu.guicore.components

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout as AndroidxDrawerLayout
import info.cemu.cemu.R

class DrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AndroidxDrawerLayout(context, attrs) {
    private var isLocked = false
    private val gestureExclusionRect = Rect()
    private val lockedModeDrawerListener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

        override fun onDrawerOpened(drawerView: View) {
            setDrawerLockMode(LOCK_MODE_UNLOCKED)
            updateSystemGestureExclusionRects()
        }

        override fun onDrawerClosed(drawerView: View) {
            setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            updateSystemGestureExclusionRects()
        }

        override fun onDrawerStateChanged(newState: Int) {}
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        super.onLayout(changed, left, top, right, bottom)
        updateSystemGestureExclusionRects()
    }

    fun setLockedMode(isLocked: Boolean) {
        if (this.isLocked == isLocked) {
            return
        }

        this.isLocked = isLocked
        if (isLocked) {
            setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            addDrawerListener(lockedModeDrawerListener)
            updateSystemGestureExclusionRects()
            return
        }

        setDrawerLockMode(LOCK_MODE_UNLOCKED)
        removeDrawerListener(lockedModeDrawerListener)
        updateSystemGestureExclusionRects()
    }

    private fun updateSystemGestureExclusionRects() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }

        if (isLocked || width <= 0 || height <= 0) {
            ViewCompat.setSystemGestureExclusionRects(this, emptyList())
            return
        }

        val exclusionWidth = resources.getDimensionPixelSize(
            R.dimen.drawer_system_gesture_exclusion_width,
        )
        if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
            gestureExclusionRect.set(width - exclusionWidth, 0, width, height)
        } else {
            gestureExclusionRect.set(0, 0, exclusionWidth, height)
        }

        ViewCompat.setSystemGestureExclusionRects(this, listOf(gestureExclusionRect))
    }
}
