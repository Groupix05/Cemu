package info.cemu.cemu.common.android.display

import android.app.Activity
import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.WindowManager

object DisplayUtils {
    private var launchDisplayId: Int? = null

    fun init(activity: Activity) {
        if (launchDisplayId == null) {
            launchDisplayId = activity.display?.displayId
                ?: activity.windowManager.defaultDisplay.displayId
        }
    }

    fun getInternalDisplay(context: Context): Display? {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        return dm.getDisplay(Display.DEFAULT_DISPLAY)
    }

    fun getExternalDisplay(context: Context): Display? {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val internalId = getInternalDisplay(context)?.displayId
        return dm.displays.firstOrNull { it.displayId != internalId }
    }
}

