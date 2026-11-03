package app.lawnchair.smartspace.provider

import android.Manifest
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.provider.Settings
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import app.lawnchair.util.broadcastReceiverFlow
import com.android.launcher3.R
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/** Graphite: battery level of connected Bluetooth headphones and other audio devices. */
class BluetoothBatteryProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_bluetooth_battery,
        { smartspaceBluetoothBattery },
    ) {
    private val adapter = context.getSystemService(BluetoothManager::class.java)?.adapter

    override val isAvailable = adapter != null

    override val internalTargets = broadcastReceiverFlow(
        context,
        IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(ACTION_BATTERY_LEVEL_CHANGED)
        },
    ).map { }.onStart { emit(Unit) }.map { targets() }

    private fun targets(): List<SmartspaceTarget> {
        if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return emptyList()
        }
        return runCatching {
            adapter?.bondedDevices.orEmpty()
                .filter { isAudio(it) && isConnected(it) }
                .mapNotNull { device -> batteryLevel(device)?.let { target(device, it) } }
        }.getOrDefault(emptyList())
    }

    private fun target(device: BluetoothDevice, level: Int) = SmartspaceTarget(
        id = "graphiteBluetooth${device.address}",
        headerAction = SmartspaceAction(
            id = "graphiteBluetoothAction${device.address}",
            icon = Icon.createWithResource(context, R.drawable.ic_smartspace_headphones),
            title = device.alias ?: device.name ?: "",
            subtitle = context.getString(R.string.n_percent, level),
            intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        ),
        score = SmartspaceScores.SCORE_BATTERY,
        featureType = SmartspaceTarget.FeatureType.FEATURE_CALENDAR,
    )

    private fun isAudio(device: BluetoothDevice): Boolean {
        val major = device.bluetoothClass?.majorDeviceClass ?: return false
        return major == BluetoothClass.Device.Major.AUDIO_VIDEO || major == BluetoothClass.Device.Major.WEARABLE
    }

    // Hidden BluetoothDevice API; Lawnchair is platform signed, so it may call it.
    private fun isConnected(device: BluetoothDevice) =
        BluetoothDevice::class.java.getMethod("isConnected").invoke(device) as Boolean

    private fun batteryLevel(device: BluetoothDevice): Int? {
        val level = BluetoothDevice::class.java.getMethod("getBatteryLevel").invoke(device) as Int
        return level.takeIf { it in 0..100 }
    }

    companion object {
        private const val ACTION_BATTERY_LEVEL_CHANGED = "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED"
    }
}
