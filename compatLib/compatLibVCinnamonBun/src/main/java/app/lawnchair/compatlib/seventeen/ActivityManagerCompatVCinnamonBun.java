package app.lawnchair.compatlib.seventeen;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.window.TaskSnapshot;
import android.window.TaskSnapshotManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import app.lawnchair.compatlib.RecentsAnimationRunnerCompat;
import app.lawnchair.compatlib.sixteen.ActivityManagerCompatVBaklava;

/**
 * Android 17 removed {@code IActivityTaskManager#startRecentsActivity} and
 * {@code IActivityTaskManager#getTaskSnapshot}, which the older compat classes call directly.
 */
@RequiresApi(37)
public class ActivityManagerCompatVCinnamonBun extends ActivityManagerCompatVBaklava {
    private static final String TAG = "ActivityManagerCompatV17";

    @Nullable
    @Override
    public TaskSnapshot getTaskSnapshot(
            int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) {
        try {
            TaskSnapshotManager manager = TaskSnapshotManager.getInstance();
            TaskSnapshot snapshot =
                    manager.getTaskSnapshot(
                            taskId,
                            isLowResolution
                                    ? TaskSnapshotManager.RESOLUTION_LOW
                                    : TaskSnapshotManager.RESOLUTION_HIGH);
            if (snapshot == null && takeSnapshotIfNeeded) {
                snapshot = manager.takeTaskSnapshot(taskId, /* updateCache= */ true);
            }
            return snapshot;
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to getTaskSnapshot", e);
            return null;
        }
    }

    @Override
    public void startRecentsActivity(
            Intent intent, long eventTime, RecentsAnimationRunnerCompat runnerCompat) {
        // Recents transitions go through SystemUiProxy (WM Shell) on Android 17; the
        // IActivityTaskManager entry point no longer exists.
        Log.w(TAG, "startRecentsActivity is not available on Android 17");
    }
}
