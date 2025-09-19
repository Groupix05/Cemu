package info.cemu.cemu.settings

import android.content.Context
import android.content.SharedPreferences

class EmulationScreenSettings(
    var isDrawerButtonVisible: Boolean,
    var isPadOnExternalDisplay: Boolean,
    var areScreensSwapped: Boolean,
    var isExternalScreenRotatedLeft: Boolean,
) {
    companion object {
        private const val IS_BUTTON_VISIBLE_KEY = "IS_BUTTON_VISIBLE"
        private const val IS_PAD_ON_EXTERNAL_DISPLAY_KEY = "IS_PAD_ON_EXTERNAL_DISPLAY"
        private const val ARE_SCREENS_SWAPPED_KEY = "ARE_SCREENS_SWAPPED"
        private const val IS_EXTERNAL_SCREEN_ROTATED_LEFT_KEY = "IS_EXTERNAL_SCREEN_ROTATED_LEFT"
    }

    constructor(sharedPreferences: SharedPreferences) : this(
        isDrawerButtonVisible = sharedPreferences.getBoolean(
            IS_BUTTON_VISIBLE_KEY,
            false
        ),
        isPadOnExternalDisplay = sharedPreferences.getBoolean(
            IS_PAD_ON_EXTERNAL_DISPLAY_KEY,
            false
        ),
        areScreensSwapped = sharedPreferences.getBoolean(
            ARE_SCREENS_SWAPPED_KEY,
            false
        ),
        isExternalScreenRotatedLeft = sharedPreferences.getBoolean(
            IS_EXTERNAL_SCREEN_ROTATED_LEFT_KEY,
            false
        ),
    )

    fun save(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().apply {
            putBoolean(IS_BUTTON_VISIBLE_KEY, isDrawerButtonVisible)
            putBoolean(IS_PAD_ON_EXTERNAL_DISPLAY_KEY, isPadOnExternalDisplay)
            putBoolean(ARE_SCREENS_SWAPPED_KEY, areScreensSwapped)
            putBoolean(IS_EXTERNAL_SCREEN_ROTATED_LEFT_KEY, isExternalScreenRotatedLeft)
            apply()
        }
    }
}

class SettingsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)

    var emulationScreenSettings: EmulationScreenSettings
        get() = EmulationScreenSettings(sharedPreferences)
        set(value) = value.save(sharedPreferences)


    companion object {
        private const val SETTINGS_NAME = "SETTINGS"
    }
}
