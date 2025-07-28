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

import android.app.KeyguardManager
import android.app.KeyguardManager.KeyguardDismissCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier

import com.android.compose.theme.PlatformTheme
import com.android.multiuser.widget.R
import com.android.multiuser.widget.domain.DialogUseCase

/**
 * Activity to display a dialog with a title, message, icon, and one or two buttons.
 *
 * This activity is launched when user switch fails or is disabled.
 */
class DialogActivity : ComponentActivity() {
    private lateinit var keyguardManager: KeyguardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        // Determine which theme to apply
        val themeResId = if (keyguardManager.isKeyguardLocked) {
            R.style.Theme_MultiuserWidgetLocked
        } else {
            R.style.Theme_MultiuserWidget
        }

        // Apply the theme. THIS MUST BE CALLED BEFORE super.onCreate()
        setTheme(themeResId)

        super.onCreate(savedInstanceState)

        val dialogUseCase = DialogUseCase(intent, resources)

        setShowWhenLocked(true)

        setContent {
            PlatformTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.2f),
                ) {

                    var postKeyguardIntent by remember { mutableStateOf<Intent?>(null) }

                    // Only display the dialog if postKeyguardIntent is null.
                    // This ensures the dialog doesn't reappear after keyguard dismissal logic
                    // has started.
                    if (postKeyguardIntent == null) {
                        DialogView(
                            dialogUseCase(),
                            { finish() },
                            {
                                it?.let {
                                    if (keyguardManager.isKeyguardLocked) {
                                        postKeyguardIntent = it
                                        keyguardManager.requestDismissKeyguard(
                                            this,
                                            object : KeyguardDismissCallback() {
                                                override fun onDismissError() {
                                                    finish()
                                                }

                                                override fun onDismissCancelled() {
                                                    finish()
                                                }

                                                override fun onDismissSucceeded() {
                                                    startActivity(postKeyguardIntent)
                                                    finish()
                                                }
                                            }
                                        )
                                    } else {
                                        startActivity(it)
                                        finish()
                                    }
                                } ?: finish()
                            })
                    }
                }
            }
        }
    }

}
