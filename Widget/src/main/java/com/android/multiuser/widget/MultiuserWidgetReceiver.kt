/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.multiuser.widget

import android.content.Context
import android.content.Intent

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.android.multiuser.widget.viewmodel.UsersViewModel

/** Broadcast receiver for multiuser widget registered in the Manifest. */
class MultiuserWidgetReceiver(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MultiuserWidget()
    private val allowedActions = arrayOf(
        // Start widget
        "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS",
        "android.intent.action.USER_INFO_CHANGED_BACKGROUND",
        "android.intent.action.USER_REMOVED",
        "android.intent.action.USER_ADDED"
    )

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action in allowedActions) {
            // inform runnung widget that data changed
            UsersViewModel.invalidate()
            CoroutineScope(dispatcher).launch {
                glanceAppWidget.updateAll(context)
            }
        }
    }
}
