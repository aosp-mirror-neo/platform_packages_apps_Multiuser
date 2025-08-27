/*
 * Copyright (C) 2025 The Android Open Source Project
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

package com.android.multiuser.widget.data.config

import android.app.ActivityManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.android.multiuser.widget.MultiuserWidget
import com.android.multiuser.widget.MultiuserWidgetUtil
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.UsersRepository
import com.android.multiuser.widget.domain.LoadCurrentUserUseCase
import com.android.multiuser.widget.viewmodel.UserViewModel
import java.util.Locale


class PersistentState(val glanceAppWidget: GlanceAppWidget = MultiuserWidget()) {

    object PrefKeys {
        val LANG = stringPreferencesKey("LANG")
        val FONT_SCALE = floatPreferencesKey("FONT_SCALE")
        val LAST_UPDATE = longPreferencesKey("LAST_UPDATE")
    }
    suspend fun updateWidgetStateFromSystem(context: Context, glanceId: GlanceId) {
        val contentResolver = context.contentResolver
        val userManager = MultiuserWidgetUtil.getUserManager(context)
        val activityManager = MultiuserWidgetUtil.getActivityManager(context)
        val widgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetOptions: Bundle = appWidgetManager.getAppWidgetOptions(widgetId)
        val widgetCategory = widgetOptions.getInt(
            AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY,
            AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD // Default to keyguard due to higher security
        )
        val locale = AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()

        val loadUseCase = LoadCurrentUserUseCase(
            UsersRepository(userManager, activityManager, contentResolver),
            ActionsRepository(context.resources),
            ImageRepository(userManager, context.resources),
            widgetCategory
        )
        val foregroundUserId = ActivityManager.getCurrentUser()
        val items = loadUseCase()
        val user = items.filterIsInstance<UserViewModel>().firstOrNull()

        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            prefs.toMutablePreferences().apply {
                // to trigger getting all strings from resources but only for user who's language changed
                if(user?.id == foregroundUserId) {
                    this[PrefKeys.LANG] = locale.language
                }
                this[PrefKeys.FONT_SCALE] =
                    Settings.System.getFloat(contentResolver, Settings.System.FONT_SCALE, 1.0f)
                items.forEach { viewModel ->
                    this[intPreferencesKey(viewModel.key)] = viewModel.stateUseCase().flag
                }
                // Making sure new job will be rescheduled on preference change
                // in case of double negation eg. disabling adding users from lockscreen
                // when user switch is already disabled does not change states of buttons
                this[PrefKeys.LAST_UPDATE] = System.currentTimeMillis()
            }
        }

        // Recompose the specific widget instance
        glanceAppWidget.update(context, glanceId)
    }

    // Call this to update all instances of this widget
    suspend fun updateAllInstances(context: Context) {
        val manager = GlanceAppWidgetManager(context)
        manager.getGlanceIds(MultiuserWidget::class.java)
            .forEach { glanceId ->
                updateWidgetStateFromSystem(context, glanceId)
            }
    }
}