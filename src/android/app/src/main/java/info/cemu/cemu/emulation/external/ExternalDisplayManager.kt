package info.cemu.cemu.emulation.external

import android.app.Activity
import android.app.Presentation
import android.content.Context
import android.hardware.display.DisplayManager
import android.util.Log
import android.os.Bundle
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import info.cemu.cemu.nativeinterface.NativeEmulation

class ExternalDisplayManager(private val activity: Activity) {

    companion object {
        private const val TAG = "ExternalDisplayMgr"
    }

    private var presentation: PadPresentation? = null
    private val displayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    fun hasExternalDisplay(): Boolean {
        val has = displayManager.displays.any { it.displayId != activity.display?.displayId }
        Log.d(TAG, "hasExternalDisplay=$has")
        return has
    }

    fun enableExternalDisplay(): Boolean {
        val externalDisplay = displayManager.displays.firstOrNull { it.displayId != activity.display?.displayId }
        if (externalDisplay == null) {
            Log.d(TAG, "No external display available")
            return false
        }
        Log.d(TAG, "Enabling external display id=${externalDisplay.displayId}")
        startPresentation(externalDisplay)
        return true
    }

    fun disableExternalDisplay() {
        if (presentation != null) {
            Log.d(TAG, "Disabling external display")
            presentation?.dismiss()
            presentation = null
        }
    }

    private fun startPresentation(externalDisplay: Display) {
        Log.d(TAG, "Starting presentation on display id=${externalDisplay.displayId}")
        presentation = PadPresentation(activity, externalDisplay).also { it.show() }
    }



    private class PadPresentation(context: Context, display: Display) : Presentation(context, display) {
        private var surfaceView: SurfaceView? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            surfaceView = SurfaceView(context)
            setContentView(surfaceView!!)
            surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    Log.d(TAG, "Pad surface created")
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    Log.d(TAG, "Pad surface changed width=$width height=$height")
                    NativeEmulation.setSurfaceSize(width, height, false)
                    NativeEmulation.setSurface(holder.surface, false)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    Log.d(TAG, "Pad surface destroyed")
                    NativeEmulation.clearSurface(false)
                }
            })
        }
    }
}
