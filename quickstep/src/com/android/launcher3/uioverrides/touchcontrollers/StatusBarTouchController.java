/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications copyright 2025, Lawnchair
 */
package com.android.launcher3.uioverrides.touchcontrollers;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import static com.android.launcher3.MotionEventsUtils.isTrackpadMultiFingerSwipe;
import static com.android.launcher3.Utilities.shouldEnableMouseInteractionChanges;
import static com.android.launcher3.logging.StatsLogManager.LauncherEvent.LAUNCHER_SWIPE_DOWN_WORKSPACE_NOTISHADE_OPEN;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.BaseActivity;
import com.android.launcher3.DeviceProfile;
import com.android.launcher3.Launcher;
import com.android.launcher3.Utilities;
import com.android.launcher3.util.MSDLPlayerWrapper;
import com.android.launcher3.util.TouchController;
import com.android.launcher3.util.VibratorWrapper;
import com.android.quickstep.SystemUiProxy;
import com.android.quickstep.util.CachedEventDispatcher;

import com.android.systemui.Flags;
import com.google.android.msdl.data.model.MSDLToken;
import java.util.function.Supplier;
import java.lang.reflect.InvocationTargetException;

import app.lawnchair.LawnchairAppKt;
import app.lawnchair.LawnchairLauncher;
import app.lawnchair.gestures.config.GestureHandlerConfig;
import app.lawnchair.util.CompatibilityKt;

/**
 * Handles touch events for the system status bar.
 * <p>
 * This controller intercepts touch events and, after a vertical drag gesture exceeds the system
 * touch slop, forwards them to the System UI. Events occurring before the touch slop are also
 * forwarded in the order we received them, to ensure ACTION_DOWN and any velocity data is passed.
 * <p>
 * The controller only handles events if:
 * - The System UI Proxy is active.
 * - A provided `isEnabledCheck` supplier returns `true`.
 * - There are no floating views disallowing status bar swipes.
 * - For touch events (not trackpad scrolls or mouse input if specific features are enabled),
 *   the initial touch occurs above the navigation bar area.
 */
public class StatusBarTouchController implements TouchController {

    private static final String TAG = "StatusBarController";

    private final BaseActivity mLauncher;
    private final SystemUiProxy mSystemUiProxy;
    private final float mTouchSlop;
    private final PointF mDownEvent = new PointF();
    private final Supplier<Boolean> mIsEnabledCheck;
    private final CachedEventDispatcher mEventDispatcher = new CachedEventDispatcher();

    /* If {@code false}, this controller should not handle the input {@link MotionEvent}.*/
    private boolean mCanIntercept;

    // LC-Note: For pulling down on notification panel (default gesture config)
    private boolean mExpanded;
    private boolean mVibrated;

    public StatusBarTouchController(BaseActivity l, Supplier<Boolean> isEnabledCheck) {
        mLauncher = l;
        mSystemUiProxy = SystemUiProxy.INSTANCE.get(mLauncher);
        mTouchSlop = ViewConfiguration.get(l).getScaledTouchSlop();
        mIsEnabledCheck = isEnabledCheck;
    }

    @Override
    public String dump() {
        return TAG + " mCanIntercept:" + mCanIntercept
                + " , mSysUiProxy available:" + mSystemUiProxy.isActive();
    }

    private void dispatchTouchEvent(MotionEvent ev) {
        if (mSystemUiProxy.isActive()) {
            mSystemUiProxy.onStatusBarTouchEvent(ev);
        } else if (!mExpanded) {
            // LC-Note: If not, call the notification panel via reflection (no animations)
            mExpanded = true;
            expand();
        }
        if (!mVibrated) {
            mVibrated = true;
            vibrate();
        }
    }

    @SuppressLint({"WrongConstant", "PrivateApi"})
    private void expand() {
        // LC-Note: For pulling down on notification panel
        if (mLauncher instanceof LawnchairLauncher launcher) {
            launcher.getGestureController().handle(GestureHandlerConfig.OpenNotifications.INSTANCE);
        } else {
            // LC-Note: Fallback using older style for expanding the notifications panel
            try {
                Class.forName("android.app.StatusBarManager")
                    .getMethod("expandNotificationsPanel")
                    .invoke(mLauncher.getSystemService("statusbar"));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void vibrate() {
        // LC-Note: For pulling down on notification panel (default gesture config)
        if (!LawnchairAppKt.getLawnchairApp(mLauncher).isVibrateOnIconAnimation()) {
            if (Flags.msdlFeedback()) {
                MSDLPlayerWrapper.INSTANCE.get(mLauncher).playToken(MSDLToken.SWIPE_THRESHOLD_INDICATOR);
            } else {
                VibratorWrapper.INSTANCE.get(mLauncher).vibrate(VibratorWrapper.OVERVIEW_HAPTIC);
            }
        }
    }

    @Override
    public final boolean onControllerInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == ACTION_DOWN) {
            mCanIntercept = canInterceptTouch(ev);
            if (!mCanIntercept) {
                return false;
            }
            mDownEvent.set(ev.getX(), ev.getY());
            mExpanded = false;
            mVibrated = false;
        }
        if (!mCanIntercept) {
            cleanupAfterGesture();
            return false;
        }

        mEventDispatcher.dispatchEvent(ev);

        if (action == ACTION_MOVE) {
            float dy = ev.getY() - mDownEvent.y;
            float dx = ev.getX() - mDownEvent.x;
            if (dy > mTouchSlop && dy > Math.abs(dx)) {
                if (!mEventDispatcher.hasConsumer()) {
                    mEventDispatcher.setConsumer(this::dispatchTouchEvent);
                }
                return true;
            }
            if (Math.abs(dx) > mTouchSlop) {
                mCanIntercept = false;
            }
        } else if (action == ACTION_UP || action == ACTION_CANCEL) {
            cleanupAfterGesture();
        }
        return false;
    }

    @Override
    public final boolean onControllerTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        mEventDispatcher.dispatchEvent(ev);
        if (action == ACTION_UP || action == ACTION_CANCEL) {
            mLauncher.getStatsLogManager().logger()
                    .log(LAUNCHER_SWIPE_DOWN_WORKSPACE_NOTISHADE_OPEN);
            cleanupAfterGesture();
        }
        return true;
    }

    private void cleanupAfterGesture() {
        mEventDispatcher.clearConsumerAndCache();
    }

    private boolean canInterceptTouch(MotionEvent ev) {
        // Lawnchair: Icon Swipe Gestures (For vertical down gestures)
        if (mLauncher instanceof Launcher launcher) {
            if (launcher.getWorkspace() != null && launcher.getDragLayer() != null) {
                float[] coord = new float[]{ev.getX(), ev.getY()};
                launcher.getDragLayer().mapCoordInSelfToDescendant(launcher.getWorkspace(), coord);
                if (launcher.getWorkspace()
                        .isTouchOnIconWithSwipeGesture(coord[0], coord[1], true)) {
                    return false;
                }
            }
        }
        if (isTrackpadMultiFingerSwipe(ev)) {
            // Trackpad events are handled separately, see e.g. TrackpadStatusBarInputConsumer.
            return false;
        }
        if (!mIsEnabledCheck.get()) {
            return false;
        }
        if (AbstractFloatingView.getTopOpenViewWithType(mLauncher,
                AbstractFloatingView.TYPE_STATUS_BAR_SWIPE_DOWN_DISALLOW) != null) {
            return false;
        }
        if (shouldEnableMouseInteractionChanges(mLauncher.asContext())
                && ev.getSource() == InputDevice.SOURCE_MOUSE) {
            return false;
        }
        // For NORMAL state, only listen if the event originated above the navbar height
        DeviceProfile dp = mLauncher.getDeviceProfile();
        if (ev.getY() > (mLauncher.getDragLayer().getHeight() - dp.getInsets().bottom)) {
            return false;
        }
        return mSystemUiProxy.isActive();
    }
}
