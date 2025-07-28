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

package com.android.multiuser.widget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat

import com.android.compose.theme.PlatformTheme
import com.android.multiuser.widget.domain.DialogUseCase

/**
 * Activity to display a dialog with a title, message, icon, and one or two buttons.
 *
 * This activity is launched when user switch fails or is disabled.
 */
class DialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the activity transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Transparent status bar
        window.statusBarColor = Color.Transparent.hashCode()
        // Transparent navigation bar (if applicable)
        window.navigationBarColor = Color.Transparent.hashCode()
        val dialogUseCase = DialogUseCase(intent, resources)

        setShowWhenLocked(true)

        setContent {
            PlatformTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent // Important: Set the surface color to transparent
                ) {
                    DialogView(
                        dialogUseCase(),
                        { finish() },
                        {
                            it?.let { intent -> startActivity(intent) }
                            finish()
                        })
                }
            }
        }
    }
}
