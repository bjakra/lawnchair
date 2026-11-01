/*
 * Copyright (C) 2026 The Android Open Source Project
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
 */
package com.android.quickstep.views;

import com.android.launcher3.statehandlers.DesktopVisibilityController;
import com.android.launcher3.util.VibratorWrapper;
import com.android.quickstep.OverviewComponentObserver;
import com.android.quickstep.RecentsModel;
import com.android.quickstep.SystemUiProxy;
import com.android.quickstep.TopTaskTracker;
import com.android.quickstep.recents.viewmodel.RecentsViewModel;
import com.android.quickstep.util.RecentsOrientedState;
import com.android.wm.shell.shared.desktopmode.DesktopState;

import javax.inject.Inject;

/** Graphite: the dependencies of {@link RecentsView}, which can't use field injection on KSP. */
public final class RecentsViewDependencies {
    private final RecentsViewModel mRecentsViewModel;
    private final RecentsViewModelHelper mHelper;
    private final DesktopState mDesktopState;
    private final DesktopVisibilityController mDesktopVisibilityController;
    private final OverviewComponentObserver mOverviewComponentObserver;
    private final RecentsModel mRecentsModel;
    private final SystemUiProxy mSystemUiProxy;
    private final TopTaskTracker mTopTaskTracker;
    private final VibratorWrapper mVibratorWrapper;
    private final RecentsOrientedState mOrientationState;
    private final RecentsViewUtils.Factory mUtilsFactory;
    private final RecentsDismissUtils.Factory mDismissUtilsFactory;

    @Inject
    RecentsViewDependencies(
            RecentsViewModel recentsViewModel,
            RecentsViewModelHelper helper,
            DesktopState desktopState,
            DesktopVisibilityController desktopVisibilityController,
            OverviewComponentObserver overviewComponentObserver,
            RecentsModel recentsModel,
            SystemUiProxy systemUiProxy,
            TopTaskTracker topTaskTracker,
            VibratorWrapper vibratorWrapper,
            RecentsOrientedState orientationState,
            RecentsViewUtils.Factory utilsFactory,
            RecentsDismissUtils.Factory dismissUtilsFactory) {
        mRecentsViewModel = recentsViewModel;
        mHelper = helper;
        mDesktopState = desktopState;
        mDesktopVisibilityController = desktopVisibilityController;
        mOverviewComponentObserver = overviewComponentObserver;
        mRecentsModel = recentsModel;
        mSystemUiProxy = systemUiProxy;
        mTopTaskTracker = topTaskTracker;
        mVibratorWrapper = vibratorWrapper;
        mOrientationState = orientationState;
        mUtilsFactory = utilsFactory;
        mDismissUtilsFactory = dismissUtilsFactory;
    }

    void applyTo(RecentsView<?, ?> view) {
        view.mRecentsViewModel = mRecentsViewModel;
        view.mHelper = mHelper;
        view.mDesktopState = mDesktopState;
        view.mDesktopVisibilityController = mDesktopVisibilityController;
        view.mOverviewComponentObserver = mOverviewComponentObserver;
        view.mRecentsModel = mRecentsModel;
        view.mSystemUiProxy = mSystemUiProxy;
        view.mTopTaskTracker = mTopTaskTracker;
        view.mVibratorWrapper = mVibratorWrapper;
        view.mOrientationState = mOrientationState;
        view.mUtilsFactory = mUtilsFactory;
        view.mDismissUtilsFactory = mDismissUtilsFactory;
    }
}
