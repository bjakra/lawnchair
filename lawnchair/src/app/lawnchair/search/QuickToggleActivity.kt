package app.lawnchair.search

import android.app.Activity
import android.app.UiModeManager
import android.bluetooth.BluetoothManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.util.Log
import app.lawnchair.search.algorithms.engine.provider.QuickToggle

/**
 * Graphite: performs a quick toggle tapped in search. Search results can only launch intents, so
 * this is an invisible, non-exported activity. If a toggle can't be done directly (or fails), it
 * opens the matching settings page instead.
 */
class QuickToggleActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dark theme recreates this activity, which must not toggle a second time
        if (savedInstanceState != null) {
            finish()
            return
        }
        val toggle = intent.getStringExtra(EXTRA_TOGGLE)
            ?.let { name -> QuickToggle.entries.find { it.name == name } }
        if (toggle != null) {
            try {
                if (!perform(toggle)) openSettings(toggle)
            } catch (e: SecurityException) {
                Log.w(TAG, "Can't toggle $toggle directly", e)
                openSettings(toggle)
            }
        }
        finish()
    }

    private fun perform(toggle: QuickToggle): Boolean {
        val resolver = contentResolver
        val on = toggle.isActive(this) == true
        when (toggle) {
            QuickToggle.AIRPLANE_MODE ->
                getSystemService(ConnectivityManager::class.java).setAirplaneMode(!on)
            QuickToggle.NIGHT_LIGHT ->
                Settings.Secure.putInt(resolver, QuickToggle.NIGHT_DISPLAY_ACTIVATED, if (on) 0 else 1)
            QuickToggle.AUTO_ROTATE ->
                Settings.System.putInt(resolver, Settings.System.ACCELEROMETER_ROTATION, if (on) 0 else 1)
            QuickToggle.DARK_THEME -> getSystemService(UiModeManager::class.java).setNightMode(
                if (on) UiModeManager.MODE_NIGHT_NO else UiModeManager.MODE_NIGHT_YES,
            )
            QuickToggle.LOCATION ->
                getSystemService(LocationManager::class.java).setLocationEnabledForUser(!on, Process.myUserHandle())
            QuickToggle.FLASHLIGHT -> return toggleFlashlight(applicationContext)
            // Allowed for system apps; false (for example while airplane mode blocks it) opens the panel instead
            @Suppress("DEPRECATION")
            QuickToggle.WIFI -> return getSystemService(WifiManager::class.java).setWifiEnabled(!on)
            // Needs BLUETOOTH_PRIVILEGED, which Lawnchair holds as a platform-signed app
            @Suppress("DEPRECATION")
            QuickToggle.BLUETOOTH -> {
                val adapter = getSystemService(BluetoothManager::class.java)?.adapter ?: return false
                return if (on) adapter.disable() else adapter.enable()
            }
            QuickToggle.HOTSPOT, QuickToggle.CAST -> return false
        }
        return true
    }

    private fun openSettings(toggle: QuickToggle) {
        val action = when (toggle) {
            QuickToggle.WIFI -> Settings.Panel.ACTION_INTERNET_CONNECTIVITY
            QuickToggle.BLUETOOTH -> Settings.ACTION_BLUETOOTH_SETTINGS
            QuickToggle.AIRPLANE_MODE -> Settings.ACTION_AIRPLANE_MODE_SETTINGS
            QuickToggle.NIGHT_LIGHT -> Settings.ACTION_NIGHT_DISPLAY_SETTINGS
            QuickToggle.DARK_THEME, QuickToggle.AUTO_ROTATE -> Settings.ACTION_DISPLAY_SETTINGS
            QuickToggle.HOTSPOT -> Settings.ACTION_WIRELESS_SETTINGS
            QuickToggle.CAST -> Settings.ACTION_CAST_SETTINGS
            QuickToggle.LOCATION -> Settings.ACTION_LOCATION_SOURCE_SETTINGS
            QuickToggle.FLASHLIGHT -> return
        }
        try {
            startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "No settings page for $toggle", e)
        }
    }

    companion object {
        private const val TAG = "QuickToggleActivity"
        const val EXTRA_TOGGLE = "toggle"

        /** The torch has no readable state, only a callback that reports it on registration. */
        private fun toggleFlashlight(context: Context): Boolean {
            val camera = context.getSystemService(CameraManager::class.java)
            val id = camera.cameraIdList.firstOrNull {
                camera.getCameraCharacteristics(it).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return false
            camera.registerTorchCallback(
                object : CameraManager.TorchCallback() {
                    override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                        if (cameraId != id) return
                        camera.unregisterTorchCallback(this)
                        try {
                            camera.setTorchMode(id, !enabled)
                        } catch (e: CameraAccessException) {
                            Log.w(TAG, "Can't set the torch", e)
                        }
                    }
                },
                Handler(Looper.getMainLooper()),
            )
            return true
        }
    }
}
